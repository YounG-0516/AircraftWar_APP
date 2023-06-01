package edu.hitsz.game;

import android.app.Activity;
import android.os.Handler;

import java.io.IOException;

import edu.hitsz.application.ImageManager;
import edu.hitsz.factory.EliteEnemyFactory;
import edu.hitsz.factory.MobEnemyFactory;

public class EasyGame extends Game {
    public EasyGame(Activity context, Handler handler) throws IOException, ClassNotFoundException {
        super(context,handler);
        this.backGround = ImageManager.BACKGROUND1_IMAGE;
    }

    @Override
    protected void generateBoss() {}

    @Override
    protected void addEnemy() {
        if (enemyAircrafts.size() < enemyMaxNumber){
            if(Math.random() <= probability){
                factory = new EliteEnemyFactory();
                enemyAircrafts.add(factory.createAircraft(0,8,40));
            }else{
                factory = new MobEnemyFactory();
                enemyAircrafts.add(factory.createAircraft(0,6,20));
            }
        }
    }

}
