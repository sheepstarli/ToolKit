package com.lcx.service.fsm.impl;

import java.util.Map;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lcx.util.JSONUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StrangerSessionFSM extends AbstractSessionFSM {
	
	public StrangerSessionFSM(SessionFSMManager sessionFSMManager) {
		this.sessionFSMManager = sessionFSMManager;
	}
	
	private static final String AUTHENTICATION_KEY = "token";
	
	private static final String AUTHENTICATION_REQUEST = "{\"authentication\":\"token\"}";

	@Override
	public void handleMessage(WebSocketSession session, TextMessage message) {
		log.info("StrangerSessionFSM handleMessage:{}", message.getPayload());
		String payload = message.getPayload();
		Map<String, Object> map = null;
		try {
			map = JSONUtil.getObjectMapper().readValue(payload, new TypeReference<Map<String, Object>>() {
			});
		} catch (Exception e) {
			log.error("parse json to map error payload:{}", payload);
			closeSession(session, CloseStatus.GOING_AWAY);
			return ;
		}
		if (!map.containsKey(AUTHENTICATION_KEY)) {
			closeSession(session, CloseStatus.GOING_AWAY);
		}
		
		//TODO check the token
		String token = (String) map.get(AUTHENTICATION_KEY);
		log.info("token is {}", token);
		
		session.getAttributes().put(AUTHENTICATION_KEY, token);
		//更改状态
		sessionFSMManager.changeState(session, new AuthenticatedSessionFSM(sessionFSMManager));
	}

	@Override
	public void firstCall(WebSocketSession session) {
		sendMessage(session, AUTHENTICATION_REQUEST);
	}


}
