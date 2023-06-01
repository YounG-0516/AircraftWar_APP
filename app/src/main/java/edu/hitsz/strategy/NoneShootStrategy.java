package edu.hitsz.strategy;

import edu.hitsz.bullet.BaseBullet;

import java.util.LinkedList;
import java.util.List;

public class NoneShootStrategy implements ShootStrategy{
    @Override
    public List<BaseBullet> shootAction(int locationX, int locationY, double speedX, double speedY, int direction) {
        return new LinkedList<>();
    }
}
