package com.ecut;

import java.awt.*;
import java.awt.event.*;

public class TankClient extends Frame{

    public static final int GAME_WIDTH=1200;         //将游戏窗口的宽度和高度定义为常量，方便修改窗口大小
    public static final int GAME_HEIGNT=800;

    Tank myTank= new Tank(50,50);

    Image offScreenImage = null;


    public void paint(Graphics g) {            //重写Paint方法画出自己的坦克
        myTank.draw(g);
    }

    public void update(Graphics g){
        if(offScreenImage == null){
            offScreenImage = this.createImage(GAME_WIDTH,GAME_HEIGNT);
        }
        Graphics gOffScreen = offScreenImage.getGraphics();
        Color c=gOffScreen.getColor();
        gOffScreen.setColor(Color.GREEN);
        gOffScreen.fillRect(0,0,GAME_WIDTH,GAME_HEIGNT);
        gOffScreen.setColor(c);
        paint(gOffScreen);
        g.drawImage(offScreenImage,0,0,null);

    }

    public void lauchFrame(){
        this.setLocation(400,300);//设置窗口位置
        this.setSize(GAME_WIDTH,GAME_HEIGNT);      //设置窗口大小
        this.setTitle("坦克大战");    //设置窗口标题
        this.addWindowListener(new WindowAdapter() {           //利用匿名类
            public void windowClosing(WindowEvent e) {      //重写父类windowClosing方法实现简单的关闭窗口
                System.exit(0);
            }
        });
        this.setResizable(false);    //设置不能随意改变窗口大小
        this.setBackground(Color.GREEN);   //窗口背景颜色

        this.addKeyListener(new KeyMonitor());

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

    private class KeyMonitor extends KeyAdapter{            //添加键盘的监听器
        public void keyPressed(KeyEvent e) {
            myTank.keyPressed(e);
        }
    }



}
