import assign from 'object-assign';
import {actions, status} from '../actions/plui-evolution-action';

const initialState = {
    user: null,
    attachments: [],
    attachmentConfiguration: {},
}

export default (state = initialState, action) => {
    console.log("sig reduce:" + action.type);
    switch (action.type) {
        case actions.INIT_ERROR: {
            return assign({}, state, {error: action.error});
        }
        case actions.ACTION_ERROR: {
            return assign({}, state, {error: action.error});
        }
        case actions.ATTACHMENT_CONFIGURATION_LOADED: {
            return assign({}, state, {attachmentConfiguration: action.attachmentConfiguration});
        }
        case actions.ATTACHMENT_ADDED: {
            let attachments = [...state.attachments];
            attachments.push(action.attachment);
            return assign({}, state, {attachments: attachments });
        }
        case actions.ATTACHMENT_REMOVED: {
            let attachments = [...state.attachments];
            attachments.splice(action.attachmentIndex, 1)
            return assign({}, state, {attachments: attachments });
        }
        case actions.USER_ME_GOT: {
            return assign({}, state, {user: action.user});
        }
        case actions.PLUI_EVOLUTION_CLOSING: {
            return assign({}, state, {closing: true});
        }
        case actions.PLUI_EVOLUTION_CANCEL_CLOSING: {
            return assign({}, state, {closing: false});
        }
        case actions.PLUI_EVOLUTION_CONFIRM_CLOSING: {
            return assign({}, state, {closing: false, status: status.REQUEST_UNLOAD_TASK});
        }
        case actions.PLUI_EVOLUTION_UPDATE_LOCALISATION: {
            return {
                ...state,
                task: {
                    ...state.task,
                    asset: {
                        ...state.task.asset,
                        localisation: action.localisation
                    }
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
