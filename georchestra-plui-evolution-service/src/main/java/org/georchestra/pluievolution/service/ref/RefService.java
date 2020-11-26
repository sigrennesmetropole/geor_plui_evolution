package org.georchestra.pluievolution.service.ref;

import org.georchestra.pluievolution.core.dto.PluiRequestStatus;
import org.georchestra.pluievolution.core.dto.PluiRequestStatusEnum;
import org.georchestra.pluievolution.core.dto.PluiRequestType;
import org.georchestra.pluievolution.core.dto.PluiRequestTypeEnum;

import java.util.List;

public interface RefService {
    /**
     * Permet de trouver la liste des status de PluiRequest
     * @return
     */
    List<PluiRequestStatus> getAllRequestStatus();

    /**
     * Permet de trouver un status (id + value) de PluiRequest
     * @param value
     * @return
     */
    PluiRequestStatus getRequestStatusByValue(PluiRequestStatusEnum value);

    /**
     * Permet de trouver la liste des type de PluiRequest
     * @return
     */
    List<PluiRequestType> getAllRequestType();

    /**
     * Permet de trouver un type (id + value) de PluiRequest
     * @param value
     * @return
     */
    PluiRequestType getRequestTypeByValue(PluiRequestTypeEnum value);
}
