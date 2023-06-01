package edu.hitsz.aircraft;

import edu.hitsz.activity.MainActivity;
import edu.hitsz.basic.Observer;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.factory.BloodPropFactory;
import edu.hitsz.factory.BombPropFactory;
import edu.hitsz.factory.BulletPropFactory;
import edu.hitsz.game.Game;
import edu.hitsz.prop.AbstractProp;

import java.util.LinkedList;
import java.util.List;

/**
 * 精英敌机
 * 可以射击
 *
 * @author hitsz
 */

public class EliteEnemy extends AbstractAircraft implements Observer {

    /**
     * 子弹射击方向 (向上发射：1，向下发射：-1)
     */
    private int direction = 1;

    /**
     * @param locationX 精英敌机位置x坐标
     * @param locationY 精英敌机位置y坐标
     * @param speedX 精英敌机射出的子弹的速度
     * @param speedY 精英敌机射出的子弹的速度
     */
    public EliteEnemy(int locationX, int locationY, double speedX, double speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= MainActivity.WINDOW_HEIGHT ) {
            vanish();
        }
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
        //工厂模式
        if(Math.random()<0.3){
            propfactory = new BloodPropFactory();
        }else if(Math.random()<0.6 && Math.random()>=0.3){
            propfactory = new BombPropFactory();
        }else if(Math.random()<0.9 && Math.random()>=0.6){
            propfactory = new BulletPropFactory();
        }

        if(propfactory != null){
            resprop.add(propfactory.createProp(this.locationX, this.locationY));
        }

        return resprop;
    }

    @Override
    public void update() {
        this.vanish();
        Game.addScore(20);
    }

}
