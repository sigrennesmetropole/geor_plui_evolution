package org.georchestra.pluievolution.service.acl.impl;

import java.util.List;

import org.georchestra.pluievolution.core.dao.acl.GeographicAreaDao;
import org.georchestra.pluievolution.core.dto.GeographicArea;
import org.georchestra.pluievolution.core.entity.acl.GeographicAreaEntity;
import org.georchestra.pluievolution.service.acl.GeographicAreaService;
import org.georchestra.pluievolution.service.exception.ApiServiceException;
import org.georchestra.pluievolution.service.exception.ApiServiceExceptionsStatus;
import org.georchestra.pluievolution.service.helper.authentification.AuthentificationHelper;
import org.georchestra.pluievolution.service.mapper.GeographicAreaMapper;
import org.locationtech.jts.geom.Geometry;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GeographicAreaServiceImpl implements GeographicAreaService {

	private final GeographicAreaDao geographicAreaDao;

	private final GeographicAreaMapper geographicAreaMapper;

	private final AuthentificationHelper authentificationHelper;

	@Override
	public GeographicArea getGeographicAreaByCodeInsee(String codeInsee) {
		return geographicAreaMapper.entityToDto(geographicAreaDao.findByCodeInsee(codeInsee));
	}

	@Override
	public GeographicArea getGeographicAreaByNom(String nom) {
		// Appliquer d'abord le formatage qu'il faut au nom avant de lancer la requete
		return geographicAreaMapper.entityToDto(geographicAreaDao.findByNom(nom));
	}

	@Override
	public List<GeographicArea> getAllGeographicArea() {
		return geographicAreaMapper.entitiesToDto(geographicAreaDao.findAll());
	}

	@Override
	public GeographicArea getCurrentUserArea() throws ApiServiceException {
		// On recupere l'organisation a laquelle appartient le user connecté
		String nom = authentificationHelper.getOrganisation();
		GeographicArea area = getGeographicAreaByNom(nom);
		if (area == null) {
			throw new ApiServiceException("Organisation inconnue", ApiServiceExceptionsStatus.NOT_FOUND);
		}
		return area;
	}

	@Override
	public GeographicAreaEntity getGeographicAreaByPoint(Geometry point) throws ApiServiceException {
		GeographicAreaEntity entity = geographicAreaDao.getByCoords(point);
		if (entity == null) {
			throw new ApiServiceException("Ce point n'est dans aucune commune connue");
		}
		return entity;
	}

	@Override
	public GeographicAreaEntity getGeographicAreaEntityByCodeInsee(String codeInsee) {
		return geographicAreaDao.findByCodeInsee(codeInsee);
	}
}
