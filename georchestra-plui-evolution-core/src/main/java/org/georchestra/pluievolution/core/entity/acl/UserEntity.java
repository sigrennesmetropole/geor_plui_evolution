/**
 * 
 */
package org.georchestra.pluievolution.core.entity.acl;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.georchestra.pluievolution.core.common.LongId;
import org.georchestra.pluievolution.core.entity.ListToStringConverter;

import lombok.Data;

/**
 * Représente un user 
 * 
 * @author FNI18300
 *
 */
@Data
@Entity
@Table(name = "user_")
public class UserEntity implements LongId {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;
	
	@Column(name = "login", nullable = false, length = 100)
	private String login;

	/**
	 * Addresse email permettant de faire le lien avec la personne authentifiée
	 */
	@Column(nullable = false, length = 150)
	private String email;

	@Column(name = "first_name", length = 150)
	private String firstName;

	@Column(name = "last_name", length = 150)
	private String lastName;
	
	@Column(name = "organization", length = 150)
	private String organization;

	@Column(name = "roles", length = 1024)
	@Convert(converter = ListToStringConverter.class)
	private List<String> roles;

}
