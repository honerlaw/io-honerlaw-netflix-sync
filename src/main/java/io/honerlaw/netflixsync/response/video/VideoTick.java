package io.honerlaw.netflixsync.response.video;

import io.honerlaw.netflixsync.response.Response;

public class VideoTick extends Response {

	public VideoTick(String url, boolean playing, int seconds) {
		super("/video/tick");
		put("url", url);
		put("playing", playing);
		put("seconds", seconds);
	}

}
