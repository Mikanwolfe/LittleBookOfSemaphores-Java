import java.util.concurrent.Semaphore;

public class BasicSynchronisation {
    
    final Semaphore sem_a = new Semaphore(0);
    final Semaphore sem_b = new Semaphore(1);
    final Semaphore mink = new Semaphore(0);

    public static void main(String[] args) {

        BasicSynchronisation bsync = new BasicSynchronisation();
        a _a = bsync.new a();
        b _b = bsync.new b();

        Thread t;

        for (int i = 0; i < 5; i++) {
            try {
                System.out.println("Starting again!");
                t = new Thread(_a, "thread a");
                t.start();
                t = new Thread(_b, "thread b");
                t.start();
            } //catch (InterruptedException e) {} 
            finally {
            }
            
        }

        for (int i = 0; i < 5; i++) {
            bsync.mink.release();
        }
        

    }

    class a implements Runnable {


        @Override
        public void run() {
            try {
                mink.acquire();
                sem_b.acquire();
                System.out.println("Josh has had lunch!");
                sem_a.release();
            } catch (Exception e) {}
            
            
        }
    }

    class b implements Runnable {

        @Override
        public void run() {
            try {
                sem_a.acquire();
                System.out.println("B1 has had lunch!");
                sem_b.release();
            } catch (Exception e) {}

            
            
        }
        
    }




}
