import React from 'react';
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
    Row
} from 'react-bootstrap';
import Message from '@mapstore/components/I18N/Message';
import ConfirmDialog from '@mapstore/components/misc/ConfirmDialog';
import {status} from '../actions/plui-evolution-action';
import {GeometryType} from '../constants/plui-evolution-constants';
//import './plui-evolution.css';
import {CSS} from './plui-evolution-css';

export class PluiEvolutionPanelComponent extends React.Component {
    static propTypes = {
        id: PropTypes.string,
        active: PropTypes.bool,
        status: PropTypes.string,
        closing: PropTypes.bool,
        drawing: PropTypes.bool,
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
        contextLayers: PropTypes.array,
        contextThemas: PropTypes.array,
        user: PropTypes.object,
        currentLayer: PropTypes.object,
        task: PropTypes.object,
        attachements: PropTypes.array,
        error: PropTypes.object,
        // redux
		initPluiEvolution: PropTypes.func,
        initDrawingSupport: PropTypes.func,
        stopDrawingSupport: PropTypes.func,
        startDrawing: PropTypes.func,
        stopDrawing: PropTypes.func,
        clearDrawn: PropTypes.func,
        loadAttachmentConfiguration: PropTypes.func,
        addAttachment: PropTypes.func,
        removeAttachment: PropTypes.func,
        getMe: PropTypes.func,
        requestClosing: PropTypes.func,
        cancelClosing: PropTypes.func,
        confirmClosing: PropTypes.func
    };

    static defaultProps = {
        id: "plui-evolution-panel",
        active: false,
        status: status.NO_TASK,
        closing: false,
        drawing: false,
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
            zIndex: 1030
        },
        dockStyle: {
            zIndex: 100,
        },
        // data
        attachmentConfiguration: null,
        user: null,
        attachements: [],
        // misc
		initPluiEvolution: ()=>{},
        initDrawingSupport: ()=>{},
        stopDrawingSupport: ()=>{},
        startDrawing: ()=>{},
        stopDrawing: ()=>{},
        clearDrawn: ()=>{},
        loadAttachmentConfiguration: ()=>{},
        addAttachment: () => {},
        removeAttachment: () => {},
        getMe: ()=>{},
        requestClosing: ()=>{},
        cancelClosing: ()=>{},
        confirmClosing: ()=>{},
        toggleControl: () => {}
    };

    constructor(props) {
        super(props);
        this.state = {
            errorAttachment: "",
            errorFields: {}
        }
		this.props.initPluiEvolution(this.props.backendurl);
        //console.log(this.state);
        //console.log(this.props);
    }

    componentWillMount() {
        this.setState({initialized: false, cssInitialized: false, loaded: false, pluirequest: null});
        this.props.loadAttachmentConfiguration();
        this.props.initDrawingSupport();
        this.props.getMe();
    }

    componentDidUpdate(prevProps, prevState) {
        console.log("pluie didUpdate...");
        // Tout est-il initialisé ?
        this.state.initialized = this.props.attachmentConfiguration !== null && this.props.user !== null;

		if( this.state.cssInitialized == false ){
            var script = document.createElement('style');
            script.innerHTML = CSS.join("\n");
            var head = document.getElementsByTagName('head')[0];
            head.appendChild(script);
            this.state.cssInitialized = true;
            console.log("pluie css loaded");
        }

       
        console.log(this.state);
    }

    render() {
        console.log("pluie render");
        if( this.props.active ){
            // si le panel est ouvert
            if( this.state.initialized ){
            }
        }
        if( this.props.active ){
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
                                        {this.renderHeader()}
                                        {
                                            !this.state.initialized || !this.state.loaded ?
                                                this.renderLoading() :
                                                this.renderForm()
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
     * Rendition du message de chargement en attendant la nouvelle tâche draft
     */
    renderLoading() {
        return (
            <div><Message msgId="pluievolution.loading"/></div>
        );
    }

    /**
     * La rendition du formulaire
     */
    renderForm() {
        return (
            <Form model={this.state.task}>
                {this.renderUserInformation()}
                {this.renderDetail()}
                {this.renderAttachments()}
                {this.renderLocalisation()}
            </Form>
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
                        <Button className="square-button no-border" onClick={() => this.create()} >
                            <Glyphicon glyph={this.props.createGlyph}/>
                        </Button>
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
                <span className="error"><Message msgId={this.props.error.message}/></span>
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
                    <legend><Message msgId="pluievolution.user"/></legend>
                    <FormGroup controlId="pluievolution.user.login">
                        <ControlLabel><Message msgId="pluievolution.login"/></ControlLabel>
                        <FormControl type="text" readOnly value={this.props.user !== null ? this.props.user.login : ''}/>
                    </FormGroup>
                    <FormGroup controlId="pluievolution.user.organization">
                        <ControlLabel><Message msgId="pluievolution.organization"/></ControlLabel>
                        <FormControl type="text" readOnly value={this.props.user !== null ? this.props.user.organization : ''}/>
                    </FormGroup>
                    <FormGroup controlId="pluievolution.user.email">
                        <ControlLabel><Message msgId="pluievolution.email"/></ControlLabel>
                        <FormControl type="text" readOnly value={this.props.user !== null ? this.props.user.email : ''}/>
                    </FormGroup>
                </fieldset>
            </div>
        );
    }

    /**
     * La rendition du détail du plui-evolution
     */
    renderDetail() {
        return (
            <div>
                <fieldset>
                    <legend><Message msgId="pluievolution.description"/></legend>
                    <FormGroup controlId="pluievolution.description">
                        <FormControl componentClass="textarea"
                                     defaultValue={this.state.pluirequest.description}
                                     onChange={this.handleDescriptionChange}
                                     maxLength={1000}
                        />
                        <HelpBlock><Message msgId="pluievolution.description.count"/> {1000 - this.state.task.asset.description.length}</HelpBlock>
                    </FormGroup>
                </fieldset>
            </div>
        );

    }

    /**
     * La rendition de la gestion des picèces jointes
     */
    renderAttachments() {
        return (
            <div>
                <fieldset>
                    <legend><Message msgId="pluievolution.attachment.files"/></legend>
                    <FormGroup controlId="formControlsFile">
                        <FormControl type="file" name="file"
                                     onChange={(e) => this.fileAddedHandler(e)} />
                        <HelpBlock><Message msgId="pluievolution.fileUpload.info"/></HelpBlock>
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
                    <table className="table">
                        <thead>
                        <tr>
                            <th scope="col">Name</th>
                            <th scope="col">Action</th>
                        </tr>
                        </thead>
                        <tbody>
                        {this.renderTable(this.props.attachments)}
                        </tbody>
                    </table>
                </fieldset>
            </div>
        )
    }

    /**
     * La rendition du panel des pièces jointes
     */
    renderTable(attachments) {

        if (attachments) {
            return attachments.map((attachment, index) => {
                return (
                    <tr key={index}>
                        <td>{attachment.name}</td>
                        <td><Button className="btn btn-sq-xs btn-danger"
                                    onClick={() => this.fileDeleteHandler(attachment.id, index)}>
                            <Glyphicon glyph={this.props.deleteGlyph}/>
                        </Button></td>
                    </tr>
                )
            })
        }
    }

    /**
     * La rendition de la saisie de la géométrie
     */
    renderLocalisation() {
        return (
            <div>
                <fieldset>
                    <legend><Message msgId="pluievolution.localization"/></legend>
                    <FormGroup controlId="localisation">
                        { this.renderGeometryDrawButton() }
                        <label className="col-sm-offset-1">{ this.renderGeometryDrawMessage() }</label>
                        <HelpBlock>
                            <Message msgId="pluievolution.localization.tips"/>
                        </HelpBlock>
                    </FormGroup>
                </fieldset>
            </div>
        )
    }

    /**
     * Affichage du bouton permettant de définir la géométrie d'un plui-evolution
     */
    renderGeometryDrawButton = ()=> {
        return (
            <Button className="square-button" bsStyle={this.props.drawing ? 'primary' : 'default'} onClick={this.onDraw}>
                <Glyphicon glyph={this.state.task.asset.geographicType.toLowerCase()}/>
            </Button>
        );
    }

    /**
     * Action sur le bouton permettant de définir la géométrie d'un plui-evolution (start ou stop du dessin)
     */
    onDraw = ()=> {
        const geometryType = GeometryType[this.state.task.asset.geographicType];
        if (this.props.drawing) {
            this.props.stopDrawing(geometryType);
        }
        else {
            this.props.startDrawing(geometryType, this.props.task.asset.localisation);
        }
    }

    /**
     * Affichage du message sur le dessin de la geometrie du plui-evolution
     */
    renderGeometryDrawMessage = ()=> {
        if (this.state.task && this.props.task.asset && this.props.task.asset.localisation && this.props.task.asset.localisation.length > 0) {
            return (
                <Message msgId="pluievolution.localization.drawn"/>
            );
        } else {
            return null;
        }
    }

    /**
     * Changement de la description
     *
     * @param {*} e l'événement
     */
    handleDescriptionChange = (e) => {
        this.state.pluirequest.description = e.target.value;
        this.setState(this.state);
    }

    /**
     * Validation de la pièce de jointe (type, taille...) avant l'uploader
     *
     * @param {*} attachment
     */
    validateAttachment(attachment) {
        let errorAttachment = "";
        if (attachment.file === undefined || !(attachment.file instanceof File) || this.props.attachmentConfiguration.mimeTypes.includes(attachment.file.type) === false) {
            errorAttachment = 'pluievolution.attachment.typeFile';
        }

        if (attachment.file.size > this.props.attachmentConfiguration.maxSize) {
            errorAttachment = `la taille du fichier est supérieur à : ${this.props.attachmentConfiguration.maxSize}`
        }

        if (this.props.attachments.length + 1 > this.props.attachmentConfiguration.maxCount) {
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
        //les differents test avant d'uploader le fichier (type, taille)
        var attachment = {file: e.target.files[0], uuid: this.state.task.asset.uuid}

        const isValid = this.validateAttachment(attachment);

        if (isValid) {
            this.setState({errorAttachment: ""});
            // uploader le fichier
            this.props.addAttachment(attachment);
        }

    }


    /**
     * Action pour supprimer une pièce jointe
     *
     * @param {*} e l'événement
     */
    fileDeleteHandler(id, index) {
        const attachment = {id: id, uuid: this.state.task.asset.uuid, index: index};
        this.props.removeAttachment(attachment);
    }

    /**
     * L'action d'abandon
     */
    cancel() {
        if(  this.state.pluirequest != null && this.state.pluirequest.uuid !== null) {
            this.props.requestClosing();
        } else {
            this.props.toggleControl();
        }
    }

    /**
     * L'action de création
     */
    create() {
        if( this.state.pluirequest != null &&  this.state.pluirequest.uuid !== null) {
        }
    }
};
