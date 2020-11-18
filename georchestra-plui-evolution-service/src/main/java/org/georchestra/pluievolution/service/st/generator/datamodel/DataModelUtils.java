package org.georchestra.pluievolution.service.st.generator.datamodel;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.ext.beans.HashAdapter;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateHashModel;

/**
 * Classe utilitaire pour la gestion des données du modèle
 * 
 * @author FNI18300
 *
 */
public final class DataModelUtils {

	private static final String NEW_LINE = "\n";
	private static final String BR = "<br/>";
	private static final Logger LOGGER = LoggerFactory.getLogger(DataModelUtils.class);

	/**
	 * 
	 * Constructeur pour DataModelUtils privé car classe utile
	 */
	private DataModelUtils() {

	}

	/**
	 * Encode chaine de charactere pour les template
	 * 
	 * @param chaine
	 * @return
	 */
	public static String encodeOdt(String chaine) {
		String result = chaine;
		result = StringUtils.replace(result, "&", "&amp;");
		result = StringUtils.replace(result, "<", "&lt;");

		return result;
	}

	/**
	 * Encode chaine de charactere pour les template
	 * 
	 * @param chaine
	 * @return
	 */
	public static String encodeHtmlAccent(String chaine) {
		String result = chaine;
		result = StringUtils.replace(result, "&", "&amp;");
		result = StringUtils.replace(result, "â", "&acirc;");
		result = StringUtils.replace(result, "à", "&agrave;");
		result = StringUtils.replace(result, "ä", "&auml;");
		result = StringUtils.replace(result, "é", "&eacute;");
		result = StringUtils.replace(result, "ê", "&ecirc;");
		result = StringUtils.replace(result, "è", "&egrave;");
		result = StringUtils.replace(result, "ë", "&euml;");
		result = StringUtils.replace(result, "î", "&icirc;");
		result = StringUtils.replace(result, "ï", "&iuml;");
		result = StringUtils.replace(result, "ô", "&ocirc;");
		result = StringUtils.replace(result, "ö", "&ouml;");
		result = StringUtils.replace(result, "œ", "&oelig;");
		result = StringUtils.replace(result, "û", "&ucirc;");
		result = StringUtils.replace(result, "ù", "&ugrave;");
		result = StringUtils.replace(result, "ü", "&uuml;");
		result = StringUtils.replace(result, "ç", "&ccedil;");
		result = StringUtils.replace(result, "£", "&pound;");

		return result;
	}

	/**
	 * Encode les \n\r\t en html
	 * 
	 * @param chaine
	 * @return la chaine encodée
	 */
	public static String encodeHtmlWhiteSpace(String chaine) {
		String result = chaine;
		result = StringUtils.replace(result, "\n\r", BR);
		result = StringUtils.replace(result, "\r", BR);
		result = StringUtils.replace(result, NEW_LINE, BR);
		result = StringUtils.replace(result, "\t", "&nbsp;&nbsp;");

		return result;
	}

	/**
	 * Encode chaine de charactere pour les template
	 * 
	 * @param chaine
	 * @return
	 */
	public static String encodeHtml(String chaine) {
		String result = encodeHtmlAccent(chaine);
		result = StringUtils.replace(result, "<", "&lt;");

		return result;
	}

	/**
	 * Encode les \n\r\t en html
	 * 
	 * @param chaine
	 * @return la chaine encodée
	 */
	public static String encodeHtmlMemo(String chaine) {
		String result = encodeHtml(chaine);
		return encodeHtmlWhiteSpace(result);
	}

	/**
	 * @param chaine
	 * @param charset
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String encodeCharset(String chaine, String charset) throws UnsupportedEncodingException {
		return new String(chaine.getBytes(StandardCharsets.UTF_8), charset);
	}

	/**
	 * @param map
	 * @param key
	 * @return un item d'une map
	 */
	public static Object mapGet(Map<?, ?> map, Object key) {
		Object result = null;
		if (map != null) {
			try {
				if (map instanceof HashAdapter) {
					TemplateHashModel templateModel = (TemplateHashModel) ((HashAdapter) map).getTemplateModel();
					if (templateModel instanceof SimpleHash) {
						Map<?, ?> innerMap = ((SimpleHash) templateModel).toMap();
						result = innerMap.get(key);
					}
				}
			} catch (Exception e) {
				LOGGER.debug("Impossible de faire un get sur la map", e);
			}
			if (result == null) {
				result = map.get(key);
			}
		}
		return result;
	}

	/**
	 * @param input
	 * @return la chaine en supprimant les caractères blancs ou \n\t\r aux
	 *         extrémités.
	 */
	public static String trimWhiteChars(String input) {
		String result = null;
		if (input != null) {
			result = input.trim();
			int st = 0;
			int len = result.length();

			while ((st < len)
					&& (result.charAt(st) == '\n' || result.charAt(st) == '\t' || result.charAt(st) == '\r')) {
				st++;
			}
			while ((st < len) && (result.charAt(len - 1) == '\n' || result.charAt(len - 1) == '\t'
					|| result.charAt(len - 1) == '\r')) {
				len--;
			}
			if (st > 0 || len < result.length()) {
				result = result.substring(st, len);
			}
		}
		return result;
	}

	/**
	 * @param input
	 * @param maxlength
	 * @param suspens
	 * @return la chaine tronquée au nombre de catactères demandés avec le chaine de
	 *         suspension.
	 */
	public static String truncate(String input, int maxlength, String suspens) {
		String result = input;
		if (result != null && result.length() > maxlength) {
			int ls = 0;
			if (suspens != null) {
				ls = suspens.length();
			}
			result = result.substring(0, maxlength - ls);
			if (suspens != null) {
				result += suspens;
			}
		}
		return result;
	}

	/**
	 * @param input
	 * @param lineLength
	 * @return le nombre de lignes en tenant compte de la longueur d'une ligne
	 */
	public static int countLines(String input, int lineLength) {
		int result = 0;
		if (input != null) {
			String[] lines = input.split(NEW_LINE);
			for (int i = 0; i < lines.length; i++) {
				result += Math.ceil(((double) lines[i].length()) / ((double) lineLength));
			}
		}
		return result;
	}

	/**
	 * @param input      la chaine
	 * @param maxLines   le nombre de lignes max
	 * @param lineLength la longueur d'une ligne
	 * @param suspens    le texte mis au bout de la chaine (la longueur est prise en
	 *                   compte pour le résultat total
	 * @return la chaine tronquées en nombre de lignes
	 */
	public static String truncateLines(String input, int maxLines, int lineLength, String suspens) {
		StringBuilder result = null;
		int countLines = 0;
		if (input != null) {
			int c = countLines(input, lineLength);
			result = new StringBuilder();
			String[] lines = input.split(NEW_LINE);
			for (int i = 0; i < lines.length; i++) {
				countLines = handleTruncateLine(result, lines[i], suspens, maxLines, lineLength, c, countLines, i);
			}
		}
		return result != null ? result.toString() : null;
	}

	private static int handleTruncateLine(StringBuilder result, String line, String suspens, int maxLines,
			int lineLength, int totalLine, int countLines, int index) {
		int currentCountLines = (int) Math.ceil(((double) line.length()) / ((double) lineLength));
		if (countLines + currentCountLines > maxLines - 1) {
			int trailingLines = maxLines - countLines;
			if (countLines + trailingLines == totalLine) {
				suspens = null;
			}
			int currentMaxLength = trailingLines * lineLength - (suspens != null ? suspens.length() : 0);
			if (result.length() > 0) {
				result.append(NEW_LINE);
			}
			result.append(line.substring(0, Math.min(currentMaxLength, line.length())));
			if (suspens != null) {
				result.append(suspens);
			}
			countLines += trailingLines;
		} else {
			countLines += currentCountLines;
			if (index > 0) {
				result.append(NEW_LINE);
			}
			result.append(line);
		}
		return countLines;
	}

}
