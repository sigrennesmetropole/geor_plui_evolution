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
    initPluiEvolution,
    changeFormStatus,
    clearDrawn,
    closeRequest,
    downloadAttachment,
    loadAttachmentConfiguration,
    loadLayerConfiguration,
    closePanel,
    getAttachments,
    getMe,
    displayEtablissement,
    savePluiRequest,
    confirmClosing,
    cancelClosing,
    loadingPluiCreateForm,
    openPanel,
    requestClosing,
    startDrawing,
    stopDrawing,
    updateAttachments,
    loadActionError
} from '../actions/plui-evolution-action';
import {
    isOpen,
    isLoadingSelector,
    isReadOnlySelector,
    pluiEvolutionAttachmentConfigurationSelector,
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
    user: pluiEvolutionMeSelector(state),
    geographicEtablissements: state.pluievolution.geographicEtablissements,
    pluiRequest: state.pluievolution.pluiRequest,
    attachments: state.pluievolution.attachments,
    status: state.pluievolution.status,
    drawing: state.pluievolution.drawing,
    error: state.pluievolution.error,
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
    savePluiRequest: savePluiRequest,
    loadingPluiCreateForm: loadingPluiCreateForm,
    requestClosing: requestClosing,
    cancelClosing: cancelClosing,
    confirmClosing: confirmClosing,
    closeRequest: closeRequest,
    loadActionError: loadActionError,
    changeFormStatus: changeFormStatus,
    toggleControl: closePanel
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
