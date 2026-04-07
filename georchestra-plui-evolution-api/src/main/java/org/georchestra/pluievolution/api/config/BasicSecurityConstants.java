package org.georchestra.pluievolution.api.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BasicSecurityConstants {

	public static final String SWAGGER_RESSOURCE_URL = "/pluievolution/swagger-resources/**";
	public static final String SWAGGER_RESSOURCE_UI = "/pluievolution/swagger-ui.html";
	public static final String SWAGGER_RESSOURCE_INDEX = "/pluievolution/swagger-ui/**";

	public static final String WEBJARS_URL = "/webjars/**";

	public static final String API_DOCS_URL = "/pluievolution/v3/api-docs/**";

	public static final String CONFIGURATION_SECURITY_URL = "/configuration/security";
	public static final String CONFIGURATION_UI_URL = "/configuration/ui";

	public static final String ASSETS_URL = "/assets/**";
	public static final String ASSETS_JSON_URL = "/assets/**/*.json";
	public static final String ASSETS_JPEG_URL = "/assets/**/*.jpeg";
	public static final String ASSETS_SVG_URL = "/assets/**/*.svg";

	public static final String ICONES_URL = "/*.ico";
	public static final String CSS_URL = "/*.css";
	public static final String SLASH_URL = "/";
	public static final String JS_URL = "/*.js";
	public static final String CRSF_URL = "/csrf";

}
