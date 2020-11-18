package org.georchestra.pluievolution.core.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DocumentContent {
	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentContent.class);

	private String url;
	private String fileName;
	private long fileSize;
	private File file;
	private String contentType;
	private InputStream fileStream;

	/**
	 * Constructeur par défaut
	 * 
	 */
	public DocumentContent() {
		super();
	}

	/**
	 * Constructeur pour DocumentContent
	 * 
	 * @param file
	 */
	public DocumentContent(File file) {
		this(null, file);
	}

	/**
	 * Constructeur pour DocumentContent
	 * 
	 * @param contentType
	 * @param file
	 */
	public DocumentContent(String contentType, File file) {
		this(file != null ? file.getName() : null, contentType, file);
	}

	/**
	 * Constructeur pour DocumentContent @param fileName
	 * 
	 * @param contentType
	 * @param file
	 */
	public DocumentContent(String fileName, String contentType, File file) {
		this(fileName, contentType, file != null ? file.length() : -1, file);
	}

	/**
	 * Constructeur pour DocumentContent
	 * 
	 * @param fileName
	 * @param contentType
	 * @param fileSize
	 * @param fileStream
	 */
	public DocumentContent(String fileName, String contentType, long fileSize, InputStream fileStream) {
		super();
		this.fileName = fileName;
		this.fileSize = fileSize;
		this.contentType = contentType;
		this.fileStream = fileStream;
	}

	/**
	 * Constructeur pour DocumentContent
	 * 
	 * @param fileName
	 * @param contentType
	 * @param fileSize
	 * @param file
	 */
	public DocumentContent(String fileName, String contentType, long fileSize, File file) {
		super();
		this.fileName = fileName;
		this.fileSize = fileSize;
		this.contentType = contentType;
		this.file = file;
	}

	/**
	 * @return vrai si contient un fichier
	 */
	public boolean isFile() {
		return file != null;
	}

	/**
	 * @return vrai si url
	 */
	public boolean isURL() {
		return url != null;
	}

	/**
	 * @return vrai si est un stream
	 */
	public boolean isStream() {
		return fileStream != null;
	}

	/**
	 * Accesseur pour url
	 * 
	 * @return le url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Affectation pour url
	 * 
	 * @param urlFichier the url à assigner
	 */
	public void setUrl(String urlFichier) {
		this.url = urlFichier;
	}

	/**
	 * Accesseur pour fileName
	 * 
	 * @return le fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Affectation pour fileName
	 * 
	 */
	public void setFileName(String nomFichier) {
		this.fileName = nomFichier;
	}

	/**
	 * Accesseur pour fileSize
	 * 
	 * @return le fileSize
	 */
	public long getFileSize() {
		return fileSize;
	}

	/**
	 * Affectation pour fileSize
	 * 
	 * @param tailleFichier
	 */
	public void setFileSize(long tailleFichier) {
		this.fileSize = tailleFichier;
	}

	/**
	 * Accesseur pour fileStream
	 * 
	 * @return le fileStream
	 * @throws FileNotFoundException
	 */
	public InputStream getFileStream() throws FileNotFoundException {
		if (fileStream == null && file != null) {
			fileStream = new FileInputStream(file);
		}
		return fileStream;
	}

	/**
	 * cloture du flux s'il est ouvert
	 */
	public void closeStream() {
		if (fileStream != null) {
			try {
				fileStream.close();
				fileStream = null;
			} catch (Exception e) {
				LOGGER.debug("Impossible de clore le flux : " + toString(), e);
			}
		}
	}

	/**
	 * Affectation pour file
	 * 
	 * @param file le file à assigner
	 */
	public void setFile(File file) {
		this.file = file;
	}

	public File getFile() {
		return file;
	}

	public void setFileStream(InputStream fileStream) {
		this.fileStream = fileStream;
	}

	/**
	 * Accesseur pour contentType
	 * 
	 * @return le contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * Affectation pour contentType
	 * 
	 * @param contentType the contentType à assigner
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@Override
	public String toString() {
		return "DocumentContent [" + (url != null ? "url=" + url + ", " : "")
				+ (fileName != null ? "fileName=" + fileName + ", " : "") + "fileSize=" + fileSize + ", "
				+ (file != null ? "file=" + file + ", " : "")
				+ (contentType != null ? "contentType=" + contentType + ", " : "")
				+ (fileStream != null ? "fileStream=" + fileStream : "") + "]";
	}

}