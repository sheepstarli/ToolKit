package com.lcx.service.fsm.impl;

import java.util.Map;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.easemob.weichat.models.util.JSONUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.lcx.consts.SessionFSMConsts;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthenticatedSessionFSM extends AbstractSessionFSM {
	
	public AuthenticatedSessionFSM(SessionFSMManager sessionFSMManager) {
		this.sessionFSMManager = sessionFSMManager;
	}

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
		if (!map.containsKey(SessionFSMConsts.PATH)) {
			closeSession(session, CloseStatus.GOING_AWAY);
		}
		
		//TODO check the path
		String path = (String) map.get(SessionFSMConsts.PATH);
		sendMessage(session, SessionFSMConsts.AUTHORIZED_RESPONSE);
		
		//设置到session中
		session.getAttributes().put(SessionFSMConsts.PATH, path);
		
		//更改状态
		sessionFSMManager.changeState(session, new AuthorisedSessionFSM(sessionFSMManager));
	}

	@Override
	public void firstCall(WebSocketSession session) {
		sendMessage(session, SessionFSMConsts.AUTHORIZED_REQUEST);
	}

}
