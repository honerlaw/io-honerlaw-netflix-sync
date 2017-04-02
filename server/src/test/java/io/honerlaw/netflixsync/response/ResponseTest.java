package io.honerlaw.netflixsync.response;

import io.vertx.core.json.JsonObject;
import junit.framework.TestCase;

public class ResponseTest extends TestCase {
	
	public void testUri() {
		ResponseMock mock = new ResponseMock("foo bar");
		JsonObject obj = new JsonObject()
				.put("uri", "foo bar");
		assertEquals(mock.asBuffer().toString(), obj.toString());
	}
	
	public void testPut() {
		ResponseMock mock = new ResponseMock("foo bar");
		mock.put("test", "testing");
		JsonObject obj = new JsonObject()
				.put("uri", "foo bar")
				.put("test", "testing");
		mock.put("test", "testing");
		assertEquals(mock.asBuffer().toString(), obj.toString());
	}
	
	private class ResponseMock extends Response {

		public ResponseMock(String uri) {
			super(uri);
		}
		
	}
	
}