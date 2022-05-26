import java.util.Random;
import java.util.concurrent.Semaphore;

public class PreloadedTurnstile {


    public final Semaphore gate_one = new Semaphore(0);
    public final Semaphore gate_two = new Semaphore(NUMBER_MINKS);
    public final Semaphore lockbox = new Semaphore(1);
    public static int carriedMinks = 0;

    public static final int NUMBER_MINKS = 500;
    public static void main (String[] args) {

        PreloadedTurnstile preloadedTurnstile = new PreloadedTurnstile();

        BusyMink[] minks = new BusyMink[NUMBER_MINKS];

        for (int i = 0; i < NUMBER_MINKS; i++) {
            BusyMink aMink = preloadedTurnstile.new BusyMink();
            Thread t = new Thread(aMink, "Busy Mink #" + i);
            t.start();
        }



    }


    class BusyMink implements Runnable{

        @Override
        public void run() {
            try {

                for (int k = 0; k < 2; k++) {
                    lockbox.acquire();
                    carriedMinks++;
                    System.out.println(Thread.currentThread().getName() + " is waiting patiently at gate 1!");
                    if (carriedMinks == NUMBER_MINKS) {
                        System.out.println(Thread.currentThread().getName() + " is unlocking gate 1!");
                        for (int i = 0; i < NUMBER_MINKS; i++) {
                            gate_one.release();
                            gate_two.acquire();
                        }
                        
                        
                    }
                    lockbox.release();

                    gate_one.acquire();
                    gate_one.release();

                    System.out.println(Thread.currentThread().getName() + " is being busy!");
                    Thread.sleep((int) (Math.random() * 100));

                    

                    lockbox.acquire();
                    carriedMinks--;
                    System.out.println(Thread.currentThread().getName() + " is waiting patiently at gate 2!");
                    if (carriedMinks == 0) {
                        System.out.println(Thread.currentThread().getName() + " is unlocking gate 2!");
                        for (int i = 0; i < NUMBER_MINKS; i++) {
                            gate_two.release();
                            gate_one.acquire();
                        }
                        
                    }
                    lockbox.release();

                    gate_two.acquire();
                    gate_two.release();
                    System.out.println(Thread.currentThread().getName() + " is heading to gate 1!");

                }
                



            } catch (Exception e) {}
        }

    }
    
}
