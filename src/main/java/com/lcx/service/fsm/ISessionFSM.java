package com.lcx.service.fsm;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public interface ISessionFSM {

	public void firstCall(WebSocketSession session);
	
	public void handleMessage(WebSocketSession session, TextMessage message);
	
	public void sendMessage(WebSocketSession session, String content);

	public void closeSession(WebSocketSession session);
	
	public void closeSession(WebSocketSession session, CloseStatus closeStatus);
	
}
