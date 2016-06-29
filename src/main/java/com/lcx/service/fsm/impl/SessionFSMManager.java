package com.lcx.service.fsm.impl;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import com.lcx.service.fsm.ISessionFSM;

@Component
public class SessionFSMManager {

	private static final String CURRENT_FSM_KEY = "CURRENT_FSM";
	
	public ISessionFSM getCurrentFSM(WebSocketSession session)  {
		ISessionFSM sessionFSM = (ISessionFSM) session.getAttributes().get(CURRENT_FSM_KEY);
		if (sessionFSM == null) {
			sessionFSM = new StrangerSessionFSM(this);
			changeState(session, sessionFSM);
		}
		return sessionFSM;
	}
	
	public void changeState(WebSocketSession session, ISessionFSM sessionFSM) {
		session.getAttributes().put(CURRENT_FSM_KEY, sessionFSM);
	}
}
