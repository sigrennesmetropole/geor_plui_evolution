import React from 'react';
import {Glyphicon} from 'react-bootstrap';
import {connect} from 'react-redux';
import {createControlEnabledSelector} from '@mapstore/selectors/controls';
import Message from '@mapstore/components/I18N/Message';
import {PluiEvolutionPanelComponent} from '../components/PluiEvolutionPanelComponent';
import * as epics from '../epics/plui-evolution-epic';
import pluiEvolutionReducer from '../reducers/plui-evolution-reducer';
import {
	initPluiEvolution,
    addAttachment,
    clearDrawn,
    initDrawingSupport,
    loadAttachmentConfiguration,
    removeAttachment,
    closePanel,
    getMe,
    confirmClosing,
    cancelClosing,
    openPanel,
    requestClosing,
    startDrawing,
    stopDrawing,
    stopDrawingSupport
} from '../actions/plui-evolution-action';
import {
    isOpen,
    pluiEvolutionAttachmentConfigurationSelector,
    PluiEvolutionMeSelector,
} from '../selectors/plui-evolution-selector';

const isEnabled = createControlEnabledSelector('pluievolution');

const Connected = connect((state) => ({
    active: /*isEnabled(state) ||*/ !!isOpen(state),
    attachmentConfiguration: pluiEvolutionAttachmentConfigurationSelector(state),
    user: pluiEvolutionMeSelector(state),
    pluierequest: state.pluievolution.pluierequest,
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
    addAttachment: addAttachment,
    removeAttachment: removeAttachment,
    getMe: getMe,
    requestClosing: requestClosing,
    cancelClosing: cancelClosing,
    confirmClosing: confirmClosing,
    toggleControl: () => closePanel()
})(PluiEvolutionPanelComponent);

export default {
	name: "pluievolution",
    component: Connected,
    epics,
    reducers: {
        pluiEvolution: pluiEvolutionReducer
    },
    containers: {
        BurgerMenu: {
            name: 'pluievolution',
            position: 9,
            panel: false,
            tooltip: "pluievolution.reporting.thema",
            text: <Message msgId="pluievolution.msgBox.title" />,
            icon: <Glyphicon glyph="exclamation-sign" />,
            action: () => openPanel(null)
        }
    }
};
