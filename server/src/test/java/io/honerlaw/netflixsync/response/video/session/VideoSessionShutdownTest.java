package io.honerlaw.netflixsync.response.video.session;

import io.vertx.core.json.JsonObject;
import junit.framework.TestCase;

public class VideoSessionShutdownTest extends TestCase {
	
	public void testPayload() {
		VideoSessionShutdown shutdown = new VideoSessionShutdown();
		JsonObject obj = new JsonObject().put("uri", "/session/shutdown");
		assertEquals(shutdown.asBuffer().toString(), obj.toString());
	}

}
