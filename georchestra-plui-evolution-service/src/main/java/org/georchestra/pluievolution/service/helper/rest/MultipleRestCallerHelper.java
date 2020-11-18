package org.georchestra.pluievolution.service.helper.rest;

import org.georchestra.pluievolution.service.exception.RestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Objet responsable d'appeler les WS externes plusieurs fois avant de remonter une vraie erreur
 */
public class MultipleRestCallerHelper {

    // Logger
    private static final Logger LOGGER = LoggerFactory.getLogger(MultipleRestCallerHelper.class);

    // L'objet faisant l'appel REST
    private RestTemplate restTemplate;

    // L'objet des paramètres d'appel REST
    private UriComponentsBuilder builder;

    // Le nombre d'essais à faire avant de remonter une erreur
    private Integer numberOfTries;

    /**
     * Constructeur de l'objet d'appel multiples
     * @param restTemplate objet appel REST
     * @param builder objet des params
     * @param numberOfTries nombre d'essais
     */
    public MultipleRestCallerHelper(RestTemplate restTemplate, UriComponentsBuilder builder, Integer numberOfTries) {
        this.restTemplate = restTemplate;
        this.builder = builder;
        this.numberOfTries = numberOfTries;
    }

    /**
     * Appel d'un WS externe en réessayant plusieurs fois si quelque chsoe tourne mal
     * @param classReturned la classe sensée être retournée par le WS
     * @param <T> Le type de la classe sensée être retournée par le WS
     * @return une réponse wrappant la classe retournée par le WS
     * @throws Exception exception levée si on a tenté plusieurs fois et qu'on a échoué à chaque fois
     */
    public <T> ResponseEntity<T> callExternalWS(Class<T> classReturned) throws RestException {
        try {
            return restTemplate.getForEntity(builder.toUriString(), classReturned);
        }
        // Si un problème d'appel survient
        catch(RestClientException e) {
            // ON log l'erreur et on réessaye un appel
            LOGGER.error("Erreur d'appel de WS externe REST pour la première tentative, des essais supplémentaires seront réalisés");

            // Les appels suivant sont forcément au moins les deuxièmes
            return retryRestCall(classReturned, 2);
        }
    }

    /**
     * Un nouvel essai d'appel de WS externe
     * @param classReturned la classe sensée être retournée par le WS
     * @param numberCall le numéro de la tentative actuelle
     * @param <T> Le type de la classe sensée être retournée par le WS
     * @return une réponse wrappant la classe retournée par le WS
     * @throws Exception exception levée si on a tenté plusieurs fois et qu'on a échoué à chaque fois
     */
    private <T> ResponseEntity<T> retryRestCall(Class<T> classReturned, Integer numberCall) throws RestException {
        try {
            return restTemplate.getForEntity(builder.toUriString(), classReturned);
        }
        // Si un problème d'appel survient
        catch(RestClientException e) {
            // ON log l'erreur
            LOGGER.error("la tentative {} d'appel du WS externe a échoué", numberCall);

            // Si on a dépassé le maximum de tentatives d'appel alors on remonte une vraie erreur d'appel
            numberCall++;
            if(numberCall > numberOfTries) {
                // L'erreur est wrappée dans une exception custom qui fait passe plat
                throw new RestException(e);
            }

            // Si on a pas dépassé le max de tentatives on réessaye encore
            return retryRestCall(classReturned, numberCall);
        }
    }
}
