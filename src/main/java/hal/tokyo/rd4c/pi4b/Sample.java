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

/**
 *
 * @author gn5r
 */
public class Sample {
    
    private static String startDir = "start/";
    private static String eventDir = "event/";
    private static String endDir = "end/";
    
    private static GpioPinDigitalInput se1, se2, se3, se4;
    private static GpioPinDigitalOutput se1LED, se2LED, se3LED, se4LED;
    private static GpioController gpio;
    
    public static void main(String[] args) throws Exception {
        init();

        /*    引数(LED, カードジャンル 0:start 1:event 2:end)    */
        se1.addListener(new SEButtonListener(se1LED, 0));
        se2.addListener(new SEButtonListener(se2LED, 1));
        se3.addListener(new SEButtonListener(se3LED, 1));
        se4.addListener(new SEButtonListener(se4LED, 2));
        
        while (true) {
            try {
                Thread.sleep(500);
            } catch (Exception e) {
            }
        }
    }
    
    private static void init() {
        gpio = GpioFactory.getInstance();

        /*    各SEボタンとLED    */
        se1 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_00, PinPullResistance.PULL_DOWN);
        se1.setShutdownOptions(true);
        
        se1LED = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "SE1", PinState.LOW);
        se1LED.setShutdownOptions(true, PinState.LOW);
        
        se2 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_04, PinPullResistance.PULL_DOWN);
        se2.setShutdownOptions(true);
        
        se2LED = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, "SE2", PinState.LOW);
        se2LED.setShutdownOptions(true, PinState.LOW);
        
        se3 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_26, PinPullResistance.PULL_DOWN);
        se3.setShutdownOptions(true);
        
        se3LED = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_27, "SE3", PinState.LOW);
        se3LED.setShutdownOptions(true, PinState.LOW);
        
        se4 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_28, PinPullResistance.PULL_DOWN);
        se4.setShutdownOptions(true);
        
        se4LED = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_29, "SE4", PinState.LOW);
        se4LED.setShutdownOptions(true, PinState.LOW);
    }
}
