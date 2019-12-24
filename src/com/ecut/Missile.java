package com.ecut;

import java.awt.*;
import java.util.List;

public class Missile {            //炮弹类
    public static final int XSPEED=20;
    public static final int YSPEED=20;

    public static final int WIDTH=10;
    public static final int HEIGHT=10;

    private int x,y;
    Tank.Direction dir;

    private boolean good;
    private boolean Live=true;           //炮弹是否存在

    private TankClient tc;


    public Missile(int x, int y, Tank.Direction dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
    }

    public Missile(int x,int y,boolean good,Tank.Direction dir,TankClient tc){
        this(x,y,dir);
        this.good=good;
        this.tc=tc;
    }

    public void draw(Graphics g){
        if(!Live){
            tc.missiles.remove(this);
            return;
        }
        Color c =g.getColor();
        g.setColor(Color.BLACK);
        g.fillOval(x,y,WIDTH,HEIGHT);
        g.setColor(c);

        move();
    }
    private void move(){
        switch (dir) {
            case L:
                x -= XSPEED;
                break;
            case LU:
                x -= XSPEED;
                y -= YSPEED;
                break;
            case LD:
                x -= XSPEED;
                y += YSPEED;
                break;
            case R:
                x += XSPEED;
                break;
            case RU:
                x += XSPEED;
                y -= YSPEED;
                break;
            case RD:
                x += XSPEED;
                y += YSPEED;
                break;
            case U:
                y -= YSPEED;
                break;
            case D:
                y += YSPEED;
                break;
            case STOP:
                break;
        }
        if(x<0||y<0||x>TankClient.GAME_WIDTH||y>TankClient.GAME_HEIGNT){
            Live=false;
            tc.missiles.remove(this);
        }
    }

    public boolean isLive() {
        return Live;
    }

    public Rectangle getRect(){               //利用碰撞检测的辅助类Rectangle 拿到子弹的矩形
        return new Rectangle(x,y,WIDTH,HEIGHT);
    }

    public boolean hitTank(Tank t){     //判断子弹是否打到坦克（碰撞检测）
        if(this.isLive()&&this.getRect().intersects(t.getRect())&&t.isLive()&&this.good!=t.isGood()) {
            if(t.isGood()) {
                t.setLife(t.getLife() - 25);
                if (t.getLife() <= 0) {
                    t.setLive(false);
                }
            }
            else { t.setLive(false); }


            this.Live=false;
            Explode e=new Explode(x,y,tc);
            tc.explodes.add(e);
            return true;
        }
        return false;
    }

    public boolean hitTanks(List<Tank> tanks){
        for(int i=0;i<tanks.size();i++){
            if(hitTank(tanks.get(i))){
                return true;
            }
        }
        return false;
    }

    public boolean hitWall(Wall w){
        if(this.Live && this.getRect().intersects(w.getRect())){
            this.Live=false;
            return true;
        }
        return false;
    }
}
