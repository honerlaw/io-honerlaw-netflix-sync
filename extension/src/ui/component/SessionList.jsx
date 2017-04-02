
import React, { Component } from "react";

import Store from "../../Store";
import { joinSession } from "../../Request";

export default class SessionList extends Component {

    constructor(props) {
        super(props);
        this.state = {
            sessions: []
        };
    }

    componentDidMount() {
        this.unsubscribe = Store.subscribe(() => {
            this.setState({
                sessions: Store.getState().sessions
            });
        });
    }

    componentWillUnmount() {
        this.unsubscribe();
    }

    render() {
        if(this.state.sessions.length === 0) {
            return null;
        }
        // list of sessions
        return <div id="session-list">
            <label>sessions</label>
            { this.state.sessions.map((session, i) => {
                return <div key={ i } className="session" onClick={ () => joinSession(session.id) }>{ session.name }</div>;
            }) }
        </div>;
    }

}
