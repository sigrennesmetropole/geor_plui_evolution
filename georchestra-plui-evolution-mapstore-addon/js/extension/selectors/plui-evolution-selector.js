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

export const pluiEvolutionLayerConfigurationSelector = (state) => get(state, "pluievolution.layerConfiguration");

export const pluiEvolutionEtablissementConfigurationSelector = (state) => get(state, "pluievolution.etablissementConfiguration");

export const pluiEvolutionMeSelector = (state) => get(state, "pluievolution.user");

/**
 * Retourne vrai si la couche du layer pluiEvolution existe, est visible et est selectionnée
 * @param state
 * @returns {boolean}
 */
export function isPluievolutionActivate(state) {
    const pluievolutionLayerId = state.pluievolution.layerConfiguration.layerWorkspace;
    let layers = state.layers.flat;
    layers = layers.filter(layer => layer.id === pluievolutionLayerId);
    let pluievolutionLayer = layers.length !== 0 ? layers[0] : null;
    return pluievolutionLayer != null && pluievolutionLayer?.visibility && state.layers.selected.includes(pluievolutionLayerId);
}
