package com.ecut;

import java.awt.*;
import java.awt.event.*;

public class TankClient extends Frame{

    int x=50,y=50;

    public void paint(Graphics g) {            //重写Paint方法画出自己的坦克
        Color c = g.getColor();
        g.setColor(Color.RED);
        g.fillOval(50,50,30,30);
        g.setColor(c);

        y += 5;
    }

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
        this.setBackground(Color.GREEN);   //窗口背景颜色
        setVisible(true);

        new Thread(new PaintThread()).start();
    }
    //////////////////////////////////////////主方法///////////////////////////////////////////////
    public static void main(String[] args){
        TankClient tc=new TankClient();
        tc.lauchFrame();
    }

    private class PaintThread implements Runnable{
        public void run() {
            while(true){
                repaint();           //调用父类的repaint()方法进行重画
                try {
                    Thread.sleep(100);    //该线程每隔100毫秒启动一次
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }



}
