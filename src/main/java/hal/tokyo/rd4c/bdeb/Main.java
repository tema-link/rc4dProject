/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hal.tokyo.rd4c.bdeb;

/**
 *
 * @author tame
 */
public class Main {

    private Setup setup;
    /* 何枚目のカードか判断用 */
    private int stage = 0;

    public void main(String[] args) throws Exception {
        setup = new Setup();

        /* 起動タスク */
        setup.deviceInit();

        /* カードタスク */
        

        /* ゲームタスク */
        
        /* 変換タスク */
    }
}
