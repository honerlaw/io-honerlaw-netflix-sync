package io.honerlaw.netflixsync.response.video.session;

import io.honerlaw.netflixsync.VideoSession;
import io.honerlaw.netflixsync.response.Response;

public class VideoSessionJoined extends Response {

	public VideoSessionJoined(VideoSession session) {
		super("/session/joined");
		put("session", session.asJsonObject());
	}

}
