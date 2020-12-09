export const actions = {
	INIT_PLUI_EVOLUTION: 'PLUI_EVOLUTION:INIT',
	INIT_PLUI_EVOLUTION_DONE: 'PLUI_EVOLUTION:INIT_DONE',
	ATTACHMENT_CONFIGURATION_LOAD: 'PLUI_EVOLUTION:ATTACHMENT_CONFIGURATION:LOAD',
	ATTACHMENT_CONFIGURATION_LOADED: 'PLUI_EVOLUTION:ATTACHMENT_CONFIGURATION:LOADED',
	USER_ME_GET: 'PLUI_EVOLUTION:USER:GET',
	USER_ME_GOT: 'PLUI_EVOLUTION:USER:GOT',
	PLUI_EVOLUTION_DISPLAY_ETABLISSEMENT: 'PLUI_EVOLUTION:ETABLISSEMENT:GET',
	PLUI_EVOLUTION_OPEN_PANEL: 'PLUI_EVOLUTION:PANEL:OPEN',
	PLUI_EVOLUTION_CLOSE_PANEL: 'PLUI_EVOLUTION:PANEL:CLOSE',
	PLUI_EVOLUTION_SAVE_PLUIREQUEST: 'PLUI_EVOLUTION:PLUIREQUEST:SAVE',
	PLUI_EVOLUTION_PLUIREQUEST_SAVED: 'PLUI_EVOLUTION:PLUIREQUEST:SAVED',
	ATTACHMENTS_UPDATED: 'PLUI_EVOLUTION:ATTACHMENTS:UPDATED',
	REMOVE_ATTACHMENT: 'PLUI_EVOLUTION:ATTACHMENT:REMOVE',
	ATTACHMENT_REMOVED: 'PLUI_EVOLUTION:ATTACHMENT:REMOVED',
	PLUI_EVOLUTION_ACTION_ERROR: 'PLUI_EVOLUTION:ACTION:ERROR',
	PLUI_EVOLUTION_CLOSING: 'PLUI_EVOLUTION:CLOSING',
	PLUI_EVOLUTION_CANCEL_CLOSING: 'PLUI_EVOLUTION:CANCEL:CLOSING',
	PLUI_EVOLUTION_CLOSE_REQUEST: 'PLUI_EVOLUTION:CLOSE:REQUEST',
	PLUI_EVOLUTION_CLOSE: 'PLUI_EVOLUTION:CONFIRM:CLOSING',
	PLUI_EVOLUTION_LOADING: 'PLUI_EVOLUTION:LOADING',
	PLUI_EVOLUTION_INIT_SUPPORT_DRAWING: 'PLUI_EVOLUTION:INIT_SUPPORT:DRAWING',
	PLUI_EVOLUTION_STOP_SUPPORT_DRAWING: 'PLUI_EVOLUTION:STOP_SUPPORT:DRAWING',
	PLUI_EVOLUTION_SET_DRAWING: 'PLUI_EVOLUTION:SET:DRAWING',
	PLUI_EVOLUTION_START_DRAWING: 'PLUI_EVOLUTION:START:DRAWING',
	PLUI_EVOLUTION_STOP_DRAWING: 'PLUI_EVOLUTION:STOP:DRAWING',
	PLUI_EVOLUTION_UPDATE_LOCALISATION: 'PLUI_EVOLUTION:UPDATE:LOCALISATION',
	PLUI_EVOLUTION_CLEAR_DRAWN: 'PLUI_EVOLUTION:CLEAR:DRAWN'
};

export const status = {
	CREATE_REQUEST: "CREATE_REQUEST",
	CLEAN_REQUEST: "CLEAN_REQUEST",
	LOAD_REQUEST: "LOAD_REQUEST",
	EMPTY: "EMPTY"
};

export function initPluiEvolution(url){
	return {
		type: actions.INIT_PLUI_EVOLUTION,
		url: url
	};
}

export function initPluiEvolutionDone(){
	return {
		type: actions.INIT_PLUI_EVOLUTION_DONE
	};
}

export function loadAttachmentConfiguration() {
	return {
		type: actions.ATTACHMENT_CONFIGURATION_LOAD
	};
}

export function loadedAttachmentConfiguration(attachmentConfiguration) {
	return {
		type: actions.ATTACHMENT_CONFIGURATION_LOADED,
		attachmentConfiguration: attachmentConfiguration
	};
}

export function updateAttachments(attachments) {
	console.log('added files action: ')
	return {
		type: actions.ATTACHMENTS_UPDATED,
		attachments: attachments
	};
}

export function getMe() {
	return {
		type: actions.USER_ME_GET
	};
}

export function gotMe(me) {
	return {
		type: actions.USER_ME_GOT,
		user: me
	};
}

export function openPanel(pluiRequest) {
	return {
		type: actions.PLUI_EVOLUTION_OPEN_PANEL,
		status: pluiRequest ? status.LOAD_REQUEST : status.CREATE_REQUEST,
		pluiRequest: pluiRequest
	};
}

export function closePanel(){
	return {
		type: actions.PLUI_EVOLUTION_CLOSE_PANEL
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
	console.log('message PLUI_EVOLUTION_ACTION_ERROR', message )
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

export function updateLocalisation(localisation) {
	return {
		type: actions.PLUI_EVOLUTION_UPDATE_LOCALISATION,
		localisation: localisation
	};
}

export function displayEtablissement(pluiRequestType) {
	return {
		type: actions.PLUI_EVOLUTION_DISPLAY_ETABLISSEMENT,
		pluiRequestType: pluiRequestType
	}
}

export function clearDrawn() {
	return {
		type: actions.PLUI_EVOLUTION_CLEAR_DRAWN
	};
}
