package com.shanghaizhida.core.parser;

public interface Parser {

	void AddToParser(byte[] data, int offset, int count);

	byte[] getRawMsg();
}
