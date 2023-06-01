package edu.hitsz.aircraft;

import edu.hitsz.activity.MainActivity;
import edu.hitsz.basic.Observer;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.game.Game;
import edu.hitsz.prop.AbstractProp;

import java.util.LinkedList;
import java.util.List;

/**
 * 普通敌机
 * 不可射击
 *
 * @author hitsz
 */
public class MobEnemy extends AbstractAircraft implements Observer {

    public MobEnemy(int locationX, int locationY, double speedX, double speedY, int hp) {
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
    public List<BaseBullet> shoot() {
        return executeStrategy(locationX, locationY, speedX, speedY, 1);
    }

    public List<AbstractProp> dropProps(){
        return new LinkedList<AbstractProp>();
    }

    @Override
    public void update() {
        this.vanish();
        Game.addScore(10);
    }

}
