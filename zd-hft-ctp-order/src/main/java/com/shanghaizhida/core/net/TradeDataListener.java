package com.shanghaizhida.core.net;

public interface TradeDataListener {

	public static final int LOGON_SUCCESS = 0;
	public static final int LOGON_FAIL = 1;

	// void onLogonResult(int code, String text);
	void onTradingData(byte[] rawMsg) throws Exception;
}
