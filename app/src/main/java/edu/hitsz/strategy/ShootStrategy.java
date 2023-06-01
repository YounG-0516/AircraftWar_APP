package edu.hitsz.strategy;

import edu.hitsz.bullet.BaseBullet;
import java.util.List;

public interface ShootStrategy {
    List<BaseBullet> shootAction(int locationX, int locationY, double speedX, double speedY,int direction);
}
