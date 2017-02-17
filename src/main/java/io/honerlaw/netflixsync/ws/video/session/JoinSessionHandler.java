package io.honerlaw.netflixsync.ws.video.session;

import io.honerlaw.netflixsync.Server;
import io.honerlaw.netflixsync.VideoSession;
import io.honerlaw.netflixsync.response.util.GenericError;
import io.honerlaw.netflixsync.response.video.session.VideoSessionJoined;
import io.honerlaw.netflixsync.ws.WebSocketHandler;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonObject;

public class JoinSessionHandler implements WebSocketHandler {

	@Override
	public void handle(Server server, ServerWebSocket socket, JsonObject json) {
		if(json.containsKey("id") && json.getString("id").trim().length() > 0) {
			
			for(VideoSession session : server.getVideoSessions()) {
				if(session.getCreator() == socket
						|| session.getUsers().contains(socket)) {
					socket.write(new GenericError("You are already in another video session.").asBuffer());
					return;
				}
			}
			
			VideoSession session = null;
			for(VideoSession videoSession : server.getVideoSessions()) {
				if(videoSession.getID() == json.getString("id")) {
					session = videoSession;
					break;
				}
			}
		
			if(session == null) {
				socket.write(new GenericError("Invalid session ID.").asBuffer());
				return;
			}
			
			session.getUsers().add(socket);
			
			VideoSessionJoined resp = new VideoSessionJoined(session);
			session.notifyCreator(resp).notifyUsers(resp);
		} else {
			socket.write(new GenericError("Session ID is required.").asBuffer());
		}
	}

}
