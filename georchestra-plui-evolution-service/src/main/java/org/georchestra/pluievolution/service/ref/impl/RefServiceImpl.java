package org.georchestra.pluievolution.service.ref.impl;

import org.georchestra.pluievolution.core.dao.ref.PluiRequestStatusDao;
import org.georchestra.pluievolution.core.dao.ref.PluiRequestTypeDao;
import org.georchestra.pluievolution.core.dto.PluiRequestStatus;
import org.georchestra.pluievolution.core.dto.PluiRequestStatusEnum;
import org.georchestra.pluievolution.core.dto.PluiRequestType;
import org.georchestra.pluievolution.core.dto.PluiRequestTypeEnum;
import org.georchestra.pluievolution.core.entity.ref.PluiRequestStatusEntity;
import org.georchestra.pluievolution.core.entity.ref.PluiRequestTypeEntity;
import org.georchestra.pluievolution.service.mapper.PluiRequestStatusMapper;
import org.georchestra.pluievolution.service.mapper.PluiRequestTypeMapper;
import org.georchestra.pluievolution.service.ref.RefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RefServiceImpl implements RefService {

    @Autowired
    PluiRequestStatusDao pluiRequestStatusDao;

    @Autowired
    PluiRequestTypeDao pluiRequestTypeDao;

    @Autowired
    PluiRequestStatusMapper pluiRequestStatusMapper;

    @Autowired
    PluiRequestTypeMapper pluiRequestTypeMapper;

    @Override
    public List<PluiRequestStatus> getAllRequestStatus() {
        return pluiRequestStatusMapper.entitiesToDto(
                pluiRequestStatusDao.findAll()
        );
    }

    @Override
    public PluiRequestStatus getRequestStatusByValue(PluiRequestStatusEnum value) {
        return pluiRequestStatusMapper.entityToDto(
                pluiRequestStatusDao.findByValue(PluiRequestStatusEntity.fromValue(value.toString()))
        );
    }

    @Override
    public List<PluiRequestType> getAllRequestType() {
        return pluiRequestTypeMapper.entitiesToDto(
                pluiRequestTypeDao.findAll()
        );
    }

    @Override
    public PluiRequestType getRequestTypeByValue(PluiRequestTypeEnum value) {
        return pluiRequestTypeMapper.entityToDto(
                pluiRequestTypeDao.findByValue(PluiRequestTypeEntity.fromValue(value.toString()))
        );
    }
}
