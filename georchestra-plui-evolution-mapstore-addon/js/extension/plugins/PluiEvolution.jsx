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
import {toggleControl} from "@mapstore/actions/controls";
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
    ensureProj4, closeViewer, openPanelAuto
} from '../actions/plui-evolution-action';
import {
    isLoadingSelector,
    isOpen, isPluiEvolutionActivate,
    isReadOnlySelector,
    isViewerModeSelector,
    pluiEvolutionAttachmentConfigurationSelector,
    pluiEvolutionEtablissementConfigurationSelector,
    pluiEvolutionFeaturesResponseSelector,
    pluiEvolutionLayerConfigurationSelector,
    pluiEvolutionMeSelector,
} from '../selectors/plui-evolution-selector';
import '../assets/plui-evolution.css';
import icon from '../assets/plui_vSIGRM.svg';
import {createPlugin} from "@mapstore/utils/PluginsUtils";
import {mapLayoutValuesSelector} from "../selectors/maplayout";


const isEnabled = createControlEnabledSelector('pluievolution');

window.pluiEvolution = { debug: (obj) => {} };
const PluiEvolutionPanelComponentConnected = connect((state) => ({
    active: !!isOpen(state),
    loading: !!isLoadingSelector(state),
    readOnly: !!isReadOnlySelector(state),
    viewerMode: !!isViewerModeSelector(state),
    response: pluiEvolutionFeaturesResponseSelector(state),
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
    dockStyle: mapLayoutValuesSelector(state, { right: true, height: true}, true),
    // debug
    state: state,
    activated: !!isPluiEvolutionActivate(state)
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
    openPanel: openPanel,
    openPanelAuto: openPanelAuto,
    savePluiRequest: savePluiRequest,
    loadingPluiCreateForm: loadingPluiCreateForm,
    requestClosing: requestClosing,
    cancelClosing: cancelClosing,
    confirmClosing: confirmClosing,
    closeRequest: closeRequest,
    loadActionError: loadActionError,
    changeFormStatus: changeFormStatus,
    toggleControl: closePanel,
    ensureProj4: ensureProj4,
    closeViewer: closeViewer,
})(PluiEvolutionPanelComponent);

export default createPlugin(name, {
    component: injectIntl(PluiEvolutionPanelComponentConnected),
    epics,
    reducers: {
        pluievolution: pluiEvolutionReducer
    },
    containers: {
        SidebarMenu: {
            name: 'pluievolution',
            position: 9,
            panel: true,
            tooltip: "pluievolution.name",
            text: <Message msgId="pluievolution.name" />,
            icon: <img src={icon} alt="" height="24" width="24" class="plui-icon" />,
            doNotHide: true,
            toggle: true,
            action: toggleControl.bind(null, 'pluievolution', null)
        }
    }
});

