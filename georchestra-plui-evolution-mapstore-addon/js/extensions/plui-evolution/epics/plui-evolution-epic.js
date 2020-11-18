import * as Rx from 'rxjs';
import axios from 'axios';
import {changeDrawingStatus, END_DRAWING, GEOMETRY_CHANGED} from "@mapstore/actions/draw";
import {changeMapInfoState} from "@mapstore/actions/mapInfo";
import {
    actions,
    initPluiEvolutionDone,
    loadedAttachmentConfiguration,
    addedAttachment,
    removedAttachment,
    gotMe,
    loadActionError,
    loadInitError,
    setDrawing,
    updateLocalisation
} from '../actions/plui-evolution-action';
import {FeatureProjection, GeometryType} from "../constants/plui-evolution-constants";

let backendURLPrefix = "/pluievolution";

export const initPluiEvolutionEpic = (action$) =>
	action$.ofType(actions.INIT_PLUI_EVOLUTION)
	    .switchMap((action) => {
	        console.log("pluie epics init:"+ action.url);
	        if( action.url ) {	        	
	        	backendURLPrefix = action.url;
	        }
	        return Rx.Observable.of(initPluiEvolutionDone()).delay(0);
	    });        

export const loadAttachmentConfigurationEpic = (action$) =>
    action$.ofType(actions.ATTACHMENT_CONFIGURATION_LOAD)
        .switchMap((action) => {
            console.log("pluie epics attachment config");
            if (action.attachmentConfiguration) {
                return Rx.Observable.of(loadedAttachmentConfiguration(action.attachmentConfiguration)).delay(0);
            }
            console.log("pluie back " + backendURLPrefix);
            const url = backendURLPrefix + "/xxxx/attachment/configuration";
            return Rx.Observable.defer(() => axios.get(url))
                .switchMap((response) => Rx.Observable.of(loadedAttachmentConfiguration(response.data)))
                .catch(e => Rx.Observable.of(loadInitError("plui-evolution.init.attattachmentConfiguration.error", e)));
        });

export const addAttachmentEpic = (action$) =>
    action$.ofType(actions.ADD_ATTACHMENT)
        .switchMap((action) => {
            console.log("pluie epics add attachement");
            const url = backendURLPrefix + "/xxxx/" + action.attachment.uuid + "/upload";
            const formData = new FormData();
            formData.append('file',action.attachment.file);

            return Rx.Observable.defer(() => axios.post(url, formData))
                .switchMap((response) => Rx.Observable.of(addedAttachment(response.data)))
                .catch(e => Rx.Observable.of(loadActionError("plui-evolution.attachment.error", e)));
        });

export const removeAttachmentEpic = (action$) =>
    action$.ofType(actions.REMOVE_ATTACHMENT)
        .switchMap((action) => {
            console.log("pluie epics remove attachement");
            const url = backendURLPrefix + "/xxxx/" + action.attachment.uuid + "/delete/" + action.attachment.id;

            return Rx.Observable.defer(() => axios.delete(url))
                .switchMap((response) => Rx.Observable.of(removedAttachment(action.attachment.index)))
                .catch(e => Rx.Observable.of(loadActionError("plui-evolution.attachment.delete.error", e)));
        });

export const loadMeEpic = (action$) =>
    action$.ofType(actions.USER_ME_GET)
        .switchMap((action) => {
            console.log("pluie epics me");
            if (action.user) {
                return Rx.Observable.of(gotMe(action.user)).delay(0);
            }
            const url = backendURLPrefix + "/user/me";
            return Rx.Observable.defer(() => axios.get(url))
                .switchMap((response) => Rx.Observable.of(gotMe(response.data)))
                .catch(e => Rx.Observable.of(loadInitError("plui-evolution.init.me.error", e)));
        });

export const initDrawingSupportEpic = action$ =>
    action$.ofType(actions.PLUI_EVOLUTION_INIT_SUPPORT_DRAWING)
        .switchMap(() => {
            return Rx.Observable.of(changeMapInfoState(false));
        });

export const startDrawingEpic = action$ =>
    action$.ofType(actions.PLUI_EVOLUTION_START_DRAWING)
        .switchMap((action) => {
            const existingLocalisation = action.localisation && action.localisation.length > 0;
            let coordinates = Array(0);
            if (existingLocalisation) {
                switch (action.geometryType) {
                    case GeometryType.POLYGON:
                        coordinates = [action.localisation.map(localisationCoords => [parseFloat(localisationCoords.x), parseFloat(localisationCoords.y)])];
                        break;
                    case GeometryType.LINE:
                        coordinates = action.localisation.map(localisationCoords => [parseFloat(localisationCoords.x), parseFloat(localisationCoords.y)]);
                        break;
                    case GeometryType.POINT:
                        coordinates = [parseFloat(action.localisation[0].x), parseFloat(action.localisation[0].y)];
                }
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
                editEnabled: true,
                featureProjection: FeatureProjection,
                selectEnabled: false,
                stopAfterDrawing: true,
                transformToFeatureCollection: false,
                translateEnabled: false,
                useSelectedStyle: false
            };
            return Rx.Observable.from([
                changeDrawingStatus("drawOrEdit", action.geometryType, "plui-evolution", [feature], drawOptions),
                setDrawing(true)
            ]);
        });

export const geometryChangeEpic = action$ =>
    action$.ofType(GEOMETRY_CHANGED)
        .filter(action => action.owner === 'pluievolution')
        .switchMap( (action) => {
            let localisation = [];
            if (action.features && action.features.length > 0) {
                const geometryType = action.features[0].geometry.type;
                const coordinates = action.features[0].geometry.coordinates;
                switch (geometryType) {
                    case GeometryType.POINT:
                        localisation = [{
                            x: coordinates[0].toString(),
                            y: coordinates[1].toString()
                        }];
                        break;
                    case GeometryType.LINE:
                        localisation = coordinates.map(coordinate => ({
                            x: coordinate[0].toString(),
                            y: coordinate[1].toString()
                        }));
                        break;
                    case GeometryType.POLYGON:
                        localisation = coordinates[0].map(coordinate => ({
                            x: coordinate[0].toString(),
                            y: coordinate[1].toString()
                        }));
                        break;
                    default:
                        localisation = [];
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
            //let actualFeatures = changedGeometriesSelector(state);
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
