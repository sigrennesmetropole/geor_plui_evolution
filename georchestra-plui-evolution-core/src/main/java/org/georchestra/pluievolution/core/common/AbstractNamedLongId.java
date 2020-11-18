/**
 * 
 */
package org.georchestra.pluievolution.core.common;

/**
 * @author FNI18300
 *
 */
public abstract class AbstractNamedLongId implements LongId, Named {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof AbstractNamedLongId && getClass().equals(obj.getClass()))) {
			return false;
		}
		AbstractNamedLongId other = (AbstractNamedLongId) obj;
		if (getId() == null && getId().equals(other.getId())) {
			return true;
		}
		if (getName() == null) {
			if (other.getName() != null) {
				return false;
			}
		} else if (!getName().equals(other.getName())) {
			return false;
		}
		return true;
	}
}
