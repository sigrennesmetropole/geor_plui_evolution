/**
 * 
 */
package org.georchestra.pluievolution.core.common;

/**
 * @author fni18300
 *
 */
public interface ProcessLinkEntity extends LongId {

	String getProcessDefinitionId();

	void setProcessDefinitionId(String processDefinitionId);

	Integer getRevision();

	void setRevision(Integer revision);

	String getUserTaskId();

	void setUserTaskId(String userTaskId);

}
