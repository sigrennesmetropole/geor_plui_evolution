import {createSelector} from 'reselect';
import {get} from "lodash";
import {status} from "../actions/plui-evolution-action";

export const getPluiEvolution = state => get(state, "pluievolution");

export const getPluiEvolutionState = createSelector(
    [ getPluiEvolution ],
    (selector) => selector
);

export const isOpen = (state) => get(state, "pluievolution.open");

export const isViewerModeSelector = (state) => get(state, "pluievolution.status") === status.VIEW_REQUEST;

export const pluiEvolutionFeaturesResponseSelector = (state) => get(state, "pluievolution.response");

export const isLoadingSelector = (state) => get(state, "pluievolution.loading");

export const isReadOnlySelector = (state) => get(state, "pluievolution.status") === status.LOAD_REQUEST;

export const pluiEvolutionAttachmentConfigurationSelector = (state) => get(state, "pluievolution.attachmentConfiguration");

export const pluiEvolutionVersionConfigurationSelector = (state) => get(state, "pluievolution.versionConfiguration");

export const pluiEvolutionLayerConfigurationSelector = (state) => get(state, "pluievolution.layerConfiguration");

export const pluiEvolutionEtablissementConfigurationSelector = (state) => get(state, "pluievolution.etablissementConfiguration");

export const pluiEvolutionMeSelector = (state) => get(state, "pluievolution.user");

export const isPluiEvolutionActivate = (state) => get(state, "pluievolution.activated");

export const pluievolutionSidebarControlSelector = (state) => get(state, "controls.pluievolution.enabled");

/**
 * Retourne vrai si la couche du layer pluiEvolution existe, est visible et est selectionnée
 * @param state
 * @returns {boolean}
 */
export function isPluievolutionActivateAndSelected(state) {
    const pluievolutionLayerId = state.pluievolution?.layerConfiguration?.layerWorkspace;
    if (pluievolutionLayerId != null) {
        let layers = state.layers?.flat;
        layers = layers != null ? layers.filter(layer => layer.id === pluievolutionLayerId) : null;
        let pluievolutionLayer = layers!= null && layers.length !== 0 ? layers[0] : null;
        return pluievolutionLayer && pluievolutionLayer.visibility && state.layers.selected.includes(pluievolutionLayerId);
    }
    return false;

}
