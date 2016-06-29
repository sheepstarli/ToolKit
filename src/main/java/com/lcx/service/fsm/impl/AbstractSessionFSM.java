package com.lcx.service.fsm.impl;

import java.io.IOException;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.lcx.service.fsm.ISessionFSM;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractSessionFSM implements ISessionFSM {

	protected SessionFSMManager sessionFSMManager;
	
	@Override
	public void closeSession(WebSocketSession session) {
		try {
			session.close();
		} catch (IOException e) {
			log.error("close session error", e);
		}
	}
	

	@Override
	public void closeSession(WebSocketSession session, CloseStatus closeStatus) {
		try {
			session.close(closeStatus);
		} catch (IOException e) {
			log.error("close session error", e);
		}
	}

	@Override
	public void sendMessage(WebSocketSession session, String content) {
		try {
			session.sendMessage(new TextMessage(content, true));
		} catch (IOException e) {
			log.error("send message error message:{}", content);
			log.error("send message error", e);
		}
	}

}
