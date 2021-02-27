package z21Drive.testing;

import z21Drive.LocoAddressOutOfRangeException;
import z21Drive.Z21;
import z21Drive.actions.Z21ActionSetLocoFunction;

/**
 * Used to test if setLocoFunction works.
 * If it's working properly, loco keeps it's headlights flashing.
 * @see Z21ActionSetLocoFunction
 * @author grizeldi
 */
public class ListenOnZ21 implements Runnable{
    private boolean exit;

    public static void main(String [] args){
        new Thread(new ListenOnZ21()).start();
    }

    private ListenOnZ21(){
        new Thread(() -> {
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            exit = true;
        }).start();
    }

    @Override
    public void run() {
        Z21 z21 = Z21.instance;
        while (!exit){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        z21.shutdown();
    }
}
