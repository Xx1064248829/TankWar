package com.ecut;

import java.awt.*;

public class Explode {          //爆炸类   模拟爆炸的场面
    int x,y;
    private boolean live=true;
    private TankClient tc;

    int [] diameter={4,7,12,18,22,26,32,30,23,14,6};
    int step=0;

    public Explode(int x,int y,TankClient tc){
        this.x=x;
        this.y=y;
        this.tc=tc;
    }

    public void draw(Graphics g){
        if(!live){
            tc.explodes.remove(this);
            return;
        }

        if(step==diameter.length){
            live=false;
            step=0;
            return;
        }

        Color c=g.getColor();
        g.setColor(Color.orange);
        g.fillOval(x,y,diameter[step],diameter[step]);
        g.setColor(c);
        step ++;
    }
}
