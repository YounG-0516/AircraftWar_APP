package edu.hitsz.game;

import android.content.Context;
import android.os.Handler;

import java.io.IOException;

import edu.hitsz.activity.MainActivity;
import edu.hitsz.application.ImageManager;
import edu.hitsz.factory.BossEnemyFactory;
import edu.hitsz.factory.EliteEnemyFactory;
import edu.hitsz.factory.MobEnemyFactory;


public class MediumGame extends Game {

    public MediumGame(Context context, Handler handler) throws IOException, ClassNotFoundException {
        super(context,handler);
        this.backGround = ImageManager.BACKGROUND2_IMAGE;
        cycleDuration = 480;
        enemyMaxNumber = 6;
        probability = 0.5;
        threshold = 500;
        BOSS_ENEMY_HP = 300;
    }

    private int MOB_ENEMY_HP = 20;
    private int ELITE_ENEMY_HP = 50;

    @Override
    protected void generateBoss() {

        if(Game.score >= threshold){
            if(!isBossExist()){
                super.generateBoss();
                factory = new BossEnemyFactory();
                enemyAircrafts.add(factory.createAircraft(5,0, BOSS_ENEMY_HP));
                System.out.println("产生boss敌机!普通模式BOSS机血量不随出现次数提升！");
            }
            threshold = threshold + 500;
        }
    }

    @Override
    protected void addEnemy() {
        if (enemyAircrafts.size() < enemyMaxNumber) {
            if (Math.random() <= probability) {
                factory = new EliteEnemyFactory();
                enemyAircrafts.add(factory.createAircraft(0, 10, ELITE_ENEMY_HP));
            } else {
                factory = new MobEnemyFactory();
                enemyAircrafts.add(factory.createAircraft(0, 8, MOB_ENEMY_HP));
            }
        }
    }




}
