package io.honerlaw.netflixsync.response.video;

import io.honerlaw.netflixsync.response.Response;

public class VideoToggle extends Response {

	public VideoToggle(boolean playing) {
		super("/video/toggle");
		put("playing", playing);
	}

}
