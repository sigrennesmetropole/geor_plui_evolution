import * as Rx from 'rxjs';
import axios from 'axios';
import {head} from 'lodash';
import {saveAs} from 'file-saver';
import {changeDrawingStatus, GEOMETRY_CHANGED} from "@mapstore/actions/draw";
import {addLayer, refreshLayerVersion, selectNode} from '@mapstore/actions/layers';
import {CLICK_ON_MAP} from '@mapstore/actions/map';
import {TOGGLE_CONTROL, toggleControl} from "@mapstore/actions/controls";
import {
    changeMapInfoState,
    hideMapinfoMarker,
    showMapinfoMarker,
    LOAD_FEATURE_INFO,
    closeIdentify, featureInfoClick
} from "@mapstore/actions/mapInfo";
import {error, show, success} from '@mapstore/actions/notifications';
import {
    actions,
    closeRequest,
    getAllGeographicEtablissement,
    getAttachments,
    getMe,
    gotMe,
    initDrawingSupport,
    initPluiEvolutionDone,
    loadActionError,
    loadedAllGeographicEtablissement,
    loadedAttachmentConfiguration,
    loadedEtablissementConfiguration,
    loadedLayerConfiguration,
    loadingPluiCreateForm,
    loadingPluiUpdateForm,
    loadPluiForm,
    openingPanel,
    pluiRequestSaved,
    setDrawing,
    status,
    updateAttachments,
    updateLocalisation,
    ensureProj4Done, loadPluiEvolutionViewer, openPanel, closePanel, closeViewer,
    pluiEvolutionUpdateMapLayout
} from '../actions/plui-evolution-action';
import {
    DEFAULT_PROJECTION, DEFAULT_PROJECTION_CODE,
    GeometryType,
    PLUI_EVOLUTION_LAYER_TITLE,
    PLUIEVOLUTION_PANEL_WIDTH, PLUIEVOLUTION_VIEWER_WIDTH,
    PluiRequestType,
    RIGHT_SIDEBAR_MARGIN_LEFT
} from "../constants/plui-evolution-constants";
import {
    isPluievolutionActivateAndSelected,
    pluiEvolutionEtablissementConfigurationSelector,
    pluievolutionSidebarControlSelector,
} from '../selectors/plui-evolution-selector';
import Proj4js from 'proj4';
import {
    FORCE_UPDATE_MAP_LAYOUT, forceUpdateMapLayout,
    UPDATE_MAP_LAYOUT,
    updateDockPanelsList
} from "@mapstore/actions/maplayout";

let backendURLPrefix = "/pluievolution";
let pluiEvolutionLayerId;
let pluiEvolutionLayerName;
let pluiEvolutionLayerProjection;
let currentLayout;

export const openPluievelutionPanelEpic = (action$, store) =>
    action$.ofType(TOGGLE_CONTROL)
        .filter((action) => action.control === "pluievolution" && !!store.getState() && !!pluievolutionSidebarControlSelector(store.getState()))
        .switchMap(() => {
            let layout = store.getState().maplayout;
            layout = {transform: layout.layout.transform, height: layout.layout.height, rightPanel: true, leftPanel: layout.layout.leftPanel, ...layout.boundingMapRect, right: PLUIEVOLUTION_PANEL_WIDTH + RIGHT_SIDEBAR_MARGIN_LEFT, boundingMapRect: {...layout.boundingMapRect, right: PLUIEVOLUTION_PANEL_WIDTH + RIGHT_SIDEBAR_MARGIN_LEFT}, boundingSidebarRect: layout.boundingSidebarRect};
            currentLayout = layout;
            return Rx.Observable.from([updateDockPanelsList('pluievolution', 'add', 'right'), pluiEvolutionUpdateMapLayout(layout), openPanel(null)]);
        });

export const closePluievelutionPanelEpic = (action$, store) =>
    action$.ofType(TOGGLE_CONTROL, actions.PLUI_EVOLUTION_CLOSE_REQUEST, actions.PLUI_EVOLUTION_CLOSE_VIEWER, actions.PLUI_EVOLUTION_CLOSE_PANEL)
        .filter(action => action.type === actions.PLUI_EVOLUTION_CLOSE_VIEWER ||
            ([actions.PLUI_EVOLUTION_CLOSE_REQUEST, actions.PLUI_EVOLUTION_CLOSE_PANEL].includes(action.type) && !!pluievolutionSidebarControlSelector(store.getState())) ||
                (action.control === "pluievolution" && !!store.getState() && !pluievolutionSidebarControlSelector(store.getState())))
        .switchMap((action) => {
            const actionsList = [updateDockPanelsList('pluievolution', 'remove', 'right')];
            if (!!pluievolutionSidebarControlSelector(store.getState())) {
                actionsList.push(toggleControl('pluievolution'));
            }
            if (store.getState().pluievolution.status === status.VIEW_REQUEST) {
                actionsList.push(closeViewer());
            } else {
                actionsList.push(closePanel());
            }
            let layout = store.getState().maplayout;
            layout = {transform: layout.layout.transform, height: layout.layout.height, rightPanel: false, leftPanel: layout.layout.leftPanel, ...layout.boundingMapRect, right: layout.boundingSidebarRect.right, boundingMapRect: {...layout.boundingMapRect, right: layout.boundingSidebarRect.right}, boundingSidebarRect: layout.boundingSidebarRect};
            currentLayout= layout;
            return Rx.Observable.from(actionsList).concat(Rx.Observable.of(pluiEvolutionUpdateMapLayout(layout)).delay(0));
        });

export function onOpeningAnotherRightPanelPlui(action$, store) {
    return action$.ofType(TOGGLE_CONTROL)
        .filter((action) => store && store.getState() &&
            action.control !== 'pluievolution' &&
            store.getState().maplayout.dockPanels.right.includes("pluievolution") &&
            store.getState().maplayout.dockPanels.right.includes(action.control))
        .switchMap(() => {
            return Rx.Observable.from([closePanel(), updateDockPanelsList("pluievolution", "remove", "right")]);
        });
}

export function onUpdatingLayoutWhenPluiPanelOpened(action$, store) {
    return action$.ofType(UPDATE_MAP_LAYOUT, FORCE_UPDATE_MAP_LAYOUT)
        .filter((action) => store && store.getState() &&
            (action.source === "pluievolution" || action.source === undefined) &&
            !!pluievolutionSidebarControlSelector(store.getState()) &&
            currentLayout?.right !== action?.layout?.right)
        .switchMap((action) => {
            let layout = store.getState().maplayout;
            layout = {transform: layout.layout.transform, height: layout.layout.height, rightPanel: true, leftPanel: layout.layout.leftPanel, ...layout.boundingMapRect, right: PLUIEVOLUTION_PANEL_WIDTH + RIGHT_SIDEBAR_MARGIN_LEFT, boundingMapRect: {...layout.boundingMapRect, right: PLUIEVOLUTION_PANEL_WIDTH + RIGHT_SIDEBAR_MARGIN_LEFT}, boundingSidebarRect: layout.boundingSidebarRect};
            currentLayout = layout;
            return Rx.Observable.of(pluiEvolutionUpdateMapLayout(layout));
        });
}

/**
 * Catch GFI response on identify load event and close identify if PLUi-Evolution identify tabs is selected
 * @param {*} action$
 * @param {*} store
 */
export function loadPluiEvolutionViewerEpic(action$, store) {
    return action$.ofType(LOAD_FEATURE_INFO)
        .filter(action => isPluievolutionActivateAndSelected(store.getState()))
        .switchMap((action) => {
            // si features présentent dans la zone de clic
            if (action?.layer?.id && action?.data?.features && action.data.features.length) {
                let layout = store.getState().maplayout;
                layout = {transform: layout.layout.transform, height: layout.layout.height, rightPanel: true, leftPanel: false, ...layout.boundingMapRect, right: PLUIEVOLUTION_VIEWER_WIDTH + RIGHT_SIDEBAR_MARGIN_LEFT, boundingMapRect: {...layout.boundingMapRect, right: PLUIEVOLUTION_VIEWER_WIDTH + RIGHT_SIDEBAR_MARGIN_LEFT}, boundingSidebarRect: layout.boundingSidebarRect};
                currentLayout = layout;
                return Rx.Observable.from([updateDockPanelsList('pluievolution', 'add', 'right'), closeIdentify(), loadPluiEvolutionViewer(action.data)]).concat(Rx.Observable.of(pluiEvolutionUpdateMapLayout(layout)).delay(0));
            }
            return  Rx.Observable.of();
        });
}

export const openPluiPanelEpic = (action$) =>
    action$.ofType(actions.PLUI_EVOLUTION_OPEN_PANEL)
        .switchMap((action) => {

            return Rx.Observable.from(
                [initDrawingSupport()]
                    .concat(action.pluiRequest
                        ? [loadingPluiUpdateForm(action.pluiRequest)]
                        : [loadingPluiCreateForm()])
                    .concat([openingPanel(action.pluiRequest)])
            );
        });

export const loadingPluiUpdateFormEpic = (action$) =>
    action$.ofType(actions.PLUI_EVOLUTION_LOADING_UPDATE_FORM)
        .switchMap((action) => {
            return Rx.Observable.from([
                showMapinfoMarker(),
                getAttachments(action.pluiRequest.uuid),
                loadPluiForm(action.pluiRequest, status.LOAD_REQUEST)
            ]);
        });

export const loadingPluiCreateFormEpic = (action$) =>
    action$.ofType(actions.PLUI_EVOLUTION_LOADING_CREATE_FORM)
        .switchMap((action) => {
            return Rx.Observable.from([
                hideMapinfoMarker(),
                updateAttachments(null),
                loadPluiForm(null, status.INIT_FORM_REQUEST)
            ]);
        });

export const initPluiEvolutionEpic = (action$) =>
    action$.ofType(actions.PLUI_EVOLUTION_INIT)
        .switchMap((action) => {
            window.pluiEvolution.debug("pluie epics init:"+ action.url);
            if( action.url ) {
                backendURLPrefix = action.url;
            }
            return Rx.Observable.of(initPluiEvolutionDone()).delay(0);
        });

export const loadPluiAttachmentConfigurationEpic = (action$) =>
    action$.ofType(actions.PLUI_EVOLUTION_ATTACHMENT_CONFIGURATION_LOAD)
        .switchMap((action) => {
            if (action.attachmentConfiguration) {
                return Rx.Observable.of(loadedAttachmentConfiguration(action.attachmentConfiguration)).delay(0);
            }
            const url = backendURLPrefix + "/attachment/configuration";
            return Rx.Observable.defer(() => axios.get(url))
                .switchMap((response) => Rx.Observable.of(loadedAttachmentConfiguration(response.data)))
                .catch(e => Rx.Observable.of(loadActionError("pluievolution.init.attachmentConfiguration.error", null, e)));
        });

export const loadPluiLayerConfigurationEpic = (action$) =>
    action$.ofType(actions.PLUI_EVOLUTION_LAYER_CONFIGURATION_LOAD)
        .switchMap((action) => {
            if (action.layerConfiguration) {
                return Rx.Observable.of(loadedLayerConfiguration(action.layerConfiguration)).delay(0);
            }
            const url = backendURLPrefix + "/carto/layerConfiguration";
            pluiEvolutionLayerId = 5;
            return Rx.Observable.defer(() => axios.get(url))
                .switchMap((response) => {
                    pluiEvolutionLayerId = response.data.layerWorkspace;
                    pluiEvolutionLayerName = response.data.layerName;
                    pluiEvolutionLayerProjection = response.data.layerProjection ? response.data.layerProjection : DEFAULT_PROJECTION;
                    return Rx.Observable.of(loadedLayerConfiguration(response.data));
                })
                .catch(e => Rx.Observable.of(loadActionError("pluievolution.init.layerConfiguration.error", null, e)));
        });

export const loadEtablissementConfigurationEpic = (action$) =>
    action$.ofType(actions.PLUI_EVOLUTION_ETABLISSEMENT_CONFIGURATION_LOAD)
        .switchMap((action) => {
            const url = backendURLPrefix + "/geographic/configuration";
            return Rx.Observable.defer(() => axios.get(url))
                .switchMap((response) => {
                    return Rx.Observable.from([loadedEtablissementConfiguration(response.data), getMe()]);
                })
                .catch(e => Rx.Observable.of(loadActionError("pluievolution.init.etablissementConfiguration.error", null, e)));
        });

export const getAllGeographicPluiEtablissementEpic = (action$) =>
    action$.ofType(actions.PLUI_EVOLUTION_GEOGRAPHIC_ETABLISSEMENT_GET_ALL)
        .switchMap((action) => {
            const url = backendURLPrefix + "/geographic/etablissements";
            return Rx.Observable.defer(() => axios.get(url))
                .switchMap((response) => Rx.Observable.of(loadedAllGeographicEtablissement(response.data)))
                .catch(e => Rx.Observable.of(loadActionError("pluievolution.init.geographicEtablissement.error", null, e)));
        });

export const getPluiAttachmentsEpic = (action$) =>
    action$.ofType(actions.PLUI_EVOLUTION_GET_ATTACHMENTS)
        .switchMap((action) => {
            const url = backendURLPrefix + "/request/" + action.uuid + "/attachments";
            return Rx.Observable.defer(() => axios.get(url))
                .switchMap((response) => Rx.Observable.of(updateAttachments(response.data)))
                .catch(e => Rx.Observable.of(
                    show({
                        title: "pluievolution.error.title",
                        message: "pluievolution.attachment.error.get",
                        uid: "pluievolution.attachment.error.get",
                        position: "tr",
                        autoDismiss: 5
                    }, 'warning')
                ));
        });

export const downloadPluiAttachmentEpic = (action$) =>
    action$.ofType(actions.PLUI_EVOLUTION_DOWNLOAD_ATTACHMENT)
        .switchMap((action) => {
            const url = backendURLPrefix + "/attachment/" + action.attachment.id + "/download";
            return Rx.Observable.defer(() => axios.get(url, {
                responseType: 'arraybuffer'
            }))
                .switchMap((response) => {
                    const fileBlob = new Blob([response.data], {type: response.headers['content-type']});
                    saveAs(fileBlob, action.attachment.name);
                    return Rx.Observable.of(
                        success({
                            title: "pluievolution.success.title",
                            message: "pluievolution.attachment.download.title",
                            uid: "pluievolution.attachment.download",
                            position: "tr",
                            autoDismiss: 3
                        }),
                    )
                })
                .catch(e => Rx.Observable.of(
                    show({
                        title: "pluievolution.error.title",
                        message: "pluievolution.attachment.error.download",
                        uid: "pluievolution.attachment.download",
                        position: "tr",
                        autoDismiss: 3
                    }, 'warning')
                ));
        });

export const loadMeEpicPlui = (action$,store) =>
    action$.ofType(actions.PLUI_EVOLUTION_USER_ME_GET)
        .switchMap((action) => {
            if (action.user) {
                return Rx.Observable.of(gotMe(action.user)).delay(0);
            }
            const state = store.getState();
            const url = backendURLPrefix + "/user/me";
            return Rx.Observable.defer(() => axios.get(url))
                .switchMap((response) => Rx.Observable.from(
                    [gotMe(response.data)]
                        .concat(response.data.organization === pluiEvolutionEtablissementConfigurationSelector(state).organisationRm
                            ? [getAllGeographicEtablissement()]
                            : [])
                ))
                .catch(e => Rx.Observable.of(loadActionError("pluievolution.init.me.error", null, e)));
        });

export const savePluiRequest = (action$) =>
    action$.ofType(actions.PLUI_EVOLUTION_SAVE_PLUIREQUEST)
        .switchMap((action) => {
            const url = backendURLPrefix + "/request";
            const params = {
                timeout: 30000,
                headers: {'Accept': 'application/json', 'Content-Type': 'application/json'}
            };

            const saveRequest = action.pluiRequest.uuid ? axios.put(url, action.pluiRequest, params) : axios.post(url, action.pluiRequest, params);
            let actualPluiRequestSaved;

            return Rx.Observable.defer(() => saveRequest)
                .switchMap(response => Rx.Observable.of(response.data))
                .catch(e => {
                    e.saveError = true;
                    return Rx.Observable.throw(e);
                })
                .switchMap((pluiRequestCreatead) => {
                    actualPluiRequestSaved = pluiRequestCreatead;
                    const attachmentsRequest = buildAttachmentsRequest(pluiRequestCreatead.uuid, action.attachments);
                    return attachmentsRequest.length > 0 ? Rx.Observable.forkJoin(attachmentsRequest) : Rx.Observable.of([]);
                })
                .switchMap(() => Rx.Observable.from([
                    success({
                        title: "pluievolution.success.title",
                        message: "pluievolution.msgBox.requestSaved.message",
                        uid: "pluievolution.msgBox.requestSaved",
                        position: "tr",
                        autoDismiss: 5
                    }),
                    closeRequest(),
                    refreshLayerVersion(pluiEvolutionLayerId)
                ]))
                .catch(e => {
                    // Erreur lors de l'enregistrement de la requete plui
                    if (e.saveError) {
                        return Rx.Observable.from([
                            loadActionError("pluievolution.create.error", null, e),
                            error({
                                title: "pluievolution.error.title",
                                message: "pluievolution.create.error",
                                uid: "pluievolution.msgBox.requestSaved",
                                position: "tr",
                                autoDismiss: 5
                            })
                        ])
                    }

                    // Erreur lors de la sauvegarde des fichiers joints
                    return Rx.Observable.from([
                        loadActionError("pluievolution.attachment.error.update", {filename: e.attachment.name}, e),
                        pluiRequestSaved(actualPluiRequestSaved),
                        error({
                            title: "pluievolution.error.title",
                            message: "pluievolution.attachment.error.update",
                            uid: "pluievolution.msgBox.requestSaved",
                            position: "tr",
                            values: {filename: e.attachment.name},
                            autoDismiss: 5
                        })
                    ])
                });
        });

export const initPluiDrawingSupportEpic = action$ =>
    action$.ofType(actions.PLUI_EVOLUTION_INIT_SUPPORT_DRAWING)
        .switchMap(() => Rx.Observable.of(changeMapInfoState(false)));

export const displayAllPluiRequest = (action$, store) =>
    action$.ofType(actions.PLUI_EVOLUTION_DISPLAY_ALL)
        .switchMap(() => {
            const pluiLayer = head(store.getState().layers.flat.filter(l => l.id === pluiEvolutionLayerId));
            return Rx.Observable.from(
                pluiLayer
                    ? [refreshLayerVersion(pluiEvolutionLayerId)]
                    : [addLayer({
                        handleClickOnLayer: true,
                        hideLoading: true,
                        id: pluiEvolutionLayerId,
                        name: pluiEvolutionLayerName,
                        title: PLUI_EVOLUTION_LAYER_TITLE,
                        type: "wms",
                        search: {
                            type: "wfs",
                            url: backendURLPrefix + "/carto/wfsRequest"
                        },
                        params: {
                            exceptions: 'application/vnd.ogc.se_xml'
                        },
                        allowedSRS: pluiEvolutionLayerProjection,
                        format: "image/png",
                        singleTile: false,
                        url: backendURLPrefix + "/carto/wmsRequest",
                        visibility: true,
                        featureInfo: {
                            format: "PROPERTIES"
                        }
                    }),
                        selectNode(pluiEvolutionLayerId,"layer",false)
                    ]
            );
        });

export const displayPluiEtablissement = (action$, store) =>
    action$.ofType(actions.PLUI_EVOLUTION_DISPLAY_ETABLISSEMENT)
        .switchMap((action) => {
            if( !Proj4js.defs(pluiEvolutionLayerProjection) ) {
                window.pluiEvolution.debug("add defs...");
                Proj4js.defs("EPSG:3948","+proj=lcc +lat_1=47.25 +lat_2=48.75 +lat_0=48 +lon_0=3 +x_0=1700000 +y_0=7200000 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs");
            }
            const state = store.getState();
            let requestEtablissement = null;

            let url = backendURLPrefix;
            if (action.pluiRequestType === PluiRequestType.INTERCOMMUNE && !action.geographicEtablissement) {
                requestEtablissement = axios.get(url + "/user/etablissement");
            }
            else if (action.pluiRequestType === PluiRequestType.INTERCOMMUNE && action.geographicEtablissement) {
                requestEtablissement = Promise.resolve({
                    data: action.geographicEtablissement
                });
            }

            else if (action.pluiRequestType === PluiRequestType.METROPOLITAIN) {
                requestEtablissement = axios.get(url + "/geographic/etablissements/" + pluiEvolutionEtablissementConfigurationSelector(state).codeInseeRm);
            }

            return Rx.Observable.defer(() => requestEtablissement)
                .switchMap(response => Rx.Observable.of(response.data))
                .catch(e => Rx.Observable.throw(e))
                .switchMap((geographicEtablissement) => {
                    const existingLocalisation = geographicEtablissement && geographicEtablissement.localisation && geographicEtablissement.localisation.coordinates && geographicEtablissement.localisation.coordinates.length > 0;
                    let coordinates;
                    if (existingLocalisation && GeometryType.POINT === geographicEtablissement.localisation.type) {
                        coordinates = geographicEtablissement.localisation.coordinates;
                    }
                    else {
                        return Rx.Observable.of(
                            show({
                                title: "pluievolution.error.title",
                                message: "pluievolution.localisation.error.etablissement",
                                uid: "pluievolution.localisation.error.etablissement",
                                position: "tr",
                                autoDismiss: 4
                            }, 'warning')
                        );
                    }

                    const feature = {
                        geometry: {
                            type: GeometryType.POINT,
                            coordinates: coordinates
                        },
                        newFeature: true,
                        type: "Feature",
                    };

                    const options = {
                        drawEnabled: false,
                        editEnabled: false,
                        featureProjection: pluiEvolutionLayerProjection,
                        selectEnabled: false,
                        stopAfterDrawing: true,
                        transformToFeatureCollection: false,
                        translateEnabled: false,
                        useSelectedStyle: false
                    };

                    return Rx.Observable.from([
                        changeDrawingStatus("drawOrEdit", GeometryType.POINT, "pluievolution", [feature], options),
                        updateLocalisation(geographicEtablissement.localisation)
                    ]);
                }).catch(() => Rx.Observable.of(
                    show({
                        title: "pluievolution.error.title",
                        message: "pluievolution.localisation.error.etablissement",
                        uid: "pluievolution.localisation.error.etablissement",
                        position: "tr",
                        autoDismiss: 4
                    }, 'warning')
                ));
        });

export const startDrawingPluiEpic = action$ =>
    action$.ofType(actions.PLUI_EVOLUTION_START_DRAWING)
        .switchMap((action) => {
            if( !Proj4js.defs(pluiEvolutionLayerProjection) ) {
                window.pluiEvolution.debug("add defs...");
                Proj4js.defs("EPSG:3948","+proj=lcc +lat_1=47.25 +lat_2=48.75 +lat_0=48 +lon_0=3 +x_0=1700000 +y_0=7200000 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs");
            }
            const existingLocalisation = action.localisation && action.localisation.coordinates && action.localisation.coordinates.length > 0;
            let coordinates = Array(0);
            if (existingLocalisation && GeometryType.POINT === action.localisation.type) {
                coordinates = action.localisation.coordinates;
            }

            const feature = {
                geometry: {
                    type: action.geometryType,
                    coordinates: coordinates
                },
                newFeature: !existingLocalisation,
                type: "Feature",
            };

            const drawOptions = {
                drawEnabled: true,
                editEnabled: false,
                featureProjection: pluiEvolutionLayerProjection,
                selectEnabled: false,
                stopAfterDrawing: true,
                transformToFeatureCollection: false,
                translateEnabled: false,
                useSelectedStyle: false
            };
            return Rx.Observable.from([
                changeDrawingStatus("drawOrEdit", action.geometryType, "pluievolution", [feature], drawOptions),
                setDrawing(true)
            ]);
        });

export const geometryChangePluiEpic = action$ =>
    action$.ofType(GEOMETRY_CHANGED)
        .filter(action => action.owner === 'pluievolution')
        .switchMap( (action) => {
            let localisation = {};
            if (action.features && action.features.length > 0) {
                const geometryType = action.features[0].geometry.type;
                const coordinates = action.features[0].geometry.coordinates;

                if (GeometryType.POINT === geometryType) {
                    localisation = {
                        type: GeometryType.POINT,
                        coordinates: [coordinates[0], coordinates[1]],
                        projection: DEFAULT_PROJECTION_CODE
                    };
                }
            }
            return Rx.Observable.of(updateLocalisation(localisation));
        });

export const clearDrawnPluiEpic = action$ =>
    action$.ofType(actions.PLUI_EVOLUTION_CLEAR_DRAWN)
        .switchMap(() => {
            return Rx.Observable.from([
                changeDrawingStatus("clean", null, "pluievolution", [], {}),
                updateLocalisation([]),
                setDrawing(false)
            ]);
        });

export const stopDrawingPluiEpic = (action$, store) =>
    action$.ofType(actions.PLUI_EVOLUTION_STOP_DRAWING)
        .switchMap((action) => {
            const state = store.getState();
            const drawOptions = {
                drawEnabled: false,
                editEnabled: false,
                featureProjection: pluiEvolutionLayerProjection,
                selectEnabled: false,
                drawing: false,
                stopAfterDrawing: true,
                transformToFeatureCollection: false,
                translateEnabled: false
            };

            //work around to avoid import of draw.js - see issues with geosolutions
            let actualFeatures = state && state.draw && state.draw.tempFeatures;
            if (!actualFeatures || actualFeatures.length === 0) {
                actualFeatures = [
                    {
                        geometry: {
                            type: action.geometryType,
                            coordinates: Array(0)
                        },
                        type: "Feature"
                    }
                ];
            }

            return Rx.Observable.from([
                changeDrawingStatus("drawOrEdit", action.geometryType, "pluievolution", actualFeatures, drawOptions),
                setDrawing(false)
            ]);
        });

export const stopDrawingSupportPluiEpic = action$ =>
    action$.ofType(actions.PLUI_EVOLUTION_STOP_SUPPORT_DRAWING)
        .switchMap(() => {
            return Rx.Observable.from([
                changeMapInfoState(true),
                changeDrawingStatus("clean", null, "pluievolution", [], {})
            ]);
        });

// L'interception des clics pour le plugin n'est faite que si le plugin est actif et sa couche est selectionnée
export const clickMapPluiEpic = (action$, store) =>
    action$.ofType(CLICK_ON_MAP)
        .filter((action) => isPluievolutionActivateAndSelected(store.getState()))
        .switchMap((action) => {
            const overrideParams = {};
            overrideParams[pluiEvolutionLayerName] = {
                info_format: "application/json"
            };
            return Rx.Observable.of(featureInfoClick(action.point, pluiEvolutionLayerName, [], overrideParams)).concat(Rx.Observable.of(forceUpdateMapLayout()));
        });

const buildAttachmentsRequest = (uuid, attachments) => {
    const url = backendURLPrefix + "/request/" + uuid + "/upload";
    return attachments ? attachments.map((attachment) => {
        const formData = new FormData();
        formData.append('file', attachment.file);
        return Rx.Observable.defer(() => axios.post(url, formData))
            .catch((e) => {
                e.attachment = attachment;
                return Rx.Observable.throw(e);
            });
    }) : [];
}

/** Cette epics sert à charger les projection présentes dans le localconfig et actuellement pas très prise en compte au chargement */
export const ensureProjectionDefsPlui = (action$) =>
    action$.ofType(actions.PLUI_EVOLUTION_ENSURE_PROJ4)
        .switchMap((action) => {
            if( action.projectionDefs && action.projectionDefs.length > 0){
                action.projectionDefs.forEach(projectionDef => {
                    window.pluiEvolution.debug("plui projectionDef :", projectionDef);
                    window.pluiEvolution.debug("plui projectionDef ?", Proj4js.defs(projectionDef.code));
                    if (!Proj4js.defs(projectionDef.code)) {
                        window.pluiEvolution.debug("plui add projection :", projectionDef.code);
                        Proj4js.defs(projectionDef.code, projectionDef.def);
                    }
                });
            }

            return Rx.Observable.of(ensureProj4Done());
        });
