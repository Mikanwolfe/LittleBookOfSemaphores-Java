import java.util.concurrent.Semaphore;

public class CigaretteSmokers {

    public final Semaphore agentSem = new Semaphore(0);
    public final Semaphore tobacco = new Semaphore(0);
    public final Semaphore paper = new Semaphore(0);
    public final Semaphore match = new Semaphore(0);


    public final Semaphore tobaccoSem = new Semaphore(0);
    public final Semaphore paperSem = new Semaphore(0);
    public final Semaphore matchSem = new Semaphore(0);

    public static boolean  tobaccoFree = false;
    public static boolean  paperFree = false;
    public static boolean  matchFree = false;

    public static boolean finished = true;
    public final Semaphore finishedSem = new Semaphore(1);

    public final Semaphore mutex = new Semaphore(1);

    public static void main(String[] args) {
        CigaretteSmokers cigaretteSmokers = new CigaretteSmokers();

        Thread t;
        t = new Thread(cigaretteSmokers.new AgentA(), "Agent A");
        t.start();
        t = new Thread(cigaretteSmokers.new AgentB(), "Agent B");
        t.start();
        t = new Thread(cigaretteSmokers.new AgentC(), "Agent C");
        t.start();

        t = new Thread(cigaretteSmokers.new MatchKeeper(), "Match Broker:");
        t.start();
        t = new Thread(cigaretteSmokers.new TobaccoKeeper(), "Tobacco Broker:");
        t.start();
        t = new Thread(cigaretteSmokers.new PaperKeeper(), "Paper Broker:");
        t.start();

        t = new Thread(cigaretteSmokers.new SmokerMatches(), "Match Smoker:");
        t.start();
        t = new Thread(cigaretteSmokers.new SmokerTobacco(), "Tobacco Smoker:");
        t.start();
        t = new Thread(cigaretteSmokers.new SmokerPaper(), "Paper Smoker:");
        t.start();


        try {
            for (int i = 0; i < 3; i++) {
                cigaretteSmokers.agentSem.release();
                System.out.println("-- We done? Let's go again");
                Thread.sleep(1000);
                
            }
            finished = false;
        } catch (Exception e) {}
        finally {}
        
    }

    class TobaccoKeeper implements Runnable {

        void announce(String action) {
            System.out.println(Thread.currentThread().getName() +  " " + action);
        }

        @Override
        public void run() {
            try {
                while(finished) {
                    
                    tobacco.acquire();
                    mutex.acquire();
                    announce("Tobacco Acquired");
                    if (paperFree) {
                        paperFree = false;
                        announce("Hey match, for you.");
                        matchSem.release();
                    } else if (matchFree) {
                        matchFree = false;
                        announce("Hey paper, for you.");
                        paperSem.release();
                    } else {
                        announce("Damn I guess no one wants my tobacco then.");
                        tobaccoFree = true;
                    }
                    mutex.release();

                }
            } catch (Exception e) {}
            finally {}
            
        }

    }

    class PaperKeeper implements Runnable {

        void announce(String action) {
            System.out.println(Thread.currentThread().getName() +  " " + action);
        }

        @Override
        public void run() {
            try {
                while(finished) {
                    paper.acquire();
                    mutex.acquire();
                    announce("Paper Acquired");
                    if (tobaccoFree) {
                        tobaccoFree = false;
                        announce("Hey match, for you.");
                        matchSem.release();
                    } else if (matchFree) {
                        matchFree = false;
                        announce("Hey tobacco, for you.");
                        tobaccoSem.release();
                    } else {
                        announce("Damn I guess no one wants my paper then.");
                        paperFree = true;
                    }
                    mutex.release();

                }
            } catch (Exception e) {}
            finally {}
            
        }

    }

    class MatchKeeper implements Runnable {

        void announce(String action) {
            System.out.println(Thread.currentThread().getName() +  " " + action);
        }

        @Override
        public void run() {
            try {
                while(finished) {
                    match.acquire();
                    mutex.acquire();
                    announce("Match Acquired");
                    if (paperFree) {
                        paperFree = false;
                        announce("Hey tobacco, for you.");
                        tobaccoSem.release();
                    } else if (tobaccoFree) {
                        tobaccoFree = false;
                        announce("Hey paper, for you.");
                        paperSem.release();
                    } else {
                        announce("Damn I guess no one wants my match then.");
                        matchFree = true;
                    }
                    mutex.release();

                }
            } catch (Exception e) {}
            finally {}
            
        }

    }
    
    class SmokerMatches implements Runnable {

        void announce(String action) {
            System.out.println(Thread.currentThread().getName() +  " " + action);
        }

        @Override
        public void run() {
            try {
                while(finished) {
                    // tobacco.acquire();
                    // announce("tobacco? thanks. have some paper?");
                    // paper.acquire();
                    // announce("paper? thanks. time for a smoke.");

                    matchSem.acquire();
                    announce("damn! that's for me? thank you. time for a smoke");
                    
                }
            } catch (Exception e) {}
            finally {}
            
        }

    }

    class SmokerTobacco implements Runnable {

        void announce(String action) {
            System.out.println(Thread.currentThread().getName() +  " " + action);
        }

        @Override
        public void run() {
            try {
                while(finished) {
                    // match.acquire();
                    // announce("match? thanks. have some paper?");
                    // paper.acquire();
                    // announce("paper? thanks. time for a smoke.");

                    tobaccoSem.acquire();
                    announce("damn! that's for me? thank you. time for a smoke");
                    
                }
            } catch (Exception e) {}
            finally {}
            
        }

    }

    class SmokerPaper implements Runnable {

        void announce(String action) {
            System.out.println(Thread.currentThread().getName() +  " " + action);
        }

        @Override
        public void run() {
            try {
                while(finished) {
                    // tobacco.acquire();
                    // announce("tobacco? thanks. have a match?");
                    // match.acquire();
                    // announce("match? thanks. time for a smoke");

                    paperSem.acquire();
                    announce("damn! that's for me? thank you. time for a smoke");
                    
                }
            } catch (Exception e) {}
            finally {}
            
        }

    }

    class AgentA implements Runnable {

        void announce(String action) {
            System.out.println(Thread.currentThread().getName() +  " " + action);
        }

        @Override
        public void run() {
            try {
                while(finished) {
                    agentSem.acquire();
                    announce("psst. tobacco.");
                    tobacco.release();
                    announce("psst. paper.");
                    paper.release();
                    
                }
            } catch (Exception e) {}
            finally {}
            
        }
        
    }

    class AgentB implements Runnable {

        void announce(String action) {
            System.out.println(Thread.currentThread().getName() +  " " + action);
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 1; i++) {
                    agentSem.acquire();
                    announce("psst. paper.");
                    paper.release();
                    announce("psst. match.");
                    match.release();
                }
            } catch (Exception e) {}
            finally {}
            
        }
        
    }


    class AgentC implements Runnable {

        void announce(String action) {
            System.out.println(Thread.currentThread().getName() +  " " + action);
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 1; i++) {
                    agentSem.acquire();
                    announce("psst. match.");
                    match.release();
                    announce("psst. tobacco.");
                    tobacco.release();
                }
            } catch (Exception e) {}
            finally {}
            
        }
        
    }
}