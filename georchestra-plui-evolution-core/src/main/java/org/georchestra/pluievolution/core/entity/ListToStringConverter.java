/**
 * 
 */
package org.georchestra.pluievolution.core.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.apache.commons.lang3.StringUtils;

/**
 * @author FNI18300
 *
 */
@Converter
public class ListToStringConverter implements AttributeConverter<List<String>, String> {

	private static final String SEPARATOR = ",";

	@Override
	public String convertToDatabaseColumn(List<String> attribute) {
		if (attribute == null || attribute.isEmpty()) {
			return "";
		}
		return StringUtils.join(attribute, SEPARATOR);
	}

	@Override
	public List<String> convertToEntityAttribute(String dbData) {
		if (dbData == null || dbData.trim().length() == 0) {
			return new ArrayList<>();
		}

		String[] data = dbData.split(SEPARATOR);
		return Arrays.asList(data);
	}
}
