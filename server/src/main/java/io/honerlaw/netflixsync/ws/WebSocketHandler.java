package io.honerlaw.netflixsync.ws;

import io.honerlaw.netflixsync.Server;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonObject;

public interface WebSocketHandler {
	
	public void handle(Server server, ServerWebSocket socket, JsonObject json);

}
