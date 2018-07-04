/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hal.tokyo.rd4c.pi4b;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import hal.tokyo.rd4c.speech2text.Speaker;
import java.util.Random;

/**
 *
 * @author gn5r
 */
public class SEButtonListener implements GpioPinListenerDigital {

    /*    ボタンLED    */
    private final GpioPinDigitalOutput ButtonLED;
    /*    カードジャンル(起承結)    */
    private final String fileName;
    private final String startDir = "start/start";
    private final String eventDir = "event/event";
    private final String endDir = "end/end";

    public SEButtonListener(GpioPinDigitalOutput ButtonLED, int SEGenre) {
        this.ButtonLED = ButtonLED;
        this.fileName = SelectRandomSE(SEGenre);
    }

    @Override
    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent gpdsce) {
        Speaker speaker = new Speaker();

        /*    該当ボタンのLEDを点灯    */
        ButtonLED.high();

        try {
            if (gpdsce.getState() == PinState.LOW) {
                /*    LED消灯    */
                ButtonLED.toggle();
                /*    ジャンルの音声をランダムに1つ選択    */
                speaker.openFile(this.fileName);
                speaker.playSE();
                speaker.stopSE();
            }
        } catch (Exception e) {
        }
    }

    private String SelectRandomSE(final int SEGenre) {
        String fileName = null;

        Random random = new Random();
        /*    Random.nextInt(範囲) + 1をすると １～範囲 の数値が返ってくる    */
        int SENum = random.nextInt(3) + 1;

        switch (SEGenre) {
            /*    startカード    */
            case 0:
                /*    1:cave 2:bluesky 3:snow*/
                fileName = startDir + SENum + ".wav";
                break;

            /*    eventカード    */
            case 1:
                /*    1:unique 2:magic 3:ghost*/
                fileName = eventDir + SENum + ".wav";
                break;

            /*    endカード    */
            case 2:
                /*    1:happy 2:normal 3:bad    */
                fileName = endDir + SENum + ".wav";
                break;
        }

        return fileName;
    }
}
