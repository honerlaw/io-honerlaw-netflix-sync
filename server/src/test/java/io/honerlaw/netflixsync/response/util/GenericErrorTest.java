package io.honerlaw.netflixsync.response.util;

import io.vertx.core.json.JsonObject;
import junit.framework.TestCase;

public class GenericErrorTest extends TestCase {
	
	public void testPayload() {
		GenericError error = new GenericError("error message");
		JsonObject obj = new JsonObject()
				.put("uri", "/error")
				.put("error", "error message");
		assertEquals(error.asBuffer().toString(), obj.toString());
	}

}
