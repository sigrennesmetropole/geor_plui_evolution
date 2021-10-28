/**
 * 
 */
package org.georchestra.pluievolution.service.bean;

import java.io.InputStream;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author FNI18300
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"stream"})
public class GeoserverStream {

	private InputStream stream;

	private String mimeType;

	private int status;

}
