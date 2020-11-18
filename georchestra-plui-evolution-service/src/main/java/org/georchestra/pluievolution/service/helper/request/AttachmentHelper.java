/**
 * 
 */
package org.georchestra.pluievolution.service.helper.request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.georchestra.pluievolution.core.dto.AttachmentConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author FNI18300
 *
 */
@Component
public class AttachmentHelper {

	private static final String KB_UNIT = "KB";

	private static final String MB_UNIT = "MB";

	private static final String GB_UNIT = "GB";

	private Map<String, Long> factorUnits;

	@Value("${attachment.max-count:5}")
	private int attachmentMaxCount;

	@Value("${spring.servlet.multipart.max-file-size}")
	private String attachmentMaxFileSize;

	@Value("#{'${attachment.mime-types}'.split(',')}")
	private List<String> attachmentMimeTypes;

	public int getAttachmentMaxCount() {
		return attachmentMaxCount;
	}

	public long getAttachmentMaxFileSize() {
		if (StringUtils.isNumeric(attachmentMaxFileSize)) {
			return Long.parseLong(attachmentMaxFileSize);
		} else {
			long maxSize = Long.parseLong(attachmentMaxFileSize.substring(0, attachmentMaxFileSize.length() - 2));
			String unit = attachmentMaxFileSize.substring(attachmentMaxFileSize.length() - 2);
			if (unit != null) {
				maxSize *= factorUnits.get(unit.toUpperCase());
			}
			return maxSize;
		}
	}

	public List<String> getAttachmentMimeTypes() {
		return attachmentMimeTypes;
	}

	public boolean acceptAttachmentMimeType(String mimeType) {
		return attachmentMimeTypes.contains(mimeType);
	}

	public AttachmentConfiguration getAttachmentConfiguration() {
		AttachmentConfiguration result = new AttachmentConfiguration();
		result.setMaxCount(getAttachmentMaxCount());
		result.setMimeTypes(getAttachmentMimeTypes());
		result.setMaxSize(getAttachmentMaxFileSize());
		return result;
	}

	@PostConstruct
	private void initializeFactorUnits() {
		factorUnits = new HashMap<>();
		factorUnits.put(KB_UNIT, 1024L);
		factorUnits.put(MB_UNIT, factorUnits.get(KB_UNIT) * 1024L);
		factorUnits.put(GB_UNIT, factorUnits.get(MB_UNIT) * 1024L);
	}

}
