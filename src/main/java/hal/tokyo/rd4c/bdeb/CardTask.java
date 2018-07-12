/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hal.tokyo.rd4c.bdeb;

import hal.tokyo.rd4c.nfc.NFCReader;

/**
 *
 * @author tame
 */
public class CardTask {

    /* 正常値の値 */
    public static final int FLAG_OK = 100;
    public static final int ERROR = -1;

    private String sendText = new String();
    private NFCReader nfcReader = new NFCReader();

    /* どのカードをセットするかBOCCOに喋ってもらう */
    public void setCard(int stage) throws Exception{

        switch (stage) {
            /* 一枚目 */
            case 0:
                sendText = Setup.textMessage.readText(TextMessage.FIRST_SET);
                break;
            /* 二枚目,三枚目 */
            case 1:
            case 2:
                sendText = Setup.textMessage.readText(TextMessage.NOT_FIRST);
                break;
            /* 四枚目 */
            case 3:
                sendText = Setup.textMessage.readText(TextMessage.LAST_SET);
                break;
            /* 例外処理, 終了 */
            default:
                System.out.println("想定外の数値が入力、終了します。");
                return;
        }

        /* ステップ押下の誘導メッセージ */
        sendText += Setup.textMessage.readText(TextMessage.CARD_STEP);
        Setup.boccoApi.postMessage(sendText);
    }

    /* NFCリーダーでデータを読み込む.   ERROR:エラー   0 - 14:正常 */
    public int readCard() throws InterruptedException {
        int BGMNum = ERROR;
        /* 正常な値が読み取れるまで */
        while (BGMNum == ERROR) {
            BGMNum = nfcReader.readBGMNum();
        }
        return BGMNum;
    }

    /* 設置されたカードが適切か判断する. */
    public int judgeCard(int stage, int BGMNum) {
        /* 異常値：ERROR, 正常値：FLAG_OK */
        int flag = ERROR;
        switch (stage) {
            /* 一枚目 */
            case 0:
                if (0 <= BGMNum && BGMNum < 3) {
                    flag = FLAG_OK;
                }
                break;
            /* 二枚目,三枚目 */
            case 1:
            case 2:
                if (3 <= BGMNum && BGMNum < 12) {
                    flag = FLAG_OK;
                }
                break;
            /* 四枚目 */
            case 3:
                if (12 <= BGMNum && BGMNum < 15) {
                    flag = FLAG_OK;
                }
                break;
            /* 例外処理 */
            default:
                System.out.println("想定外の数値が入力されました。");
                break;
        }
        return flag;
    }
}
