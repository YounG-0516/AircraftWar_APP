package edu.hitsz.aircraft;

import edu.hitsz.basic.Observer;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.factory.BloodPropFactory;
import edu.hitsz.factory.BombPropFactory;
import edu.hitsz.factory.BulletPropFactory;
import edu.hitsz.game.Game;
import edu.hitsz.prop.AbstractProp;

import java.util.LinkedList;
import java.util.List;

public class BossEnemy extends AbstractAircraft implements Observer {

    /**
     * 子弹一次发射数量
     */
    private int propNum = 3;

    /**
     * 子弹射击方向 (向上发射：1，向下发射：-1)
     */
    private int direction = 1;

    /**
     * @param locationX BOSS敌机位置x坐标
     * @param locationY BOSS敌机位置y坐标
     * @param speedX BOSS敌机射出的子弹的速度
     * @param speedY BOSS敌机射出的子弹的速度
     */
    public BossEnemy(int locationX, int locationY, double speedX, double speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    @Override
    /**
     * 通过射击产生子弹
     * @return 射击出的子弹List
     */
    public List<BaseBullet> shoot(){
        return executeStrategy(locationX, locationY, speedX, speedY, direction);
    }

    @Override
    public List<AbstractProp> dropProps() {
        List<AbstractProp> resprop = new LinkedList<AbstractProp>();
        for(int i=0; i<propNum; i++) {
            //工厂模式
            if (Math.random() < 0.33) {
                propfactory = new BloodPropFactory();
            } else if (Math.random() < 0.67 && Math.random() >= 0.33) {
                propfactory = new BombPropFactory();
            } else {
                propfactory = new BulletPropFactory();
            }

            resprop.add(propfactory.createProp(this.locationX + (i - 1) * 50, this.locationY));
        }
        return resprop;
    }

    @Override
    public void update() {

        if(this.getHp()>=100){
            this.decreaseHp(100);
            Game.addScore(50);

        }else{
            this.decreaseHp(getHp()-1);
            System.out.println("注意！BOSS机仅剩一滴血！");
        }

    }
}
