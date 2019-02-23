package com.ecut;

import java.awt.*;

public class Blood {     //血块类
    int x,y,w,h;
    TankClient tc;
    int step=0;

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    private boolean live=true;

    private int[][] pos={
            {350,300},{700,300},{500,500},{1000,500},{250,650},{800,600}
    };

    public Blood(){
        x=pos[0][0];
        y=pos[0][1];
    }

    public void draw(Graphics g){
        if(!live)return;
        Color c=g.getColor();
        g.setColor(Color.pink);
        g.fillRect(x,y,w,h);
        g.setColor(c);
        move();
    }

    private void move(){
        step++;
        if(step==pos.length){step=0;}
        x=pos[step][0];
        y=pos[step][1];
        w=h=15;
    }

    public Rectangle getRect(){
        return new Rectangle(x,y,w,h);
    }
}
