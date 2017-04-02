
import React, { Component } from "react";

import { leaveSession } from "../../Request";
import Chat from "../component/Chat";

export default class Create extends Component {

    constructor(props) {
        super(props);
    }

    close(e) {
        this.props.navigator.pop();
        leaveSession();
    }

    render() {
        return <div id="session">
            <div id="close" onClick={ this.close.bind(this) }>X</div>
            <div id="header">
                <div id="name">{ this.props.session.name }</div>
                <span className="larger">{ this.props.session.users } users</span>
                <span>{ this.props.session.id }</span>
            </div>
            <Chat />
        </div>;
    }

}
