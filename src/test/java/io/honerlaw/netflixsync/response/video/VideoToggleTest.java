package io.honerlaw.netflixsync.response.video;

import io.vertx.core.json.JsonObject;
import junit.framework.TestCase;

public class VideoToggleTest extends TestCase {
	
	public void testPayload() {
		VideoToggle videoToggle = new VideoToggle(true);
		JsonObject obj = new JsonObject()
				.put("uri", "/video/toggle")
				.put("playing", true);
		assertEquals(videoToggle.asBuffer().toString(), obj.toString());
	}

}
