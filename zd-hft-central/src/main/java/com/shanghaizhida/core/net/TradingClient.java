package com.shanghaizhida.core.net;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shanghaizhida.CommonFunction;

public class TradingClient extends BaseClient {

	private static Logger logger=LoggerFactory.getLogger(TradingClient.class);
	
	private ConnectionStateListener connectionStateListener = null;
	public BlockingQueue<byte[]> dataQueue = null;

	public TradingClient(String hostIP, String hostPort) {
		super(hostIP, hostPort);
		dataQueue = new LinkedBlockingQueue<byte[]>();
	}

	@Override
	public void onMsgReady(byte[] rawMsg) {
		try {
			dataQueue.put(rawMsg);

		} catch (InterruptedException ie) {
			logger.warn("TradingClient onMsgReady:{}",ie.getMessage());
		}
	}

	@Override
	public void onConnectStateChange(int code, String text) {
		if (connectionStateListener != null)
			connectionStateListener.onConnectStateChange(code, text);
	}

	public void setConnectionStateListener(ConnectionStateListener listener) {
		this.connectionStateListener = listener;
	}

	/**
	 * 发送Ascii编码的消息
	 * 
	 * @param str
	 *            消息内容
	 */
	public void sendAsciiMsg(String str) {
		byte[] reqBytes = CommonFunction.asciiStrToNetBytes(str);
		sendData(reqBytes);
	}

	/**
	 * 发送UTF-8编码的消息
	 * 
	 * @param str
	 *            消息内容
	 */
	public void sendUTF8Msg(String str) {
		byte[] reqBytes = CommonFunction.utf8StrToNetBytes(str);
		sendData(reqBytes);
	}

	/**
	 * 发送消息,不对编码进行处理
	 * 
	 * @param byteArrMsg
	 *            byte消息内容
	 */
	public void sendMsg(byte[] byteArrMsg) {
		sendData(byteArrMsg);
	}
}
