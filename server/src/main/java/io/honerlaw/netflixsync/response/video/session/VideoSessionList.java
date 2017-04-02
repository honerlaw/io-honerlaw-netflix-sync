package io.honerlaw.netflixsync.response.video.session;

import java.util.Collection;

import io.honerlaw.netflixsync.VideoSession;
import io.honerlaw.netflixsync.response.Response;
import io.vertx.core.json.JsonArray;

public class VideoSessionList extends Response {

	public VideoSessionList(Collection<VideoSession> sessions) {
		super("/session/list");
		JsonArray list = new JsonArray();
		sessions.forEach(session -> {
			list.add(session.asJsonObject());
		});
		put("sessions", list);
	}

}
