import * as Rx from 'rxjs';
import axios from 'axios';
import {head} from 'lodash';
import { saveAs } from 'file-saver';
import {changeDrawingStatus, END_DRAWING, GEOMETRY_CHANGED} from "@mapstore/actions/draw";
import {addLayer, refreshLayerVersion, selectNode} from '@mapstore/actions/layers';
import {CLICK_ON_MAP} from '@mapstore/actions/map';
import {changeMapInfoState, featureInfoClick, LOAD_FEATURE_INFO, showMapinfoMarker, hideMapinfoMarker} from "@mapstore/actions/mapInfo";
import { success, error, show } from '@mapstore/actions/notifications';
import {
    actions,
    closeRequest,
    initPluiEvolutionDone,
    loadedAttachmentConfiguration,
    getAttachments,
    gotMe,
    initDrawingSupport,
    loadActionError,
    loadingPluiCreateForm,
    loadingPluiUpdateForm,
    loadPluiForm,
    openingPanel,
    openPanel,
    pluiRequestSaved,
    setDrawing,
    setAllPluiRequestDisplay,
    updateAttachments,
    updateLocalisation, status
} from '../actions/plui-evolution-action';
import {
    CODE_INSEE_RENNES_METROPOLE,
    FeatureProjection,
    GeometryType,
    PluiRequestType,
    PLUI_EVOLUTION_LAYER_ID,
    PLUI_EVOLUTION_LAYER_NAME,
    PLUI_EVOLUTION_LAYER_TITLE
} from "../constants/plui-evolution-constants";
import {SET_CURRENT_BACKGROUND_LAYER} from "@mapstore/actions/backgroundselector";
import {SET_CONTROL_PROPERTY} from "@mapstore/actions/controls";


let backendURLPrefix = "/pluievolution";

export const openPanelEpic = (action$) =>
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
            console.log("pluie epics init:"+ action.url);
            if( action.url ) {
                backendURLPrefix = action.url;
            }
            return Rx.Observable.of(initPluiEvolutionDone()).delay(0);
        });

export const loadAttachmentConfigurationEpic = (action$) =>
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

export const getAttachmentsEpic = (action$) =>
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

export const downloadAttachmentEpic = (action$) =>
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

export const loadMeEpic = (action$) =>
    action$.ofType(actions.PLUI_EVOLUTION_USER_ME_GET)
        .switchMap((action) => {
            if (action.user) {
                return Rx.Observable.of(gotMe(action.user)).delay(0);
            }
            const url = backendURLPrefix + "/user/me";
            return Rx.Observable.defer(() => axios.get(url))
                .switchMap((response) => Rx.Observable.of(gotMe(response.data)))
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
                    refreshLayerVersion(PLUI_EVOLUTION_LAYER_ID)
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

export const initDrawingSupportEpic = action$ =>
    action$.ofType(actions.PLUI_EVOLUTION_INIT_SUPPORT_DRAWING)
        .switchMap(() => Rx.Observable.of(changeMapInfoState(false)));

export const displayAllPluiRequest = (action$, store) =>
    action$.ofType(SET_CURRENT_BACKGROUND_LAYER)
        .filter(() => {
            return head(store.getState().layers.flat.filter(l => l.id === PLUI_EVOLUTION_LAYER_ID)) === null;
        })
        .merge(action$.ofType(SET_CONTROL_PROPERTY)
            .filter((action) => {
                return action.control === 'backgroundSelector';
            })
            .take(1)
            .switchMap((action) => {
                const url = backendURLPrefix + "/carto/wmsRequest";
                return Rx.Observable.from([
                    addLayer({
                        handleClickOnLayer: true,
                        hideLoading: true,
                        id: PLUI_EVOLUTION_LAYER_ID,
                        name: PLUI_EVOLUTION_LAYER_NAME,
                        title: PLUI_EVOLUTION_LAYER_TITLE,
                        type: "wms",
                        search: {
                            type: "wfs",
                            url: backendURLPrefix + "/carto/wfsRequest"
                        },
                        params: {
                            exceptions: 'application/vnd.ogc.se_xml'
                        },
                        format: "image/png",
                        singleTile: false,
                        url: url,
                        visibility: true
                    }),
                    setAllPluiRequestDisplay(true),
                    selectNode(PLUI_EVOLUTION_LAYER_ID,"layer",false)
                ]);
            })
        );


export const startDrawingEpic = action$ =>
    action$.ofType(actions.PLUI_EVOLUTION_START_DRAWING)
        .switchMap((action) => {
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
                featureProjection: FeatureProjection,
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

export const displayEtablissement = action$ =>
    action$.ofType(actions.PLUI_EVOLUTION_DISPLAY_ETABLISSEMENT)
        .switchMap((action) => {

            let url = backendURLPrefix;
            if (action.pluiRequestType === PluiRequestType.INTERCOMMUNE) {
                url += "/user/etablissement";
            }
            else if (action.pluiRequestType === PluiRequestType.METROPOLITAIN) {
                url += "/geographic/etablissements/" + CODE_INSEE_RENNES_METROPOLE;
            }

            return Rx.Observable.defer(() => axios.get(url))
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

                    return Rx.Observable.from([
                        changeDrawingStatus("replace", GeometryType.POINT, "pluievolution", [feature], {}),
                        updateLocalisation(geographicEtablissement.localisation),
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

export const geometryChangeEpic = action$ =>
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
                        coordinates: coordinates
                    };
                }
            }
            return Rx.Observable.of(updateLocalisation(localisation));
        });

export const endDrawingEpic = action$ =>
    action$.ofType(END_DRAWING)
        .filter(action => action.owner === 'pluievolution')
        .switchMap(() => {
            return Rx.Observable.of(setDrawing(false));
        });

export const clearDrawnEpic = action$ =>
    action$.ofType(actions.PLUI_EVOLUTION_CLEAR_DRAWN)
        .switchMap(() => {
            return Rx.Observable.from([
                changeDrawingStatus("clean", null, "pluievolution", [], {}),
                updateLocalisation([]),
                setDrawing(false)
            ]);
        });

export const stopDrawingEpic = (action$, store) =>
    action$.ofType(actions.PLUI_EVOLUTION_STOP_DRAWING)
        .switchMap((action) => {
            const state = store.getState();
            const drawOptions = {
                drawEnabled: false,
                editEnabled: false,
                featureProjection: FeatureProjection,
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

export const stopDrawingSupportEpic = action$ =>
    action$.ofType(actions.PLUI_EVOLUTION_STOP_SUPPORT_DRAWING)
        .switchMap(() => {
            return Rx.Observable.from([
                changeMapInfoState(true),
                changeDrawingStatus("clean", null, "pluievolution", [], {})
            ]);
        });

export const clickMapEpic = (action$) =>
    action$.ofType(CLICK_ON_MAP)
        .switchMap((action) => {
            const overrideParams = {};
            overrideParams[PLUI_EVOLUTION_LAYER_NAME] = {
                info_format: "application/json"
            };
            return Rx.Observable.of(featureInfoClick(action.point, PLUI_EVOLUTION_LAYER_NAME, [], overrideParams));
        });

export const loadFeatureInfoEpic = (action$) =>
    action$.ofType(LOAD_FEATURE_INFO)
        .filter(action => action.layer && action.layer.id === PLUI_EVOLUTION_LAYER_ID)
        .switchMap((action) => {
            if (action.data) {
                const features = action.data.features;
                if (features.length === 1) {
                    const properties = features[0].properties;
                    console.log('selected point', properties);
                    return Rx.Observable.from([
                        getAttachments(properties.uuid),
                        openPanel(properties),
                        showMapinfoMarker()
                    ]);
                }
                else if (features.length > 1) {
                    // TODO: Ouverture panel plusieurs pluiRequest
                }
            }
            // pas de demande request sur la carte sur ce point
            return Rx.Observable.empty();
        });

const buildAttachmentsRequest = (uuid, attachments) => {
    const url = backendURLPrefix + "/request/" + uuid + "/upload";
    return attachments ? attachments.map((attachment) => {
        const formData = new FormData();
        formData.append('file', attachment.file);
        return Rx.Observable.defer(() => axios.post(url, formData))
            .catch((e) =>  {
                e.attachment = attachment;
                return Rx.Observable.throw(e);
            });
    }) : [];
}
