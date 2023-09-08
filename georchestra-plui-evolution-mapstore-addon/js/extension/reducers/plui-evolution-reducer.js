import assign from 'object-assign';
import {actions, status} from '../actions/plui-evolution-action';
import {FEATURE_INFO_CLICK} from 'mapstore2/web/client/actions/mapInfo';

const initialState = {
    user: null,
    attachments: null,
    attachmentConfiguration: {},
    layerConfiguration: {},
    layers: {}
}

export default (state = initialState, action) => {
    if (action.type !== actions.PLUI_EVOLUTION_DISPLAY_LOG && action.type !== actions.PLUI_EVOLUTION_DISPLAY_LOG_DONE && state.activated) {
        console.log("pluie reduce:" + action.type);

    }

    switch (action.type) {
        case actions.PLUI_EVOLUTION_ACTION_ERROR: {
            return assign({}, state, {error: action.error, loading: false});
        }
        case actions.PLUI_EVOLUTION_ATTACHMENT_CONFIGURATION_LOADED: {
            return assign({}, state, {attachmentConfiguration: action.attachmentConfiguration});
        }
        case actions.PLUI_EVOLUTION_ATTACHMENTS_UPDATED: {
            return assign({}, state, {error: null, attachments: action.attachments});
        }
        case actions.PLUI_EVOLUTION_LAYER_CONFIGURATION_LOADED: {
            return assign({}, state, {layerConfiguration: action.layerConfiguration});
        }
        case actions.PLUI_EVOLUTION_USER_ME_GOT: {
            return assign({}, state, {user: action.user});
        }
        case actions.PLUI_EVOLUTION_GEOGRAPHIC_ETABLISSEMENT_ALL_LOADED: {
            return assign({}, state, {geographicEtablissements: action.geographicEtablissements});
        }
        case actions.PLUI_EVOLUTION_ETABLISSEMENT_CONFIGURATION_LOADED: {
            return assign({}, state, {etablissementConfiguration: action.etablissementConfiguration});
        }
        case actions.PLUI_EVOLUTION_PLUIREQUEST_SAVED: {
            return assign({}, state, {pluiRequest: action.pluiRequest});
        }
        case actions.PLUI_EVOLUTION_LOADING: {
            return assign({}, state, {loading: true});
        }
        case actions.PLUI_EVOLUTION_OPENING_PANEL: {
            return assign({}, state, {open: true, activated: true});
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
        case actions.PLUI_EVOLUTION_LOAD_FORM: {
            return assign({}, state, {status: action.status, pluiRequest: action.pluiRequest});
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
        case actions.PLUI_EVOLUTION_CHANGE_FORM_STATUS: {
            return assign({}, state, {status: action.status});
        }
        case actions.PLUI_EVOLUTION_OPEN_VIEWER: {
            return assign({}, state, {response: action.response, viewerLoading: action.loading, status: status.VIEW_REQUEST, open: true});
        }
        case actions.PLUI_EVOLUTION_CLOSE_VIEWER: {
            return assign({}, state, {response: {}, status: status.EMPTY, open: false});
        }
        case FEATURE_INFO_CLICK: {
            return assign({}, state, {pluiRequest: null, status: status.CLEAN_REQUEST, loading: false});
        }
        case actions.PLUI_EVOLUTION_DISPLAY_LOG: {
            return assign({}, state, {consoleLogWasPrinted: true});
        }
        case actions.PLUI_EVOLUTION_DISPLAY_LOG_DONE: {
            return assign({}, state, {consoleLogWasPrinted: false});
        }
        default: {
            return state;
        }
    }
};
