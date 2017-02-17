package io.honerlaw.netflixsync.response.video.session;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.mockito.Mockito;

import io.honerlaw.netflixsync.VideoSession;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import junit.framework.TestCase;

public class VideoSessionListTest extends TestCase {
	
	public void testPayload() {
		String firstId = UUID.randomUUID().toString();
		ServerWebSocket firstSocket = Mockito.mock(ServerWebSocket.class);
		VideoSession firstSession = new VideoSession(firstSocket, firstId, "first session");
		String secondId = UUID.randomUUID().toString();
		ServerWebSocket secondSocket = Mockito.mock(ServerWebSocket.class);
		VideoSession secondSession = new VideoSession(secondSocket, secondId, "second session");
		List<VideoSession> sessions = new ArrayList<VideoSession>();
		sessions.add(firstSession);
		sessions.add(secondSession);
		VideoSessionList list = new VideoSessionList(sessions);
		JsonArray sessionArr = new JsonArray()
				.add(new JsonObject()
						.put("id", firstId)
						.put("name", "first session")
						.put("users", 1))
				.add(new JsonObject()
						.put("id", secondId)
						.put("name", "second session")
						.put("users", 1));
		JsonObject obj = new JsonObject()
				.put("uri", "/session/list")
				.put("sessions", sessionArr);
		assertEquals(list.asBuffer().toString(), obj.toString());
	}

}
