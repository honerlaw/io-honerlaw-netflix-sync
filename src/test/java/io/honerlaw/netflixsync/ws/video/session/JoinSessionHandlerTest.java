package io.honerlaw.netflixsync.ws.video.session;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import org.mockito.ArgumentCaptor;

import io.honerlaw.netflixsync.Server;
import io.honerlaw.netflixsync.VideoSession;
import io.honerlaw.netflixsync.response.video.session.VideoSessionJoined;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonObject;
import junit.framework.TestCase;

public class JoinSessionHandlerTest extends TestCase {
	
	private Server server;
	private ServerWebSocket socket;
	
	public void setUp() {
		server = mock(Server.class);
		socket = mock(ServerWebSocket.class);
	}
	
	public void testHandleBadRequest() {
		new JoinSessionHandler().handle(server, socket, new JsonObject());
		verify(server, times(0)).getVideoSessions();
		verify(socket, times(1)).write(any(Buffer.class));
	}
	
	public void testHandleAlreadyInSession() {
		VideoSession session = new VideoSession(socket, "foo", "bar");
		when(server.getVideoSessions()).thenReturn(Arrays.asList(session));		
		new JoinSessionHandler().handle(server, socket, new JsonObject().put("id", "foobar"));
		verify(server, times(1)).getVideoSessions();
		verify(socket, times(1)).write(any(Buffer.class));
	}
	
	public void testHandleInvalidSession() {
		when(server.getVideoSessions()).thenReturn(new ArrayList<VideoSession>());		
		new JoinSessionHandler().handle(server, socket, new JsonObject().put("id", "foobar"));
		verify(server, times(2)).getVideoSessions();
		verify(socket, times(1)).write(any(Buffer.class));
	}
	
	public void testHandleGoodRequest() {
		ArgumentCaptor<VideoSessionJoined> argument = ArgumentCaptor.forClass(VideoSessionJoined.class);
		String id = UUID.randomUUID().toString();
		VideoSession session = spy(new VideoSession(mock(ServerWebSocket.class), id, "foo bar"));
		String expected = new JsonObject()
				.put("uri", "/session/joined")
				.put("session", new JsonObject()
						.put("id", id)
						.put("name", "foo bar")
						.put("users", 2)).toString();
		when(server.getVideoSessions()).thenReturn(Arrays.asList(session));
		new JoinSessionHandler().handle(server,  socket, new JsonObject().put("id", id));
		verify(server, times(2)).getVideoSessions();
		verify(session, times(1)).notifyCreator(any(VideoSessionJoined.class));
		verify(session, times(1)).notifyUsers(any(VideoSessionJoined.class));
		verify(session).notifyUsers(argument.capture());
		assertEquals(argument.getValue().asBuffer().toString(), expected);
		verify(session).notifyCreator(argument.capture());
		assertEquals(argument.getValue().asBuffer().toString(), expected);
		assertTrue(session.getUsers().contains(socket));
	}

}
