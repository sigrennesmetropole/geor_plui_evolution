package org.georchestra.pluievolution.service.st.generator.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.Document;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.FontProvider;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.ElementHandler;
import com.itextpdf.tool.xml.ElementList;
import com.itextpdf.tool.xml.Pipeline;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CSSFileWrapper;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.css.CssFileProcessor;
import com.itextpdf.tool.xml.css.CssFilesImpl;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.exceptions.RuntimeWorkerException;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.TagProcessorFactory;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.ElementHandlerPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

/**
 * 
 * @author FNI18300
 * @see original file {XMLWorkerHelper}
 */
public class CustomXMLWorkerHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomXMLWorkerHelper.class);

	private static final String DEFAULT_CSS = "/default.css";
	private static CustomXMLWorkerHelper myself = new CustomXMLWorkerHelper();
	private TagProcessorFactory tagProcessorFactory;
	private CssFile defaultCssFile;

	public void setTagProcessorFactory(TagProcessorFactory tagProcessorFactory) {
		this.tagProcessorFactory = tagProcessorFactory;
	}

	public static synchronized CustomXMLWorkerHelper getInstance() {
		return myself;
	}

	private CustomXMLWorkerHelper() {
	}

	public static synchronized CssFile getCSS(InputStream in) {
		CssFile cssFile = null;
		if (null != in) {
			CssFileProcessor cssFileProcessor = new CssFileProcessor();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			try {
				char[] buffer = new char[8192];

				int length;
				while ((length = br.read(buffer)) > 0) {
					for (int i = 0; i < length; ++i) {
						cssFileProcessor.process(buffer[i]);
					}
				}

				cssFile = new CSSFileWrapper(cssFileProcessor.getCss(), true);
			} catch (IOException var14) {
				throw new RuntimeWorkerException(var14);
			} finally {
				try {
					in.close();
				} catch (IOException var13) {
					LOGGER.warn("Failed to close stream.", var13);
				}
			}
		}

		return cssFile;
	}

	public synchronized CssFile getDefaultCSS() {
		if (null == this.defaultCssFile) {
			this.defaultCssFile = getCSS(XMLWorkerHelper.class.getResourceAsStream(DEFAULT_CSS));
		}

		return this.defaultCssFile;
	}

	public void parseXHtml(ElementHandler d, Reader in) throws IOException {
		CssFilesImpl cssFiles = new CssFilesImpl();
		cssFiles.add(this.getDefaultCSS());
		StyleAttrCSSResolver cssResolver = new StyleAttrCSSResolver(cssFiles);
		HtmlPipelineContext hpc = new HtmlPipelineContext((CssAppliers) null);
		hpc.setAcceptUnknown(true).autoBookmark(true).setTagFactory(this.getDefaultTagProcessorFactory());
		@SuppressWarnings("rawtypes")
		Pipeline<?> pipeline = new CssResolverPipeline(cssResolver,
				new HtmlPipeline(hpc, new ElementHandlerPipeline(d, (Pipeline) null)));
		XMLWorker worker = new XMLWorker(pipeline, true);
		XMLParser p = new XMLParser();
		p.addListener(worker);
		p.parse(in);
	}

	public void parseXHtml(PdfWriter writer, Document doc, Reader in) throws IOException {
		CssFilesImpl cssFiles = new CssFilesImpl();
		cssFiles.add(this.getDefaultCSS());
		StyleAttrCSSResolver cssResolver = new StyleAttrCSSResolver(cssFiles);
		HtmlPipelineContext hpc = new HtmlPipelineContext((CssAppliers) null);
		hpc.setAcceptUnknown(true).autoBookmark(true).setTagFactory(this.getDefaultTagProcessorFactory());
		Pipeline<?> pipeline = new CssResolverPipeline(cssResolver,
				new HtmlPipeline(hpc, new PdfWriterPipeline(doc, writer)));
		XMLWorker worker = new XMLWorker(pipeline, true);
		XMLParser p = new XMLParser();
		p.addListener(worker);
		p.parse(in);
	}

	public void parseXHtml(PdfWriter writer, Document doc, InputStream in) throws IOException {
		this.parseXHtml(writer, doc, in, XMLWorkerHelper.class.getResourceAsStream(DEFAULT_CSS), (Charset) null,
				new XMLWorkerFontProvider());
	}

	public void parseXHtml(PdfWriter writer, Document doc, InputStream in, Charset charset, FontProvider fontProvider)
			throws IOException {
		this.parseXHtml(writer, doc, in, XMLWorkerHelper.class.getResourceAsStream(DEFAULT_CSS), charset, fontProvider);
	}

	public void parseXHtml(PdfWriter writer, Document doc, InputStream in, Charset charset) throws IOException {
		this.parseXHtml(writer, doc, in, XMLWorkerHelper.class.getResourceAsStream(DEFAULT_CSS), charset);
	}

	public void parseXHtml(PdfWriter writer, Document doc, InputStream in, InputStream inCssFile, Charset charset,
			FontProvider fontProvider) throws IOException {
		this.parseXHtml(writer, doc, in, inCssFile, charset, fontProvider, (String) null);
	}

	public void parseXHtml(PdfWriter writer, Document doc, InputStream in, InputStream inCssFile, Charset charset,
			FontProvider fontProvider, String resourcesRootPath) throws IOException {
		CssFilesImpl cssFiles = new CssFilesImpl();
		if (inCssFile != null) {
			cssFiles.add(getCSS(inCssFile));
		} else {
			cssFiles.add(this.getDefaultCSS());
		}

		StyleAttrCSSResolver cssResolver = new StyleAttrCSSResolver(cssFiles);
		HtmlPipelineContext hpc = new HtmlPipelineContext(new CssAppliersImpl(fontProvider));
		hpc.setAcceptUnknown(true).autoBookmark(true).setTagFactory(this.getDefaultTagProcessorFactory())
				.setResourcesRootPath(resourcesRootPath);
		HtmlPipeline htmlPipeline = new HtmlPipeline(hpc, new PdfWriterPipeline(doc, writer));
		Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, htmlPipeline);
		XMLWorker worker = new XMLWorker(pipeline, true);
		XMLParser p = new XMLParser(true, worker, charset);
		if (charset != null) {
			p.parse(in, charset);
		} else {
			p.parse(in);
		}

	}

	public void parseXHtml(PdfWriter writer, Document doc, InputStream in, InputStream inCssFile) throws IOException {
		this.parseXHtml(writer, doc, in, inCssFile, (Charset) null, new XMLWorkerFontProvider());
	}

	public void parseXHtml(PdfWriter writer, Document doc, InputStream in, InputStream inCssFile,
			FontProvider fontProvider) throws IOException {
		this.parseXHtml(writer, doc, in, inCssFile, (Charset) null, fontProvider);
	}

	public void parseXHtml(PdfWriter writer, Document doc, InputStream in, InputStream inCssFile, Charset charset)
			throws IOException {
		this.parseXHtml(writer, doc, in, inCssFile, charset, new XMLWorkerFontProvider());
	}

	public void parseXHtml(ElementHandler d, InputStream in, Charset charset) throws IOException {
		CssFilesImpl cssFiles = new CssFilesImpl();
		cssFiles.add(this.getDefaultCSS());
		StyleAttrCSSResolver cssResolver = new StyleAttrCSSResolver(cssFiles);
		HtmlPipelineContext hpc = new HtmlPipelineContext((CssAppliers) null);
		hpc.setAcceptUnknown(true).autoBookmark(true).setTagFactory(this.getDefaultTagProcessorFactory());
		@SuppressWarnings("rawtypes")
		Pipeline<?> pipeline = new CssResolverPipeline(cssResolver,
				new HtmlPipeline(hpc, new ElementHandlerPipeline(d, (Pipeline) null)));
		XMLWorker worker = new XMLWorker(pipeline, true);
		XMLParser p = new XMLParser(true, worker, charset);
		if (charset != null) {
			p.parse(in, charset);
		} else {
			p.parse(in);
		}

	}

	public CSSResolver getDefaultCssResolver(boolean addDefaultCss) {
		CSSResolver resolver = new StyleAttrCSSResolver();
		if (addDefaultCss) {
			resolver.addCss(this.getDefaultCSS());
		}

		return resolver;
	}

	protected synchronized TagProcessorFactory getDefaultTagProcessorFactory() {
		if (null == this.tagProcessorFactory) {
			this.tagProcessorFactory = Tags.getHtmlTagProcessorFactory();
		}

		return this.tagProcessorFactory;
	}

	public static ElementList parseToElementList(String html, String css) throws IOException {
		CSSResolver cssResolver = new StyleAttrCSSResolver();
		if (css != null) {
			CssFile cssFile = getCSS(new ByteArrayInputStream(css.getBytes()));
			cssResolver.addCss(cssFile);
		}

		CssAppliers cssAppliers = new CssAppliersImpl(FontFactory.getFontImp());
		HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
		htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
		htmlContext.autoBookmark(false);
		ElementList elements = new ElementList();
		ElementHandlerPipeline end = new ElementHandlerPipeline(elements, (Pipeline) null);
		HtmlPipeline htmlPipeline = new HtmlPipeline(htmlContext, end);
		CssResolverPipeline cssPipeline = new CssResolverPipeline(cssResolver, htmlPipeline);
		XMLWorker worker = new XMLWorker(cssPipeline, true);
		XMLParser p = new XMLParser(worker);
		p.parse(new ByteArrayInputStream(html.getBytes()));
		return elements;
	}

}
