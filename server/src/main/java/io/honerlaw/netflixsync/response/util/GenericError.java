package io.honerlaw.netflixsync.response.util;

import io.honerlaw.netflixsync.response.Response;

public class GenericError extends Response {

	public GenericError(String error) {
		super("/error");
		put("error", error);
	}
	
}
