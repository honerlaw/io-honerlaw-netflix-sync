package io.honerlaw.netflixsync.ws.video;

import io.honerlaw.netflixsync.Server;
import io.honerlaw.netflixsync.response.video.VideoToggle;
import io.honerlaw.netflixsync.ws.WebSocketHandler;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonObject;

public class ToggleVideoHandler implements WebSocketHandler {

	@Override
	public void handle(Server server, ServerWebSocket socket, JsonObject json) {
		if(!json.containsKey("playing")) {
			return;
		}
		server.getVideoSessions().forEach(session -> {
			if(session.getCreator() == socket) {
				session.notifyUsers(new VideoToggle(json.getBoolean("playing")));
			}
		});
	}

}
