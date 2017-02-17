package io.honerlaw.netflixsync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.honerlaw.netflixsync.ws.WebSocketHandler;
import io.honerlaw.netflixsync.ws.chat.ChatMessageHandler;
import io.honerlaw.netflixsync.ws.video.SeekVideoHandler;
import io.honerlaw.netflixsync.ws.video.TickVideoHandler;
import io.honerlaw.netflixsync.ws.video.ToggleVideoHandler;
import io.honerlaw.netflixsync.ws.video.session.CreateSessionHandler;
import io.honerlaw.netflixsync.ws.video.session.JoinSessionHandler;
import io.honerlaw.netflixsync.ws.video.session.LeaveSessionHandler;
import io.honerlaw.netflixsync.ws.video.session.ListSessionHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

/**
 * The main server verticle to handle incoming requests
 *
 * @author Derek Honerlaw <honerlawd@gmail.com>
 */
public class Server extends AbstractVerticle {

    /**
     * The {@link List} of video sessions that are currently available
     */
    private final List<VideoSession> videoSessions = new ArrayList<VideoSession>();

    @Override
    public void start(final Future<Void> future) {

        final Map<String, WebSocketHandler> wsHandlers = new HashMap<String, WebSocketHandler>();
        wsHandlers.put("/session/create", new CreateSessionHandler());
        wsHandlers.put("/session/join", new JoinSessionHandler());
        wsHandlers.put("/session/leave", new LeaveSessionHandler());
        wsHandlers.put("/session/list", new ListSessionHandler());
        wsHandlers.put("/chat/message", new ChatMessageHandler());
        wsHandlers.put("/video/tick", new TickVideoHandler());
        wsHandlers.put("/video/toggle", new ToggleVideoHandler());
        wsHandlers.put("/video/seek", new SeekVideoHandler());

        final Router router = Router.router(getVertx());

        router.route("/ws").handler(ctx -> {

            final ServerWebSocket socket = ctx.request().upgrade();

            socket.handler(buf -> {
                final JsonObject json = buf.toJsonObject();
                final WebSocketHandler handler = wsHandlers.get(json.getString("uri"));
                if (handler != null) {
                    handler.handle(this, socket, json);
                }
            });

            socket.exceptionHandler(throwable -> {
                throwable.printStackTrace();
                socket.close();
            });

            socket.closeHandler(v -> {
                for (final Iterator<VideoSession> it = getVideoSessions().iterator(); it.hasNext();) {
                    final VideoSession session = it.next();
                    if (session.getCreator() == socket) {
                        session.shutdown(false);
                        it.remove();
                    }
                    else {
                        session.getUsers().remove(socket);
                    }
                }
            });

        });

        router.route().handler(StaticHandler.create("webroot"));

        getVertx().createHttpServer().requestHandler(router::accept).listen(8001);
    }

    /**
     * @return {@link List} The list of video sessions currently active
     */
    public List<VideoSession> getVideoSessions() {
        return videoSessions;
    }

    public static void main(final String[] args) {
        Vertx.vertx().deployVerticle(new Server());
    }

}
