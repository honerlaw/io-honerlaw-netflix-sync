package io.honerlaw.netflixsync.response.video;

import io.vertx.core.json.JsonObject;
import junit.framework.TestCase;

public class VideoSeekTest extends TestCase {
	
	public void testPayload() {
		VideoSeek videoSeek = new VideoSeek(15);
		JsonObject obj = new JsonObject()
				.put("uri", "/video/seek")
				.put("seconds", 15);
		assertEquals(videoSeek.asBuffer().toString(), obj.toString());
	}

}
