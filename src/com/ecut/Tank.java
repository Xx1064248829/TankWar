package com.ecut;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

public class Tank {
    public static final int XSPEED=10;           //定义坦克移动速度
    public static final int YSPEED=10;

    public static final int WIDTH=30;           //定义坦克大小
    public static final int HEIGHT=30;

    private boolean live=true;   //判断坦克的死活
    private BloodBar bb=new BloodBar();

    private int life=100;

    TankClient tc;

    private boolean good;

    private int x, y;
    private int oldx,oldy;

    private static Random r=new Random();        //创建一个静态的随机数产生器

    private boolean bL=false,bU=false,bR=false,bD=false;

    enum Direction {                              //定义 一个枚举类型
        L, LU, R, RU, RD, LD, U, D, STOP;
    }

    private Direction dir=Direction.STOP;
    private Direction ptDir=Direction.D;         //炮筒

    private int step=r.nextInt(12)+3;               //创建一个变量记录坏坦克移动的步数

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }


    public boolean isGood() {
        return good;
    }

    public void setGood(boolean good) {
        this.good = good;
    }

    public Tank(int x, int y,boolean good){
        this.x=x;
        this.y=y;
        this.oldx=x;
        this.oldy=y;
        this.good=good;
    }

    public  Tank(int x,int y,boolean good,Direction dir,TankClient tc){
        this(x,y,good);
        this.dir=dir;
        this.tc=tc;
    }

    public void draw(Graphics g){                    //画笔，画出自己的坦克
        if(!live){
            if(!good){
                tc.tanks.remove(this);
            }
            return;
        }

        Color c = g.getColor();
        if(good) {g.setColor(Color.RED);}
        else {g.setColor(Color.white);}
        g.fillOval(x,y,WIDTH,HEIGHT);
        g.setColor(c);
        if(good) {
            bb.draw(g);
        }

        switch (ptDir){                   //画出坦克八个方向的炮筒朝向
            case L:
                g.drawLine(x+Tank.WIDTH/2,y+Tank.HEIGHT/2,x-5,y+Tank.HEIGHT/2);
                break;
            case LU:
                g.drawLine(x+Tank.WIDTH/2,y+Tank.HEIGHT/2,x,y);
                break;
            case LD:
                g.drawLine(x+Tank.WIDTH/2,y+Tank.HEIGHT/2,x,y+Tank.HEIGHT);
                break;
            case R:
                g.drawLine(x+Tank.WIDTH/2,y+Tank.HEIGHT/2,x+Tank.WIDTH+5,y+Tank.HEIGHT/2);
                break;
            case RU:
                g.drawLine(x+Tank.WIDTH/2,y+Tank.HEIGHT/2,x+Tank.WIDTH,y);
                break;
            case RD:
                g.drawLine(x+Tank.WIDTH/2,y+Tank.HEIGHT/2,x+Tank.WIDTH,y+Tank.HEIGHT);
                break;
            case U:
                g.drawLine(x+Tank.WIDTH/2,y+Tank.HEIGHT/2,x+Tank.WIDTH/2,y-5);
                break;
            case D:
                g.drawLine(x+Tank.WIDTH/2,y+Tank.HEIGHT/2,x+Tank.WIDTH/2,y+Tank.HEIGHT+5);
                break;




        }

        move();
    }

    void move(){

        this.oldx=x;
        this.oldy=y;

        switch (dir){
            case L:
                x-=XSPEED;
                break;
            case LU:
                x-=XSPEED;
                y-=YSPEED;
                break;
            case LD:
                x-=XSPEED;
                y+=YSPEED;
                break;
            case R:
                x+=XSPEED;
                break;
            case RU:
                x+=XSPEED;
                y-=YSPEED;
                break;
            case RD:
                x+=XSPEED;
                y+=YSPEED;
                break;
            case U:
                y-=YSPEED;
                break;
            case D:
                y+=YSPEED;
                break;
            case STOP:
                break;
        }
        if(this.dir!=Direction.STOP){
            this.ptDir=this.dir;
        }

        if(x<10){ x=10;}              //定义坦克的活动范围，解决坦克出界的问题
        if(y<50){ y=50;}
        if(x+(Tank.WIDTH+10)>TankClient.GAME_WIDTH){x=TankClient.GAME_WIDTH-Tank.WIDTH-10;}
        if(y+(Tank.HEIGHT+10)>TankClient.GAME_HEIGNT){y=TankClient.GAME_HEIGNT-Tank.HEIGHT-10;}

        if(!good){
            Direction[] dirs=Direction.values();                 //创建数组
            if(step==0){
                step=r.nextInt(12)+3;
                int rn=r.nextInt(dirs.length);                       //随机创建一个dirs数组范围内的值
                dir=dirs[rn];
            }
            step --;

            if(r.nextInt(40)>38){
                this.fire();
            }
        }

    }

    private void stay(){
        x=oldx;
        y=oldy;
    }

    public void keyPressed(KeyEvent e){        //处理键盘按下时指令
        int key=e.getKeyCode();
        switch(key) {
            case KeyEvent.VK_F2:
                if(!this.live){
                    this.live=true;
                    this.life=100;
                }
                break;
            case KeyEvent.VK_RIGHT:
                bR=true;
                break;
            case KeyEvent.VK_LEFT:
                bL=true;
                break;
            case KeyEvent.VK_UP:
                bU=true;
                break;
            case KeyEvent.VK_DOWN:
                bD=true;
                break;
            case KeyEvent.VK_Z:
                superFire();
                break;
        }
        locateDirection();
    }

    public Missile fire(){                 //处理开火时炮弹的轨迹
        if(!live)return null;
        int x= this.x+Tank.WIDTH/2-Missile.WIDTH/2;
        int y= this.y+Tank.HEIGHT/2-Missile.HEIGHT/2;
        Missile m=new Missile(x,y,good,ptDir,this.tc);
        tc.missiles.add(m);
        return m;
    }
    public Missile fire(Direction dir){
        if(!live)return null;
        int x= this.x+Tank.WIDTH/2-Missile.WIDTH/2;
        int y= this.y+Tank.HEIGHT/2-Missile.HEIGHT/2;
        Missile m=new Missile(x,y,good,dir,this.tc);
        tc.missiles.add(m);
        return m;
    }

    private void superFire(){
        Direction[] dirs=Direction.values();
        for(int i=0;i<8;i++){
            fire(dirs[i]);
        }
    }

    private class BloodBar{       //设置血条
        public void draw(Graphics g){
            Color c=g.getColor();
            g.setColor(Color.yellow);
            g.drawRect(x,y-5,WIDTH,5);
            int w=WIDTH*life/100;
            g.fillRect(x,y-5,w,5);
            g.setColor(c);
        }
    }

    public Rectangle getRect(){               //拿到坦克的矩形
        return new Rectangle(x,y,WIDTH,HEIGHT);
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public void keyReleased(KeyEvent e){       //处理键盘抬起时的指令
        int key=e.getKeyCode();
        switch(key) {
            case KeyEvent.VK_SPACE:
                fire();
                break;
            case KeyEvent.VK_RIGHT:
                bR=false;
                break;
            case KeyEvent.VK_LEFT:
                bL=false;
                break;
            case KeyEvent.VK_UP:
                bU=false;
                break;
            case KeyEvent.VK_DOWN:
                bD=false;
                break;
        }
        locateDirection();
    }

    void locateDirection(){                  //为坦克移动定向
        if(bL && !bU && !bR && !bD) {
            dir =Direction.L;
        } else if(bL &&  bU && !bR && !bD) {
            dir =Direction.LU;
        } else if(bL && !bU && !bR &&  bD) {
            dir =Direction.LD;
        } else if(!bL && !bU && bR && !bD) {
            dir =Direction.R;
        } else if(!bL &&  bU && bR && !bD) {
            dir =Direction.RU;
        } else if(!bL && !bU && bR &&  bD) {
            dir =Direction.RD;
        } else if(!bL && bU && !bR && !bD) {
            dir =Direction.U;
        } else if(!bL && !bU && !bR && bD) {
            dir =Direction.D;
        } else if(!bL && !bU && !bR && !bD) {
            dir =Direction.STOP;
        }
    }

    

    public boolean collidesWithWall(Wall w){
        if(this.live && this.getRect().intersects(w.getRect())){
            this.stay();
            return true;
        }
        return false;
    }

    public boolean collidesWithTanks(java.util.List<Tank> tanks){
        for(int i=0;i<tanks.size();i++){
            Tank t = tanks.get(i);
            if(this !=t){
                if(this.live && t.isLive() &&this.getRect().intersects(t.getRect())){
                    this.stay();
                    t.stay();
                    return true;
                }
            }
        }
        return false;
    }

    public boolean eat(Blood b){
        if(this.live && b.isLive() && this.getRect().intersects(b.getRect())){
            this.life=100;
            b.setLive(false);
            return true;
        }
        return false;
    }

}
