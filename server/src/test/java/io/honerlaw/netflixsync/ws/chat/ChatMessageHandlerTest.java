package io.honerlaw.netflixsync.ws.chat;

import java.util.ArrayList;
import java.util.List;

import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import io.honerlaw.netflixsync.Server;
import io.honerlaw.netflixsync.VideoSession;
import io.honerlaw.netflixsync.response.chat.ChatMessage;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonObject;
import junit.framework.TestCase;

public class ChatMessageHandlerTest extends TestCase {
	
	private Server server;
	private ServerWebSocket socket;
	
	public void setUp() {
		server = Mockito.mock(Server.class);
		socket = Mockito.mock(ServerWebSocket.class);
	}
	
	public void testHandleBadRequest() {
		ChatMessageHandler handler = new ChatMessageHandler();
		handler.handle(server, socket, new JsonObject());
		Mockito.verify(server, Mockito.times(0)).getVideoSessions();
	}
	
	public void testHandleBadRequestEmptyMessage() {
		ChatMessageHandler handler = new ChatMessageHandler();
		handler.handle(server, socket, new JsonObject().put("message", ""));
		Mockito.verify(server, Mockito.times(0)).getVideoSessions();
	}
	
	public void testHandleGoodRequestCreator() throws Throwable {
		ArgumentCaptor<ChatMessage> argument = ArgumentCaptor.forClass(ChatMessage.class);
		String expected =  new JsonObject()
				.put("uri", "/chat/message")
				.put("message", "foo bar test message").toString();
		VideoSession session = Mockito.spy(new VideoSession(socket, "foo", "bar"));
		List<VideoSession> sessions = new ArrayList<VideoSession>();
		sessions.add(session);
		Mockito.when(server.getVideoSessions()).thenReturn(sessions);
		new ChatMessageHandler().handle(server, socket, new JsonObject()
				.put("message", "foo bar test message"));
		Mockito.verify(session, Mockito.times(1)).notifyCreator(Mockito.any(ChatMessage.class));
		Mockito.verify(session, Mockito.times(1)).notifyUsers(Mockito.any(ChatMessage.class));
		Mockito.verify(session).notifyUsers(argument.capture());
		assertEquals(argument.getValue().asBuffer().toString(), expected);
		Mockito.verify(session).notifyCreator(argument.capture());
		assertEquals(argument.getValue().asBuffer().toString(), expected);
	}
	
	public void testHandleGoodRequestUser() throws Throwable {
		ArgumentCaptor<ChatMessage> argument = ArgumentCaptor.forClass(ChatMessage.class);
		String expected =  new JsonObject()
				.put("uri", "/chat/message")
				.put("message", "foo bar test message").toString();
		VideoSession session = Mockito.spy(new VideoSession(Mockito.mock(ServerWebSocket.class), "foo", "bar"));
		List<VideoSession> sessions = new ArrayList<VideoSession>();
		session.getUsers().add(socket);
		sessions.add(session);
		Mockito.when(server.getVideoSessions()).thenReturn(sessions);
		new ChatMessageHandler().handle(server, socket, new JsonObject()
				.put("message", "foo bar test message"));
		Mockito.verify(session, Mockito.times(1)).notifyCreator(Mockito.any(ChatMessage.class));
		Mockito.verify(session, Mockito.times(1)).notifyUsers(Mockito.any(ChatMessage.class));
		Mockito.verify(session).notifyUsers(argument.capture());
		assertEquals(argument.getValue().asBuffer().toString(), expected);
		Mockito.verify(session).notifyCreator(argument.capture());
		assertEquals(argument.getValue().asBuffer().toString(), expected);
	}
	
	public void testHandleGoodRequestNeitherUserOrCreator() throws Throwable {
		VideoSession session = Mockito.spy(new VideoSession(Mockito.mock(ServerWebSocket.class), "foo", "bar"));
		List<VideoSession> sessions = new ArrayList<VideoSession>();
		sessions.add(session);
		Mockito.when(server.getVideoSessions()).thenReturn(sessions);
		new ChatMessageHandler().handle(server, socket, new JsonObject()
				.put("message", "foo bar test message"));
		Mockito.verify(session, Mockito.times(0)).notifyCreator(Mockito.any(ChatMessage.class));
		Mockito.verify(session, Mockito.times(0)).notifyUsers(Mockito.any(ChatMessage.class));
	}

}
