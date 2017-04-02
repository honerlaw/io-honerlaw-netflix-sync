
import React, { Component } from "react";

import Store from "../../Store";
import { sendChatMessage } from "../../Request";

export default class Chat extends Component
{

    constructor(props) {
        super(props);
        this.state = {
            message: '',
            messages: []
        };
    }

    componentDidMount() {
        this.unsubscribe = Store.subscribe(() => {
            this.setState({
                messages: Store.getState().messages
            });
        });
    }

    componentWillUnmount() {
        this.unsubscribe();
    }

    send(e) {
        let key = e.keyCode || e.which;
        if(key === undefined || key === 13) {
            sendChatMessage(this.state.message);
            this.setState({
                message: ''
            });
        }
    }

    render() {
        return <div id="chat">
            <div id="chat-messages">
                { this.state.messages.map((message, i) => {
                    return <div key={ i } className="chat-message">{ message }</div>;
                }) }
            </div>
            <input type="text" value={ this.state.message } onChange={ (e) => this.setState({ message : e.target.value }) } onKeyPress={ this.send.bind(this) } />
            <div className="nav-button full" onClick={ this.send.bind(this) }>send</div>
            <div style={ { clear : "both" } }></div>
        </div>;
    }

}
