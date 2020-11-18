/**
 * 
 */
package org.georchestra.pluievolution.core.common;

/**
 * @author fni18300
 *
 */
public abstract class AbstractProcessLinkEntity implements ProcessLinkEntity {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		result = prime * result + ((getProcessDefinitionId() == null) ? 0 : getProcessDefinitionId().hashCode());
		result = prime * result + ((getRevision() == null) ? 0 : getRevision().hashCode());
		result = prime * result + ((getUserTaskId() == null) ? 0 : getUserTaskId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof AbstractProcessLinkEntity)) {
			return false;
		}
		AbstractProcessLinkEntity other = (AbstractProcessLinkEntity) obj;
		if (getId() != null && getId().equals(other.getId())) {
			return true;
		}
		if (getProcessDefinitionId() == null) {
			if (other.getProcessDefinitionId() != null) {
				return false;
			}
		} else if (!getProcessDefinitionId().equals(other.getProcessDefinitionId())) {
			return false;
		}
		if (getRevision() == null) {
			if (other.getRevision() != null) {
				return false;
			}
		} else if (!getRevision().equals(other.getRevision())) {
			return false;
		}
		if (getUserTaskId() == null) {
			if (other.getUserTaskId() != null) {
				return false;
			}
		} else if (!getUserTaskId().equals(other.getUserTaskId())) {
			return false;
		}
		return true;
	}

}
