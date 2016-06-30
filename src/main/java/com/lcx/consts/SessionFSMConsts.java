package com.lcx.consts;

public interface SessionFSMConsts {

	public static final String CURRENT_FSM_KEY = "CURRENT_FSM";
	
	public static final String PATH = "path";
	public static final String AUTHORIZED_REQUEST = "{\"authorize\":\"path\"}";
	public static final String AUTHORIZED_RESPONSE = "{\"authorize\":\"ok\"}";
	
	public static final String TOKEN = "token";
	public static final String AUTHENTICATION_REQUEST = "{\"authentication\":\"token\"}";
	public static final String AUTHENTICATION_RESPONSE = "{\"authentication\":\"ok\"}";
	
}
