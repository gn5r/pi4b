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
    /*    各SEのディレクトリ    */
    private final String startDir = "start/start";
    private final String eventDir = "event/";
    private final String endDir = "end/end";

    public SEButtonListener(GpioPinDigitalOutput ButtonLED, int BGMNum) {
        this.ButtonLED = ButtonLED;
        this.fileName = SelectRandomSE(BGMNum);

        /*    該当ボタンのLEDを点灯    */
        ButtonLED.high();
        System.out.println("ファイル名:" + this.fileName);
    }

    @Override
    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent gpdsce) {

        try {

            if (gpdsce.getState() == PinState.LOW) {
                System.out.println("押されたピン:" + gpdsce.getPin());
                Speaker speaker = new Speaker();
                /*    LED消灯    */
                ButtonLED.low();
                /*    ジャンルの音声をランダムに1つ選択    */
                speaker.openFile(this.fileName);
                speaker.playSE();
                speaker.stopSE();

            } else {
                ButtonLED.high();
            }

        } catch (Exception e) {
        }
    }

    /*    BGM番号でカードの種類を判別し、ランダムでSEを１つ選択    */
    private String SelectRandomSE(final int BGMNum) {
        /*    ランダムで選択されたSEファイル名    */
        String selectFile = null;

        Random random = new Random();
        /*    Random.nextInt(範囲) + 1をすると １~範囲 の数値が返ってくる    */
        int SENum = random.nextInt(3) + 1;

        /*    BGM番号
                 0~2 : start
                 3~11 : event
                 12~14 : end
         */
        switch (BGMNum) {
            /*    startカード    */
            case 0:
            case 1:
            case 2:
                /*    1:cave 2:bluesky 3:snow*/
                selectFile = startDir + SENum + ".wav";
                break;

            /*    eventカード    */
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
                /*    1:unique 2:magic 3:ghost*/
                selectFile = eventDir + SENum + ".wav";
                break;

            /*    endカード    */
            case 12:
            case 13:
            case 14:
                /*    1:happy 2:normal 3:bad    */
                selectFile = endDir + SENum + ".wav";
                break;
        }

        return selectFile;
    }
}
