import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;


public class ReaderWritersProblem {

    public final static int NUM_READERS = 5;
    public final static int NUM_WRITERS = 5;

    public static int SharedData;

    public final Semaphore turnstile = new Semaphore(1);
    public final Semaphore readersBlock = new Semaphore(1);
    public final Semaphore roomEmpty = new Semaphore(1);

    public static Reader[] readers = new Reader[NUM_READERS];
    public static Writer[] writers = new Writer[NUM_WRITERS];

    public static int readerCount = 0;
    

    public static void main(String[] args) {
        ReaderWritersProblem readerWritersProblem = new ReaderWritersProblem();

        for (int i = 0; i < NUM_READERS; i++) {
            readers[i] = readerWritersProblem.new Reader();
            writers[i] = readerWritersProblem.new Writer();

            Thread t;

            
            t = new Thread(writers[i], "Writer #" + i);
            t.start();
            t = new Thread(readers[i], "Reader #" + i);
            t.start();
        }
        
    }

    class LightSwitch {
        int counter;
        Semaphore mutex;

        LightSwitch() {
            this.counter = 0;
            this.mutex = new Semaphore(1);
        }

        public void lock(Semaphore light) {
            try {
                mutex.acquire();
                counter++;
                if (counter == 1) {
                    // the first thread that entires acquires the lock
                    light.acquire();
                }
                mutex.release();

            } catch (Exception e) {}
        }

        public void unlock(Semaphore light) {
            try {
                mutex.acquire();
                counter--;
                if (counter == 0) {
                    // the last thread should unlock
                    light.release();
                }
                mutex.release();
            } catch(Exception e) {}
        }
    }

    class Reader implements Runnable {
        void announce(String action) {
            System.out.println(Thread.currentThread().getName() +  " " +action);

        }
        

        @Override
        public void run() {
            
            try {
                for (int i = 0; i < 5; i++) {
                    //Thread.sleep((int)(Math.random() * 50));

                    turnstile.acquire();
                    turnstile.release();

                    readersBlock.acquire();
                    readerCount++;
                    if (readerCount == 1) {
                        roomEmpty.acquire();
                    }
                    readersBlock.release();
                    

                    announce(Integer.toString(SharedData));

                    readersBlock.acquire();
                    readerCount--;
                    if (readerCount == 0) {
                        roomEmpty.release();
                    }
                    readersBlock.release();



                }
            } catch (Exception e) {}
            finally {}

            
        }


    }

    class Writer implements Runnable {
        void announce(String action) {
            System.out.println(Thread.currentThread().getName() +  " " +action);

        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 5; i++) {
                    //Thread.sleep((int)(Math.random() * 50));
                    
                    turnstile.acquire();
                    roomEmpty.acquire();
                    
                    SharedData =  (int)(Math.random() * 50);
                    announce("Writing " + SharedData);
                    turnstile.release();
                    roomEmpty.release();
                    

                }
            } catch (Exception e) {}
            finally {}
            
        }

    }
}
