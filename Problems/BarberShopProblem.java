import java.util.concurrent.Semaphore;



public class BarberShopProblem {

    public final static int N_CHAIRS = 5;
    public static int n = 0;
    public final static Semaphore mutex = new Semaphore(1);

    public final static Semaphore customer = new Semaphore(0);
    public final static Semaphore barber = new Semaphore(0);
    public final static Semaphore customerDone = new Semaphore(0);
    public final static Semaphore barberDone = new Semaphore(0);
    public final static Semaphore turnstile = new Semaphore(1);

    
    public static void main(String[] args) {
        BarberShopProblem barberShopProblem = new BarberShopProblem();

        Thread t;

        for (int i = 0; i < 20; i++) {
            t = new Thread(barberShopProblem.new Customer(), "Customer #" + i);
            t.start();
        }
        t = new Thread(barberShopProblem.new Barber(), "Barber");
        t.start();
     } 

        
    class Barber implements Runnable {

        void announce(String action) {
            System.out.println(Thread.currentThread().getName() +  " " + action);
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 20; i++) {
                    barber.acquire();
                    announce("Welcome welcome! Please take a seat.");
                    customer.release();
                    barberDone.release();
                    announce("Your hair has been cut! Thank-you for your patronage.");

                }
            } catch (Exception e) {}
            finally {}   
        }
    }

    class Customer implements Runnable {

        void announce(String action) {
            System.out.println(Thread.currentThread().getName() +  " " + action);
        }

        void waitRand() {
            try {
                Thread.sleep((int)(Math.random()*100));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 1; i++) {
                    mutex.acquire();

                    turnstile.acquire();
                    turnstile.release();

                    if (n > N_CHAIRS) {
                        announce("The barbershop is full. I will lock the turnstile");
                        mutex.release();
                        turnstile.acquire();
                    } else {
                        announce("I will take a seat.");
                        n++;
                    }
                    mutex.release();
                    announce("Mr Barber! A haircut, the usual!");
                    mutex.acquire();
                    n--;
                    if (n < N_CHAIRS) {
                        turnstile.release();
                    }
                    
                    mutex.release();
                    barber.release();
                    announce("Let me know where to sit.");
                    customer.acquire();
                    // on the chair
                    announce("Alright, Please let me know when you are done.");
                    barberDone.acquire();
                    
                    announce("Thank you! I will leave now!");

                    
                }
                



                
            } catch (Exception e) {}
            finally {}   
        }
    }
}
