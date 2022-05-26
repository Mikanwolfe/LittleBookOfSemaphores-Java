import java.util.concurrent.Semaphore;

public class Rendezvous {
    
    public final Semaphore sem_a = new Semaphore(0);
    public final Semaphore sem_b = new Semaphore(0);

    public static void main(String[] args) {
        Rendezvous rendezvous = new Rendezvous();

        a _a = rendezvous.new a();
        b _b = rendezvous.new b();

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
                
                doAction("Statement A1");
                sem_a.release();
                sem_b.acquire();
                doAction("Statement A2");
            } catch(Exception e) {}
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
                doAction("Statement B1");
                sem_b.release();
                sem_a.acquire();
                doAction("Statement B2");
            } catch(Exception e) {}
        }
    }

}
