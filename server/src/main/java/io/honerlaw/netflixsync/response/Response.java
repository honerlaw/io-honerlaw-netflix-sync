package io.honerlaw.netflixsync.response;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;

public abstract class Response {
	
	private final JsonObject jsonObject = new JsonObject();
	
	public Response(String uri) {
		put("uri", uri);
	}
	
	protected void put(String key, Object value) {
		jsonObject.put(key, value);
	}
	
	public Buffer asBuffer() {
		return Buffer.buffer(jsonObject.toString());
	}
	
}
