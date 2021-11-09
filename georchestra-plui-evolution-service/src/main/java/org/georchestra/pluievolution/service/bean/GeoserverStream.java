/**
 * 
 */
package org.georchestra.pluievolution.service.bean;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author FNI18300
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeoserverStream {

	private InputStream stream;

	private String content;

	private String mimeType;

	private int status;

	public String getContent() throws IOException {
		if (content != null) {
			return content;
		} else if (stream != null) {
			return IOUtils.toString(getStream(), StandardCharsets.UTF_8);
		} else {
			return null;
		}
	}

}
