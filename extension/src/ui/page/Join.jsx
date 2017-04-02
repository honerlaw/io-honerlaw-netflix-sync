
import React, { Component } from "react";
import { joinSession } from "../../Request";

export default class Join extends Component {

    constructor(props) {
        super(props);
        this.state = {
            id: ''
        };
    }

    submit(e) {
        e.preventDefault();
        joinSession(this.state.id);
    }

    render() {
        return <div id="join" className="center">
            <div className="closeButton" onClick={ () => this.props.navigator.pop() }>X</div>
            <div style={ { clear : "both" } }></div>
            <h1>Join<br />Session</h1>
            <label>Session Identifier</label>
            <input type="text" value={ this.state.id } onChange={ (e) => this.setState({ id: e.target.value }) } />
            <div className="nav-button" onClick={ this.submit.bind(this) }>Join</div>
        </div>;
    }

}
