import {hideMapinfoMarker} from "@mapstore/actions/mapInfo";
import {UPDATE_MAP_LAYOUT} from "@mapstore/actions/maplayout";

export const actions = {
	PLUI_EVOLUTION_INIT: 'PLUI_EVOLUTION:INIT',
	PLUI_EVOLUTION_INIT_DONE: 'PLUI_EVOLUTION:INIT_DONE',
	PLUI_EVOLUTION_ATTACHMENT_CONFIGURATION_LOAD: 'PLUI_EVOLUTION:ATTACHMENT_CONFIGURATION:LOAD',
	PLUI_EVOLUTION_VERSION_CONFIGURATION_LOAD: 'PLUI_EVOLUTION:VERSION_CONFIGURATION:LOAD',
	PLUI_EVOLUTION_ATTACHMENT_CONFIGURATION_LOADED: 'PLUI_EVOLUTION:ATTACHMENT_CONFIGURATION:LOADED',
	PLUI_EVOLUTION_VERSION_CONFIGURATION_LOADED: 'PLUI_EVOLUTION:VERSION_CONFIGURATION:LOADED',
	PLUI_EVOLUTION_LAYER_CONFIGURATION_LOAD: 'PLUI_EVOLUTION:LAYER_CONFIGURATION:LOAD',
	PLUI_EVOLUTION_LAYER_CONFIGURATION_LOADED: 'PLUI_EVOLUTION:LAYER_CONFIGURATION:LOADED',
	PLUI_EVOLUTION_ETABLISSEMENT_CONFIGURATION_LOAD: 'PLUI_EVOLUTION:ETABLISSEMENT_CONFIGURATION:LOAD',
	PLUI_EVOLUTION_ETABLISSEMENT_CONFIGURATION_LOADED: 'PLUI_EVOLUTION:ETABLISSEMENT_CONFIGURATION:LOADED',
	PLUI_EVOLUTION_GEOGRAPHIC_ETABLISSEMENT_GET_ALL: 'PLUI_EVOLUTION:GEOGRAPHIC_ETABLISSEMENT:GET_ALL',
	PLUI_EVOLUTION_GEOGRAPHIC_ETABLISSEMENT_ALL_LOADED: 'PLUI_EVOLUTION:GEOGRAPHIC_ETABLISSEMENT:ALL_LOADED',
	PLUI_EVOLUTION_USER_ME_GET: 'PLUI_EVOLUTION:USER:GET',
	PLUI_EVOLUTION_USER_ME_GOT: 'PLUI_EVOLUTION:USER:GOT',
	PLUI_EVOLUTION_DISPLAY_ETABLISSEMENT: 'PLUI_EVOLUTION:ETABLISSEMENT:GET',
	PLUI_EVOLUTION_DISPLAY_ALL: 'PLUI_EVOLUTION:ALL:DISPLAY',
	PLUI_EVOLUTION_OPENING_PANEL: 'PLUI_EVOLUTION:PANEL:OPENING',
	PLUI_EVOLUTION_OPEN_PANEL: 'PLUI_EVOLUTION:PANEL:OPEN',
	PLUI_EVOLUTION_AUTO_OPEN_PANEL: 'PLUI_EVOLUTION:PANEL:OPEN_AUTO',
	PLUI_EVOLUTION_CLOSE_PANEL: 'PLUI_EVOLUTION:PANEL:CLOSE',
	PLUI_EVOLUTION_LOAD_FORM: 'PLUI_EVOLUTION:FORM:LOAD',
	PLUI_EVOLUTION_LOADING_CREATE_FORM: 'PLUI_EVOLUTION:CREATE_FORM:LOADING',
	PLUI_EVOLUTION_LOADING_UPDATE_FORM: 'PLUI_EVOLUTION:UPDATE_FORM:LOADING',
	PLUI_EVOLUTION_SAVE_PLUIREQUEST: 'PLUI_EVOLUTION:PLUIREQUEST:SAVE',
	PLUI_EVOLUTION_PLUIREQUEST_SAVED: 'PLUI_EVOLUTION:PLUIREQUEST:SAVED',
	PLUI_EVOLUTION_ATTACHMENTS_UPDATED: 'PLUI_EVOLUTION:ATTACHMENTS:UPDATED',
	PLUI_EVOLUTION_GET_ATTACHMENTS: 'PLUI_EVOLUTION:ATTACHMENTS:GET',
	PLUI_EVOLUTION_DOWNLOAD_ATTACHMENT: 'PLUI_EVOLUTION:ATTACHMENT:DOWNLOAD',
	PLUI_EVOLUTION_ACTION_ERROR: 'PLUI_EVOLUTION:ACTION:ERROR',
	PLUI_EVOLUTION_CLOSING: 'PLUI_EVOLUTION:CLOSING',
	PLUI_EVOLUTION_CANCEL_CLOSING: 'PLUI_EVOLUTION:CANCEL:CLOSING',
	PLUI_EVOLUTION_CLOSE_REQUEST: 'PLUI_EVOLUTION:CLOSE:REQUEST',
	PLUI_EVOLUTION_CONFIRM_CLOSING: 'PLUI_EVOLUTION:CONFIRM:CLOSING',
	PLUI_EVOLUTION_CLOSE: 'PLUI_EVOLUTION:CONFIRM:CLOSING',
	PLUI_EVOLUTION_LOADING: 'PLUI_EVOLUTION:LOADING',
	PLUI_EVOLUTION_INIT_SUPPORT_DRAWING: 'PLUI_EVOLUTION:INIT_SUPPORT:DRAWING',
	PLUI_EVOLUTION_STOP_SUPPORT_DRAWING: 'PLUI_EVOLUTION:STOP_SUPPORT:DRAWING',
	PLUI_EVOLUTION_SET_DRAWING: 'PLUI_EVOLUTION:SET:DRAWING',
	PLUI_EVOLUTION_START_DRAWING: 'PLUI_EVOLUTION:START:DRAWING',
	PLUI_EVOLUTION_STOP_DRAWING: 'PLUI_EVOLUTION:STOP:DRAWING',
	PLUI_EVOLUTION_UPDATE_LOCALISATION: 'PLUI_EVOLUTION:UPDATE:LOCALISATION',
	PLUI_EVOLUTION_CLEAR_DRAWN: 'PLUI_EVOLUTION:CLEAR:DRAWN',
	PLUI_EVOLUTION_CHANGE_FORM_STATUS: 'PLUI_EVOLUTION:FORM_STATUS:CHANGE',
	PLUI_EVOLUTION_ENSURE_PROJ4: 'PLUI_EVOLUTION:ENSURE_PROJ4',
	PLUI_EVOLUTION_ENSURE_PROJ4_DONE: 'PLUI_EVOLUTION:ENSURE_PROJ4_DONE',
	PLUI_EVOLUTION_OPEN_VIEWER: 'PLUI_EVOLUTION:OPEN:VIEWER',
	PLUI_EVOLUTION_CLOSE_VIEWER: 'PLUI_EVOLUTION:CLOSE:VIEWER',
};

export const status = {
	INIT_FORM_REQUEST: "INIT_FORM_REQUEST",
	CREATE_REQUEST: "CREATE_REQUEST",
	CLEAN_REQUEST: "CLEAN_REQUEST",
	LOAD_REQUEST: "LOAD_REQUEST",
	VIEW_REQUEST: "VIEW_REQUEST",
	EMPTY: "EMPTY"
};

export function changeFormStatus(formStatus) {
	return {
		type: actions.PLUI_EVOLUTION_CHANGE_FORM_STATUS,
		status: formStatus
	}
}

export function initPluiEvolution(url){
	return {
		type: actions.PLUI_EVOLUTION_INIT,
		url: url
	};
}

export function initPluiEvolutionDone(){
	return {
		type: actions.PLUI_EVOLUTION_INIT_DONE
	};
}

export function loadAttachmentConfiguration() {
	return {
		type: actions.PLUI_EVOLUTION_ATTACHMENT_CONFIGURATION_LOAD
	};
}

export function loadedAttachmentConfiguration(attachmentConfiguration) {
	return {
		type: actions.PLUI_EVOLUTION_ATTACHMENT_CONFIGURATION_LOADED,
		attachmentConfiguration: attachmentConfiguration
	};
}

export function loadVersionConfiguration() {
	return {
		type: actions.PLUI_EVOLUTION_VERSION_CONFIGURATION_LOAD,
	};
}

export function loadedVersionConfiguration(versionConfiguration) {
	return {
		type: actions.PLUI_EVOLUTION_VERSION_CONFIGURATION_LOADED,
		versionConfiguration: versionConfiguration
	};
}

export function loadLayerConfiguration() {
	return {
		type: actions.PLUI_EVOLUTION_LAYER_CONFIGURATION_LOAD
	};
}

export function loadedLayerConfiguration(layerConfiguration) {
	return {
		type: actions.PLUI_EVOLUTION_LAYER_CONFIGURATION_LOADED,
		layerConfiguration: layerConfiguration
	};
}

export function loadEtablissementConfiguration() {
	return {
		type: actions.PLUI_EVOLUTION_ETABLISSEMENT_CONFIGURATION_LOAD
	};
}

export function loadedEtablissementConfiguration(etablissementConfiguration) {
	return {
		type: actions.PLUI_EVOLUTION_ETABLISSEMENT_CONFIGURATION_LOADED,
		etablissementConfiguration: etablissementConfiguration
	};
}

export function getAllGeographicEtablissement() {
	return {
		type: actions.PLUI_EVOLUTION_GEOGRAPHIC_ETABLISSEMENT_GET_ALL
	};
}

export function loadedAllGeographicEtablissement(geographicEtablissements) {
	return {
		type: actions.PLUI_EVOLUTION_GEOGRAPHIC_ETABLISSEMENT_ALL_LOADED,
		geographicEtablissements: geographicEtablissements
	};
}

export function getAttachments(uuid) {
	return {
		type: actions.PLUI_EVOLUTION_GET_ATTACHMENTS,
		uuid: uuid
	};
}

export function downloadAttachment(attachment) {
	return {
		type: actions.PLUI_EVOLUTION_DOWNLOAD_ATTACHMENT,
		attachment: attachment
	};
}

export function updateAttachments(attachments) {
	return {
		type: actions.PLUI_EVOLUTION_ATTACHMENTS_UPDATED,
		attachments: attachments
	};
}

export function getMe() {
	return {
		type: actions.PLUI_EVOLUTION_USER_ME_GET
	};
}

export function gotMe(me) {
	return {
		type: actions.PLUI_EVOLUTION_USER_ME_GOT,
		user: me
	};
}

export function openingPanel(pluiRequest, requestStatus) {
	return {
		type: actions.PLUI_EVOLUTION_OPENING_PANEL,
		status: requestStatus,
		pluiRequest: pluiRequest
	}
}

export function openPanel(pluiRequest) {
	return {
		type: actions.PLUI_EVOLUTION_OPEN_PANEL,
		pluiRequest: pluiRequest
	}
}

export function openPanelAuto() {
	return {
		type: actions.PLUI_EVOLUTION_AUTO_OPEN_PANEL,
	}
}

export function closePanel(){
	return (dispatch) => {
		dispatch(hideMapinfoMarker());
		dispatch(stopDrawingSupport());
		dispatch({
			type: actions.PLUI_EVOLUTION_CLOSE_PANEL
		});
	};
}

export function requestClosing() {
	return {
		type: actions.PLUI_EVOLUTION_CLOSING,
		status: status.REQUEST_UNLOAD_TASK
	};
}

export function cancelClosing() {
	return {
		type: actions.PLUI_EVOLUTION_CANCEL_CLOSING,
	};
}

export function confirmClosing() {
	return {
		type: actions.PLUI_EVOLUTION_CONFIRM_CLOSING,
	};
}

export function closeRequest() {
	return {
		type: actions.PLUI_EVOLUTION_CLOSE_REQUEST,
	};
}

export function isLoading() {
	return {
		type: actions.PLUI_EVOLUTION_LOADING
	}
}

export function savePluiRequest(pluiRequest, attachments) {
	return (dispatch) => {
		dispatch(isLoading());
		dispatch({
			type: actions.PLUI_EVOLUTION_SAVE_PLUIREQUEST,
			pluiRequest: pluiRequest,
			attachments: attachments
		});
	};
}

export function pluiRequestSaved(pluiRequest) {
	return {
		type: actions.PLUI_EVOLUTION_PLUIREQUEST_SAVED,
		pluiRequest: pluiRequest
	};
}

function loadError(type, message, messageParams, e) {
	window.pluiEvolution.debug('message PLUI_EVOLUTION_ACTION_ERROR', message )
	return {
		type: type,
		error: message || e ? {
			message: message,
			messageParams: messageParams,
			e: e
		} : null
	};
}

export function loadActionError(message, messageParams, e) {
	return loadError(actions.PLUI_EVOLUTION_ACTION_ERROR, message, messageParams, e);
}

export function initDrawingSupport() {
	return {
		type: actions.PLUI_EVOLUTION_INIT_SUPPORT_DRAWING
	};
}

export function stopDrawingSupport() {
	return {
		type: actions.PLUI_EVOLUTION_STOP_SUPPORT_DRAWING
	};
}

export function setDrawing(drawing) {
	return {
		type: actions.PLUI_EVOLUTION_SET_DRAWING,
		drawing
	};
}

export function startDrawing(geometryType, localisation) {
	return {
		type: actions.PLUI_EVOLUTION_START_DRAWING,
		geometryType: geometryType,
		localisation: localisation
	};
}

export function stopDrawing(geometryType) {
	return {
		type: actions.PLUI_EVOLUTION_STOP_DRAWING,
		geometryType: geometryType
	};
}

export function clearDrawn() {
	return {
		type: actions.PLUI_EVOLUTION_CLEAR_DRAWN
	};
}

export function updateLocalisation(localisation) {
	return {
		type: actions.PLUI_EVOLUTION_UPDATE_LOCALISATION,
		localisation: localisation
	};
}

export function displayEtablissement(pluiRequestType, geographicEtablissement) {
	return {
		type: actions.PLUI_EVOLUTION_DISPLAY_ETABLISSEMENT,
		pluiRequestType: pluiRequestType,
		geographicEtablissement: geographicEtablissement
	};
}

export function displayAllPluiRequest() {
	return {
		type: actions.PLUI_EVOLUTION_DISPLAY_ALL
	};
}

export function loadPluiForm(pluiRequest, formStatus) {
	return {
		type: actions.PLUI_EVOLUTION_LOAD_FORM,
		pluiRequest: pluiRequest,
		status: formStatus
	}
}

export function loadingPluiCreateForm() {
	return {
		type: actions.PLUI_EVOLUTION_LOADING_CREATE_FORM
	}
}

export function loadingPluiUpdateForm(pluiRequest) {
	return {
		type: actions.PLUI_EVOLUTION_LOADING_UPDATE_FORM,
		pluiRequest: pluiRequest
	}
}

export function ensureProj4(projectionDefs) {
	return {
		type: actions.PLUI_EVOLUTION_ENSURE_PROJ4,
		projectionDefs: projectionDefs
	};
}

export function ensureProj4Done() {
	return {
		type: actions.PLUI_EVOLUTION_ENSURE_PROJ4_DONE,
	}
}

export function loadPluiEvolutionViewer(response) {
	return {
		type: actions.PLUI_EVOLUTION_OPEN_VIEWER,
		response: response
	};
}

export function closeViewer() {
	return {
		type: actions.PLUI_EVOLUTION_CLOSE_VIEWER
	}
}

export function pluiEvolutionUpdateMapLayout(layout) {
	return {
		layout,
		type: UPDATE_MAP_LAYOUT,
		source: "pluievolution"
	};
}