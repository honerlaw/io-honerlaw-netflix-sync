package io.honerlaw.netflixsync.ws.video.session;

import io.honerlaw.netflixsync.Server;
import io.honerlaw.netflixsync.response.video.session.VideoSessionList;
import io.honerlaw.netflixsync.ws.WebSocketHandler;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonObject;

public class ListSessionHandler implements WebSocketHandler {

	@Override
	public void handle(Server server, ServerWebSocket socket, JsonObject json) {
		socket.write(new VideoSessionList(server.getVideoSessions()).asBuffer());
	}

}
