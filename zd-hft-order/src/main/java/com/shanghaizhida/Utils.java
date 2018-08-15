package com.shanghaizhida;

import java.nio.ByteBuffer;

public class Utils {

	public static byte[] intToByteArray(final int i) {
		// BigInteger bigInt = BigInteger.valueOf(i);
		// return bigInt.toByteArray();

		ByteBuffer dbuf = ByteBuffer.allocate(4);
		dbuf.putInt(i);

		return dbuf.array();
	}

	public static int byteArrayToInt(byte[] arr) {
		// byte[] arr = { 0x00, 0x01 };

		ByteBuffer wrapped = ByteBuffer.wrap(arr, 0, 4); // big-endian by default

		return wrapped.getInt();
	}
}
