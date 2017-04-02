package io.honerlaw.netflixsync.response.chat;

import io.vertx.core.json.JsonObject;
import junit.framework.TestCase;

public class ChatMessageTest extends TestCase {
	
	public void testPayload() {
		ChatMessage message = new ChatMessage("foo bar");
		JsonObject obj = new JsonObject()
				.put("uri", "/chat/message")
				.put("message", "foo bar");
		assertEquals(message.asBuffer().toString(), obj.toString());
	}

}
