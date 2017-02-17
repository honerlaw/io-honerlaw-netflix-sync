package io.honerlaw.netflixsync.response.video;

import io.vertx.core.json.JsonObject;
import junit.framework.TestCase;

public class VideoTickTest extends TestCase {
	
	public void testPayload() {
		VideoTick videoTick = new VideoTick("http://foo/bar", true, 15);
		JsonObject obj = new JsonObject()
				.put("uri", "/video/tick")
				.put("url", "http://foo/bar")
				.put("playing", true)
				.put("seconds", 15);
		assertEquals(videoTick.asBuffer().toString(), obj.toString());
	}

}
