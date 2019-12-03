package edu.udacity.java.nano.chat;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import java.util.HashMap;
import java.util.Map;

/**
 * WebSocket message model
 */
public class Message {
    @JSONField(alternateNames = "username")
    private String username;
    @JSONField(alternateNames = "msg")
    private String message;
    private MessageType messageType;

    public Message() {
    }

    public static String getJsonString(Message message, int onlineUserCount) {
        Map<String, String> messageData = new HashMap<>();
        messageData.put("username", message.getUsername());
        messageData.put("msg", message.getMessage());
        if (message.getMessageType() == MessageType.SPEAK) {
            messageData.put("type", "SPEAK");
        }
        messageData.put("onlineCount", String.valueOf(onlineUserCount));
        JSONObject jsonObject = new JSONObject();
        jsonObject.putAll(messageData);
        return jsonObject.toJSONString();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public enum MessageType {
        JOIN,
        SPEAK,
        LEAVE
    }
}
