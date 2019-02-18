package com.ecut;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.ArrayList;

public class TankClient extends Frame{

    public static final int GAME_WIDTH=1200;         //将游戏窗口的宽度和高度定义为常量，方便修改窗口大小
    public static final int GAME_HEIGNT=800;

    Tank myTank= new Tank(50,50,true,Tank.Direction.STOP,this);    //创建自己的好坦克

    List<Explode> explodes=new ArrayList<Explode>();            //保存爆炸数量
    List<Missile> missiles=new ArrayList<Missile>();            //保存子弹数量
    List<Tank> tanks=new ArrayList<Tank>();                     //保存坦克数量


    Image offScreenImage = null;


    public void paint(Graphics g) {            //重写Paint方法画出自己的坦克
        g.drawString("子弹数量:"+missiles.size(),10,45);        //显示存在炮弹的数量
        g.drawString("爆炸数量:"+explodes.size(),150,45);       //显示产生爆炸的数量
        g.drawString("坦克数量:"+tanks.size(),290,45);          //显示产生坦克的数量

        for(int i=0;i<missiles.size();i++){
            Missile m=missiles.get(i);
            m.hitTanks(tanks);
            m.draw(g);
            //if(!m.isLive())missiles.remove(m);
            //else m.draw(g);
        }
        for(int i=0;i<explodes.size();i++){
            Explode e=explodes.get(i);
            e.draw(g);
        }
        for(int i=0;i<tanks.size();i++){
            Tank t=tanks.get(i);
            t.draw(g);
        }

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
        for(int i=0;i<10;i++){
            tanks.add(new Tank(50+40*(i+1),50,false,Tank.Direction.D,this));
        }

        this.setLocation(400,150);//设置窗口位置
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
        public void keyReleased(KeyEvent e) {
            myTank.keyReleased(e);
        }
    }



}
