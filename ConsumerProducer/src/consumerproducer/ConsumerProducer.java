/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package consumerproducer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author insomniac
 */
public class ConsumerProducer {
    private static Buffer buffer = new Buffer();
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.execute(new ProducerTask());
        executor.execute(new ConsumerTask());
        executor.shutdown();
    }
    
    private static class ProducerTask implements Runnable
    {

        @Override
        public void run() {
            int i=1;
            while(true)
            {
                try {
                    System.out.println("producer: "+i);
                    buffer.write(i++);
                    Thread.sleep((int)(Math.random()*10000));
                }
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                catch (InterruptedException ex) {
                    Logger.getLogger(ConsumerProducer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
    }
    
    private static class ConsumerTask implements Runnable
    {

        @Override
        public void run() {
            while(true)
            {
                try {
                    System.out.println("Consumer : "+buffer.read());
                    Thread.sleep((int)(Math.random()*10000));
                }
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                catch (InterruptedException ex) {
                    Logger.getLogger(ConsumerProducer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
    }
    
    private static class Buffer
    {
        private static final int CAPACITY = 1;
        private java.util.LinkedList<Integer> queue =
                new java.util.LinkedList<Integer>();
        private static Lock lock = new ReentrantLock();
        private static Condition notEmpty= lock.newCondition();
        private static Condition notFull = lock.newCondition();
        public void write(int value)
        {
            lock.lock();
            try{
                while(queue.size() == CAPACITY)
                {
                    System.out.println("wait for notFull Condition.");
                    notFull.await();
                }
            
                queue.offer(value);
                notEmpty.signal();
            } catch(InterruptedException ex){
                ex.printStackTrace();
            } finally{
                lock.unlock();
            }
        }
        
        public int read()
        {
            int value=0;
            lock.lock();
            try{
                while(queue.isEmpty())
                {
                    System.out.println("wait for the nonEmpty Condition.");
                    notEmpty.await();
                }
                value=queue.remove();
                notFull.signal();
            } catch(InterruptedException ex){
                ex.printStackTrace();
            } finally{
                lock.unlock();
                return value;
            }
        }
        
   }
}
