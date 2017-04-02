
import React, { Component } from "react";

import Store from "../Store";
import Home from "./page/Home";
import Join from "./page/Join";
import Create from "./page/Create";
import Session from "./page/Session";

/**
 * Show home page
 * contains a list of public sessions you can join,
 * two buttons (join (you can enter the name of the session), create)
 *
 * So really we have like 3 or 4 pages
 */
export default class Navigator extends Component {

    constructor(props) {
        super(props);
        this.state = {
            pages: [<Home navigator={ this } />],
            session: null,
            error: null
        };
    }

    componentDidMount() {
        this.unsubscribe = Store.subscribe(() => {
            if(Store.getState().session !== null && this.state.session === null) {
                this.setState({ session: Store.getState().session });
                this.push("session");
            } else {
                this.setState({ session: Store.getState().session });
            }
            if(Store.getState().error !== null) {
                this.setState({
                    error: Store.getState().error
                });
                setTimeout(() => {
                    Store.dispatch({
                        type: '/error',
                        error: null
                    });
                    this.setState({
                        error: null
                    });
                }, 4000);
            }
        });
    }

    componentWillUnmount() {
        this.unsubscribe();
    }

    // open a page
    push(name) {
        let page = null;
        switch(name.toLowerCase()) {
            case "home":
                page = <Home navigator={ this } />;
                break;
            case "join":
                page = <Join navigator={ this } />;
                break;
            case "create":
                page = <Create navigator={ this } />;
                break;
            case "session":
                page = <Session navigator={ this } session={ this.state.session } />;
                break;
        }
        this.setState({ pages: this.state.pages.concat(page) });
    }

    // close a page
    pop() {
        if(this.state.pages.length === 1) {
            return;
        }
        this.state.pages.pop();
        this.setState({
            pages: this.state.pages
        });
    }

    // render the correct page
    render() {
        let page = this.state.pages[this.state.pages.length - 1];
        let error = null;
        if(this.state.error !== null) {
            error = <div id="error-message">{ this.state.error }</div>;
        }
        return <div id="navigator">
            { error }
            { page }
        </div>;
    }

}
