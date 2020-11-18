/**
 * 
 */
package org.georchestra.pluievolution.service.st.generator.datamodel;

/**
 * @author FNI18300
 *
 */
public enum GenerationFormat {

	/** Mime-type PDF */
	PDF("application/pdf", "pdf"),
	/** Mime-type Excel */
	EXCEL("application/vnd.ms-excel", "xls"),
	/** Mime-type HTML */
	HTML("text/html", "html"),
	/** Mime-type Text */
	TEXT("text/plain", "txt"),
	/** Mime-type CSV */
	CSV("text/csv", "csv"),
	/** Mime-type ZIP */
	ZIP("application/zip", "zip"),
	/** Mime type JPEG */
	JPEG("image/jpg", "jpg"),
	/** Mime type PNG */
	PNG("image/png", "png"),
	/** Mime type GIF */
	GIF("image/gif", "gif");

	private String typeMime;

	private String extension;

	/**
	 * Constructeur pour FormatDocumentEnum
	 * 
	 * @param format
	 */
	private GenerationFormat(String format, String extension) {
		this.typeMime = format;
		this.extension = extension;
	}

	public String getExtension() {
		return extension;
	}

	public String getTypeMime() {
		return typeMime;
	}

	/**
	 * @param prefix
	 * @return le nom d'un fichier avec son extension
	 */
	public String generateFileName(String prefix) {
		if (prefix == null) {
			throw new IllegalArgumentException("le nom du fichier ne peut être null");
		}
		return new StringBuffer(prefix).append('.').append(getExtension()).toString();
	}

	/**
	 * @param typeMime
	 * @return le format pour le type mime
	 */
	public static GenerationFormat lookupFromMimeType(String typeMime) {
		GenerationFormat result = null;
		for (GenerationFormat formatDocumentEnum : values()) {
			if (formatDocumentEnum.getTypeMime().equalsIgnoreCase(typeMime)) {
				result = formatDocumentEnum;
			}
		}
		return result;
	}

	/**
	 * @param extension
	 * @return le format pour l'extension
	 */
	public static GenerationFormat lookupFromExtension(String extension) {
		GenerationFormat result = null;
		for (GenerationFormat formatDocumentEnum : values()) {
			if (formatDocumentEnum.getExtension().equalsIgnoreCase(extension)) {
				result = formatDocumentEnum;
			}
		}
		return result;
	}
}
