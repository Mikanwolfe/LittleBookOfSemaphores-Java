import java.util.concurrent.Semaphore;

public class ReaderWritersWithSwitch {

    public final static int NUM_READERS = 5;
    public final static int NUM_WRITERS = 5;

    public static int SharedData;

    public final Semaphore turnstile = new Semaphore(1);
    public final Semaphore readersBlock = new Semaphore(1);
    public final Semaphore roomEmpty = new Semaphore(1);

  //add writers and add light switch as per page 70ish

    public static int readerCount = 0;
    
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

}
