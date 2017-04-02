package io.honerlaw.netflixsync.ws.video.session;

import java.util.Iterator;

import io.honerlaw.netflixsync.Server;
import io.honerlaw.netflixsync.VideoSession;
import io.honerlaw.netflixsync.response.video.session.VideoSessionLeft;
import io.honerlaw.netflixsync.ws.WebSocketHandler;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonObject;

public class LeaveSessionHandler implements WebSocketHandler {

	@Override
	public void handle(Server server, ServerWebSocket socket, JsonObject json) {
		for(Iterator<VideoSession> it = server.getVideoSessions().iterator(); it.hasNext(); ) {
			VideoSession session = it.next();
			if(session.getCreator() == socket) {
				session.shutdown(true);
				it.remove();
			} else {
				session.getUsers().remove(socket);
			}
		}
		socket.write(new VideoSessionLeft().asBuffer());	
	}

}
