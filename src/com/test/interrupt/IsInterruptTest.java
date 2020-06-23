package com.test.interrupt;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IsInterruptTest {
	
	
	private static Logger logger = LoggerFactory.getLogger(IsInterruptTest.class);
	
	public static void main(String[] args) {
		Thread t=new Thread(new Runnable() {
			
			@Override
			public void run() {
				while (true) {
					if (Thread.currentThread().isInterrupted()) {
						logger.info("thread is interrupted");
						break;
					}
					try {
						TimeUnit.SECONDS.sleep(1);
					} catch (InterruptedException e) {
						logger.info("catch interrupted exceptin");
						Thread.currentThread().interrupt();
					}
					
				}
				System.out.println("Iteration finished");
			}
		});
		t.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		t.interrupt();
		
	}
}
