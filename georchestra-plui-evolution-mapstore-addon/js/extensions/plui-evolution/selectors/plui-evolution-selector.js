import { createSelector } from 'reselect';
import { get } from "lodash";

export const getPluiEvolution = state => get(state, "pluievolution");

export const getPluiEvolutionState = createSelector(
    [ getPluiEvolution ],
    (selector) => selector
);

export const isOpen = (state) => get(state, "pluievolution.open");

export const pluiEvolutionAttachmentConfigurationSelector = (state) => get(state, "pluievolution.attachmentConfiguration");

export const pluiEvolutionMeSelector = (state) => get(state, "pluievolution.user");
