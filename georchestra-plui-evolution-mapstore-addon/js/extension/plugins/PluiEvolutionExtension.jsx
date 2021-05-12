import React from 'react';
import {Glyphicon} from 'react-bootstrap';
import {connect} from 'react-redux';
import {createControlEnabledSelector} from '@mapstore/selectors/controls';
import Message from '@mapstore/components/I18N/Message';
import {injectIntl} from 'react-intl';
import { name } from '../../../config';
import {PluiEvolutionPanelComponent} from '../components/PluiEvolutionPanelComponent';
import * as epics from '../epics/plui-evolution-epic';
import pluiEvolutionReducer from '../reducers/plui-evolution-reducer';
import {
    cancelClosing,
    changeFormStatus,
    clearDrawn,
    closePanel,
    closeRequest,
    confirmClosing,
    displayAllPluiRequest,
    displayEtablissement,
    downloadAttachment,
    getAttachments,
    getMe,
    initPluiEvolution,
    loadActionError,
    loadAttachmentConfiguration,
    loadEtablissementConfiguration,
    loadingPluiCreateForm,
    loadLayerConfiguration,
    openPanel,
    requestClosing,
    savePluiRequest,
    startDrawing,
    stopDrawing,
    updateAttachments,
    ensureProj4
} from '../actions/plui-evolution-action';
import {
    isLoadingSelector,
    isOpen,
    isReadOnlySelector,
    pluiEvolutionAttachmentConfigurationSelector,
    pluiEvolutionEtablissementConfigurationSelector,
    pluiEvolutionLayerConfigurationSelector,
    pluiEvolutionMeSelector,
} from '../selectors/plui-evolution-selector';
import '../assets/plui-evolution.css';

const isEnabled = createControlEnabledSelector('pluievolution');

const PluiEvolutionPanelComponentConnected = connect((state) => ({
    active: !!isOpen(state),
    loading: !!isLoadingSelector(state),
    readOnly: !!isReadOnlySelector(state),
    attachmentConfiguration: pluiEvolutionAttachmentConfigurationSelector(state),
    layerConfiguration: pluiEvolutionLayerConfigurationSelector(state),
    etablissementConfiguration: pluiEvolutionEtablissementConfigurationSelector(state),
    user: pluiEvolutionMeSelector(state),
    geographicEtablissements: state.pluievolution.geographicEtablissements,
    pluiRequest: state.pluievolution.pluiRequest,
    attachments: state.pluievolution.attachments,
    status: state.pluievolution.status,
    drawing: state.pluievolution.drawing,
    error: state.pluievolution.error,
    localConfig: state.localConfig,
    // debug
    state : state
}), {
    initPluiEvolution: initPluiEvolution,
    startDrawing: startDrawing,
    stopDrawing: stopDrawing,
    clearDrawn: clearDrawn,
    loadAttachmentConfiguration: loadAttachmentConfiguration,
    loadLayerConfiguration: loadLayerConfiguration,
    updateAttachments: updateAttachments,
    getAttachments: getAttachments,
    downloadAttachment: downloadAttachment,
    getMe: getMe,
    displayEtablissement: displayEtablissement,
    displayAllPluiRequest: displayAllPluiRequest,
    loadEtablissementConfiguration: loadEtablissementConfiguration,
    savePluiRequest: savePluiRequest,
    loadingPluiCreateForm: loadingPluiCreateForm,
    requestClosing: requestClosing,
    cancelClosing: cancelClosing,
    confirmClosing: confirmClosing,
    closeRequest: closeRequest,
    loadActionError: loadActionError,
    changeFormStatus: changeFormStatus,
    toggleControl: closePanel,
    ensureProj4: ensureProj4
})(PluiEvolutionPanelComponent);

export default {
    name,
    component: injectIntl(PluiEvolutionPanelComponentConnected),
    epics,
    reducers: {
        pluievolution: pluiEvolutionReducer
    },
    containers: {
        BurgerMenu: {
            name: 'pluievolution',
            position: 9,
            panel: false,
            tooltip: "pluievolution.name",
            text: <Message msgId="pluievolution.name" />,
            icon: <Glyphicon glyph="exclamation-sign" />,
            action: () => openPanel(null)
        }
    }
};
