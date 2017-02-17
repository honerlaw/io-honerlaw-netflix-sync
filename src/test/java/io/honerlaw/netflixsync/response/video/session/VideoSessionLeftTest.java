package io.honerlaw.netflixsync.response.video.session;

import io.vertx.core.json.JsonObject;
import junit.framework.TestCase;

public class VideoSessionLeftTest extends TestCase {
	
	public void testPayload() {
		VideoSessionLeft left = new VideoSessionLeft();
		JsonObject obj = new JsonObject().put("uri", "/session/left");
		assertEquals(left.asBuffer().toString(), obj.toString());
	}

}
