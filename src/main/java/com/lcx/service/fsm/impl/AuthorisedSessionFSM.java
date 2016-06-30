package com.lcx.service.fsm.impl;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthorisedSessionFSM extends AbstractSessionFSM {

	public AuthorisedSessionFSM(SessionFSMManager sessionFSMManager) {
		this.sessionFSMManager = sessionFSMManager;
	}
	
	@Override
	public void handleMessage(WebSocketSession session, TextMessage message) {
//		log.info("AuthorisedSessionFSM handleMessage:{}", message.getPayload());
	}

	@Override
	public void firstCall(WebSocketSession session) {
		
	}

}
