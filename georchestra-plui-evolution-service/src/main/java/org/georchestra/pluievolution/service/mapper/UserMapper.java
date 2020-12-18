package org.georchestra.pluievolution.service.mapper;

import org.georchestra.pluievolution.core.dto.User;
import org.georchestra.pluievolution.core.entity.acl.UserEntity;
import org.mapstruct.*;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends AbstractMapper<UserEntity, User> {

	@Override
	@InheritInverseConfiguration
	UserEntity dtoToEntity(User dto);

	@Override
	User entityToDto(UserEntity entity);
	
	void dtoToEntity(User user, @MappingTarget UserEntity entity);

	@IterableMapping(qualifiedByName = "entityToDto")
	List<User> entitiesToDtos(Collection<UserEntity> entities);
}
