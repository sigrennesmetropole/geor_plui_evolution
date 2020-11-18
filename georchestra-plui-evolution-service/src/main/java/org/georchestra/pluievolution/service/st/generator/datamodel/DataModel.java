package org.georchestra.pluievolution.service.st.generator.datamodel;

import java.io.IOException;
import java.util.Map;

/**
 * Data model pour les données injectées dans les modèles de documents
 * 
 * @author FNI18300
 *
 */
public abstract class DataModel {

	private GenerationFormat format;

	private String modelFileName;

	private String outputFileName;

	/**
	 * Constructeur pour DataModel
	 */
	public DataModel() {
		super();
	}

	/**
	 * Constructeur pour DataModel
	 * 
	 * @param format
	 */
	public DataModel(GenerationFormat format) {
		this(format, null);
	}

	public DataModel(GenerationFormat format, String modelFileName) {
		super();
		this.format = format;
		this.modelFileName = modelFileName;
	}

	/**
	 * renvoie une map permettant de remplacer les variables contenues par le modèle
	 * 
	 * @return une map avec en clé le nom de la variable contenu par le modele et en
	 *         valeur la valeur de la variable correspondante
	 * @throws IOException
	 */
	public abstract Map<Object, Object> getDataModel() throws IOException;

	protected abstract String generateFileName();

	public String getModelFileName() {
		return modelFileName;
	}

	public void setModelFileName(String modelFileName) {
		this.modelFileName = modelFileName;
	}

	public String getOutputFileName() {
		if (outputFileName == null) {
			outputFileName = generateFileName();
		}
		return outputFileName;
	}

	public void setOutputFileName(String outputFileName) {
		this.outputFileName = outputFileName;
	}

	/**
	 * Affectation pour nomFichierResult
	 * 
	 * @param nomFichierResult the nomFichierResult à assigner
	 */
	public void setNomFichierResult(String nomFichierResult) {
		this.outputFileName = nomFichierResult;
	}

	public GenerationFormat getFormat() {
		return format;
	}

	public void setFormat(GenerationFormat format) {
		this.format = format;
	}

	/**
	 * @param input
	 * @return la chaine encodé HTML
	 */
	public String encodeOdt(String input) {
		return DataModelUtils.encodeOdt(input);
	}

	/**
	 * @param input
	 * @return la chaine encodé HTML
	 */
	public String encode(String input) {
		return DataModelUtils.encodeHtml(input);
	}

	/**
	 * @param input
	 * @return la chaine encodé HTML
	 */
	public String encodeMemo(String input) {
		return DataModelUtils.encodeHtmlMemo(input);
	}

	/**
	 * @param input
	 * @return la chaine encodé HTML
	 */
	public String encodeCharset(String input, String charset) {
		try {
			return DataModelUtils.encodeCharset(input, charset);
		} catch (Exception e) {
			return input;
		}
	}

	/**
	 * @param input
	 * @return la chaine trimmée
	 */
	public String trimWhiteChars(String input) {
		return DataModelUtils.trimWhiteChars(input);
	}

	/**
	 * @param input
	 * @param maxlength
	 * @param suspens
	 * @return la chaine tronquée
	 */
	public String truncate(String input, int maxlength, String suspens) {
		return DataModelUtils.truncate(input, maxlength, suspens);
	}

	/**
	 * @param input
	 * @param lineLength
	 * @return
	 */
	public int countLines(String input, int lineLength) {
		return DataModelUtils.countLines(input, lineLength);
	}

	/**
	 * @param input
	 * @param maxLines
	 * @param lineLength
	 * @param suspens
	 * @return la chaine tronquées en nombre de lignes
	 */
	public String truncateLines(String input, int maxLines, int lineLength, String suspens) {
		return DataModelUtils.truncateLines(input, maxLines, lineLength, suspens);
	}

	/**
	 * @param map
	 * @param key
	 * @return une item d'une map
	 */
	public Object mapGet(Map<?, ?> map, Object key) {
		return DataModelUtils.mapGet(map, key);
	}
}
