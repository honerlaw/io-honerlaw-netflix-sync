package io.honerlaw.netflixsync.response.chat;

import io.honerlaw.netflixsync.response.Response;

public class ChatMessage extends Response {

	public ChatMessage(String message) {
		super("/chat/message");
		put("message", message);
	}

}
