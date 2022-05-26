import java.util.concurrent.Semaphore;


public class Mutex {


    public final Semaphore sem_a = new Semaphore(1);
    public int count = 0;

    public static void main(String[] args) {
        Mutex mutex = new Mutex();

        a _a = mutex.new a();
        b _b = mutex.new b();

        _a.start();
        _b.start();
    }

    class a extends Thread {

        private void doAction(String action) throws InterruptedException {
            System.out.println(action);
            Thread.sleep((int) (Math.random() * 100));
        }

        @Override
        public void run() {
            try {

                sem_a.acquire();
                for (int i = 0; i < 1000000; i++) {
                    count++;
                }
                System.out.println("Count is " + count);
                
                
            } catch(Exception e) {}
            finally {
                sem_a.release();
            }
        }

    }

    class b extends Thread {

        private void doAction(String action) throws InterruptedException {
            System.out.println(action);
            Thread.sleep((int) (Math.random() * 100));
        }

        @Override
        public void run() {
            try {
                sem_a.acquire();
                for (int i = 0; i < 1000000; i++) {
                    count++;
                }
                System.out.println("Count is " + count);

            } catch(Exception e) {}
            finally {
                sem_a.release();
            }

        }
    }
    
}
