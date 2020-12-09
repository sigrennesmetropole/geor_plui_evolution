import assign from 'object-assign';
import {actions, status} from '../actions/plui-evolution-action';

const initialState = {
    user: null,
    attachments: [],
    attachmentConfiguration: {}
}

export default (state = initialState, action) => {
    console.log("sig reduce:" + action.type);
    switch (action.type) {
        case actions.PLUI_EVOLUTION_ACTION_ERROR: {
            return assign({}, state, {error: action.error, loading: false});
        }
        case actions.ATTACHMENT_CONFIGURATION_LOADED: {
            return assign({}, state, {attachmentConfiguration: action.attachmentConfiguration});
        }
        case actions.ATTACHMENTS_UPDATED: {
            return assign({}, state, {error: null, attachments: action.attachments});
        }
        case actions.ATTACHMENT_REMOVED: {
            let attachments = [...state.attachments];
            attachments.splice(action.attachmentIndex, 1)
            return assign({}, state, {attachments: attachments });
        }
        case actions.USER_ME_GOT: {
            return assign({}, state, {user: action.user});
        }
        case actions.PLUI_EVOLUTION_PLUIREQUEST_SAVED: {
            return assign({}, state, {pluiRequest: action.pluiRequest});
        }
        case actions.PLUI_EVOLUTION_LOADING: {
            return assign({}, state, {loading: true});
        }
        case actions.PLUI_EVOLUTION_OPEN_PANEL: {
            return assign({}, state, {open: true, status: action.status, pluiRequest: action.pluiRequest});
        }
        case actions.PLUI_EVOLUTION_CLOSE_PANEL: {
            return assign({}, state, {open: false, status: status.EMPTY});
        }
        case actions.PLUI_EVOLUTION_CLOSE_REQUEST: {
            return assign({}, state, {pluiRequest: null, status: status.CLEAN_REQUEST, loading: false});
        }
        case actions.PLUI_EVOLUTION_CLOSING: {
            return assign({}, state, {closing: true});
        }
        case actions.PLUI_EVOLUTION_CANCEL_CLOSING: {
            return assign({}, state, {closing: false});
        }
        case actions.PLUI_EVOLUTION_CONFIRM_CLOSING: {
            return assign({}, state, {closing: false, status: status.CLEAN_REQUEST});
        }
        case actions.PLUI_EVOLUTION_UPDATE_LOCALISATION: {
            return {
                ...state,
                pluiRequest: {
                    ...state.pluiRequest,
                    localisation: action.localisation
                }
            };
        }
        case actions.PLUI_EVOLUTION_SET_DRAWING: {
            return assign({}, state, {drawing: action.drawing});
        }
        default: {
            return state;
        }
    }
};
