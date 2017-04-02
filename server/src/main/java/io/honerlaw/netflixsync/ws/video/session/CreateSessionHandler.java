package io.honerlaw.netflixsync.ws.video.session;

import java.util.UUID;

import io.honerlaw.netflixsync.Server;
import io.honerlaw.netflixsync.VideoSession;
import io.honerlaw.netflixsync.response.util.GenericError;
import io.honerlaw.netflixsync.response.video.session.VideoSessionCreated;
import io.honerlaw.netflixsync.ws.WebSocketHandler;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonObject;

public class CreateSessionHandler implements WebSocketHandler {

	@Override
	public void handle(Server server, ServerWebSocket socket, JsonObject json) {
		if(json.containsKey("name") && json.getString("name").trim().length() > 0) {
			for(VideoSession session : server.getVideoSessions()) {
				if(session.getCreator() == socket || session.getUsers().contains(socket)) {
					socket.write(new GenericError("You are already in a session.").asBuffer());
					return;
				}
			}
			VideoSession session = new VideoSession(socket, UUID.randomUUID().toString(), json.getString("name"));
			server.getVideoSessions().add(session);
			socket.write(new VideoSessionCreated(session).asBuffer());
		} else {
			socket.write(new GenericError("Session name is required.").asBuffer());
		}
	}

}
