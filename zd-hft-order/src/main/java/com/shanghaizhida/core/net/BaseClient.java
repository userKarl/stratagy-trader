package com.shanghaizhida.core.net;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shanghaizhida.core.parser.NetInfoParser;
import com.shanghaizhida.core.parser.RecvStateObject;

public abstract class BaseClient implements Runnable {

	private static Logger logger = LoggerFactory.getLogger(BaseClient.class);

	private SocketAddress socketAddr = null;

	private Thread dataFeedThread = null;

	private Thread dateSendTherad = null;

	private BlockingQueue<byte[]> sendQueue = null;

	public boolean stopFlag = true;

	public Socket receiveSocket = null;
	public boolean isSocketBroken = true;
	public RecvStateObject networkState = null;
	private OutputStream outputStream = null;

	public byte[] heartbeatMsg = null;
	public long previousTimeMillis = 0;

	public boolean fromStart = true;

	public BaseClient(String serverIP, String strPort) {
		socketAddr = new InetSocketAddress(serverIP, Integer.parseInt(strPort));
		networkState = new RecvStateObject();
		networkState.parser = new NetInfoParser();
		sendQueue = new LinkedBlockingQueue<byte[]>();
	}

	public void start() throws Exception {
		if (networkState.parser == null)
			throw new Exception("BaseClient#networkState.networkParser is not initialized!");

		// assemble heartbeatMsg
		try {
			heartbeatMsg = "{(len=18)TEST0001@@@@@@@@&9}".getBytes("US-ASCII");
		} catch (Exception ex) {
			logger.warn("BaseClient-----1-----{}", ex.getMessage());
		}

		fromStart = true;

		stopFlag = false;
		dataFeedThread = new Thread(this);
		dataFeedThread.start();

		dateSendTherad = new Thread() {
			@Override
			public void run() {
				doSendData();
			}
		};
		dateSendTherad.start();
	}

	public void stop() {
		stopFlag = true;
		dateSendTherad.interrupt();
	}

	public void sendData(byte[] data) {
		synchronized (networkState) {
			try {
				if (data != null)
					sendQueue.put(data);
			} catch (InterruptedException ie) {
				logger.warn("BaseClient-----sendData InterruptedException-----{}", ie.getStackTrace().toString());
			}
			/*
			 * try {
			 * 
			 * // if (data != null && outputStream != null) { // outputStream.write(data);
			 * // } // Modify by xiang at 20160511 // 去掉了 <outputStream != null>
			 * 如果outputStream为空说明连接有问题 // 就让他进Exception,重建连接 if (data != null) {
			 * outputStream.write(data); } } catch (IOException ioe) { isSocketBroken =
			 * true; onConnectStateChange(ConnectionStateListener.CONNECTION_LOST,
			 * "connection broken!"); if (errorLogger != null) {
			 * errorLogger.log(ZDLogger.LVL_ERROR, "BaseClient-----2-----" +
			 * ioe.toString()); } }
			 */
		}
	}

	@Override
	public void run() {
		try {
			doRecvData();
		} catch (Exception ex) {
			// write log
			logger.warn("BaseClient-----3-----{}", ex.toString());
		}
	}

	private void doSendData() {
		while (!stopFlag) {
			try {
				byte[] data = sendQueue.take();
				if (data != null) {
					outputStream.write(data);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			} catch (IOException e) {
				isSocketBroken = true;
				onConnectStateChange(ConnectionStateListener.CONNECTION_LOST, "connection broken!");
				logger.warn("BaseClient-----2-----{}", e.toString());
			} catch (NullPointerException e) {
				e.printStackTrace();
				System.out.println("doSendData NullPointerException");
				isSocketBroken = true;
				onConnectStateChange(ConnectionStateListener.CONNECTION_LOST, "connection broken!");
			}
		}
	}

	public void doRecvData() {
		while (!stopFlag) {
			try {
				if (isSocketBroken) {
					connectHost();
					if (stopFlag)
						break;

					if (isSocketBroken) {
						try {
							Thread.sleep(3000);
						} catch (InterruptedException ie) {
						}
						continue;
					}
				} else {
					long nowTick = System.currentTimeMillis();
					if (nowTick - previousTimeMillis > 30000) {
						// send heartbeat message
						sendData(heartbeatMsg);
						previousTimeMillis = nowTick;
					}
				}

				int readLen = 0;
				byte[] netMsg = networkState.parser.getRawMsg();

				if (netMsg == null) {

					try {
						readLen = networkState.inputStream.read(networkState.buffer);

						if (readLen > 0)
							networkState.parser.AddToParser(networkState.buffer, 0, readLen);
					} catch (Exception ste) {
						// ste.printStackTrace();
					}

				} else {
					onMsgReady(netMsg);
				}
			} catch (Exception ioe) {
				logger.warn("BaseClient-----4-----{}", ioe.toString());

				// Reconnect
				isSocketBroken = true;
				onConnectStateChange(ConnectionStateListener.CONNECTION_LOST, "connection broken!");
			}
		}

		// Exit and release socket resource
		try {
			if (receiveSocket != null) {
				receiveSocket.close();
				receiveSocket = null;
			}
		} catch (IOException ioe) {
			logger.warn("BaseClient-----5-----{}", ioe.toString());
		}
	}

	public void connectHost() {

		closeSocket();

		try {
			receiveSocket = new Socket();

			receiveSocket.setTcpNoDelay(true);
			receiveSocket.setSoLinger(true, 3000);
			receiveSocket.setSoTimeout(10000);

			receiveSocket.connect(socketAddr, 10000);
			networkState.inputStream = receiveSocket.getInputStream();
			outputStream = receiveSocket.getOutputStream();
			isSocketBroken = false;

			onConnectStateChange(ConnectionStateListener.RECONNECTED, "connection reconnected!");
		} catch (SocketTimeoutException st) {

			if (stopFlag)
				return;

			logger.warn("BaseClient-----8-----{}", st.toString());

			isSocketBroken = true;

			onConnectStateChange(ConnectionStateListener.SCOKET_CONNECTIONFAIL, "connection fail!");
		} catch (SocketException se) {
			// write log
			logger.warn("BaseClient-----9-----{}", se.toString());

			isSocketBroken = true;

			onConnectStateChange(ConnectionStateListener.SCOKET_CONNECTIONFAIL, "connection fail!");
		} catch (IOException ioe) {
			// write log
			logger.warn("BaseClient-----10-----{}", ioe.toString());

			ioe.printStackTrace();

			isSocketBroken = true;

			onConnectStateChange(ConnectionStateListener.SCOKET_CONNECTIONFAIL, "connection fail!");
		}

	}

	public void closeSocket() {
		if (receiveSocket != null) {
			try {
				receiveSocket.shutdownInput();
				receiveSocket.shutdownOutput();
			} catch (Exception ex) {
				logger.warn("BaseClient-----6-----{}", ex.toString());
			}

			try {
				receiveSocket.close();
				receiveSocket = null;
			} catch (Exception ex) {
				logger.warn("BaseClient-----7-----{}", ex.toString());
			}
		}
	}

	public abstract void onMsgReady(byte[] rawMsg);

	public abstract void onConnectStateChange(int code, String text);
}
