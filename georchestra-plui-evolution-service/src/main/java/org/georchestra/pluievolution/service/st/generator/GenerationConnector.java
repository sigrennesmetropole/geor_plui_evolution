package org.georchestra.pluievolution.service.st.generator;

import java.io.IOException;

import org.georchestra.pluievolution.core.common.DocumentContent;
import org.georchestra.pluievolution.service.exception.DocumentGenerationException;
import org.georchestra.pluievolution.service.exception.DocumentModelNotFoundException;
import org.georchestra.pluievolution.service.st.generator.datamodel.DataModel;

/**
 * @author FNI18300
 *
 */
public interface GenerationConnector {

	/**
	 * génère un document à partir d'un modele odt et de données
	 * 
	 * @param dataModel le data model contenant le données à insérer et le nom du
	 *                  modèle à utiliser
	 * @throws DocumentModelNotFoundException si le modèle de document nécessaire à
	 *                                        la génération n'a pas été trouvé
	 * @throws DocumentGenerationException    si une erreur se produit a la
	 *                                        génération
	 * @throws IOException
	 */
	DocumentContent generateDocument(DataModel dataModel)
			throws DocumentModelNotFoundException, DocumentGenerationException, IOException;

	/**
	 * Convertit le fichier d'entrée au format pdf
	 * 
	 * @param inputDocument
	 * @return le nouveau fichier pdf
	 * @throws DocumentGenerationException
	 */
	DocumentContent convertDocumentPDF(PDFConverterType type, DocumentContent inputDocument)
			throws DocumentGenerationException;

}
