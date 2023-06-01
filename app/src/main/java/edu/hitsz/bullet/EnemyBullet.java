package edu.hitsz.bullet;

import edu.hitsz.basic.Observer;

/**
 * @Author hitsz
 */
public class EnemyBullet extends BaseBullet implements Observer {

    public EnemyBullet(int locationX, int locationY, double speedX, double speedY, int power) {
        super(locationX, locationY, speedX, speedY, power);
    }


    @Override
    public void update() {
        this.vanish();
    }
}
