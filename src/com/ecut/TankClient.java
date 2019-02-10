package com.ecut;

import  java.awt.*;
import java.awt.event.*;

public class TankClient extends Frame{

    public void lauchFrame(){
        this.setLocation(400,300);//设置窗口位置
        this.setSize(1200,800);      //设置窗口大小
        this.setTitle("坦克大战");    //设置窗口标题
        this.addWindowListener(new WindowAdapter() {           //利用匿名类
            public void windowClosing(WindowEvent e) {      //重写父类windowClosing方法实现简单的关闭窗口
                System.exit(0);
            }
        });
        this.setResizable(false);    //设置不能随意改变窗口大小
        setVisible(true);
    }

    public static void main(String[] args){
        TankClient tc=new TankClient();
        tc.lauchFrame();
    }
}
