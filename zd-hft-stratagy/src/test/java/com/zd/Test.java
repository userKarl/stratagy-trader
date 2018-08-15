package com.zd;

import java.util.concurrent.ConcurrentHashMap;

import com.zd.business.entity.Stratagy;

public class Test {

	public static void main(String[] args) {
		for(int i=0;i<10;i++) {
			try {
				
				if(i==5) {
					throw new RuntimeException();
				}else {
					System.out.println(i);
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			} 
		}
	}
}
