package com.zd.common.utils;

import java.util.Random;

public class RandomCode {

	public static String getCode(int count){
		String code="";
		Random random=new Random();
		for(int i=0;i<count;i++){
			code+=random.nextInt(9);
		}
		return code;
	}	
}
