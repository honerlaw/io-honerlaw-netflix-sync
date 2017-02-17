package io.honerlaw.netflixsync.response.video.session;

import java.util.UUID;

import org.mockito.Mockito;

import io.honerlaw.netflixsync.VideoSession;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonObject;
import junit.framework.TestCase;

public class VideoSessionJoinedTest extends TestCase {
	
	public void testPayload() {
		String id = UUID.randomUUID().toString();
		ServerWebSocket socket = Mockito.mock(ServerWebSocket.class);
		VideoSession session = new VideoSession(socket, id, "foo bar");
		VideoSessionJoined joined = new VideoSessionJoined(session);
		JsonObject obj = new JsonObject()
				.put("uri", "/session/joined")
				.put("session", new JsonObject()
						.put("id", id)
						.put("name", "foo bar")
						.put("users", 1));
		assertEquals(joined.asBuffer().toString(), obj.toString());
	}

}
