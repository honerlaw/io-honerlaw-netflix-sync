package io.honerlaw.netflixsync.ws.video;

import io.honerlaw.netflixsync.Server;
import io.honerlaw.netflixsync.response.video.VideoTick;
import io.honerlaw.netflixsync.ws.WebSocketHandler;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonObject;

public class TickVideoHandler implements WebSocketHandler {

	@Override
	public void handle(Server server, ServerWebSocket socket, JsonObject json) {
		if(!json.containsKey("url") || !json.containsKey("playing") || !json.containsKey("seconds")) {
			return;
		}
		server.getVideoSessions().forEach(session -> {
			if(session.getCreator() == socket) {
				session.notifyUsers(new VideoTick(json.getString("url"), json.getBoolean("playing"), json.getInteger("seconds")));
			}
		});
	}

}
