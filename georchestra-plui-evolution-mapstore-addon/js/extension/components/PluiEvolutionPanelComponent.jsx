import React from 'react';
import {connect} from 'react-redux';
import Dock from 'react-dock';
import ContainerDimensions from 'react-container-dimensions';
import {PropTypes} from 'prop-types';
import {
    Button,
    Col,
    ControlLabel,
    Form,
    FormControl,
    FormGroup,
    Glyphicon,
    Grid,
    HelpBlock,
    InputGroup,
    Radio,
    Row
} from 'react-bootstrap';
import Message from '@mapstore/components/I18N/Message';
import ConfirmDialog from '@mapstore/components/misc/ConfirmDialog';
import LoadingSpinner from '@mapstore/components/misc/LoadingSpinner';
import {getMessageById} from '@mapstore/utils/LocaleUtils';
import {setViewer} from '@mapstore/utils/MapInfoUtils';
import {closeIdentify} from '@mapstore/actions/mapInfo';
import {PLUI_EVOLUTION_REQUEST_VIEWER, PluiEvolutionRequestViewer} from './PluiEvolutionRequestViewer';
import {openPanel, status} from '../actions/plui-evolution-action';
import {
    GeometryType,
    MAX_NB_CHARACTERS_PLUI_OBJECT,
    PluiRequestType
} from '../constants/plui-evolution-constants';
import {PluiEvolutionViewer} from "@js/extension/components/PluiEvolutionViewer";

export class PluiEvolutionPanelComponent extends React.Component {
    static propTypes = {
        id: PropTypes.string,
        active: PropTypes.bool,
        status: PropTypes.string,
        closing: PropTypes.bool,
        drawing: PropTypes.bool,
        loading: PropTypes.bool,
        readOnly: PropTypes.bool,
        viewerMode: PropTypes.bool,
        response: PropTypes.object,
        // config
        wrap: PropTypes.bool,
        wrapWithPanel: PropTypes.bool,
        panelStyle: PropTypes.object,
        panelClassName: PropTypes.string,
        closeGlyph: PropTypes.string,
        createGlyph: PropTypes.string,
        deleteGlyph: PropTypes.string,
        buttonStyle: PropTypes.object,
        style: PropTypes.object,
        dockProps: PropTypes.object,
        width: PropTypes.number,
        // data
        attachmentConfiguration: PropTypes.object,
        layerConfiguration: PropTypes.object,
        etablissementConfiguration: PropTypes.object,
        geographicEtablissements: PropTypes.array,
        contextThemas: PropTypes.array,
        user: PropTypes.object,
        currentLayer: PropTypes.object,
        pluiRequest: PropTypes.object,
        attachments: PropTypes.array,
        error: PropTypes.object,
        localConfig: PropTypes.object,
        // redux
        initPluiEvolution: PropTypes.func,
        startDrawing: PropTypes.func,
        stopDrawing: PropTypes.func,
        clearDrawn: PropTypes.func,
        loadAttachmentConfiguration: PropTypes.func,
        updateAttachments: PropTypes.func,
        removeAttachment: PropTypes.func,
        getAttachments: PropTypes.func,
        downloadAttachment: PropTypes.func,
        loadLayerConfiguration: PropTypes.func,
        getMe: PropTypes.func,
        displayEtablissement: PropTypes.func,
        displayAllPluiRequest: PropTypes.func,
        loadEtablissementConfiguration: PropTypes.func,
        savePluiRequest: PropTypes.func,
        requestClosing: PropTypes.func,
        cancelClosing: PropTypes.func,
        confirmClosing: PropTypes.func,
        closeRequest: PropTypes.func,
        loadActionError: PropTypes.func,
        loadingPluiCreateForm: PropTypes.func,
        changeFormStatus: PropTypes.func,
        ensureProj4: PropTypes.func,
        closeViewer: PropTypes.func
    };

    static defaultProps = {
        id: "plui-evolution-panel",
        active: false,
        status: status.EMPTY,
        closing: false,
        drawing: false,
        loading: false,
        readOnly: false,
        viewerMode: false,
        response: {features: []},
        // config
        wrap: false,
        modal: true,
        wrapWithPanel: false,
        panelStyle: {
            zIndex: 100,
            overflow: "hidden",
            height: "100%"
        },
        panelClassName: "plui-evolution-panel",
        closeGlyph: "1-close",
        createGlyph: "ok",
        deleteGlyph: "trash",
        // side panel properties
        width: 660,
        dockProps: {
            dimMode: "none",
            size: 0.30,
            fluid: true,
            position: "right",
            zIndex: 1050
        },
        dockStyle: {
            zIndex: 100,
        },
        // data
        attachmentConfiguration: null,
        user: null,
        geographicEtablissements: null,
        etablissementConfiguration: null,
        attachments: null,
        pluiRequest: null,
        layerConfiguration: null,
        localConfig: null,
        // misc
        initPluiEvolution: ()=>{},
        startDrawing: ()=>{},
        stopDrawing: ()=>{},
        clearDrawn: ()=>{},
        loadAttachmentConfiguration: ()=>{},
        updateAttachments: () => {},
        removeAttachment: () => {},
        getAttachments: () => {},
        downloadAttachment: () => {},
        loadLayerConfiguration: ()=>{},
        getMe: ()=>{},
        displayEtablissement: ()=>{},
        displayAllPluiRequest: ()=>{},
        loadEtablissementConfiguration: ()=>{},
        savePluiRequest: ()=>{},
        requestClosing: ()=>{},
        cancelClosing: ()=>{},
        confirmClosing: ()=>{},
        closeRequest: ()=>{},
        loadingPluiCreateForm: ()=>{},
        changeFormStatus: ()=>{},
        loadActionError: ()=>{},
        toggleControl: () => {},
        ensureProj4: () => {},
        closeViewer: () => {},
    };

    static contextTypes = {
        messages: PropTypes.object
    };

    initialState = {
        errorAttachment: "",
        errorFields: {}
    };

    constructor(props) {
        super(props);
        this.state = this.initialState;
        this.props.initPluiEvolution(this.props.backendURL);
        console.log('pluie construct component...');

        // chargement des projections dans localconfig si nécessaire
        this.props.ensureProj4(this.props.localConfig.projectionDefs);


    }

    registerViewer() {
        console.log('pluie check viewer', getViewer(PLUI_EVOLUTION_REQUEST_VIEWER));
        if( !getViewer(PLUI_EVOLUTION_REQUEST_VIEWER)) {
            const PluiEvolutionRequestViewerConnected = connect((state) => ({
                // debug
                state : state
            }), {
                openPanel: openPanel,
                closeIdentify: closeIdentify
            })(PluiEvolutionRequestViewer);
            console.log('pluie register viewer');
            setViewer(PLUI_EVOLUTION_REQUEST_VIEWER, PluiEvolutionRequestViewerConnected);
            console.log('pluie registered viewer:', getViewer(PLUI_EVOLUTION_REQUEST_VIEWER));
        }
    }

    componentWillMount() {
        this.setState({initialized: false, loaded: false});
        this.props.loadAttachmentConfiguration();
        this.props.loadLayerConfiguration();
        this.props.loadLayerConfiguration();
        this.props.loadEtablissementConfiguration();
    }

    componentDidUpdate(prevProps, prevState) {
        console.log("pluie didUpdate...");
        // Tout est-il initialisé ?
        this.state.initialized = this.props.attachmentConfiguration !== null
            && this.props.user !== null
            && this.loadGeographicEtablissement(this.props.user)
            && this.props.layerConfiguration != null
            && this.props.etablissementConfiguration != null;

        if (this.props.status === status.LOAD_REQUEST) {
            this.state.initialized &= this.props.pluiRequest != null && this.props.attachments != null;
        }

        if (this.props.pluiRequest != null && this.state.pluiRequest != null) {
            this.state.pluiRequest.localisation = this.props.pluiRequest.localisation;
            this.state.pluiRequest.uuid = this.props.pluiRequest.uuid;
        }

        if (this.props.status === status.INIT_FORM_REQUEST) {
            this.state.pluiRequest = {
                object: "",
                subject: "",
                type: "",
                codeInsee: "",
                localisation: ""
            };
            this.state.attachments = [];
            this.setState(this.state);
            this.props.changeFormStatus(status.CREATE_REQUEST);

            this.props.displayAllPluiRequest();
        }

        if (this.props.status === status.LOAD_REQUEST && !this.state.pluiRequest && this.state.initialized) {
            this.state.pluiRequest = this.props.pluiRequest;
            this.state.attachments = this.props.attachments;
            this.setState(this.state);
            this.props.getAttachments(this.props.pluiRequest.uuid);
        }

        if (this.props.status === status.CLEAN_REQUEST) {
            this.setState({...this.initialState, pluiRequest: null, attachments: null});
            this.props.updateAttachments(null);
            this.props.toggleControl();
        }

    }

    render() {
        console.log("pluie render");
        console.log('this.props render', this.props);
        console.log('this.state render', this.state);
        if( this.props.active ){
            
            //this.registerViewer();
            //this.connectViewer();
            // le panel est ouvert
            return (
                <ContainerDimensions>
                    { ({ width }) =>
                        <span>
                            <span className="ms-plui-evolution-panel react-dock-no-resize ms-absolute-dock ms-side-panel">
                                <Dock
                                    dockStyle={this.props.dockStyle} {...this.props.dockProps}
                                    isVisible={this.props.active}
                                    size={this.props.width / width > 1 ? 1 : this.props.width / width} >
                                    <div className={this.props.panelClassName}>
                                        {!this.props.viewerMode && this.renderHeader()}
                                        {
                                            !this.state.initialized &&
                                            this.renderLoading("pluievolution.open.loading")
                                        }
                                        {
                                            this.state.initialized && !this.props.viewerMode &&
                                            this.renderForm()
                                        }
                                        {
                                            this.state.initialized && this.props.viewerMode &&
                                            this.renderViewer()
                                        }
                                    </div>
                                </Dock>
                            </span>
                            {this.renderModelClosing()}
                        </span>
                    }
                </ContainerDimensions>
            );
        } else {
            return null;
        }
    }

    renderViewer() {
        return (
            <PluiEvolutionViewer viewerMode={this.props.viewerMode} response={this.props.response}
            openPanel={this.props.openPanel} closeViewer={this.props.closeViewer}/>
        );
    }

    /**
     * La rendition de la fenêtre modal de confirmation d'abandon
     */
    renderModelClosing(){
        if (this.props.closing ) {
            // si closing == true on demande l'abandon
            console.log("pluieclosing");
            return (<ConfirmDialog
                show
                modal
                onClose={this.props.cancelClosing}
                onConfirm={this.props.confirmClosing}
                confirmButtonBSStyle="default"
                closeGlyph="1-close"
                confirmButtonContent={<Message msgId="pluievolution.msgBox.ok" />}
                closeText={<Message msgId="pluievolution.msgBox.cancel" />}>
                <Message msgId="pluievolution.msgBox.info"/>
            </ConfirmDialog>);
        } else {
            return null;
        }
    }

    /**
     * La rendition du formulaire
     */
    renderForm() {
        if (this.state.pluiRequest) {
            return (
                <Form model={this.state.pluiRequest}>
                    {this.renderUserInformation()}
                    {this.renderPluiManual()}
                    {this.renderActivationButtonPluiRequestForm()}
                    {this.renderPluiRequestInformations()}
                    {this.props.loading ? this.renderLoading("pluievolution.create.loading") : null}
                </Form>
            );
        }
        return null;
    }

    renderLoading(msgId) {
        return (
            <div className="plui-loading-container">
                <div className="plui-loading">
                    <LoadingSpinner />
                    <Message msgId={msgId} />
                </div>
            </div>
        );
    }

    /**
     * La rendition de l'entête
     */
    renderHeader() {
        return (
            <Grid fluid className="ms-header" style={this.props.styling || this.props.mode !== "list" ? { width: '100%', boxShadow: 'none'} : { width: '100%' }}>
                <Row>
                    <Col xs={2}>
                        <Button className="square-button no-events">
                            <Glyphicon glyph="exclamation-sign"/>
                        </Button>
                    </Col>
                    <Col xs={8}>
                        <h4><Message msgId="pluievolution.msgBox.title"/></h4>
                        {this.renderMessage()}
                    </Col>
                    <Col xs={2}>
                        <Button className="square-button no-border" onClick={() => this.cancel()} >
                            <Glyphicon glyph={this.props.closeGlyph}/>
                        </Button>
                    </Col>
                </Row>
            </Grid>
        );
    }

    /**
     * La rendition d'un message d'erreur
     */
    renderMessage(){
        if( this.props.error ){
            return (
                <span className="error">
                    <Message msgId={this.props.error.message}
                             msgParams={this.props.error.messageParams ? this.props.error.messageParams : {}}/>
                </span>
            );
        } else if( this.props.message ){
            return (
                <span className="info"><Message msgId={this.props.message}/></span>
            );
        } else {
            return null;
        }
    }

    /**
     * La rendition de la partie utilisateur
     */
    renderUserInformation() {
        return (
            <div>
                <fieldset>
                    <FormGroup controlId="pluievolution.user.organization">
                        <InputGroup className="input-group">
                            <InputGroup.Addon className="addon">
                                <Message msgId="pluievolution.commune"/>
                            </InputGroup.Addon>
                            <FormControl type="text" readOnly value={this.props.user !== null ? this.props.user.organization : ''}/>
                        </InputGroup>
                    </FormGroup>
                </fieldset>
            </div>
        );
    }

    loadGeographicEtablissement(user) {
        if (this.isAgentRmUser(user)) {
            return !!this.props.geographicEtablissements;
        }
        else {
            return true;
        }
    }

    isAgentRmUser(user) {
        return user.organization === this.props.etablissementConfiguration.organisationRm;
    }

    renderPluiManual() {
        return (
            <div className="manual">
                <fieldset>
                    <Message msgId="pluievolution.msgBox.manual"/>
                </fieldset>
            </div>
        );
    }

    renderActivationButtonPluiRequestForm() {
        return (
            <div className="block-display-plui-request">
                <fieldset>
                    <Button bsSize="large" bsStyle="primary" onClick={this.disPlayRequestForm}>
                        <Message msgId="pluievolution.displayRequestForm"/>
                    </Button>
                </fieldset>
            </div>
        );
    }

    disPlayRequestForm = () => {
        this.state.formRequestIsDisplayed = true;
        this.setState(this.state);
        if (this.props.readOnly) {
            this.props.loadingPluiCreateForm();
        }
    }

    renderPluiRequestInformations() {
        if (this.state.formRequestIsDisplayed || this.props.readOnly) {
            return (
                <div >
                    {this.renderDetail()}
                    {this.renderAttachments()}
                    {this.renderLocalisation()}
                    {this.renderRequestComment()}
                    {this.renderFormButton()}
                </div>
            );
        }
    }

    /**
     * La rendition du détail du plui-evolution
     */
    renderDetail() {
        return (
            <div>
                <fieldset>
                    {this.renderStatut()}
                    {this.renderType()}
                    {this.renderSubject()}
                    {this.renderObject()}
                </fieldset>
            </div>
        );
    }

    renderStatut() {
        if (this.props.readOnly) {
            return (
                <FormGroup controlId="pluievolution.status">
                    <InputGroup className="input-group">
                        <InputGroup.Addon className="addon">
                            <Message msgId="pluievolution.status.title"/>
                        </InputGroup.Addon>
                        <FormControl type="text"
                                     value={this.state.pluiRequest.status}
                                     readOnly/>
                    </InputGroup>
                </FormGroup>
            );
        }
    }

    renderType() {
        if (this.props.readOnly) {
            return (
                <FormGroup controlId="pluievolution.type">
                    <InputGroup className="input-group">
                        <InputGroup.Addon className="addon">
                            <Message msgId="pluievolution.type.title"/>
                        </InputGroup.Addon>
                        <FormControl type="text"
                                     value={this.state.pluiRequest.type}
                                     readOnly/>
                    </InputGroup>
                </FormGroup>
            );
        }
    }

    renderSubject() {
        console.log('actual subject', this.state.pluiRequest.subject);
        return (
            <FormGroup controlId="pluievolution.subject"
                       validationState={this.state.errorFields.subject ? "error" : null}>
                <InputGroup className="input-group">
                    <InputGroup.Addon className="addon">
                        <Message msgId="pluievolution.subject.title"/>
                    </InputGroup.Addon>
                    <FormControl componentClass="textarea"
                                 bsSize="small"
                                 rows={2}
                                 value={this.state.pluiRequest.subject}
                                 placeholder={getMessageById(this.context.messages, "pluievolution.subject.placeholder")}
                                 onChange={this.handleSubjectChange}
                                 maxLength={130}
                                 readOnly={this.props.readOnly}
                                 required/>
                </InputGroup>
            </FormGroup>
        );
    }

    renderObject() {
        return (
            <FormGroup controlId="pluievolution.object"
                       validationState={this.state.errorFields.object ? "error" : null}>
                <InputGroup className="input-group">
                    <InputGroup.Addon className="addon">
                        <Message msgId="pluievolution.object.title"/>
                    </InputGroup.Addon>
                    <FormControl componentClass="textarea"
                                 bsSize="small"
                                 rows={4}
                                 value={this.state.pluiRequest.object}
                                 placeholder={getMessageById(this.context.messages, "pluievolution.object.placeholder")}
                                 onChange={this.handleObjectChange}
                                 maxLength={300}
                                 readOnly={this.props.readOnly}
                                 required/>
                </InputGroup>
                <HelpBlock><Message msgId="pluievolution.object.count"/> {this.getNbCharactersLeftForObject()}</HelpBlock>
            </FormGroup>
        );
    }

    getNbCharactersLeftForObject = () => {
        const actualNbCharacters = this.state.pluiRequest.object ? this.state.pluiRequest.object.length: 0;
        return MAX_NB_CHARACTERS_PLUI_OBJECT - actualNbCharacters;
    }

    /**
     * La rendition de la gestion des picèces jointes
     */
    renderAttachments() {
        return (
            <div>
                <fieldset>
                    <legend><Message msgId="pluievolution.attachment.files"/></legend>
                    {this.renderChooseFilesAttachments()}
                    <table className="table">
                        {this.renderHeaderTableAttachments()}
                        <tbody>
                        {this.renderTable(this.state.attachments)}
                        </tbody>
                    </table>
                </fieldset>
            </div>
        )
    }

    renderChooseFilesAttachments() {
        if (!this.props.readOnly) {
            return (
                <div>
                    <FormGroup controlId="formControlsFile">
                        <FormControl type="file" name="file" accept={this.getAllowedExtensions()}
                                     onChange={(e) => this.fileAddedHandler(e)} />
                        <HelpBlock>
                            <Message msgId="pluievolution.fileUpload.info"
                                     msgParams={{maxSizeFormatted: this.formatBytes(this.props.attachmentConfiguration.maxSize)}}/>
                        </HelpBlock>
                    </FormGroup>
                    <div className="col-sm-12">
                        <div id="passwordHelp" className="text-danger">
                            {
                                this.state.errorAttachment ? (
                                    <Message msgId={this.state.errorAttachment}/>
                                ) : null
                            }
                        </div>
                    </div>
                </div>
            );
        }
    }

    renderHeaderTableAttachments() {
        if (this.props.readOnly) {
            return (
                <thead>
                <tr>
                    <th className="col-sm-12" scope="col">
                        <Message msgId="pluievolution.attachment.tableName.title" />
                    </th>
                </tr>
                </thead>
            );
        }
        else {
            return (
                <thead>
                <tr>
                    <th className="col-sm-10" scope="col">
                        <Message msgId="pluievolution.attachment.tableName.title" />
                    </th>
                    <th className="col-sm-2" scope="col">
                        <Message msgId="pluievolution.attachment.tableAction.title" />
                    </th>
                </tr>
                </thead>
            );
        }
    }

    getAllowedExtensions = () => {
        return this.props.attachmentConfiguration.mimeTypes.join(",");
    }

    /**
     * La rendition du panel des pièces jointes
     */
    renderTable(attachments) {

        if (attachments) {
            return attachments.map((attachment, index) => {
                if (this.props.readOnly) {
                    return (
                        <tr key={index}>
                            <td className="col-sm-12">
                                <Button bsStyle="link" onClick={() => this.props.downloadAttachment(attachment)}>{attachment.name}</Button>
                            </td>
                        </tr>
                    );
                }
                else {
                    return (
                        <tr key={index}>
                            <td className="col-sm-10">{attachment.name}</td>
                            <td className="col-sm-2">
                                <Button className="btn btn-sq-xs btn-danger"
                                        onClick={() => this.fileDeleteHandler(index)}>
                                    <Glyphicon glyph={this.props.deleteGlyph}/>
                                </Button>
                            </td>
                        </tr>
                    );
                }
            });
        }
    }

    renderRequestComment() {
        if (this.props.readOnly) {
            return (
                <div>
                    <fieldset>
                        <FormGroup controlId="pluievolution.comment">
                            <InputGroup className="input-group">
                                <InputGroup.Addon className="addon">
                                    <Message msgId="pluievolution.comment.title"/>
                                </InputGroup.Addon>
                                <FormControl componentClass="textarea"
                                             bsSize="small"
                                             rows={4}
                                             readOnly
                                             value={this.state.pluiRequest.comment ? this.state.pluiRequest.comment : ""}/>
                            </InputGroup>
                        </FormGroup>
                    </fieldset>
                </div>
            );
        }
    }

    /**
     * La rendition de la saisie de la géométrie
     */
    renderLocalisation() {
        if (!this.props.readOnly) {
            return (
                <div>
                    <fieldset>
                        <legend><Message msgId="pluievolution.localisation.legend"/></legend>
                        <FormGroup controlId="localisation"
                                   validationState={this.state.errorFields.type ? "error" : null}>
                            <ControlLabel>
                                <Message msgId="pluievolution.localisation.title"/>
                            </ControlLabel>
                            <div className="radio-form">
                                <div className="radio-block">
                                    <Radio name="radioGroup"
                                           checked={this.state.pluiRequest.type === PluiRequestType.COMMUNE}
                                           onChange={() => this.setPluiRequestType(PluiRequestType.COMMUNE)}>
                                        <Message msgId="pluievolution.localisation.graphique"/>
                                    </Radio>
                                    <Button
                                        disabled={this.state.pluiRequest.type !== PluiRequestType.COMMUNE}
                                        bsSize="small"
                                        bsStyle="primary"
                                        onClick={this.drawTypeCommune}>
                                        {this.renderGeometryDrawMessage()}
                                    </Button>
                                </div>
                            </div>
                            <div className="radio-form">
                                <div className="radio-block">
                                    <Radio name="radioGroup"
                                           checked={this.state.pluiRequest.type === PluiRequestType.INTERCOMMUNE}
                                           onChange={() => this.setPluiRequestType(PluiRequestType.INTERCOMMUNE)}>
                                        <Message msgId="pluievolution.localisation.commune"/>
                                    </Radio>
                                    {!this.isAgentRmUser(this.props.user) &&
                                    <Button
                                        disabled={this.state.pluiRequest.type !== PluiRequestType.INTERCOMMUNE}
                                        bsSize="small"
                                        bsStyle="primary"
                                        onClick={() => this.drawEtablissement()}>
                                        <Message msgId="pluievolution.clickHere"/>
                                    </Button>
                                    }
                                </div>
                            </div>
                            <div className="radio-form">
                                <div className="radio-block">
                                    <Radio name="radioGroup"
                                           checked={this.state.pluiRequest.type === PluiRequestType.METROPOLITAIN}
                                           onChange={() => this.setPluiRequestType(PluiRequestType.METROPOLITAIN)}>
                                        <Message msgId="pluievolution.localisation.metropolitain"/>
                                    </Radio>
                                    {!this.isAgentRmUser(this.props.user) &&
                                    <Button
                                        disabled={this.state.pluiRequest.type !== PluiRequestType.METROPOLITAIN}
                                        bsSize="small"
                                        bsStyle="primary"
                                        onClick={() => this.drawEtablissement()}>
                                        <Message msgId="pluievolution.clickHere"/>
                                    </Button>
                                    }
                                </div>
                            </div>
                        </FormGroup>
                        {this.renderGeographicEtablissements()}
                    </fieldset>
                </div>
            );
        }
    }

    renderGeographicEtablissements() {
        if (this.isAgentRmUser(this.props.user)) {
            return (
                <FormGroup controlId="mairie"
                           validationState={this.state.errorFields.codeInsee ? "error" : null}>
                    <InputGroup className="input-group">
                        <InputGroup.Addon className="addon">
                            <Message msgId="pluievolution.localisation.cityHall.title"/>
                        </InputGroup.Addon>
                        <FormControl componentClass="select"
                                     disabled={this.state.pluiRequest.type !== PluiRequestType.INTERCOMMUNE && this.state.pluiRequest.type !== PluiRequestType.METROPOLITAIN}
                                     onChange={this.handleEtablissementChange}>
                            <option key="0" value="">Sélectionnez une mairie</option>
                            {
                                this.props.geographicEtablissements.map((geoEtbl, index) => {
                                    return <option key={geoEtbl.codeInsee} value={index}>{geoEtbl.nom}</option>
                                })
                            }
                        </FormControl>
                    </InputGroup>
                </FormGroup>
            );
        }
    }

    setPluiRequestType(pluiRequestType) {
        this.state.pluiRequest.type = pluiRequestType;
        this.setState(this.state);
        this.disableFormErrors();
        this.props.clearDrawn();
    }

    formatBytes(bytes, decimals = 2) {
        if (bytes === 0) return '0 Bytes';

        const k = 1024;
        const dm = decimals < 0 ? 0 : decimals;
        const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];

        const i = Math.floor(Math.log(bytes) / Math.log(k));

        return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
    }

    /**
     * Action sur le bouton permettant de définir la géométrie d'un plui-evolution (start ou stop du dessin)
     */
    drawTypeCommune = ()=> {
        const geometryType = GeometryType.POINT;
        if (this.props.drawing) {
            this.props.stopDrawing(geometryType);
        }
        else {
            this.props.startDrawing(geometryType, this.props.pluiRequest ? this.props.pluiRequest.localisation : null);
        }
    }

    /**
     * Affichage du message sur le dessin de la geometrie du plui-evolution
     */
    renderGeometryDrawMessage = ()=> {
        if (this.props.drawing) {
            return (
                <Message msgId="pluievolution.loading"/>
            );
        } else {
            return (
                <Message msgId="pluievolution.localisation.geolocate"/>
            );
        }
    }

    drawEtablissement(geographicEtablissement) {
        if (this.isAgentRmUser(this.props.user) && !geographicEtablissement) {
            this.state.errorFields.codeInsee = true;
            this.setState(this.state);
            this.props.loadActionError("pluievolution.codeInsee.error");
        }
        else {
            this.props.displayEtablissement(this.state.pluiRequest.type, geographicEtablissement);
        }
    }

    renderFormButton = () => {
        if (!this.props.readOnly) {
            return (
                <div>
                    <fieldset>
                        <div className="block-valid-form">
                            <Button bsStyle="warning"
                                    bsSize="large"
                                    onClick={this.cancel}>
                                <Message msgId="pluievolution.cancel"/>
                            </Button>
                            <Button className="validation-button"
                                    bsStyle="primary"
                                    bsSize="large"
                                    onClick={this.savePluiRequest}>
                                <Message msgId="pluievolution.validate"/>
                            </Button>
                        </div>
                    </fieldset>
                </div>
            );
        }
        else {
            return (
                <div>
                    <fieldset>
                        <div className="block-valid-form">
                            <Button bsStyle="default"
                                    bsSize="large"
                                    onClick={this.cancel}>
                                <Message msgId="pluievolution.close"/>
                            </Button>
                        </div>
                    </fieldset>
                </div>
            );
        }
    }

    /**
     * Changement du sujet
     *
     * @param {*} e l'événement
     */
    handleSubjectChange = (e) => {
        this.state.pluiRequest.subject = e.target.value;
        this.setState(this.state);
    }

    /**
     * Changement de l'objet
     *
     * @param {*} e l'événement
     */
    handleObjectChange = (e) => {
        this.state.pluiRequest.object = e.target.value;
        this.setState(this.state);
    }

    handleEtablissementChange = (e) => {
        console.log('handleEtablissementChange e', e);
        console.log('handleEtablissementChange e value', e.target.value);
        if (e.target.value !== 0) {
            this.state.etablissementSelected = this.props.geographicEtablissements[e.target.value];
            this.state.pluiRequest.codeInsee = this.state.etablissementSelected.codeInsee;
            // on dessine la localisation de l'etablissement sur la carte
            this.drawEtablissement(this.state.etablissementSelected);
        }
        else {
            this.state.etablissementSelected = {};
            this.state.pluiRequest.codeInsee = "";
            // on on la localisation de l'etablissement precedemment selectionné sur la carte
            this.props.clearDrawn();
        }

        this.setState(this.state);
    }

    /**
     * Validation de la pièce de jointe (type, taille...) avant l'uploader
     *
     * @param {*} attachment
     */
    validateAttachment(attachment) {
        let errorAttachment = "";
        if (!attachment.file || !(attachment.file instanceof File) || this.props.attachmentConfiguration.mimeTypes.includes(attachment.file.type) === false) {
            errorAttachment = 'pluievolution.attachment.typeFile';
        }

        if (attachment.file.size > this.props.attachmentConfiguration.maxSize) {
            errorAttachment = `la taille du fichier est supérieur à : ${this.props.attachmentConfiguration.maxSize}`
        }

        if (this.state.attachments.length + 1 > this.props.attachmentConfiguration.maxCount) {
            errorAttachment = `Vous ne pouvez pas ajouter plus de " : ${this.props.attachmentConfiguration.maxCount} fichiers`
        }

        if (errorAttachment) {
            this.setState({errorAttachment});
            return false;
        }
        return true;
    }

    /**
     * Action pour ajouter une pièce jointe
     *
     * @param {*} e l'événement
     */
    fileAddedHandler(e) {
        console.log('event file', e);
        //les différents test avant d'uploader le fichier (type, taille)
        const attachment = {
            name: e.target.files[0].name,
            file: e.target.files[0]
        };

        const isValid = this.validateAttachment(attachment);

        if (isValid) {
            if (!this.state.attachments) {
                this.state.attachments = [];
            }
            this.state.attachments.push(attachment)
            this.state.errorAttachment = "";

            this.setState(this.state);
        }

    }

    /**
     * Action pour supprimer une pièce jointe
     *
     * @param {number} index l'événement
     */
    fileDeleteHandler(index) {
        this.state.attachments.splice(index, 1);
        this.setState(this.state);
    }

    disableFormErrors() {
        this.state.errorFields = {};
        this.setState(this.state);
    }

    checkPluiRequestForm() {
        this.disableFormErrors();
        this.props.loadActionError(null);
        if (!this.state.pluiRequest.subject) {
            this.state.errorFields.subject = true;
            this.setState(this.state);
            this.props.loadActionError("pluievolution.subject.error");
            return false;
        }
        if (!this.state.pluiRequest.object) {
            this.state.errorFields.object = true;
            this.setState(this.state);
            this.props.loadActionError("pluievolution.object.error");
            return false;
        }
        if (!this.state.pluiRequest.type) {
            this.state.errorFields.type = true;
            this.setState(this.state);
            this.props.loadActionError("pluievolution.type.error");
            return false;
        }
        if ((this.state.pluiRequest.type === PluiRequestType.INTERCOMMUNE || this.state.pluiRequest.type === PluiRequestType.METROPOLITAIN)
            && this.isAgentRmUser(this.props.user)
            && !this.state.pluiRequest.codeInsee) {
            this.state.errorFields.codeInsee = true;
            this.setState(this.state);
            this.props.loadActionError("pluievolution.localisation.error.cityHall");
            return false;
        }
        if (!this.state.pluiRequest.localisation || this.state.pluiRequest.localisation.length === 0) {
            this.state.errorFields.localisation = true;
            this.setState(this.state);
            this.props.loadActionError("pluievolution.localisation.error.geolocate");
            return false;
        }

        return true;
    }

    /**
     * L'action d'abandon
     */
    cancel = () => {
        this.props.closeRequest();
    }

    /**
     * L'action de création
     */
    savePluiRequest = () => {
        if(this.checkPluiRequestForm()) {
            this.props.savePluiRequest(this.state.pluiRequest, this.state.attachments);
        }
    }
}
