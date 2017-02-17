package io.honerlaw.netflixsync.ws.video.session;

import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.mockito.ArgumentCaptor;

import io.honerlaw.netflixsync.Server;
import io.honerlaw.netflixsync.VideoSession;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import junit.framework.TestCase;

public class ListSessionHandlerTest extends TestCase {
	
	private Server server;
	private ServerWebSocket socket;
	
	public void setUp() {
		server = mock(Server.class);
		socket = mock(ServerWebSocket.class);
	}
	
	public void testHandle() {
		ArgumentCaptor<Buffer> argument = ArgumentCaptor.forClass(Buffer.class);
		VideoSession session = new VideoSession(socket, "foo", "bar");
		String expected = new JsonObject()
				.put("uri", "/session/list")
				.put("sessions", new JsonArray()
						.add(session.asJsonObject())).toString();
		when(server.getVideoSessions()).thenReturn(Arrays.asList(session));
		new ListSessionHandler().handle(server, socket, new JsonObject());
		verify(socket, times(1)).write(any(Buffer.class));
		verify(socket).write(argument.capture());
		assertEquals(argument.getValue().toString(), expected);
	}

}
