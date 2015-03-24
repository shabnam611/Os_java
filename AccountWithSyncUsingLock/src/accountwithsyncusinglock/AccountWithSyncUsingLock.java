/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package accountwithsyncusinglock;

/**
 *
 * @author shabnam
 */
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class AccountWithSyncUsingLock {

    /**
     * @param args the command line arguments
     */
    private static Account account = new Account();
    public static void main(String[] args) {
        // TODO code application logic here
        
        ExecutorService executor = Executors.newCachedThreadPool();
        //create & launch 100 threads
        for(int i=0;i<100;i++)
        {
            executor.execute(new AddAPennyTask());
        }
        
        executor.shutdown();
        
        //wait untill all task is finished 
        
        while(!executor.isTerminated()){
        
    }
        System.out.println("What is Balance?" + account.getBalance());
    }
        
        //A thread for adding a penny into account
        public static class AddAPennyTask implements Runnable{
            @Override
            public void run(){
                account.deposit(1);
            }
        }
        
        //inner class for account
        
        public static class Account{
            private static Lock lock = new ReentrantLock(); // it creates a lock
            private int balance = 0;
            
            public int getBalance(){
                return balance;
            }
            
            public void deposit(int amount){
                lock.lock(); // acquire the lock
                
                try{
                    int newBalance = balance + amount; 
                    Thread.sleep(5);
                    
                    balance = newBalance;
                }catch(InterruptedException ex){
                    
                }
                finally{
                    lock.unlock(); // release the lock
                }
                    
                }
            }
        }
    
