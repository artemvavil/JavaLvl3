package homework4;

public class Threads {
    static volatile char firstLetter = 'A';
    static Object obj = new Object();

    static class CountThread implements Runnable {
        private char currentLetter;
        private char nextLetter;
        final int COUNT = 5;

        public CountThread(char currentLetter, char nextLetter) {
            this.currentLetter = currentLetter;
            this.nextLetter = nextLetter;
        }

        @Override
        public void run() {
            for (int i = 0; i < COUNT; i++) {
                synchronized (obj) {
                    try {
                        while (firstLetter != currentLetter)
                            obj.wait();
                        System.out.print(currentLetter);
                        firstLetter = nextLetter;
                        obj.notifyAll();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String[] args) {

        new Thread(new CountThread('A', 'B')).start();
        new Thread(new CountThread('B', 'C')).start();
        new Thread(new CountThread('C', 'A')).start();
    }
}

