/**
 * 
 */
package org.georchestra.pluievolution.service.sm.impl;

import org.apache.commons.lang3.StringUtils;
import org.georchestra.pluievolution.core.dao.acl.UserDao;
import org.georchestra.pluievolution.core.dto.User;
import org.georchestra.pluievolution.core.entity.acl.UserEntity;
import org.georchestra.pluievolution.service.helper.authentification.AuthentificationHelper;
import org.georchestra.pluievolution.service.mapper.UserMapper;
import org.georchestra.pluievolution.service.sm.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author FNI18300
 *
 */
@Service
public class UserServiceImpl implements UserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private AuthentificationHelper authentificationHelper;

	@Autowired
	private UserDao userDao;

	@Autowired
	private UserMapper userMapper;

	@Override
	@Transactional(readOnly = true)
	public User getMe() {
		return loadUserByUsername(authentificationHelper.getUsername());
	}

	@Override
	@Transactional(readOnly = true)
	public User loadUserByUsername(String username) {
		LOGGER.info("Search user by login {}", username);
		UserEntity userEntity = userDao.findByLogin(username);
		return userMapper.entityToDto(userEntity);
	}

	@Override
	@Transactional(readOnly = false)
	public User createUser(User user) {
		if (user == null || StringUtils.isEmpty(user.getLogin())) {
			throw new IllegalArgumentException("Invaluder user : " + user);
		}
		UserEntity userEntity = userMapper.dtoToEntity(user);
		userDao.save(userEntity);
		return userMapper.entityToDto(userEntity);
	}

	@Override
	@Transactional(readOnly = false)
	public User updateUser(User user) {
		if (user == null || StringUtils.isEmpty(user.getLogin())) {
			throw new IllegalArgumentException("Invaluder user : " + user);
		}
		UserEntity userEntity = userDao.findByLogin(user.getLogin());
		userMapper.dtoToEntity(user, userEntity);
		userDao.save(userEntity);
		return userMapper.entityToDto(userEntity);
	}
}
