package com.lcx.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.easemob.weichat.models.util.JSONUtil;
import com.easemob.weichat.service.message.BlockingRedisMessageConsumer.Worker;
import com.easemob.weichat.service.pushservice.PushServiceData;
import com.fasterxml.jackson.core.type.TypeReference;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageDistributeWorker implements Worker {
	
	private WebSocketSessionManager webScoketSessionManager;
	
	public MessageDistributeWorker(WebSocketSessionManager webScoketSessionManager) {
		this.webScoketSessionManager = webScoketSessionManager;
	}

	@Override
	public void run(String message) throws Exception {
		log.info("MessageDistributeWorker reveive message:{}", message);
		if (StringUtils.isBlank(message)) {
			return ;
		}
		PushServiceData pushServiceData = JSONUtil.getObjectMapper().readValue(message, new TypeReference<PushServiceData>() {
		});
		if (StringUtils.isBlank(pushServiceData.getPath())) {
			return ;
		}
		List<WebSocketSession> sessions = webScoketSessionManager.getSessions(pushServiceData.getPath());
		for (WebSocketSession session : sessions) {
			try {
				session.sendMessage(new TextMessage(JSONUtil.getObjectMapper().writeValueAsString(pushServiceData.getPayload()), true));
			} catch (Exception e) {
				log.error("send error sessionId:{}", session.getId());
				log.error("send error", e);
				
			}
		}
		
	}

}
