package com.shanghaizhida;

public class CommonFunction {
	public static byte[] utf8StrToNetBytes(String inPut) {
		try {
			byte[] b = inPut.getBytes("UTF-8");
			// System.out.println(b.length);

			StringBuffer sb = new StringBuffer();
			sb.append("{(len=").append(b.length).append(")").append(inPut)
					.append("}");

			return sb.toString().getBytes("UTF-8");
		} catch (Exception ex) {
			// Donothing
			return null;
		}
	}

	public static byte[] asciiStrToNetBytes(String inPut) {
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("{(len=").append(inPut.length()).append(")")
					.append(inPut).append("}");

			return sb.toString().getBytes("US-ASCII");
		} catch (Exception ex) {
			// Donothing
			return null;
		}
	}
}
