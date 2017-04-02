package io.honerlaw.netflixsync.response.video;

import io.honerlaw.netflixsync.response.Response;

public class VideoSeek extends Response {

	public VideoSeek(int seconds) {
		super("/video/seek");
		put("seconds", seconds);
	}

}
