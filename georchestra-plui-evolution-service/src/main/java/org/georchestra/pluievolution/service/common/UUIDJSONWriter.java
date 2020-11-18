/**
 * 
 */
package org.georchestra.pluievolution.service.common;

import java.io.IOException;
import java.util.UUID;

import net.minidev.json.JSONStyle;
import net.minidev.json.reader.JsonWriterI;

/**
 * @author FNI18300
 *
 */
public class UUIDJSONWriter implements JsonWriterI<UUID> {

	@Override
	public <E extends UUID> void writeJSONString(E value, Appendable out, JSONStyle compression) throws IOException {
		if (value == null) {
			if (!compression.ignoreNull()) {
				out.append("null");
			}
		} else {
			out.append('"').append(value.toString()).append('"');
		}
	}

}
