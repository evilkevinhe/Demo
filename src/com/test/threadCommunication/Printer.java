package com.test.threadCommunication;

public class Printer {
	private int flag = 1;
	public void print1() throws InterruptedException {
		synchronized(this) {
			while(flag != 1) {
				this.wait(); //当前线程等待
			}
				System.out.print("黑");
				System.out.print("马");
				System.out.print("程");
				System.out.print("序");
				System.out.print("员");
				System.out.print(Thread.currentThread().getName());
				System.out.print("\r\n");
				flag = 2;
				this.notifyAll();
		}
	}
	public void print2() throws InterruptedException {
		synchronized(this) {
			while(flag != 2) {
				this.wait(); //线程2在此等待
			}
			System.out.print("传");
			System.out.print("智");
			System.out.print("播");
			System.out.print("客");
			System.out.print(Thread.currentThread().getName());
			System.out.print("\r\n");
			flag = 3;
			this.notifyAll();
		}
	}
	public void print3() throws InterruptedException {
		synchronized(this) {
			while(flag != 3) {
				this.wait(); //线程3在此等待,if语句是在哪里等待,就在哪里起来
				//while循环是循环判断,每次都会判断标记
			}
			System.out.print("i");
			System.out.print("t");
			System.out.print("h");
			System.out.print("e");
			System.out.print("i");
			System.out.print("m");
			System.out.print("a");
			System.out.print(Thread.currentThread().getName());
			System.out.print("\r\n");
			flag = 1;
			this.notifyAll();
		}
	}
}
