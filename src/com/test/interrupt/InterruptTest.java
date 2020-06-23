package com.test.interrupt;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.plaf.basic.BasicBorders.ToggleButtonBorder;

import org.junit.Test;

public class InterruptTest {
	
	public static void main(String[] args) throws InterruptedException
    {
        TestThread tt = new TestThread("test");
        tt.start();
        System.out.println(tt.getName()+" interrupt before "+tt.isInterrupted());
        tt.interrupt();//中断sleep将会清空中断状态
        Thread.sleep(2000);//等待test线程
        System.out.println(tt.getName()+" interrupt after "+tt.isInterrupted());

    }
	
	@Test
    public void testLock() {  
        Thread t1 = new Thread(new RunIt());  
        Thread t2 = new Thread(new RunIt());  
          
        t1.start();  
        t2.start();  
        t2.interrupt();  
          
    } 
}

class TestThread extends Thread
{
    public TestThread(String name){
        setName(name);
    }
    public void run()
    {
        try{
            Thread.sleep(10);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}


class RunIt implements Runnable{  
	  
    private static Lock lock = new ReentrantLock();  
    @Override  
    public void run() {  
        try {  
            runJob();  
        } catch (InterruptedException e) {  
            System.out.println(Thread.currentThread().getName()+" Interrrupted from runJob.");  
        }  
    }  
      
    private void runJob() throws InterruptedException{  
//        lock.lockInterruptibly();  
        lock.lock();  
        System.out.println(Thread.currentThread().getName()+" 到此一游....");  
        try{  
            System.out.println(Thread.currentThread().getName() + " running");  
            System.out.println(Thread.currentThread().getName() + " finished");  
            TimeUnit.SECONDS.sleep(3);  
            System.out.println(Thread.currentThread().getName() + " afterSleep");
              
        }catch(InterruptedException e){  
            System.out.println(Thread.currentThread().getName() + " interrupted");  
        }finally{  
            lock.unlock();  
        }  
    }  
}  