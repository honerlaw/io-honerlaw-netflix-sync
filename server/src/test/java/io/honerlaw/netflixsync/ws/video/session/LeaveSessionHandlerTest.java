package io.honerlaw.netflixsync.ws.video.session;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.mockito.ArgumentCaptor;

import io.honerlaw.netflixsync.Server;
import io.honerlaw.netflixsync.VideoSession;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonObject;
import junit.framework.TestCase;

public class LeaveSessionHandlerTest extends TestCase {
	
	private Server server;
	private ServerWebSocket socket;
	
	public void setUp() {
		server = mock(Server.class);
		socket = mock(ServerWebSocket.class);
	}
	
	public void testHandleInSessionCreatorLeave() {
		VideoSession session = spy(new VideoSession(socket, "foo", "bar"));
		when(server.getVideoSessions()).thenReturn(new ArrayList<VideoSession>(Arrays.asList(session)));
		new LeaveSessionHandler().handle(server, socket, new JsonObject());
		verify(server, times(1)).getVideoSessions();
		verify(socket, times(2)).write(any(Buffer.class)); // called once in shutdown and once at the end of handle
		verify(session, times(1)).shutdown(true);
		assertEquals(server.getVideoSessions().size(), 0);
	}
	
	public void testHandleInSessionUserLeave() {
		ArgumentCaptor<Buffer> argument = ArgumentCaptor.forClass(Buffer.class);
		VideoSession session = spy(new VideoSession(mock(ServerWebSocket.class), "foo", "bar"));
		session.getUsers().add(socket);
		when(server.getVideoSessions()).thenReturn(Arrays.asList(session));
		new LeaveSessionHandler().handle(server, socket, new JsonObject());
		verify(server, times(1)).getVideoSessions();
		verify(socket, times(1)).write(any(Buffer.class));
		assertEquals(session.getUsers().size(), 0);
		verify(socket).write(argument.capture());
		assertEquals(argument.getValue().toString(), new JsonObject().put("uri", "/session/left").toString());
	}
	
	public void testHandleNotInSessionLeave() {
		ArgumentCaptor<Buffer> argument = ArgumentCaptor.forClass(Buffer.class);
		new LeaveSessionHandler().handle(server, socket, new JsonObject());
		verify(socket, times(1)).write(any(Buffer.class));
		verify(socket).write(argument.capture());
		assertEquals(argument.getValue().toString(), new JsonObject().put("uri", "/session/left").toString());
	}

}
