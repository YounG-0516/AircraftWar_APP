package edu.hitsz.game;

import android.content.Context;
import android.os.Handler;

import java.io.IOException;

import edu.hitsz.activity.MainActivity;
import edu.hitsz.application.ImageManager;
import edu.hitsz.factory.BossEnemyFactory;
import edu.hitsz.factory.EliteEnemyFactory;
import edu.hitsz.factory.MobEnemyFactory;

public class HardGame extends Game {
    public HardGame(Context context, Handler handler) throws IOException, ClassNotFoundException {
        super(context,handler);
        this.backGround = ImageManager.BACKGROUND3_IMAGE;
        cycleDuration = 400;
        enemyMaxNumber = 8;
        probability = 0.60;
        threshold = 300;
        BOSS_ENEMY_HP = 500;
    }

    private int MOB_ENEMY_HP = 30;
    private int ELITE_ENEMY_HP = 60;
    private int BossNum=0;

    @Override
    protected void generateBoss() {

        if(Game.score >= threshold){
            if(!isBossExist()){
                super.generateBoss();
                factory = new BossEnemyFactory();
                BOSS_ENEMY_HP = (BossNum==0)?BOSS_ENEMY_HP:BOSS_ENEMY_HP+100;
                enemyAircrafts.add(factory.createAircraft(7,0,BOSS_ENEMY_HP));
                BossNum++;
                System.out.println("第"+BossNum+"次产生boss敌机!当前总血量："+BOSS_ENEMY_HP);
            }
            threshold = threshold + 300;
        }
    }

    @Override
    protected void addEnemy() {
        if (enemyAircrafts.size() < enemyMaxNumber) {
            if (Math.random() <= probability) {
                factory = new EliteEnemyFactory();
                enemyAircrafts.add(factory.createAircraft(0, 12, ELITE_ENEMY_HP));
            } else {
                factory = new MobEnemyFactory();
                enemyAircrafts.add(factory.createAircraft(0, 10, MOB_ENEMY_HP));
            }
        }
    }
}

