package io.honerlaw.netflixsync.ws.video;

import java.util.ArrayList;
import java.util.List;

import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import io.honerlaw.netflixsync.Server;
import io.honerlaw.netflixsync.VideoSession;
import io.honerlaw.netflixsync.response.video.VideoToggle;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonObject;
import junit.framework.TestCase;

public class ToggleVideoHandlerTest extends TestCase {
	
	private Server server;
	private ServerWebSocket socket;
	
	public void setUp() {
		server = Mockito.mock(Server.class);
		socket = Mockito.mock(ServerWebSocket.class);
	}
	
	public void testHandleBadRequest() {
		new ToggleVideoHandler().handle(server, socket, new JsonObject());
		Mockito.verify(server, Mockito.times(0)).getVideoSessions();
	}
	
	public void testHandleGoodRequest() {
		ArgumentCaptor<VideoToggle> argument = ArgumentCaptor.forClass(VideoToggle.class);
		String expected = new JsonObject()
				.put("uri", "/video/toggle")
				.put("playing", true).toString();
		VideoSession session = Mockito.spy(new VideoSession(socket, "foo", "bar"));
		List<VideoSession> sessions = new ArrayList<VideoSession>();
		sessions.add(session);
		Mockito.when(server.getVideoSessions()).thenReturn(sessions);
		new ToggleVideoHandler().handle(server, socket, new JsonObject().put("playing", true));
		Mockito.verify(server, Mockito.times(1)).getVideoSessions();
		Mockito.verify(session, Mockito.times(1)).notifyUsers(Mockito.any(VideoToggle.class));
		Mockito.verify(session, Mockito.times(0)).notifyCreator(Mockito.any(VideoToggle.class));
		Mockito.verify(session).notifyUsers(argument.capture());
		assertEquals(argument.getValue().asBuffer().toString(), expected);
	}

}
