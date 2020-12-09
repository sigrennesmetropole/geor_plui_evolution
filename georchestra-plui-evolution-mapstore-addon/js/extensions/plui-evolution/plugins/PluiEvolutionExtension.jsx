import React from 'react';
import {Glyphicon} from 'react-bootstrap';
import {connect} from 'react-redux';
import {createControlEnabledSelector} from '@mapstore/selectors/controls';
import Message from '@mapstore/components/I18N/Message';
import {injectIntl} from 'react-intl';
import {PluiEvolutionPanelComponent} from '../components/PluiEvolutionPanelComponent';
import * as epics from '../epics/plui-evolution-epic';
import pluiEvolutionReducer from '../reducers/plui-evolution-reducer';
import {
    initPluiEvolution,
    clearDrawn,
    closeRequest,
    initDrawingSupport,
    loadAttachmentConfiguration,
    closePanel,
    getMe,
    displayEtablissement,
    savePluiRequest,
    confirmClosing,
    cancelClosing,
    openPanel,
    requestClosing,
    startDrawing,
    stopDrawing,
    stopDrawingSupport,
    updateAttachments,
    loadActionError
} from '../actions/plui-evolution-action';
import {
    isOpen,
    isLoadingSelector,
    pluiEvolutionAttachmentConfigurationSelector,
    pluiEvolutionMeSelector,
} from '../selectors/plui-evolution-selector';

const isEnabled = createControlEnabledSelector('pluievolution');

const Connected = connect((state) => ({
    active: !!isOpen(state),
    loading: !!isLoadingSelector(state),
    attachmentConfiguration: pluiEvolutionAttachmentConfigurationSelector(state),
    user: pluiEvolutionMeSelector(state),
    pluiRequest: state.pluievolution.pluiRequest,
    attachments: state.pluievolution.attachments,
    status: state.pluievolution.status,
    drawing: state.pluievolution.drawing,
    error: state.pluievolution.error,
    // debug
    state : state
}), {
    initPluiEvolution: initPluiEvolution,
    initDrawingSupport: initDrawingSupport,
    stopDrawingSupport: stopDrawingSupport,
    startDrawing: startDrawing,
    stopDrawing: stopDrawing,
    clearDrawn: clearDrawn,
    loadAttachmentConfiguration: loadAttachmentConfiguration,
    updateAttachments: updateAttachments,
    getMe: getMe,
    displayEtablissement: displayEtablissement,
    savePluiRequest: savePluiRequest,
    requestClosing: requestClosing,
    cancelClosing: cancelClosing,
    confirmClosing: confirmClosing,
    closeRequest: closeRequest,
    loadActionError: loadActionError,
    toggleControl: () => closePanel()
})(PluiEvolutionPanelComponent);

export default {
    name: "pluievolution",
    component: injectIntl(Connected),
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
