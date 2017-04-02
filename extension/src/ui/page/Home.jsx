
import React, { Component } from "react";

import { sessionList } from "../../Request";
import SessionList from "../component/SessionList";

export default class Home extends Component {

    constructor(props) {
        super(props);
        this.image = chrome.extension.getURL("icons/netflix-sync-128x128.png");
    }

    componentDidMount() {
        sessionList();
    }

    render() {
        return <div id="home" className="center">
            <img src={ this.image } />
            <h1>Netflix Sync</h1>
            <SessionList />
            <div className="nav-button" onClick={ () => this.props.navigator.push("join") }>Join</div>
            <div className="nav-button right" onClick={ () => this.props.navigator.push("create") }>Create</div>
            <div style={ { clear : "both" } }></div>
        </div>;
    }

}
