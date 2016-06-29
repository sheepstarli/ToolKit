package com.lcx.handler;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.lcx.service.fsm.impl.SessionFSMManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MainHandler extends TextWebSocketHandler {
	
	private static final String PATH = "PATH";
	
	@Autowired
	private SessionFSMManager sessionFSMManager;
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		if (session.getAttributes().containsKey(PATH)) {
			sessionMap.remove(session.getAttributes().get(PATH));
		}
	}
	
	private ConcurrentHashMap<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String payload = message.getPayload();
		log.info("handleTextMessage attributes:{} payload:{}", session.getAttributes(), payload);
		sessionFSMManager.getCurrentFSM(session).handleMessage(session, message);
	}

}
