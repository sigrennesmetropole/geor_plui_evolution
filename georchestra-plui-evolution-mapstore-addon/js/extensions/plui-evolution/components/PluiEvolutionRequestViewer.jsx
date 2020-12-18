import React from 'react';
import {PropTypes} from 'prop-types';
import {Button, Col, ControlLabel, Form, FormControl, FormGroup, Glyphicon} from "react-bootstrap";
import Message from '@mapstore/components/I18N/Message';
import {CSS} from './plui-evolution-css';

export const PLUI_EVOLUTION_REQUEST_VIEWER = "PluiEvolutionRequestViewer";

export class PluiEvolutionRequestViewer extends React.Component {


    static propTypes = {
        openPanel: PropTypes.func,
        closeIdentify: PropTypes.func
    };

    static defaultProps = {
        openPanel: () => {},
        closeIdentify: () => {},
    }

    constructor(props) {
        super(props);
        this.state= {
            index: 0
        }
    }

    componentWillMount() {
        this.setState({initialized: false});
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if( this.state.cssInitialized === false ) {
            var script = document.createElement('style');
            script.innerHTML = CSS.join("\n");
            var head = document.getElementsByTagName('head')[0];
            head.appendChild(script);
            this.state.cssInitialized = true;
            console.log("pluie css loaded");
        }
    }

    render() {
        if (this.props.response.features.length > 0) {
            return (
                <div className="plui-evolution-request-viewer">
                    <Form>
                        {this.renderPluiRequestsNavigation()}
                        {this.renderPluiRequestInfo()}
                        {this.renderPluiRequestButtonValidation()}
                    </Form>
                </div>
            )
        } else {
            return null;
        }
    }

    /**
     * Affichage des boutons de navigations (précédent et suivant)
     * permettant de naviguer entre les demandes plui
     * @return {Element}
     */
    renderPluiRequestsNavigation() {
        return(
            <div className="button-navigation">
                <button
                    className="square-button-md btn btn-primary"
                    disabled={this.state.index === 0}
                    onClick={this.handleClickButtonDisplayTaskBefore}>
                    <Glyphicon glyph="glyphicon glyphicon-chevron-left" />
                </button>
                <button
                    className="square-button-md btn btn-primary"
                    disabled={this.state.index === this.props.response.features.length - 1}
                    onClick={this.handleClickButtonDisplayTaskAfter}>
                    <Glyphicon glyph="glyphicon glyphicon-chevron-right" />
                </button>
            </div>
        )
    }

    /**
     * La rendition de la partie info de la demande plui
     */
    renderPluiRequestInfo() {
        const pluiRequest = this.props.response.features[this.state.index].properties;
        return (
            <div>
                <fieldset>
                    <FormGroup controlId="pluievolution.subject">
                        <ControlLabel className="col-sm-4">
                            <Message msgId="pluievolution.subject.title"/>
                        </ControlLabel>
                        <Col sm={8} >
                            <FormControl type="text"
                                         value={pluiRequest.subject}
                                         readOnly/>
                        </Col>
                    </FormGroup>
                </fieldset>
                <fieldset>
                    <FormGroup controlId="pluievolution.object">
                        <ControlLabel className="col-sm-4">
                            <Message msgId="pluievolution.object.title"/>
                        </ControlLabel>
                        <Col sm={8} >
                            <FormControl componentClass="textarea"
                                         type="text"
                                         value={pluiRequest.object}
                                         bsSize="small"
                                         rows={4}
                                         readOnly/>
                        </Col>
                    </FormGroup>
                </fieldset>
                <fieldset>
                    <FormGroup controlId="pluievolution.type">
                        <ControlLabel className="col-sm-4">
                            <Message msgId="pluievolution.type.title"/>
                        </ControlLabel>
                        <Col sm={8} >
                            <FormControl type="text"
                                         value={pluiRequest.type}
                                         readOnly/>
                        </Col>
                    </FormGroup>
                </fieldset>
            </div>
        )
    }

    /**
     * La rendition des buttons d'actions
     */
    renderPluiRequestButtonValidation() {
        return (
            <div className="block-valid-form">
                <Button bsStyle="default" bsSize="large"
                        onClick={this.handleClickButtonCancelPluiRequest}>
                    <Message msgId="pluievolution.close"/>
                </Button>
                <Button className="validation-button" bsStyle="info" bsSize="large"
                        onClick={this.handleClickButtonOpenPluiRequest}>
                    <Message msgId="pluievolution.validate"/>
                </Button>
            </div>
        );
    }

    /**
     * L'action pour faire la mise à jour d'une tâche
     */
    handleClickButtonOpenPluiRequest = () => {
        this.props.openPanel(this.props.response.features[this.state.index].properties);
        this.handleClickButtonCancelPluiRequest();
    }

    /**
     * L'action pour fermer la view
     */
    handleClickButtonCancelPluiRequest = () => {
        this.props.closeIdentify();
    }

    /**
     * Action pour afficher la demande plui suivante
     */
    handleClickButtonDisplayTaskAfter = () => {
        this.setState({index : ++this.state.index});
    }

    /**
     * Action pour afficher la demande plui précédente
     */
    handleClickButtonDisplayTaskBefore = () => {
        this.setState({index : --this.state.index});
    }
}
