package com.shanghaizhida.core.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.shanghaizhida.Utils;

public class NetInfoParser implements Parser {

	private static Logger logger = LoggerFactory.getLogger(NetInfoParser.class);

	private byte[] buffer_ = null;
	private int msgBeginindex = 0;
	private int spaceAvailabeIndex = 0;
	private int initSize = 0;

	// This flag should be set true for server side because server should not
	// maintain big buffer for each connection
	public boolean isIntendSmallMsg = true;

	public static final int GOOD_PARSER_OFFSET = 8;
	public static final int LEN_PAKCKET_SIZE = 4;

	public NetInfoParser() {
		initSize = 1024 * 32;
		buffer_ = new byte[initSize];
	}

	public NetInfoParser(int bufferSize) {
		initSize = bufferSize;
		buffer_ = new byte[bufferSize];
	}

	public static final String NET_HEAD_STR = "{(len=";
	public static final int NET_HEAD_LEN = 6;

	private MultipileReturnVals returnVals = new MultipileReturnVals();

	@Override
	public void AddToParser(byte[] data, int offset, int count) {
		if (buffer_.length - spaceAvailabeIndex < count) {
			int lengthOfPartMsg = spaceAvailabeIndex - msgBeginindex;
			if (buffer_.length >= lengthOfPartMsg + count) {
				// Shift partial data of current message
				System.arraycopy(buffer_, msgBeginindex, buffer_, 0, lengthOfPartMsg);
				System.arraycopy(data, offset, buffer_, lengthOfPartMsg, count);
				msgBeginindex = 0;
				spaceAvailabeIndex = lengthOfPartMsg + count;
			} else {
				// int i = 0;
				// int contentSize = 0;
				// int packetSize = 0;
				// MultipileReturnVals returnVals = new MultipileReturnVals();
				returnVals.clear();
				getPacketInfo(returnVals);

				// In case of one small message and one big message come at the same time,
				// or first message is big one
				if (returnVals.packetSize < count)
					returnVals.packetSize = count;

				// Array.Resize(ref buffer_, buffer_.Length + packetSize);
				byte[] newBuffer = new byte[buffer_.length + returnVals.packetSize];
				System.arraycopy(buffer_, msgBeginindex, newBuffer, 0, lengthOfPartMsg);
				System.arraycopy(data, offset, newBuffer, lengthOfPartMsg, count);
				spaceAvailabeIndex = lengthOfPartMsg + count;
				buffer_ = newBuffer;
			}
		} else {
			System.arraycopy(data, offset, buffer_, spaceAvailabeIndex, count);
			spaceAvailabeIndex += count;
		}
	}

	@Override
	public byte[] getRawMsg() {

		// Message length part is not enough
		int dataLength = spaceAvailabeIndex - msgBeginindex;
		if (dataLength < NET_HEAD_LEN)
			return null;

		// int i = 0;
		// int contentSize = 0;
		// int packetSize = 0;
		// MultipileReturnVals returnVals = new MultipileReturnVals();
		returnVals.clear();
		if (!getPacketInfo(returnVals))
			return null;

		if (dataLength < returnVals.packetSize)
			return null;

		// rawMsg = new byte[GOOD_PARSER_OFFSET + contentSize];
		byte[] rawMsg = getByteArr(GOOD_PARSER_OFFSET + returnVals.contentSize);
		System.arraycopy(buffer_, returnVals.index + 1, rawMsg, GOOD_PARSER_OFFSET, returnVals.contentSize);
		// System.arraycopy(BitConverter.GetBytes(returnVals.contentSize), rawMsg,
		// LEN_PAKCKET_SIZE);
		System.arraycopy(Utils.intToByteArray(returnVals.contentSize), 0, rawMsg, 0, LEN_PAKCKET_SIZE);

		msgBeginindex += returnVals.packetSize;

		if (isIntendSmallMsg && returnVals.packetSize > initSize) {
			byte[] newBuffer = new byte[initSize];
			// Shift partial data of current message
			int lengthOfPartMsg = spaceAvailabeIndex - msgBeginindex;
			System.arraycopy(buffer_, msgBeginindex, newBuffer, 0, lengthOfPartMsg);
			msgBeginindex = 0;
			spaceAvailabeIndex = lengthOfPartMsg;
			buffer_ = newBuffer;
		}

		return rawMsg;
	}

	public boolean getPacketInfo(MultipileReturnVals returnVals) {
		boolean isContinue = false;
		// Message data part is not enough
		int i = msgBeginindex + NET_HEAD_LEN + 1;
		for (; i < spaceAvailabeIndex; i++) {
			if (buffer_[i] == ')') {
				isContinue = true;
				break;
			}
		}

		if (isContinue) {
			int lenBeginPos = msgBeginindex + NET_HEAD_LEN;
			// String contentLen = System.Text.ASCIIEncoding.ASCII.GetString(buffer_,
			// lenBeginPos, i - lenBeginPos);
			String contentLen = new String(buffer_, lenBeginPos, i - lenBeginPos);
			// contentSize = Convert.ToInt32(contentLen);
			// Network data get bad
			boolean ret = false;
			try {
				returnVals.contentSize = Integer.parseInt(contentLen);
				ret = true;
			} catch (NumberFormatException nfe) {
			}

			// if (!int.TryParse(contentLen, out contentSize))
			if (!ret) {
				int netDataLen = spaceAvailabeIndex - msgBeginindex;
				// String badMsg = System.Text.ASCIIEncoding.ASCII.GetString(buffer_,
				// msgBeginindex, netDataLen);
				String badMsg = new String(buffer_, msgBeginindex, netDataLen);
				logger.warn("Bad message data: {}", badMsg);

				int possibleNextGoodIdx = badMsg.indexOf(NET_HEAD_STR, NET_HEAD_LEN);
				if (possibleNextGoodIdx > -1) {
					msgBeginindex += possibleNextGoodIdx;
				} else {
					// Shift data to avoid buffer overflow
					System.arraycopy(buffer_, msgBeginindex, buffer_, 0, netDataLen);
					msgBeginindex = 0;
					spaceAvailabeIndex = netDataLen;
				}

				returnVals.packetSize = -1;
				returnVals.index = -1;
				return false;
			}

			returnVals.packetSize = i - msgBeginindex + 1 + returnVals.contentSize + 1;
			returnVals.index = i;
			return true;
		} else {
			// Message length part is not enough
			return false;
		}
	}

	public byte[] getByteArr(int size) {
		return new byte[size];
	}

	class MultipileReturnVals {
		public int packetSize;
		public int contentSize;
		public int index;

		public void clear() {
			packetSize = 0;
			contentSize = 0;
			index = 0;
		}
	}
}
