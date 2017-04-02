
import React, { Component } from "react";
import { createSession } from "../../Request";

export default class Create extends Component {

    constructor(props) {
        super(props);
        this.state = {
            name: ''
        };
    }

    submit(e) {
        e.preventDefault();
        createSession(this.state.name);
    }

    render() {
        return <div id="create" className="center">
            <div className="closeButton" onClick={ () => this.props.navigator.pop() }>X</div>
            <div style={ { clear : "both" } }></div>
            <h1>Create<br />Session</h1>
            <label>Session Name</label>
            <input type="text" value={ this.state.name } onChange={ (e) => this.setState({ name: e.target.value }) } />
            <div className="nav-button" onClick={ this.submit.bind(this) }>Create</div>
        </div>;
    }

}
