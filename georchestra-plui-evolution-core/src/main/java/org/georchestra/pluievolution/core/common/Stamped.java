/**
 * 
 */
package org.georchestra.pluievolution.core.common;

import java.util.Date;

/**
 * @author FNI18300
 *
 */
public interface Stamped {

	Date getOpeningDate();

	void setOpeningDate(Date d);

	Date getClosingDate();

	void setClosingDate(Date d);
}
