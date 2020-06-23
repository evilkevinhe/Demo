package com.test.threadCommunication;

public class NotifyAll {
	
	public static void main(String[] args) {
		
		final Printer p = new Printer();
		
		new Thread() {
			public void run() {
				while(true) {
					try {
						p.print1();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
		
		new Thread() {
			public void run() {
				while(true) {
					try {
						p.print2();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
		
		new Thread() {
			public void run() {
				while(true) {
					try {
						p.print3();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
}
