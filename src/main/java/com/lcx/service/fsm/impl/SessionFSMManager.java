package com.lcx.service.fsm.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import com.lcx.consts.SessionFSMConsts;
import com.lcx.service.WebSocketSessionManager;
import com.lcx.service.fsm.ISessionFSM;

@Component
public class SessionFSMManager {

	@Autowired
	private WebSocketSessionManager webSocketSessionManager;
	
	public ISessionFSM getCurrentFSM(WebSocketSession session)  {
		ISessionFSM sessionFSM = (ISessionFSM) session.getAttributes().get(SessionFSMConsts.CURRENT_FSM_KEY);
		if (sessionFSM == null) {
			sessionFSM = new StrangerSessionFSM(this);
			changeState(session, sessionFSM);
		}
		return sessionFSM;
	}
	
	public void changeState(WebSocketSession session, ISessionFSM toSessionFSM) {
		toSessionFSM.firstCall(session);
		Map<String, Object> attributes = session.getAttributes();
		session.getAttributes().put(SessionFSMConsts.CURRENT_FSM_KEY, toSessionFSM);
		if (attributes.containsKey(SessionFSMConsts.PATH)) {
			webSocketSessionManager.addSession((String) attributes.get(SessionFSMConsts.PATH), session);
		}
	}
}
