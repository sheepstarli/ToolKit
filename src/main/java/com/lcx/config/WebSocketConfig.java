package com.lcx.config;

import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.easemob.weichat.service.message.BlockingRedisMessageConsumer;
import com.easemob.weichat.service.message.IMessageConsumer;
import com.lcx.handler.MainHandler;
import com.lcx.service.MessageDistributeWorker;
import com.lcx.service.WebSocketSessionManager;

/**
 * http://docs.spring.io/spring/docs/current/spring-framework-reference/html/websocket.html
 * @author lcx
 * @date 2016年6月28日 下午9:12:44
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

	@Autowired
	private MainHandler mainHandler;
	
	@Autowired
    private MetricRegistry metricRegistry;
	
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(mainHandler, "/hello")
			.setAllowedOrigins("*")
//			.addInterceptors(new HttpSessionHandshakeInterceptor())
			.withSockJS();
	}
	
	@Bean
    public IMessageConsumer messageDistributeConsumer(StringRedisTemplate redisTemplate, WebSocketSessionManager webScoketSessionManager) throws URISyntaxException {
		MessageDistributeWorker worker = new MessageDistributeWorker(webScoketSessionManager);
		Timer timer = metricRegistry.timer("pusherservice.message.consumer.timer");
        BlockingRedisMessageConsumer cs = new BlockingRedisMessageConsumer(
                redisTemplate,
                "pushservice:data", 1, timer);
        cs.start(worker);
        return cs;
	}

}
