package org.georchestra.pluievolution.service.bean;

import lombok.Data;

/**
 * Classe contenant les élements de configuration
 */
@Data
public class Configuration {

	// Version de l'application
	private String version;
	
	/**
	 * Constructeur par defaut A conserver pour l'utilisation des mapper mapStruct
	 */
	public Configuration() {
	}

	/**
	 * Constructeur avec la version
	 * 
	 * @param version
	 */
	public Configuration(String version) {
		this.version = version;
	}

}
