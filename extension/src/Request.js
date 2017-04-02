
import Store from "./Store";

let socket = null;

function connect(openCallback) {
    socket = new WebSocket("wss://netflixsync.honerlaw.io/ws");
    socket.onopen = function() {
        openCallback();
    };
    socket.onmessage = function(e) {
        let reader = new FileReader();
        reader.onload = function() {
            let resp = JSON.parse(reader.result);
            resp.type = resp.uri;
            delete resp.uri;
            Store.dispatch(resp);
        };
        reader.readAsText(e.data);
    };
    socket.onclose = function() {
        socket = null;
    };
}

function send(data) {
    if(socket === null) {
        connect(function() {
            socket.send(JSON.stringify(data));
        });
    } else {
        socket.send(JSON.stringify(data));
    }
}

export function createSession(name) {
    send({
        uri: '/session/create',
        name: name
    });
}

export function sessionList() {
    send({
        uri: '/session/list'
    });
}

export function leaveSession() {
    send({
        uri: '/session/leave'
    });
}

export function joinSession(id) {
    send({
        uri: '/session/join',
        id: id
    });
}

export function sendChatMessage(message) {
    send({
        uri: '/chat/message',
        message: message
    });
}

export function toggleVideo(playing) {
    send({
        uri: '/video/toggle',
        playing: playing
    });
}

export function seekVideo(seconds) {
    send({
        uri: '/video/seek',
        seconds: seconds
    });
}

export function tickVideo(url, playing, currentTime) {
    send({
        uri: '/video/tick',
        url: url,
        playing: playing,
        currentTime: currentTime
    });
}
