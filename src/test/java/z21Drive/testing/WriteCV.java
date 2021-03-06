package z21Drive.testing;

import z21Drive.LocoAddressOutOfRangeException;
import z21Drive.Z21;
import z21Drive.actions.Z21ActionLanXCVWrite;
import z21Drive.actions.Z21ActionLanXTrackPowerOff;
import z21Drive.actions.Z21ActionLanXTrackPowerOn;
import z21Drive.record.Z21Record;
import z21Drive.record.Z21RecordType;
import z21Drive.record.xbus.Z21RecordLanXCVResult;
import z21Drive.responses.Z21ResponseListener;

/**
 * 
 * 
 * BE CAREFUL!
 * 
 * This Class writes a new Loco ID to CV 1
 * 
 * BE CAREFUL!
 * 
 * 
 */
public class WriteCV implements Runnable{

    public static void main(String[] args) {
        //Start things up
        new Thread(new WriteCV()).start();
        while (true){}
    }

    public void run(){
        final Z21 z21 = Z21.instance;
        z21.sendActionToZ21(new Z21ActionLanXTrackPowerOff());
        z21.addResponseListener(new Z21ResponseListener() {
            @Override
            public void responseReceived(Z21RecordType type, Z21Record response) {
                if (type == Z21RecordType.LAN_X_CV_RESULT){
                	// Output of the Results Response
                	Z21RecordLanXCVResult bc = (Z21RecordLanXCVResult) response;
                    String o = Integer.toBinaryString(bc.getValue());
                    while (o.length() < 8) {
                    	o = "0" + o;
                    }
                    System.out.println(String.format("%3d: %3d %s", bc.getCVadr(), bc.getValue(), o));
                } else if (type == Z21RecordType.LAN_X_CV_NACK){
                	System.out.println("Write CV failed.");
                }
                // Active Track Power
    			z21.sendActionToZ21(new Z21ActionLanXTrackPowerOn());
    			System.exit(0);
            }

            @Override
            public Z21RecordType[] getListenerTypes() {
                return new Z21RecordType[]{Z21RecordType.LAN_X_CV_RESULT, Z21RecordType.LAN_X_CV_NACK};
            }
        });
        sendNext(z21);
    }

	private void sendNext(Z21 z21) {
			try {
				z21.sendActionToZ21(new Z21ActionLanXCVWrite(1, 7));
			} catch (LocoAddressOutOfRangeException e) {
				e.printStackTrace();
			}
	}
}
