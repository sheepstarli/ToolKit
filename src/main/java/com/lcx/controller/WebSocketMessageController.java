package com.lcx.controller;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@ConditionalOnProperty(name = "tool.websocket.message", havingValue = "true", matchIfMissing = false)
public class WebSocketMessageController {

	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public Greeting greetings(HelloMessage message) throws InterruptedException {
		log.info("greeting {}", message.getName());
		Thread.sleep(1000L);
		return new Greeting("Hello " + message.getName());
	}
	
	@Data
	@AllArgsConstructor
	public static class Greeting {
		private String content;
	}
	@Data
	public static class HelloMessage {
		private String name;
	}
}
