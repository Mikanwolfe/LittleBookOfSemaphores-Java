import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class BasicProducerConsumer {
    public final static int NUM_PRODUCERS = 8;
    public static final int NUM_CONSUMERS = 8;
    public final static int QUEUE_SIZE = 3;

    public static Queue<Integer> queue = new LinkedList<Integer>();
    public final Semaphore mutex = new Semaphore(1);

    public final Semaphore items = new Semaphore(0);
    public final Semaphore spaces = new Semaphore(QUEUE_SIZE);

    public static Producer[] producers = new Producer[NUM_PRODUCERS];
    public static Consumer[] consumers = new Consumer[NUM_CONSUMERS];




    public static void main(String[] args) {
        BasicProducerConsumer basicProducerConsumer = new BasicProducerConsumer();

        for (int i = 0; i < NUM_PRODUCERS; i++) {
            producers[i] = basicProducerConsumer.new Producer();
            consumers[i] = basicProducerConsumer.new Consumer();

            Thread t;

            t = new Thread(producers[i], "Producer #" + i);
            t.start();
            t = new Thread(consumers[i], "Consumer #" + i);
            t.start();
        }
        


    }

    class Producer implements Runnable {
        void announce(String action) {
            System.out.println(Thread.currentThread().getName() +  " " +action);

        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 3; i++) {

                    

                    spaces.acquire();
                    mutex.acquire();
                    announce("trying to add item to queue, queue status ->" + queue.toString()  );

                    int item = (int)(Math.random() * 50);
                    

                    queue.add(item);
                    announce("added " + item + " to the queue" );
                    
                    mutex.release();
                    items.release();

                    



                }
            }  catch (Exception e) {}
            finally {}
            
            
        }

    }

    class Consumer implements Runnable {
        void announce(String action) {
            System.out.println(Thread.currentThread().getName() +  " " +action);

        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 3; i++) {

                    announce("trying to acquiire items");
                    items.acquire();
                    mutex.acquire();
                    announce("trying to remove item from queue, queue status ->" + queue.toString() );
                    int item = queue.remove();
                    announce("removed " + item + " from the queue" );
                    mutex.release();
                    spaces.release();
                }
            }  catch (Exception e) {}
            finally {}
            
        }

    }


    
}
