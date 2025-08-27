package org.georchestra.pluievolution.service.acl.impl;

import java.util.List;

import org.georchestra.pluievolution.core.dao.acl.GeographicEtablissementDao;
import org.georchestra.pluievolution.core.dto.GeographicArea;
import org.georchestra.pluievolution.core.dto.GeographicEtablissement;
import org.georchestra.pluievolution.core.entity.acl.GeographicEtablissementEntity;
import org.georchestra.pluievolution.service.acl.GeographicAreaService;
import org.georchestra.pluievolution.service.acl.GeographicEtablissementService;
import org.georchestra.pluievolution.service.exception.ApiServiceException;
import org.georchestra.pluievolution.service.exception.ApiServiceExceptionsStatus;
import org.georchestra.pluievolution.service.helper.authentification.AuthentificationHelper;
import org.georchestra.pluievolution.service.mapper.GeographicEtablissementMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GeographicEtablissementServiceImpl implements GeographicEtablissementService {

	private final GeographicEtablissementDao geographicEtablissementDao;

	private final GeographicEtablissementMapper geographicEtablissementMapper;

	private final GeographicAreaService geographicAreaService;

	private final AuthentificationHelper authentificationHelper;

	@Override
	public GeographicEtablissement getGeographicEtablissementByCodeInsee(String codeInsee) {
		return geographicEtablissementMapper.entityToDto(geographicEtablissementDao.findByCodeInsee(codeInsee));
	}

	@Override
	public List<GeographicEtablissement> searchEtablissements() {
		List<GeographicEtablissementEntity> geographicAreaEntities = geographicEtablissementDao.findAll();
		return geographicEtablissementMapper.entitiesToDto(geographicAreaEntities);
	}

	@Override
	public GeographicEtablissement getCurrentUserEtablissement() throws ApiServiceException {
		// nom organisation == nom geographic area
		// on recupere donc la geographic area a partir du nom
		String nom = authentificationHelper.getOrganisation();
		GeographicArea geographicArea = geographicAreaService.getGeographicAreaByNom(nom);
		GeographicEtablissementEntity entity = geographicEtablissementDao
				.findByCodeInsee(geographicArea.getCodeInsee());

		if (entity == null) {
			throw new ApiServiceException("Organisation inconnue", ApiServiceExceptionsStatus.NOT_FOUND);
		}
		return geographicEtablissementMapper.entityToDto(entity);
	}
}
