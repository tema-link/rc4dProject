/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hal.tokyo.rd4c.bdeb;

import hal.tokyo.rd4c.speech2text.GoogleSpeechAPI;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author tame
 */
public class ConvertTask {

    /* 録音配列の限界値 */
    private static final int RECOR_DATA = 20;
    /* Googleに送る為の分割間隔 */
    private static final int DATA_CUT = 100;
    private static final String GOOGLE_API_KEY = "";

    /* GoogleAPIで音声データを文字列化する. -> BOCCOへの送信上限が120文字のためDATA_CUTで等分割 */
    public String[] convertToRecorData(File data) throws Exception {
        String[] result = new String[RECOR_DATA];
        String str = new String();

        /* 変換後の文字列取得 */
        GoogleSpeechAPI googleSpeech = new GoogleSpeechAPI(GOOGLE_API_KEY, data.getPath());
        str = googleSpeech.postGoogleAPI();
        /* 終端子挿入 */
        str += "~";

        /* ファイルに書き込み */
        writeFile(str);

        /* DATA_CUT間隔でresult配列に格納 配列はRECOR_DATA分用意 */
        for (int cnt = 0; cnt / DATA_CUT < RECOR_DATA; cnt += DATA_CUT) {
            /* DATA_CUTずつ格納 */
            result[cnt / DATA_CUT] = str.substring(cnt, cnt + DATA_CUT);
            /* 終端子をが入っていた場合、終端子を入れ替えforから抜ける. */
            if (result[cnt / DATA_CUT].indexOf("~") != CardTask.ERROR) {
                result[cnt / DATA_CUT].replace("~", "。");
                cnt = RECOR_DATA;
            }
        }
        return result;
    }

    /* 音声->文字列化したものをファイルに書き込み */
    private void writeFile(String data) throws IOException {
        try {
            File file = new File(Setup.recFileName);

            /* 書き込みが可能ならば */
            if (checkBeforeWritefile(file)) {
                PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
                /* 文字列の書き込み(終端子 ”~”込み) */
                pw.println(data);
                /* ファイルを閉じる */
                pw.close();
            } else {
                System.out.println("ファイルに書き込めません");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /* 書き込み可能か判断する. */
    private static boolean checkBeforeWritefile(File file) {
        if (file.exists()) {
            if (file.isFile() && file.canWrite()) {
                return true;
            }
        }
        return false;
    }
    
    /* ファイルの文字列をBOCCOに送信 */
    public void allStorySendToBOCCO(){
        
    }
}