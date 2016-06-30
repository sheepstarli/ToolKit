package com.lcx.handler;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.lcx.consts.SessionFSMConsts;
import com.lcx.service.WebSocketSessionManager;
import com.lcx.service.fsm.ISessionFSM;
import com.lcx.service.fsm.impl.SessionFSMManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MainHandler extends TextWebSocketHandler {
	
	@Autowired
	private SessionFSMManager sessionFSMManager;
	
	@Autowired
	private WebSocketSessionManager webSocketSessionManager;
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		log.info("session connected id:{}", session.getId());
		sessionFSMManager.getCurrentFSM(session);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		log.info("sesion closed id:{}", session.getId());
		String path = (String) session.getAttributes().get(SessionFSMConsts.PATH);
		if (StringUtils.isBlank(path)) {
			log.warn("session has no path id:{} attributes:{}", session.getId(), session.getAttributes());
			return ;
		}
		webSocketSessionManager.removeSession(path);
	}
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String payload = message.getPayload();
		log.info("handleTextMessage attributes:{} payload:{}", session.getAttributes(), payload);
		ISessionFSM currentFSM = sessionFSMManager.getCurrentFSM(session);
		currentFSM.handleMessage(session, message);
	}

}
