/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myconsumerproducer;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

/**
 *
 * @author shabnam
 */
public class MyConsumerProducer {
    private static Buffer buffer = new Buffer();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
             //creating threadpool 
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.execute(new ConsumerTask());
        executor.execute(new ProducerTask());
        executor.shutdown();
    }
    
    //adding integer to buffer
    
    private static class ProducerTask implements Runnable{
        @Override
        public void run(){
            try{
                 int i=1;
                 while(true){
                     System.out.println("Producer writes" + i);
                     buffer.write(i++); // adding value to  buffer
                     Thread.sleep((int)(Math.random()*10000));//putting thread to sleep
     
                 }
                
            }catch(InterruptedException ex){
              ex.printStackTrace();
            }
        }
    }
    
    private static class ConsumerTask implements Runnable{
        @Override
        public void run()
        {
            try{
                while(true){
                   System.out.println("\t\t\t Consumer reads"+ buffer.read());
                   //put thread into sleep
                   Thread.sleep((int)(Math.random()*10000));
                    
                }
            }catch(InterruptedException ex){
                        ex.printStackTrace();
                        }
            }
        }
        
        //inner class for buffer 
        private static class Buffer{
            private static final int CAPACITY = 1;
            private java.util.LinkedList<Integer> queue = 
                    new java.util.LinkedList<Integer>();
            
            //creating new lock
            private static Lock lock = new ReentrantLock();
            
            //creating two conditions
            private static Condition notEmpty = lock.newCondition();
            private static Condition notFull = lock.newCondition();
            
            public void write(int value)
            {
                lock.lock();
                try{
                    while(queue.size()==CAPACITY)
                    {
                        System.out.println("wait for notFUll Condition");
                        notFull.await();
                    }
                    
                    queue.offer(value);
                    notEmpty.signal();//signal notEMpty condition
                }catch(InterruptedException ex){
                    ex.printStackTrace();
                    
                }finally{
                    lock.unlock(); //release the lock
                }
            }
            
            public int read(){
                int value = 0;
                lock.lock();
                try{
                    while(queue.isEmpty()){
                        System.out.println("\t\tWait for notEmpty condition");
                        notEmpty.await();
                    }
                    value=queue.remove();
                    notFull.signal();
                }catch(InterruptedException ex){
                    ex.printStackTrace();
                    
                }finally{
                    lock.unlock();
                    return value;
                }
            }
            
            
            
        }
    }
    
