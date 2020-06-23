package com.test.threadCommunication;

public class Service {
	private volatile boolean flag = false; //这是一个控制sub和mian的开关，加关键字volatile保证不同的线程看到的flag值是最新的
	public synchronized void sub(){
		while(flag) {  //因为flag初始值是false，所以当子线程获取锁进入时不会等待，会直接输出打印
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		for(int i=0;i<5;i++) {
			System.out.println("子线程运行  "  +i);
		}
		flag = true;  //打印完成后，应当切换到main，同时应当唤醒正在等待的主线程
		this.notifyAll(); //子线程再次试图进入sub时，flag为true，就会进入等待，切换到了main中
	}
	
	public synchronized void main() {
		while(!flag) {  //flag初始值是false，因此，即使主线程先获取锁也会进入等待状态，并释放锁，一直到sub执行完后，flag变为true，同时，主线程被唤醒
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//主线程被唤醒后，开始打印输出
		
		for(int i=0;i<5;i++) {
			System.out.println("main线程运行  "  +i);
		}
		
		flag = false;  //然后又切换到sub，并唤醒等待的子线程
		this.notifyAll();
	}
	
	public static void main(String[] args) throws InterruptedException {
		
		final Service service = new Service();
		
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
				for(int i=0;i<5;i++) {
					service.sub();         //多次试图运行子线程
				}
			}
		};
		
		Thread thread = new Thread(r);
		thread.start();
		
		for(int i=0;i<5;i++) {
			service.main();   //和子线程竞争锁
		}
	}
}

