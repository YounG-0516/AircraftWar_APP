package edu.hitsz.strategy;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

public class ScatterShootStrategy implements ShootStrategy{
    @Override
    public List<BaseBullet> shootAction(int locationX, int locationY, double speedX, double speedY, int direction) {
        int shootNum = 3;
        int power = (direction>0)? 50: 30;
        List<BaseBullet> res = new LinkedList<>();
        int x = locationX;
        int y = locationY + direction*2;
        double speedx;
        double speedy = speedY + direction*10;
        BaseBullet bullet;
        for(int i=0; i<shootNum; i++){
            // 子弹发射位置相对飞机位置向前偏移
            // 多个子弹横向分散
            speedx = (direction>0)? speedX + (i-1)*2 : (i-1)*2;

            if(direction>0){
                bullet = new EnemyBullet(x + (i*2 - shootNum + 1)*10, y, speedx, speedy, power);
            }else{
                bullet = new HeroBullet(x + (i*2 - shootNum + 1)*10, y, speedx, speedy, power);
            }
            res.add(bullet);
        }
        return res;
    }
}
