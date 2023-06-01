package edu.hitsz.factory;

import edu.hitsz.activity.MainActivity;
import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.strategy.ScatterShootStrategy;

public class BossEnemyFactory implements AircraftFactory{

    @Override
    public AbstractAircraft createAircraft(double speedX, double speedY, int hp) {
        int rand = Math.random()>0.5? 1: -1;
        AbstractAircraft bossEnemy =new BossEnemy(
                MainActivity.WINDOW_WIDTH/2,
                (int) (MainActivity.WINDOW_HEIGHT * 0.13),
                rand * speedX,
                speedY,
                hp
        );
        bossEnemy.setStrategy(new ScatterShootStrategy());
        return bossEnemy;
    }
}
