import java.util.concurrent.Semaphore;
public class DiningSavages {


    public final static int NUM_TAKODACHIS = 5;
    public static int ServingsCount = NUM_TAKODACHIS;
    public final Semaphore PotServings = new Semaphore(NUM_TAKODACHIS);
    public final Semaphore PotEmpty = new Semaphore(0);
    public final Semaphore PotFull = new Semaphore(0);

    public final Semaphore mutex = new Semaphore(1);

    public static void main(String[] args) {
        DiningSavages diningSavages = new DiningSavages();

        Thread t;

        for (int i = 0; i < 100; i++) {
            t = new Thread(diningSavages.new Savage(), "Savage " + i);
            t.start();
        }

        for (int i = 0; i < 20; i++) {
            t = new Thread(diningSavages.new Cook(), "Cook " + i);
            t.start();
        }



    }


    public class Cook implements Runnable {
        void announce(String action) {
            System.out.println(Thread.currentThread().getName() +  " " + action);
        }


        @Override
        public void run() {
            try {

                PotEmpty.acquire();
                announce("Aight more tako coming ri' up!");
                PotServings.release(NUM_TAKODACHIS);
                PotFull.release();
                
            } catch(Exception e) {} 
            finally {}
            
        }

    }

    public class Savage implements Runnable {
        void announce(String action) {
            System.out.println(Thread.currentThread().getName() +  " " + action);
        }


        @Override
        public void run() {
            try {

                
                
                PotServings.acquire();
                announce("Consuming a takodachi!");

                mutex.acquire();
                ServingsCount--;
                if (ServingsCount == 0) {
                    announce("POT EMPTY! COOK!");
                    PotEmpty.release();
                    PotFull.acquire();
                    ServingsCount = NUM_TAKODACHIS;
                }

                mutex.release();
                
            } catch(Exception e) {} 
            finally {}
            
        }

    }

}