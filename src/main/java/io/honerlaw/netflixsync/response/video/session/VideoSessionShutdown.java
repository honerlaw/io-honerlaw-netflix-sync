package io.honerlaw.netflixsync.response.video.session;

import io.honerlaw.netflixsync.response.Response;

public class VideoSessionShutdown extends Response {
	
	public VideoSessionShutdown() {
		super("/session/shutdown");
	}

}
