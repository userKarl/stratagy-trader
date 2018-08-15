package com.zd.business.service;

public class SendData2OrderThread implements Runnable{

	private volatile Thread thread;
	
	public void start() {
		thread=new Thread(this);
		thread.start();
	}
	
	public void stop() {
		thread.interrupt();
		thread=null;
	}
	
	@Override
	public void run() {
		while(true) {
			
		}
	}

}
