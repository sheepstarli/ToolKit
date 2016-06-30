package com.lcx.service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.security.Principal;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketExtension;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

@Component
public class WebSocketSessionManager {

	private AtomicLong count = new AtomicLong();
	private ConcurrentHashMap<String, Object> agentSessions = new ConcurrentHashMap<>();
	
	/**
	 * 	/tenants/5817/agents/99852d0b-e418-4408-87b3-67634048cf8e/1467212211009
	 * 	/tenants/5817/agents/99852d0b-e418-4408-87b3-67634048cf8e
	 * 	/tenants/5817/agents
	 */
	public List<WebSocketSession> getSessions(String path) {
		String[] paths = buildPath(path);
		
		List<WebSocketSession> resultList = new LinkedList<>();
		search(findPathRoot(agentSessions, paths, 0), resultList);
		return resultList;
	}
	
	public void addSession(String path, WebSocketSession webSocketSession) {
		String[] paths = buildClientPath(path);
		ConcurrentHashMap<String, Object> buildPathRoot = buildPathRoot(agentSessions, paths, 0);
		buildPathRoot.put(paths[paths.length - 1], webSocketSession);
		count.incrementAndGet();
	}
	
	public void removeSession(String path) {
		String[] paths = buildClientPath(path);
		ConcurrentHashMap<String, Object> buildPathRoot = buildPathRoot(agentSessions, paths, 0);
		buildPathRoot.remove(paths[paths.length - 1]);
		count.decrementAndGet();
	}
	
	public Long countSession() {
		return count.get();
	}
	
	public void cleanSession() {
		agentSessions.clear();
	}
	
	public ConcurrentHashMap<String, Object> getAgentSessions() {
		return agentSessions;
	}
	
	@SuppressWarnings("unchecked")
	private Object findPathRoot(Object object, String[] paths, int i) {
		if (object == null) {
			return null;
		}
		if (i == paths.length) {
			return object;
		}
		if (object instanceof WebSocketSession) {
			return object;
		} else if (object instanceof ConcurrentHashMap) {
			ConcurrentHashMap<String, Object> map = (ConcurrentHashMap<String, Object>) object;
			Object object2 = map.get(paths[i]);
			return findPathRoot(object2, paths, ++i);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private ConcurrentHashMap<String, Object> buildPathRoot(ConcurrentHashMap<String, Object> map, String[] paths, int i) {
		if (paths.length - i < 2) {
			return map;
		}
		map.putIfAbsent(paths[i], new ConcurrentHashMap<>());
		//由于此时可能别的进程也已经创建了，则再取回
		ConcurrentHashMap<String, Object> targetMap = (ConcurrentHashMap<String, Object>) map.get(paths[i]);
		return buildPathRoot(targetMap, paths, ++i);
	}
	
	@SuppressWarnings("unchecked")
	private void search(Object object, List<WebSocketSession> resultList) {
		if (object == null) {
			return ;
		}
		if (object instanceof WebSocketSession) {
			resultList.add((WebSocketSession) object);
			return ;
		} else if (object instanceof ConcurrentHashMap) {
			ConcurrentHashMap<String, Object> map = (ConcurrentHashMap<String, Object>) object;
			Set<Entry<String, Object>> entrySet = map.entrySet();
			for (Entry<String, Object> entry : entrySet) {
				search(entry.getValue(), resultList);
			}
			return ;
		}
		return ;
	}
	
	private String[] buildClientPath(String path) {
		if (StringUtils.isBlank(path)) {
			return null;
		}
		String[] arr = StringUtils.split(path, "/");
		return Arrays.copyOf(arr, arr.length - 1);
	}
	
	private String[] buildPath(String path) {
		if (StringUtils.isBlank(path)) {
			return null;
		}
		return StringUtils.split(path, "/");
	}
	
	public static void main(String[] args) {
		WebSocketSessionManager webSocketSessionManager = new WebSocketSessionManager();
		webSocketSessionManager.addSession("/tenants/5817/agents/99852d0b-e418-4408-87b3-67634048cf8e/1467212211009", new WebSocketSession() {
			
			@Override
			public void setTextMessageSizeLimit(int messageSizeLimit) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setBinaryMessageSizeLimit(int messageSizeLimit) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void sendMessage(WebSocketMessage<?> message) throws IOException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean isOpen() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public URI getUri() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getTextMessageSizeLimit() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public InetSocketAddress getRemoteAddress() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Principal getPrincipal() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public InetSocketAddress getLocalAddress() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getId() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public HttpHeaders getHandshakeHeaders() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public List<WebSocketExtension> getExtensions() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getBinaryMessageSizeLimit() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Map<String, Object> getAttributes() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getAcceptedProtocol() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void close(CloseStatus status) throws IOException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void close() throws IOException {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	
}
