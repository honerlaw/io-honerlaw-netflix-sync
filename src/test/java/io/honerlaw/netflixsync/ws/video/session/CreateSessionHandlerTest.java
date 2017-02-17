package io.honerlaw.netflixsync.ws.video.session;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;

import io.honerlaw.netflixsync.Server;
import io.honerlaw.netflixsync.VideoSession;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonObject;
import junit.framework.TestCase;

public class CreateSessionHandlerTest extends TestCase {
	
	private Server server;
	private ServerWebSocket socket;
	
	public void setUp() {
		server = mock(Server.class);
		socket = mock(ServerWebSocket.class);
	}
	
	public void testHandleBadRequest() {
		new CreateSessionHandler().handle(server, socket, new JsonObject());
		verify(server, times(0)).getVideoSessions();
		verify(socket, times(1)).write(any(Buffer.class));
	}
	
	public void testHandleAlreadyInSession() {
		VideoSession session = new VideoSession(socket, "foo", "bar");
		when(server.getVideoSessions()).thenReturn(Arrays.asList(session));
		new CreateSessionHandler().handle(server, socket, new JsonObject()
				.put("name", "foo bar"));
		verify(server, times(1)).getVideoSessions();
		verify(socket, times(1)).write(any(Buffer.class));
	}
	
	public void testHandleGoodRequest() {
		when(server.getVideoSessions()).thenReturn(new ArrayList<VideoSession>());
		new CreateSessionHandler().handle(server, socket, new JsonObject()
				.put("name", "foo bar"));
		verify(server, times(2)).getVideoSessions();
		verify(socket, times(1)).write(any(Buffer.class));
		assertEquals(server.getVideoSessions().size(), 1);
	}

}
