
import { createStore } from "redux";

// the initial state for the application
let initialState = {
    session: null,
    sessions: [],
    error: null,
    messages: [],
    playing: null,
    seconds: null,
    url: null
};

let Store = createStore(function(state = initialState, action) {
    switch(action.type) {
        case '/session/created':
        case '/session/joined':
            return Object.assign({}, state, {
                session: action.session
            });
        case '/session/list':
            return Object.assign({}, state, {
                sessions: action.sessions
            });
        case '/session/left':
        case '/session/shutdown':
            return Object.assign({}, state, {
                session: null,
                messages: []
            });
        case '/error':
            return Object.assign({}, state, {
                error: action.error
            });
        case '/chat/message':
            return Object.assign({}, state, {
                messages: state.messages.concat(action.message)
            });
        case '/video/toggle':
            return Object.assign({}, state, {
                playing: action.playing
            });
            return;
        case '/video/seek':
            return Object.assign({}, state, {
                seconds: action.seconds
            });
        case '/video/tick':
            return Object.assign({}, state, {
                url: action.url,
                seconds: action.seconds,
                playing: action.playing
            });
        default:
            return state;
    }
});
export default Store;
