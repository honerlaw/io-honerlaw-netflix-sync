package io.honerlaw.netflixsync;

import java.util.HashSet;
import java.util.Set;

import io.honerlaw.netflixsync.response.Response;
import io.honerlaw.netflixsync.response.video.session.VideoSessionShutdown;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonObject;

/**
 * Represents a single video session in the server
 * 
 * @author Derek Honerlaw <honerlawd@gmail.com>
 */
public class VideoSession {
	
	/**
	 * The {@link String} representing the unique id for this session
	 */
	private final String id;
	
	/**
	 * The {@link ServerWebSocket} representing the creator of the session
	 */
	private final ServerWebSocket creator;
	
	/**
	 * The {@link String} representing the name or alias of the session (not unique)
	 */
	private final String name;
	
	/**
	 * The {@link Set} of sockets (users) in this session (excluding the creator)
	 */
	private final Set<ServerWebSocket> users;
	
	/**
	 * Initialize the video session
	 * 
	 * @param creator The creator of the session
	 * @param id The unique id of the session
	 * @param name The name or alias of the session
	 */
	public VideoSession(ServerWebSocket creator, String id, String name) {
		this.id = id;
		this.creator = creator;
		this.name = name;
		this.users = new HashSet<ServerWebSocket>();
	}
	
	/**
	 * @return {@link String} representing the unique session id
	 */
	public String getID() {
		return id;
	}
	
	/**
	 * 
	 * @return {@link ServerWebSocket} that represents the creator of the session
	 */
	public ServerWebSocket getCreator() {
		return creator;
	}
	
	/**
	 * @return {@link String} representing the name or alias for the session
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return {@link Set} representing all users in this session except the creator
	 */
	public Set<ServerWebSocket> getUsers() {
		return users;
	}
	
	/**
	 * Notify the creator of the session about the given response
	 * 
	 * @param resp The response object to send to the creator
	 * 
	 * @return {@link VideoSession} to allow fluid API
	 */
	public VideoSession notifyCreator(Response resp) {
		getCreator().write(resp.asBuffer());
		return this;
	}
	
	/**
	 * Notify all users (except the creator) of the session about the given response
	 * 
	 * @param resp The response object to send to the users
	 * 
	 * @return {@link VideoSession} to allow fluid API
	 */
	public VideoSession notifyUsers(Response resp) {
		getUsers().forEach(socket -> {
			socket.write(resp.asBuffer());
		});
		return this;
	}
	
	/**
	 * Notify all users and optionally the creator that the session is being shut down
	 * 
	 * @param notifyCreator Whether or not to notify the creator
	 * 
	 * @return {@link VideoSession} to allow fluid API
	 */
	public VideoSession shutdown(boolean notifyCreator) {
		Response resp = new VideoSessionShutdown();
		if(notifyCreator) {
			notifyCreator(resp);
		}
		return notifyUsers(resp);
	}
	
	/**
	 * Convert this session into a json object
	 * 
	 * @return The {@link JsonObject} representing this session
	 */
	public JsonObject asJsonObject() {
		return new JsonObject()
				.put("id", getID())
				.put("name", getName())
				.put("users", getUsers().size() + 1);
	}

}
