package com.lcx.service.fsm.impl;

import java.util.Map;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lcx.util.JSONUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthenticatedSessionFSM extends AbstractSessionFSM {
	
	public AuthenticatedSessionFSM(SessionFSMManager sessionFSMManager) {
		this.sessionFSMManager = sessionFSMManager;
	}

	private static final String AUTHORIZATION_KEY = "path";
	private static final String AUTHORIZE_REQUEST = "{\"authorize\":\"path\"}";

	@Override
	public void handleMessage(WebSocketSession session, TextMessage message) {
		log.info("AuthenticatedSessionFSM handleMessage:{}", message.getPayload());
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
		if (!map.containsKey(AUTHORIZATION_KEY)) {
			closeSession(session, CloseStatus.GOING_AWAY);
		}
		
		//TODO check the path
		String path = (String) map.get(AUTHORIZATION_KEY);
		
		session.getAttributes().put(AUTHORIZATION_KEY, path);
		
		//更改状态
		sessionFSMManager.changeState(session, new AuthorisedSessionFSM(sessionFSMManager));
	}

	@Override
	public void firstCall(WebSocketSession session) {
		sendMessage(session, AUTHORIZE_REQUEST);
	}

}
