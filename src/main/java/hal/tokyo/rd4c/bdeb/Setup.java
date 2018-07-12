/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hal.tokyo.rd4c.bdeb;

import hal.tokyo.rd4c.speech2text.MicroPhone;
import hal.tokyo.rd4c.speech2text.Speaker;
import hal.tokyo.rd4c.bocco4j.BoccoAPI;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author tame
 */
public class Setup {

    private String setupSound = "setup/setup.wav";
    private MicroPhone microPhone;
    private Speaker speaker;
    /* BOCCOと接続用String */
    private static String BOCCO_API_KEY = "";
    private static String Email = "";
    private static String Password = "";
    /* BoocoAPI(String APIKey, String Email, String Password) */
    public static BoccoAPI boccoApi = new BoccoAPI(BOCCO_API_KEY, Email, Password);
    public static TextMessage textMessage = new TextMessage();
    /* 変換後文字列を格納するためのファイル名を格納 */
    public static String recFileName = new String();

    /*  */
    public void deviceInit() throws Exception {
        microPhone = new MicroPhone();
        speaker = new Speaker();

        microPhone.init();

        /* 起動音を鳴らす */
        speaker.openFile(setupSound);
        speaker.playSE();
        speaker.stopSE();

        /* BOCCOの接続が確立できた場合 */
        if (boccoApi.createSessions() == true) {
            boccoApi.getFirstRooID();
            boccoApi.postMessage(textMessage.readText(TextMessage.SESSION_OK));
        }
        
        /* ファイル作成 */
        recFileName = createFile();
    }

    /* 変換後文字列を格納するためのtxtファイル(ファイル名は日付) */
    private String createFile() throws IOException {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        String strDate = sdf.format(cal.getTime());
        File recorFile = new File(strDate);

        try {
            if (recorFile.createNewFile()) {
                System.out.println("ファイルの作成に成功しました。");
            } else {
                System.out.println("既に同じ名前のファイルがあります。");
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return strDate;
    }
}
