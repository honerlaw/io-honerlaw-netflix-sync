package io.honerlaw.netflixsync.response.video.session;

import io.honerlaw.netflixsync.VideoSession;
import io.honerlaw.netflixsync.response.Response;

public class VideoSessionCreated extends Response {

	public VideoSessionCreated(VideoSession session) {
		super("/session/created");
		put("session", session.asJsonObject());
	}

}
