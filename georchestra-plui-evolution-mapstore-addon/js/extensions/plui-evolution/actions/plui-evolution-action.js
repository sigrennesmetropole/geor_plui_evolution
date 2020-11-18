export const actions = {
	INIT_PLUI_EVOLUTION: 'PLUI_EVOLUTION:INIT',
	INIT_PLUI_EVOLUTION_DONE: 'PLUI_EVOLUTION:INIT_DONE',
	ATTACHMENT_CONFIGURATION_LOAD: 'PLUI_EVOLUTION:ATTACHMENT_CONFIGURATION:LOAD',
	ATTACHMENT_CONFIGURATION_LOADED: 'PLUI_EVOLUTION:ATTACHMENT_CONFIGURATION:LOADED',
	USER_ME_GET: 'PLUI_EVOLUTION:USER:GET',
	USER_ME_GOT: 'PLUI_EVOLUTION:USER:GOT',
	PLUI_EVOLUTION_OPEN_PANEL: 'PLUI_EVOLUTION:PANEL:OPEN',
	PLUI_EVOLUTION_CLOSE_PANEL: 'PLUI_EVOLUTION:PANEL:CLOSE',
	ADD_ATTACHMENT: 'PLUI_EVOLUTION:ATTACHMENT:ADD',
	ATTACHMENT_ADDED: 'PLUI_EVOLUTION:ATTACHMENT:ADDED',
	REMOVE_ATTACHMENT: 'PLUI_EVOLUTION:ATTACHMENT:REMOVE',
	ATTACHMENT_REMOVED: 'PLUI_EVOLUTION:ATTACHMENT:REMOVED',
	INIT_ERROR: 'PLUI_EVOLUTION:INIT:ERROR',
	ACTION_ERROR: 'PLUI_EVOLUTION:ACTION:ERROR',
	PLUI_EVOLUTION_CLOSING: 'PLUI_EVOLUTION:CLOSING',
	PLUI_EVOLUTION_CANCEL_CLOSING: 'PLUI_EVOLUTION:CANCEL:CLOSING',
	PLUI_EVOLUTION_CONFIRM_CLOSING: 'PLUI_EVOLUTION:CONFIRM:CLOSING',
	PLUI_EVOLUTION_INIT_SUPPORT_DRAWING: 'PLUI_EVOLUTION:INIT_DRAWING:DRAWING',
	PLUI_EVOLUTION_STOP_SUPPORT_DRAWING: 'PLUI_EVOLUTION:STOP_DRAWING:DRAWING',
	PLUI_EVOLUTION_SET_DRAWING: 'PLUI_EVOLUTION:SET:DRAWING',
	PLUI_EVOLUTION_START_DRAWING: 'PLUI_EVOLUTION:START:DRAWING',
	PLUI_EVOLUTION_STOP_DRAWING: 'PLUI_EVOLUTION:STOP:DRAWING',
	PLUI_EVOLUTION_UPDATE_LOCALISATION: 'PLUI_EVOLUTION:UPDATE:LOCALISATION',
	PLUI_EVOLUTION_CLEAR_DRAWN: 'PLUI_EVOLUTION:CLEAR:DRAWN'
};


export function initPluiEvolution(url){
	return {
		type: actions.INIT_PLUI_EVOLUTION,
		url: url
	}
}

export function initPluiEvolutionDone(){
	return {
		type: actions.INIT_PLUI_EVOLUTION_DONE
	}
}

export function loadAttachmentConfiguration() {
	return {
		type: actions.ATTACHMENT_CONFIGURATION_LOAD
	}
}

export function loadedAttachmentConfiguration(attachmentConfiguration) {
	return {
		type: actions.ATTACHMENT_CONFIGURATION_LOADED,
		attachmentConfiguration: attachmentConfiguration
	}
}

export function addAttachment(attachment) {
	console.log('add file action: ' + attachment)
	return {
		type: actions.ADD_ATTACHMENT,
		attachment : attachment
	}
}

export function addedAttachment(attachment) {
	console.log('added file action: ' + attachment)
	return {
		type: actions.ATTACHMENT_ADDED,
		attachment : attachment
	}
}

export function removeAttachment(attachment) {
	console.log('add file action: ' + attachment)
	return {
		type: actions.REMOVE_ATTACHMENT,
		attachment : attachment
	}
}

export function removedAttachment(attachmentIndex) {
	console.log('remove file action: ' + attachmentIndex)
	return {
		type: actions.ATTACHMENT_REMOVED,
		attachmentIndex : attachmentIndex
	}
}

export function getMe() {
	return {
		type: actions.USER_ME_GET
	}
}

export function gotMe(me) {
	return {
		type: actions.USER_ME_GOT,
		user: me
	}
}


export function requestClosing() {
	return {
		type: actions.PLUI_EVOLUTION_CLOSING,
		status: status.REQUEST_UNLOAD_TASK
	}
}

export function cancelClosing() {
	return {
		type: actions.PLUI_EVOLUTION_CANCEL_CLOSING,
	}
}

export function confirmClosing() {
	return {
		type: actions.PLUI_EVOLUTION_CONFIRM_CLOSING,
	}
}

function loadError(type, message, e){
	console.log("message:" + message);
	console.log(e);
	return {
		type: type,
		error: {
			message: message,
			e: e
		}
	}
}

export function loadActionError(message, e) {
	return loadError(actions.ACTION_ERROR, message, e);
}

export function loadInitError(message, e) {
	return loadError(actions.INIT_ERROR, message, e);
}


export function initDrawingSupport() {
	return {
		type: actions.PLUI_EVOLUTION_INIT_SUPPORT_DRAWING
	}
}

export function stopDrawingSupport() {
	return {
		type: actions.PLUI_EVOLUTION_STOP_SUPPORT_DRAWING
	}
}

export function setDrawing(drawing) {
	return {
		type: actions.PLUI_EVOLUTION_SET_DRAWING,
		drawing
	}
}

export function startDrawing(geometryType, localisation) {
	return {
		type: actions.PLUI_EVOLUTION_START_DRAWING,
		geometryType: geometryType,
		localisation: localisation
	}
}

export function stopDrawing(geometryType) {
	return {
		type: actions.PLUI_EVOLUTION_STOP_DRAWING,
		geometryType: geometryType
	}
}

export function updateLocalisation(localisation) {
	return {
		type: actions.PLUI_EVOLUTION_UPDATE_LOCALISATION,
		localisation: localisation
	}
}

export function clearDrawn() {
	return {
		type: actions.PLUI_EVOLUTION_CLEAR_DRAWN
	}
}
