package io.honerlaw.netflixsync;

import static org.mockito.Mockito.*;

import java.util.UUID;

import org.mockito.Matchers;

import io.honerlaw.netflixsync.response.Response;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonObject;
import junit.framework.TestCase;

public class VideoSessionTest extends TestCase {
	
	public void testGetters() {
		String id = UUID.randomUUID().toString();
		ServerWebSocket mockSocket = mock(ServerWebSocket.class);
		VideoSession videoSession = new VideoSession(mockSocket, id, "foo bar");
		assertEquals(videoSession.getID(), id);
		assertSame(videoSession.getCreator(), mockSocket);
		assertEquals(videoSession.getName(), "foo bar");
		assertEquals(videoSession.getUsers().size(), 0);
		videoSession.getUsers().add(mockSocket);
		videoSession.getUsers().add(mockSocket);
		assertEquals(videoSession.getUsers().size(), 1);
	}
	
	public void testNotify() {
		String id = UUID.randomUUID().toString();
		ServerWebSocket mockSocket = mock(ServerWebSocket.class);
		when(mockSocket.write(Matchers.any(Buffer.class))).thenReturn(mockSocket);
		VideoSession videoSession = spy(new VideoSession(mockSocket, id, "foo bar"));
		Response mockResponse = mock(Response.class);
		assertTrue(videoSession.notifyCreator(mockResponse) instanceof VideoSession);
		assertTrue(videoSession.notifyUsers(mockResponse) instanceof VideoSession);
		videoSession = spy(new VideoSession(mockSocket, id, "foo bar"));
		videoSession.shutdown(true);
		verify(videoSession, times(1)).notifyUsers(any(Response.class));
		verify(videoSession, times(1)).notifyCreator(any(Response.class));
		videoSession = spy(new VideoSession(mockSocket, id, "foo bar"));
		videoSession.shutdown(false);
		verify(videoSession, times(1)).notifyUsers(any(Response.class));
		verify(videoSession, times(0)).notifyCreator(any(Response.class));
	}
	
	public void testAsJsonObject() {
		String id = UUID.randomUUID().toString();
		ServerWebSocket mockSocket = mock(ServerWebSocket.class);
		JsonObject obj = new VideoSession(mockSocket, id, "foo bar").asJsonObject();
		assertEquals(obj.getString("id"), id);
		assertEquals(obj.getString("name"), "foo bar");
		assertEquals(obj.getInteger("users").intValue(), 1);
	}

}
