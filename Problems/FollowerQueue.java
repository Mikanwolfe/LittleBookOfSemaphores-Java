import java.util.concurrent.Semaphore;

public class FollowerQueue {
    
    public final Semaphore followerQueue = new Semaphore(0);
    public final Semaphore leaderQueue = new Semaphore(0);
    public final Semaphore mutex = new Semaphore(1);
    public final Semaphore rendezvous = new Semaphore(0);
    public static int NUM_QUEUE = 2;

    public static int leaders, followers = 0;


    public static void main(String[] args) {

        Leader[] leaders = new Leader[NUM_QUEUE];
        Follower[] followers = new Follower[NUM_QUEUE];

        FollowerQueue followerQueue = new FollowerQueue();
        
        for (int i = 0; i < NUM_QUEUE; i++) {
            leaders[i] = followerQueue.new Leader();
            followers[i] = followerQueue.new Follower();

            Thread t;
            t = new Thread(leaders[i], "Leader #" + i);
            t.start();
            t = new Thread(followers[i], "Follower #" + i);
            t.start();
        }
    }

    class Leader implements Runnable {

        void announce(String action) {
            System.out.println(Thread.currentThread().getName() +  " " +action);

        }

        @Override
        public void run() {
            try {

                for (int i = 0; i < 5; i++) {
                    
                    announce("is looking for a ticket");

                    mutex.acquire();
                    announce("has acquired a ticket!");
                
                    if (followers > 0) {
                        announce("found a follower! Signalling them now...");
                        followers--;
                        followerQueue.release();
                        
                    } else {
                        announce("Could not find a follower :( Sending new ticket and waiting.");
                        leaders++;
                        mutex.release();
                        leaderQueue.acquire();
                    }


                    System.out.println(Thread.currentThread().getName() + "  Dances!");


                    announce("waiting for follower");
                    rendezvous.acquire();
                    announce("sending a new ticket!");
                    mutex.release();
                }
                

            } catch (Exception e) {} 
            finally {}
            
        }

    }

    class Follower implements Runnable {

        void announce(String action) {
            System.out.println(Thread.currentThread().getName() +  " " +action);

        }

        @Override
        public void run() {
            try {

                for (int i = 0; i < 5; i++) {
                    announce("is looking for a ticket");
                    mutex.acquire();
                    announce("has acquired a ticket!");
                

                    if (leaders > 0) {
                        announce("found a leader! Signalling them");
                        leaders--;
                        leaderQueue.release();
                    } else {
                        announce("could not find a leader, waiting...");
                        followers++;
                        mutex.release();
                        followerQueue.acquire();
                    }
 
                    System.out.println(Thread.currentThread().getName() + " Dances!");
                    
                    rendezvous.release();
                    announce("follower has arrived!");
                }

            } catch (Exception e) {} 
            finally {}
            
            
        }

    }


}
