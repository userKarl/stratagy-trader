package com.shanghaizhida.core.net;

public interface ConnectionStateListener {

	public static final int CONNECTION_LOST = 1;
	public static final int RECONNECTED = 2;
	public static final int SCOKET_CONNECTIONFAIL = 3;

	void onConnectStateChange(int code, String text);
}
