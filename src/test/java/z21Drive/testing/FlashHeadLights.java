package z21Drive.testing;

import z21Drive.LocoAddressOutOfRangeException;
import z21Drive.Z21;
import z21Drive.actions.Z21ActionSetLocoFunction;
import z21Drive.broadcasts.BroadcastFlagHandler;
import z21Drive.broadcasts.BroadcastFlags;

/**
 * Used to test if setLocoFunction works.
 * If it's working properly, loco keeps it's headlights flashing.
 * @see z21Drive.actions.Z21ActionSetLocoFunction
 * @author grizeldi
 */
public class FlashHeadLights implements Runnable{
    private boolean exit;

    public static void main(String [] args){
        new Thread(new FlashHeadLights()).start();
    }

    private FlashHeadLights(){
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
        BroadcastFlagHandler.setReceive(BroadcastFlags.LOCONET_DETECTOR, true);
        while (!exit){
            try {
                z21.sendActionToZ21(new Z21ActionSetLocoFunction(9, 0, true));
                Thread.sleep(1000);
                z21.sendActionToZ21(new Z21ActionSetLocoFunction(9, 0, false));
                Thread.sleep(1000);

            } catch (LocoAddressOutOfRangeException e) {
                e.printStackTrace();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        z21.shutdown();
    }
}
