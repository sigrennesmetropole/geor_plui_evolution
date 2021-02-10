import { createSelector } from 'reselect';
import { get } from "lodash";
import {status} from "../actions/plui-evolution-action"

export const getPluiEvolution = state => get(state, "pluievolution");

export const getPluiEvolutionState = createSelector(
    [ getPluiEvolution ],
    (selector) => selector
);

export const isOpen = (state) => get(state, "pluievolution.open");

export const isLoadingSelector = (state) => get(state, "pluievolution.loading");

export const isReadOnlySelector = (state) => get(state, "pluievolution.status") === status.LOAD_REQUEST;

export const pluiEvolutionAttachmentConfigurationSelector = (state) => get(state, "pluievolution.attachmentConfiguration");

export const pluiEvolutionLayerConfigurationSelector = (state) => get(state, "pluievolution.layerConfiguration");

export const pluiEvolutionMeSelector = (state) => get(state, "pluievolution.user");
