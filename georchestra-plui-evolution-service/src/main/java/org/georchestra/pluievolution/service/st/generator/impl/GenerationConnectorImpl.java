package org.georchestra.pluievolution.service.st.generator.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.georchestra.pluievolution.core.common.DocumentContent;
import org.georchestra.pluievolution.service.exception.DocumentGenerationException;
import org.georchestra.pluievolution.service.exception.DocumentModelNotFoundException;
import org.georchestra.pluievolution.service.st.generator.GenerationConnector;
import org.georchestra.pluievolution.service.st.generator.GenerationConnectorConstants;
import org.georchestra.pluievolution.service.st.generator.PDFConverterType;
import org.georchestra.pluievolution.service.st.generator.datamodel.DataModel;
import org.georchestra.pluievolution.service.st.generator.datamodel.GenerationFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.html.HTML;
import com.itextpdf.tool.xml.html.TagProcessorFactory;
import com.itextpdf.tool.xml.html.Tags;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

/**
 * Methodes pour la génération de documents
 * 
 * @author FNI18300
 *
 */
@Component
public class GenerationConnectorImpl implements GenerationConnector {

	private static final String FONT_TTF = "ttf";

	private static final String FONT_OTF = "otf";

	private static final String[] FREE_MARKER_EXTENSION = { "ftl", "html", "txt" };

	private static final Logger LOGGER = LoggerFactory.getLogger(GenerationConnectorImpl.class);

	private Map<String, File> fontMap = new HashMap<>();

	@Value("${freemarker.clearCache:true}")
	private boolean freemarkerClearCache;

	@Value("${temporary.directory}")
	private String temporaryDirectory;

	@Value("${freemarker.baseDirectory}")
	private String freemarkerBaseDirectory;

	@Value("${freemarker.basePackage}")
	private String freemarkerBasePackage;

	@Value("${freemarker.cssFile}")
	private String freemarkerCssFile;

	@Value("${freemarker.fontsPath}")
	private String freemarkerFontsPath;

	private CompositeTemplateLoader compositeTemplateLoader = null;

	/**
	 * 
	 * Constructeur pour GenerationConnectorImpl
	 */
	public GenerationConnectorImpl() {
		super();
	}

	@Override
	public DocumentContent generateDocument(DataModel dataModel)
			throws DocumentModelNotFoundException, DocumentGenerationException, IOException {
		String nomFichierModele = dataModel.getModelFileName();
		String extensionModele = FilenameUtils.getExtension(nomFichierModele);

		if (ArrayUtils.contains(FREE_MARKER_EXTENSION, extensionModele)
				|| nomFichierModele.startsWith(GenerationConnectorConstants.STRING_TEMPLATE_LOADER_PREFIX)) {
			return generateFreeMarkerDocument(dataModel);
		}
		throw new DocumentGenerationException("Invalid format:" + extensionModele);
	}

	/**
	 * @param dataModel
	 * @return un document freemarker
	 * @throws DocumentModelNotFoundException
	 * @throws DocumentGenerationException
	 * @throws IOException
	 */
	protected DocumentContent generateFreeMarkerDocument(DataModel dataModel)
			throws DocumentModelNotFoundException, DocumentGenerationException, IOException {
		if (dataModel == null) {
			throw new IllegalArgumentException("Missing data model");
		}
		DocumentContent result = null;

		ensureTargetDirectoryFile();

		File generateFile = File.createTempFile("tmp", ".gen", new File(temporaryDirectory));
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Temporary generation file:{}", generateFile);
		}

		String nomFichierModele = dataModel.getModelFileName();
		Template template = initModeleFreeMarker(nomFichierModele);
		try {
			template.process(dataModel.getDataModel(), new FileWriter(generateFile));

			result = new DocumentContent(dataModel.getOutputFileName(), dataModel.getFormat().getTypeMime(),
					generateFile);

		} catch (Exception e) {
			throw new DocumentGenerationException("Failed to generate document:" + dataModel.getModelFileName(), e);
		}

		return result;
	}

	protected Template initModeleFreeMarker(String templateName) throws DocumentModelNotFoundException {
		Template template = null;
		String realTemplateName = templateName;
		try {
			Configuration configuration = new Configuration(Configuration.VERSION_2_3_28);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Freemarker model base:{} /{}", freemarkerBaseDirectory, freemarkerBasePackage);
			}
			if (compositeTemplateLoader == null) {
				compositeTemplateLoader = new CompositeTemplateLoader(new File(freemarkerBaseDirectory),
						Thread.currentThread().getContextClassLoader(), freemarkerBasePackage);
			}
			configuration.setTemplateLoader(compositeTemplateLoader);

			if (templateName.startsWith(GenerationConnectorConstants.STRING_TEMPLATE_LOADER_PREFIX)) {
				realTemplateName = extractTemplateName(templateName);
				compositeTemplateLoader.putTemplate(realTemplateName, extractTemplateContent(templateName));
			}
			configuration.setTemplateUpdateDelayMilliseconds(5L * 60L * 1000L);
			configuration.setDefaultEncoding(StandardCharsets.UTF_8.name());
			configuration.setLocale(Locale.FRANCE);
			configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

			if (freemarkerClearCache) {
				configuration.clearTemplateCache();
			}

			template = configuration.getTemplate(realTemplateName);
		} catch (Exception e) {
			String message = "Failed to load template " + templateName;
			LOGGER.error(message);
			throw new DocumentModelNotFoundException(message, e);
		}

		return template;
	}

	private String extractTemplateContent(String templateName) {
		String nameAndContent = templateName.substring(GenerationConnectorConstants.STRING_TEMPLATE_LOADER_PREFIX.length());
		int index = nameAndContent.indexOf(':');
		return nameAndContent.substring(index + 1);
	}

	private String extractTemplateName(String templateName) {
		String nameAndContent = templateName.substring(GenerationConnectorConstants.STRING_TEMPLATE_LOADER_PREFIX.length());
		int index = nameAndContent.indexOf(':');
		return templateName.substring(0, index + GenerationConnectorConstants.STRING_TEMPLATE_LOADER_PREFIX.length());
	}

	@Override
	public DocumentContent convertDocumentPDF(PDFConverterType type, DocumentContent inputDocument)
			throws DocumentGenerationException {
		// on gère 2 types de convertisseurs PDF
		if (PDFConverterType.FLYING_SAUCER == type) {
			return convertDocumentPDFFlyingSaucer(inputDocument);
		} else {
			return convertDocumentPDFIText(inputDocument);
		}
	}

	/**
	 * 
	 * @param inputDocument
	 * @return
	 * @throws DocumentGenerationException
	 */
	protected DocumentContent convertDocumentPDFIText(DocumentContent inputDocument)
			throws DocumentGenerationException {
		if (inputDocument == null) {
			throw new IllegalArgumentException("Missing input document");
		}

		ensureTargetDirectoryFile();

		File convertedFile = null;
		Document document = null;
		InputStream cssFileInputStream = null;
		FileOutputStream fop = null;
		try {
			convertedFile = File.createTempFile("tmp", ".cnv", new File(temporaryDirectory));
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Temporary convert itext file:{}", convertedFile);
			}

			fop = new FileOutputStream(convertedFile);

			document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, fop);
			document.open();

			URL rootUrl = Thread.currentThread().getContextClassLoader().getResource(freemarkerBaseDirectory);
			if (StringUtils.isNotEmpty(freemarkerCssFile)) {
				cssFileInputStream = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream(freemarkerCssFile);
			}
			XMLWorkerFontProvider xmlWorkerFontProvider = null;
			if (StringUtils.isNotEmpty(freemarkerFontsPath)) {
				xmlWorkerFontProvider = new XMLWorkerFontProvider(freemarkerFontsPath);
			} else {
				xmlWorkerFontProvider = new XMLWorkerFontProvider();
			}

			final TagProcessorFactory tagProcessorFactory = Tags.getHtmlTagProcessorFactory();
			tagProcessorFactory.removeProcessor(HTML.Tag.IMG);
			tagProcessorFactory.addProcessor(new ImageTagProcessor(), HTML.Tag.IMG);

			CustomXMLWorkerHelper customXMLWorkerHelper = CustomXMLWorkerHelper.getInstance();
			customXMLWorkerHelper.setTagProcessorFactory(tagProcessorFactory);
			customXMLWorkerHelper.parseXHtml(writer, document, inputDocument.getFileStream(), cssFileInputStream,
					StandardCharsets.UTF_8, xmlWorkerFontProvider, rootUrl.getFile());
		} catch (Exception e) {
			String message = "Failed to convert document";
			LOGGER.error(message);
			throw new DocumentGenerationException(message, e);
		} finally {
			if (document != null) {
				document.close();
			}
			inputDocument.closeStream();
			if (cssFileInputStream != null) {
				try {
					cssFileInputStream.close();
				} catch (IOException e) {
					LOGGER.warn("Failed to close cssStream", e);
				}
			}
			if (fop != null) {
				try {
					fop.close();
				} catch (IOException e) {
					LOGGER.warn("Failed to close file stream", e);
				}
			}

		}

		return new DocumentContent(
				FilenameUtils.getBaseName(inputDocument.getFileName()) + "." + GenerationFormat.PDF.getExtension(),
				GenerationFormat.PDF.getTypeMime(), convertedFile);
	}

	/**
	 * 
	 * @param inputDocument
	 * @return
	 * @throws DocumentGenerationException
	 */
	protected DocumentContent convertDocumentPDFFlyingSaucer(DocumentContent inputDocument)
			throws DocumentGenerationException {
		if (inputDocument == null) {
			throw new IllegalArgumentException("Missing input document");
		}
		ensureTargetDirectoryFile();

		File convertedFile = null;
		FileOutputStream fop = null;
		try {
			convertedFile = File.createTempFile("tmp", ".cnv", new File(temporaryDirectory));
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Temporary flyingsaucer convert file:{}", convertedFile);
			}
			inputDocument = convertToXhtml(inputDocument);

			ITextRenderer renderer = new ITextRenderer();
			loadFonts(renderer);
			renderer.setDocument(inputDocument.getFile());
			renderer.setScaleToFit(true);
			renderer.layout();

			// Création du fichier PDF
			fop = new FileOutputStream(convertedFile);
			renderer.createPDF(fop);
			fop.close();

		} catch (Exception e) {
			throw new DocumentGenerationException("Failed to flysauve html", e);
		} finally {
			if (fop != null) {
				try {
					fop.close();
				} catch (IOException e) {
					LOGGER.warn("Failed to close file", e);
				}
			}
		}
		return new DocumentContent(
				FilenameUtils.getBaseName(inputDocument.getFileName()) + "." + GenerationFormat.PDF.getExtension(),
				GenerationFormat.PDF.getTypeMime(), convertedFile);
	}

	private void loadFonts(ITextRenderer renderer) {
		try {
			URL url = Thread.currentThread().getContextClassLoader().getResource(freemarkerFontsPath);
			String path = url.toURI().toString();
			if (path.startsWith("jar:")) {
				// si c'est dans le jar
				loadFontJar(renderer, url);
			} else {
				// si c'est dans le système de fichiers
				loadFontFile(renderer, url);
			}
		} catch (Exception e) {
			LOGGER.warn("Failed to load fonts", e);
		}
	}

	private void loadFontJar(ITextRenderer renderer, URL url) {
		String path = url.toString();
		// extraction du nom du jar contenant la ressource
		int index2 = path.lastIndexOf('!');
		String mainJarFile = path.substring(0, index2);
		try (InputStream fip = Thread.currentThread().getContextClassLoader().getResourceAsStream(mainJarFile);
				ZipInputStream zip = new ZipInputStream(fip);) {
			ZipEntry zipEntry = null;
			while ((zipEntry = zip.getNextEntry()) != null) {
				loadFontJar(renderer, zipEntry);

			}
		} catch (IOException e) {
			LOGGER.warn("Failed to open file:" + mainJarFile, e);
		}
	}

	private void loadFontJar(ITextRenderer renderer, ZipEntry zipEntry) throws IOException {
		String name = zipEntry.getName();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Find item:{}", name);
		}
		// si c'est bien une font dans le répertoire avec l'extension supportée
		if (name.startsWith(freemarkerFontsPath) && (name.endsWith(FONT_TTF) || name.endsWith(FONT_OTF))) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Load font:{}", name);
			}
			InputStream inputStream = null;
			try {
				// mise en chache du fichier temporaire car flying saucer supporte pas les
				// fichiers dans les jars pour les fonts
				if (!fontMap.containsKey(name)) {
					inputStream = cacheFont(name);
				}
				renderer.getFontResolver().addFont(fontMap.get(name).getAbsolutePath(), BaseFont.IDENTITY_H,
						BaseFont.EMBEDDED);
			} catch (Exception e) {
				LOGGER.warn("Failed to load font:" + name, e);
			} finally {
				if (inputStream != null) {
					inputStream.close();
				}
			}
		}
	}

	private InputStream cacheFont(String name) throws IOException {
		InputStream inputStream;
		String extension = FilenameUtils.getExtension(name);
		File fontFile = File.createTempFile("font", "." + extension, new File(temporaryDirectory));
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Temporary font file:{}", fontFile);
		}
		ClassLoader parentClassLoader = Thread.currentThread().getContextClassLoader();
		inputStream = parentClassLoader.getResourceAsStream(name);
		FileUtils.copyInputStreamToFile(inputStream, fontFile);
		fontMap.put(name, fontFile);
		return inputStream;
	}

	private void loadFontFile(ITextRenderer renderer, URL url) {
		File root = new File(url.getFile());
		// parcourt des fichiers supportés par flying saucer
		File[] fontFiles = root
				.listFiles((File dir, String fileName) -> fileName.endsWith(FONT_TTF) || fileName.endsWith(FONT_OTF));
		if (ArrayUtils.isNotEmpty(fontFiles)) {
			for (File fontFile : fontFiles) {
				try {
					renderer.getFontResolver().addFont(fontFile.getAbsolutePath(), BaseFont.IDENTITY_H,
							BaseFont.EMBEDDED);
				} catch (Exception e) {
					LOGGER.warn("Failed to load font:" + fontFile, e);
				}
			}
		}
	}

	/**
	 * Formate le contenu HTML.
	 *
	 * @param html contenu html
	 * @return html bien formaté
	 * @throws IOException
	 */
	private DocumentContent convertToXhtml(DocumentContent documentContent) throws IOException {
		File convertedFile = File.createTempFile("tmp", ".cnt", new File(temporaryDirectory));
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Temporary convert xhtml file:{}", convertedFile);
		}
		Tidy tidy = new Tidy();
		tidy.setInputEncoding(StandardCharsets.UTF_8.name());
		tidy.setOutputEncoding(StandardCharsets.UTF_8.name());
		tidy.setXHTML(true);
		try (FileOutputStream fop = new FileOutputStream(convertedFile)) {
			tidy.parseDOM(documentContent.getFileStream(), fop);
			return new DocumentContent(documentContent.getFileName(), documentContent.getContentType(), convertedFile);
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	private void ensureTargetDirectoryFile() throws DocumentGenerationException {
		File targetDirectoryFile = new File(temporaryDirectory);
		if (!targetDirectoryFile.exists()) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Create directory:{}", temporaryDirectory);
			}
			if (!targetDirectoryFile.mkdirs()) {
				throw new DocumentGenerationException("Failed to create directory:" + temporaryDirectory);
			}
		}
	}
}
