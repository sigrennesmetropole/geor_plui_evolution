package org.georchestra.pluievolution.api.controller.common;

import org.georchestra.pluievolution.core.common.DocumentContent;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.FileNotFoundException;

public abstract class AbstractExportDocumentApi {

    private static final String ATTACHMENT_FILENAME = "attachment; filename=";

    /**
     * Téléchargement d'un document
     * @param documentContent           contenu du document
     * @return                          ResponseEntity
     * @throws FileNotFoundException    fichier du document introuvable
     */
    public ResponseEntity<Resource> downloadDocument(DocumentContent documentContent) throws FileNotFoundException {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.CONTENT_DISPOSITION, ATTACHMENT_FILENAME + documentContent.getFileName());
        responseHeaders.add(HttpHeaders.CONTENT_TYPE, documentContent.getContentType());
        responseHeaders.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
        InputStreamResource inputStreamResource = new InputStreamResource(documentContent.getFileStream());
        return new ResponseEntity<>(inputStreamResource, responseHeaders, HttpStatus.OK);
    }
}
