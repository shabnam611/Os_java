/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myaccountwithsyncusinglock;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

/**
 *
 * @author shabnam
 */
public class MyaccountWithSyncUsingLock{
    private static Account account = new Account();
    
    public static void main(String[] args){
        ExecutorService executor = Executors.newCachedThreadPool();
        //create and launch threads 
        for(int i=0;i<100;i++)
        {
            executor.execute(new AddAPennyTask());
        }
        executor.shutdown();
        while(!executor.isTerminated()){
    }
        System.out.println("What is balance ?"+ account.getBalance());
  
}
    public static class AddAPennyTask implements Runnable{
        @Override
        public void run(){
            account.deposit(1);
        }
    }
    
    public static class Account{
        private static Lock lock = new ReentrantLock();
        private int balance = 0;
        
        public int getBalance(){
            return balance;
        }
        
        public void deposit(int amount){
            lock.lock();
            try{
                int newBalance=balance + amount;
                Thread.sleep(5);
                balance = newBalance;
            }catch(InterruptedException ex){
        }finally{
                lock.unlock();
            }
    }
   }
    
}
