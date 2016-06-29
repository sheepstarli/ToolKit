package com.lcx.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.lcx.handler.MainHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * http://docs.spring.io/spring/docs/current/spring-framework-reference/html/websocket.html
 * @author lcx
 * @date 2016年6月28日 下午9:12:44
 */
@Configuration
@EnableWebSocket
@Slf4j
public class WebSocketConfig implements WebSocketConfigurer {

	@Autowired
	private MainHandler mainHandler;
	
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(mainHandler, "/hello")
			.setAllowedOrigins("*")
//			.addInterceptors(new HttpSessionHandshakeInterceptor())
			.withSockJS();
	}
	

}
