package com.shanghaizhida.core.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

/**
 * 解压k线等数据的类
 * 
 * @author xiang <br>
 *         2015年8月13日 下午3:17:34
 * 
 */
public class NetInfoDecomParser {
	private int BYTESIZE = 1024;
	public byte[] buffertemp = new byte[BYTESIZE];
	public byte[] buffer = new byte[BYTESIZE];
	public int MsgLength = -1;

	public int offset = 0;

	private int decomOffSet = 0;

	public byte[] getPackgeInfo(int len) throws Exception {

		MsgLength = byte2int(buffer);

		if (offset < MsgLength + 4)
			return null;
		if (MsgLength < 0)
			return null;

		byte[] myByte = new byte[MsgLength];
		// 将一条完整数据复制出来
		System.arraycopy(buffer, 4, myByte, 0, myByte.length);
		// 移除复制出来的这条数据
		System.arraycopy(buffer, MsgLength + 4, buffer, 0, buffer.length
				- MsgLength - 4);

		offset -= MsgLength + 4;

		myByte = decompress(myByte);

		boolean isContinue = false;

		// Message data part is not enough
		int i = NetInfoParser.NET_HEAD_LEN + 1;
		for (; i < MsgLength; i++) {
			if (myByte[i] == ')') {
				isContinue = true;
				break;
			}
		}

		if (isContinue) {

			// String contentLen = new String(myByte, NetInfoParser.NET_HEAD_LEN, i - NetInfoParser.NET_HEAD_LEN);
			// int contentSize = 0;
			// try{
			// contentSize = Integer.parseInt(contentLen);
			// }catch(NumberFormatException nfe){
			// }

			byte[] msgByte = new byte[getDecomLength() - i];

			System.arraycopy(myByte, i + 1, msgByte, 0, getDecomLength() - i
					- 2);
			// System.arraycopy(Utils.intToByteArray(contentSize), 0, msgByte, 0, NetInfoParser.LEN_PAKCKET_SIZE);

			return msgByte;
		}
		return null;

	}

	public void addToParser(int len) {

		int spaceAvailabeLengths = buffer.length - offset;

		if (spaceAvailabeLengths < len) {

			// //如果不是第一次进while uncompressedData中必然是有数据的,这时候需要先把temp中的数据追加到最后面,offset为下面保存的字节偏移量
			// System.arraycopy(buffertemp, 0, buffer, offset, buffertemp.length);
			// //然后赋值给temp临时保存
			// buffertemp = buffer;
			// //对uncompressedData做扩容,准备下次进while存数据
			// buffer = new byte[buffer.length + BYTESIZE];
			// //将temp中临时保存的数据在复制到已经扩容过的uncompressedData中
			// System.arraycopy(buffertemp, 0, buffer, 0, buffertemp.length);
			// //将temp的大小重新new成1024
			// buffertemp = new byte[BYTESIZE];

			byte[] newBuffer = new byte[BYTESIZE * 2 + buffer.length];
			System.arraycopy(buffer, 0, newBuffer, 0, offset);
			System.arraycopy(buffertemp, 0, newBuffer, offset, len);
			buffer = newBuffer;

		} else {
			System.arraycopy(buffertemp, 0, buffer, offset, len);
		}

		// 存储字节偏移量
		offset += len;
	}

	/**
	 * 取byte的前四位转换为int(数据体长度)
	 * 
	 * @param res
	 *            4位byte数组
	 * @return 转换出来的int
	 */
	private int byte2int(byte[] res) {
		// 一个byte数据左移24位变成0x??000000，再右移8位变成0x00??0000

		// int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00) // | 表示安位或
		// | ((res[2] << 24) >>> 8) | (res[3] << 24);
		// return targets;

		int number = res[0] & 0xFF;
		// "|="按位或赋值。
		number |= ((res[1] << 8) & 0xFF00);
		number |= ((res[2] << 16) & 0xFF0000);
		number |= ((res[3] << 24) & 0xFF000000);
		return number;

	}

	/**
	 * 解压被压缩的数据
	 * 
	 * @author lichengwu
	 * @created 2013-02-07
	 * 
	 * @param data
	 * @return
	 * @throws IOException
	 * @throws DataFormatException
	 */
	public byte[] decompress(byte[] date) throws Exception {

		Inflater decompresser = new Inflater(true);
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
				date);
		InputStream stream = new InflaterInputStream(byteArrayInputStream,
				decompresser);

		byte[] uncompressedData = new byte[BYTESIZE];
		byte[] uncompressedDataTemp = new byte[BYTESIZE];

		int len = 0;
		decomOffSet = 0;
		// 将流中的字符缓存到Temp中
		while ((len = stream.read(uncompressedDataTemp)) != -1) {

			int spaceAvailabeLengths = uncompressedData.length - decomOffSet;

			if (spaceAvailabeLengths < len) {

				// //如果不是第一次进while uncompressedData中必然是有数据的,这时候需要先把temp中的数据追加到最后面,offset为下面保存的字节偏移量
				// System.arraycopy(uncompressedDataTemp, 0, uncompressedData, offset, uncompressedDataTemp.length);
				// //然后赋值给temp临时保存
				// uncompressedDataTemp = uncompressedData;
				// //对uncompressedData做扩容,准备下次进while存数据
				// uncompressedData = new byte[uncompressedData.length + BYTESIZE];
				// //将temp中临时保存的数据在复制到已经扩容过的uncompressedData中
				// System.arraycopy(uncompressedDataTemp, 0, uncompressedData, 0, uncompressedDataTemp.length);
				// //将temp的大小重新new成1024
				// uncompressedDataTemp = new byte[BYTESIZE];

				byte[] newBuffer = new byte[BYTESIZE * 2
						+ uncompressedData.length];
				System.arraycopy(uncompressedData, 0, newBuffer, 0, decomOffSet);
				System.arraycopy(uncompressedDataTemp, 0, newBuffer,
						decomOffSet, len);
				uncompressedData = newBuffer;

			} else {
				System.arraycopy(uncompressedDataTemp, 0, uncompressedData,
						decomOffSet, len);
			}

			// 存储字节偏移量
			decomOffSet += len;
		}

		return uncompressedData;
	}

	public int getDecomLength() {
		return decomOffSet;
	}
}
