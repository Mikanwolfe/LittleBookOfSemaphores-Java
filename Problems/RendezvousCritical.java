import java.io.Console;
import java.util.concurrent.Semaphore;

public class RendezvousCritical {
    
    public static final int NUM_THREADS = 8;
    public final Semaphore tickets = new Semaphore(NUM_THREADS);
    public final Semaphore gate = new Semaphore(0);
    public final Semaphore gate2 = new Semaphore(1);
    public final Semaphore lockbox = new Semaphore(1);

    public static int count_a = 0;
    public static int count_b = 0;
    public static int threads_arrived=0;

    public static void main(String[] args) {
        RendezvousCritical rendezvousCritical = new RendezvousCritical();

        Josh[] joshes = new Josh[NUM_THREADS];

        
        for (int i = 0; i < NUM_THREADS; i++) {
            joshes[i] = rendezvousCritical.new Josh();
            Thread t = new Thread(joshes[i], "Josh Number " + i);
            t.start();
        }

        
        //System.out.println("Starting");
        //while (threads_arrived != NUM_THREADS-1) {}
        //rendezvousCritical.gate.release();

        
    }


    class Josh implements Runnable {

        @Override
        public void run() {

            try {


                for (int j = 0; j < 5; j++) {

                    
                    lockbox.acquire();
                    threads_arrived++;
                    System.out.println("TA 1: "+ threads_arrived + " is " + Thread.currentThread().getName());
                    if (threads_arrived == NUM_THREADS) {
                        gate.release();
                        gate2.acquire();
                    }
                    lockbox.release();

                    
                    gate.acquire();
                    gate.release();
                    System.out.println(Thread.currentThread().getName() + " has arrived");

                    for (int i = 0; i < 1000000; i++) {
                        count_a++;
                    }
                    System.out.println( Thread.currentThread().getName() + " counts "+ count_a);

                    lockbox.acquire();
                    threads_arrived--;
                    System.out.println("TA 2: "+ threads_arrived + " is " + Thread.currentThread().getName());
                    if (threads_arrived == 0) {

                        gate.acquire();
                        gate2.release();
                    }
                    lockbox.release();

                    gate2.acquire();
                    gate2.release();


                }
                

                



            } catch (InterruptedException e) {}
            finally {
                //gate.release();
                
            }
            
            
        }

    }




}
