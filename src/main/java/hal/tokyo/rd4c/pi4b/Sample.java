/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hal.tokyo.rd4c.pi4b;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import hal.tokyo.rd4c.bocco4j.BoccoAPI;
import hal.tokyo.rd4c.speech2text.GoogleSpeechAPI;
import hal.tokyo.rd4c.speech2text.MicroPhone;
import java.io.File;

/**
 *
 * @author gn5r
 */
public class Sample {

    private static GpioPinDigitalInput se1, se2, se3, se4;
    private static GpioPinDigitalOutput se1LED, se2LED, se3LED, se4LED;
    private static GpioController gpio;

    /*    args[0] : GoogleAPIKey args[1] : BoccoAPIKey args[2] : Email args[3] : Password    */
    public static void main(String[] args) throws Exception {

        System.out.println("GPIO初期化開始...");
        init();

        /*    引数(LED, カードジャンル 0:start 1:event 2:end)    */
        System.out.println("リスナーをセット...");
        se1.addListener(new SEButtonListener(se1LED, 0));
        se2.addListener(new SEButtonListener(se2LED, 1));
        se3.addListener(new SEButtonListener(se3LED, 1));
        se4.addListener(new SEButtonListener(se4LED, 2));

        System.out.println("セット完了！");

        MicroPhone microPhone = new MicroPhone();
        microPhone.init();
        microPhone.startRec();
        microPhone.stopRec();
        File data = microPhone.convertWav();

        GoogleSpeechAPI google = new GoogleSpeechAPI(args[0], data.getPath());
        String result = google.postGoogleAPI();

        BoccoAPI boccoAPI = new BoccoAPI(args[1], args[2], args[3]);
        boccoAPI.createSessions();
        boccoAPI.getFirstRooID();
        boccoAPI.postMessage(result);

        while (true) {
            try {
                Thread.sleep(500);
            } catch (Exception e) {
            }
        }
    }

    /*    GPIO初期化メソッド    */
    private static void init() {

        gpio = GpioFactory.getInstance();

        /*    各SEボタンとLED    */
        se1 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_00, PinPullResistance.PULL_UP);
        se1.setShutdownOptions(true);

        se1LED = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "SE1", PinState.LOW);
        se1LED.setShutdownOptions(true, PinState.LOW);

        se2 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_04, PinPullResistance.PULL_UP);
        se2.setShutdownOptions(true);

        se2LED = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, "SE2", PinState.LOW);
        se2LED.setShutdownOptions(true, PinState.LOW);

        se3 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_26, PinPullResistance.PULL_UP);
        se3.setShutdownOptions(true);

        se3LED = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_27, "SE3", PinState.LOW);
        se3LED.setShutdownOptions(true, PinState.LOW);

        se4 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_28, PinPullResistance.PULL_UP);
        se4.setShutdownOptions(true);

        se4LED = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_29, "SE4", PinState.LOW);
        se4LED.setShutdownOptions(true, PinState.LOW);
    }
}
