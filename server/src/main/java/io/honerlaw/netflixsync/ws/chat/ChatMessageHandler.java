package io.honerlaw.netflixsync.ws.chat;

import io.honerlaw.netflixsync.Server;
import io.honerlaw.netflixsync.response.chat.ChatMessage;
import io.honerlaw.netflixsync.ws.WebSocketHandler;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonObject;

public class ChatMessageHandler implements WebSocketHandler {

	@Override
	public void handle(Server server, ServerWebSocket socket, JsonObject json) {
		
		// discard empty or invalid requests
		if(!json.containsKey("message") || json.getString("message").trim().length() == 0) {
			return;
		}
		
		// find the session the user is in
		server.getVideoSessions().forEach(session -> {
			if(session.getCreator() == socket || session.getUsers().contains(socket)) {
				
				// notify all users in the session of the message
				ChatMessage resp = new ChatMessage(json.getString("message"));
				session.notifyCreator(resp).notifyUsers(resp);
			}
		});
		
	}

}
