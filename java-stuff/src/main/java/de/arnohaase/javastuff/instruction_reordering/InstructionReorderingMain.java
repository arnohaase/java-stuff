package de.arnohaase.javastuff.instruction_reordering;

/**
 * @author arno
 */
public class InstructionReorderingMain {
    public static void main(String[] args) throws InterruptedException {
        final A a = new A();
        new Thread(a).start();

        Thread.sleep (1000); // for HotSpot

        while (true) {
            if(a.x != 0) {
                System.out.println("!");
            }
//            if(a.y != 0) {
//                System.out.println("y: " + a.y);
//            }
        }
    }
}


class A implements Runnable {
    public int x;
    public /*volatile*/ int y;

    @Override public void run() {
        while (true) {
            x++;
            y++;
            x--;
            y++;
        }
    }
}