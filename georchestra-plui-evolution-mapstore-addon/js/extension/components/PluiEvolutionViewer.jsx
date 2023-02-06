import React from "react";
import {PropTypes} from 'prop-types';
import {
    Col,
    Form,
    Glyphicon,
    Grid,
    Row
} from 'react-bootstrap';
import {PluiEvolutionRequestViewer} from "@js/extension/components/PluiEvolutionRequestViewer";
import {reproject} from "mapstore2/web/client/utils/CoordinatesUtils";
import {DEFAULT_PROJECTION, SYSTEM_PROJECTION} from "@js/extension/constants/plui-evolution-constants";

/**
 * Composant englobant le viewer PluiEvolutionRequestViewer
 * Contient un header et des boutons de navigation pour controler le viewer
 */
export class PluiEvolutionViewer extends React.Component {
    static propTypes = {
        openPanel: PropTypes.func,
        closeViewer: PropTypes.func,
        viewerMode: PropTypes.bool,
        response: PropTypes.object
    };

    static defaultProps = {
        openPanel: () => {},
        closeViewer: () => {},
        viewerMode: false,
        response: {features: []}
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

    render() {
        if (this.props.viewerMode) {
            return (
            <div className="plui-evolution-viewer">
                <Form>
                    {this.renderCoordinates()}
                    {this.renderPluiRequestsNavigation()}
                    <PluiEvolutionRequestViewer closeViewer={this.props.closeViewer}
                    openPanel={this.props.openPanel}
                    response={this.props.response}
                    index={this.state.index}
                    viewerMode={this.props.viewerMode}/>
                </Form>
            </div>
            )
        }
        return null;
    }

    /**
     * La rendition de l'entête
     */
    renderHeader() {
        return (
            <Grid fluid className="ms-header ms-primary" style={{ width: '100%', boxShadow: 'none'}}>
                <Row>
                    <Col xs={2}>
                        <button className="square-button bg-primary no-border no-events btn btn-primary">
                            <Glyphicon glyph="map-marker"/>
                        </button>
                    </Col>
                    <Col xs={8}>
                    </Col>
                    <Col xs={2}>
                        <button className="square-button no-border bg-primary btn btn-primary" onClick={() => this.close()} >
                            <Glyphicon glyph="1-close"/>
                        </button>
                    </Col>
                </Row>
            </Grid>
        );
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
                <h4>
                    {this.state.index + 1} / {this.props.response?.features?.length}
                </h4>
                <button
                    className="square-button-md btn btn-primary"
                    disabled={this.state.index === this.props.response?.features?.length - 1}
                    onClick={this.handleClickButtonDisplayTaskAfter}>
                    <Glyphicon glyph="glyphicon glyphicon-chevron-right" />
                </button>
            </div>
        )
    }

    /**
     * Permet la rendition de la barre d'affiche de la coordonnée cliquée
     * @returns {JSX.Element}
     */
    renderCoordinates() {
        const feature = this.props.response.features[0];
        const coordinates = (feature) ? feature.geometry.coordinates : [0, 0];
        const normalizedCoordinates = reproject(coordinates, SYSTEM_PROJECTION, DEFAULT_PROJECTION);
        const longitude = Number(normalizedCoordinates.x);
        const latitude = Number(normalizedCoordinates.y);
        return (
            <div className="coordinates-text">
                <span>
                    <Glyphicon glyph="map-marker"/>
                </span>
                <span> Lat: {latitude.toLocaleString(undefined, {maximumFractionDigits:3})}</span>
                <span>  -  Long: {longitude.toLocaleString(undefined, {maximumFractionDigits:3})}</span>
            </div>
        )
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


    /**
     * Permet de fermer le viewer
     */
    close = () => {
        this.props.closeViewer();
    }
}
