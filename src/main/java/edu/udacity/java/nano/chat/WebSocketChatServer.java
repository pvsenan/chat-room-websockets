package edu.udacity.java.nano.chat;

import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSON;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket Server
 *
 * @see ServerEndpoint WebSocket Client
 * @see Session   WebSocket Session
 */

@Component
@ServerEndpoint("/chat")
public class WebSocketChatServer {

    /**
     * All chat sessions.
     */
    private static Map<String, Session> onlineSessions = new ConcurrentHashMap<>();

    private static void sendMessageToAll(String msg) {
        onlineSessions.forEach((s, session) -> {
            try {
                session.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });    }

    /**
     * Open connection, 1) add session, 2) add user.
     */
    @OnOpen
    public void onOpen(Session session) {
        Message message = new Message();
        message.setMessageType(Message.MessageType.JOIN);
        session.getUserProperties().put("messageObject", message);
        onlineSessions.put(session.getId(),session);
        sendMessageToAll(Message.getJsonString(message,onlineSessions.size()));    }

    /**
     * Send message, 1) get username and session, 2) send message to all.
     */
    @OnMessage
    public void onMessage(Session session, String jsonStr) {
        Message message = (Message) JSON.parseObject(jsonStr, Message.class);
        message.setMessageType(Message.MessageType.SPEAK);
        sendMessageToAll(Message.getJsonString(message,onlineSessions.size()));    }

    /**
     * Close connection, 1) remove session, 2) update user.
     */
    @OnClose
    public void onClose(Session session) throws IOException {
        onlineSessions.remove(session.getId());
        Message message = (Message) session.getUserProperties().get("messageObject");
        sendMessageToAll(Message.getJsonString(message,onlineSessions.size()));
        session.close();    }

    /**
     * Print exception.
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

}
