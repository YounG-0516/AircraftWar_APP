package edu.hitsz.aircraft;

import edu.hitsz.activity.MainActivity;
import edu.hitsz.application.ImageManager;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.game.Game;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.strategy.StraightShootStrategy;

import java.util.LinkedList;
import java.util.List;

/**
 * 英雄飞机，游戏玩家操控
 */
public class HeroAircraft extends AbstractAircraft {

    /**
     * 子弹射击方向 (向上发射：1，向下发射：-1)
     */
    private int direction = -1;

    /**
     * @param locationX 英雄机位置x坐标
     * @param locationY 英雄机位置y坐标
     * @param speedX 英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param speedY 英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param hp    初始生命值
     */
    private volatile static HeroAircraft heroAircraft;

    private HeroAircraft(int locationX, int locationY, double speedX, double speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    public static HeroAircraft getHeroAircraft(){
        if(heroAircraft == null){
            synchronized (HeroAircraft.class){
                if(heroAircraft == null){
                    heroAircraft = new HeroAircraft(
                            MainActivity.WINDOW_WIDTH / 2,
                            MainActivity.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight() ,
                            0, 0,  10000);
                }
            }
        }
        heroAircraft.setStrategy(new StraightShootStrategy());
        return heroAircraft;
    }


    public static void refreshHero(){
        if(heroAircraft == null){
            synchronized (HeroAircraft.class){
                if(heroAircraft == null){
                    heroAircraft = new HeroAircraft(
                            MainActivity.WINDOW_WIDTH / 2,
                            MainActivity.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight(),
                            0, 0, 10000);

                    heroAircraft.setStrategy(new StraightShootStrategy());
                }
            }
        }else{
            heroAircraft.locationX = MainActivity.WINDOW_WIDTH / 2;
            heroAircraft.locationY = MainActivity.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight();
            heroAircraft.speedX = 0;
            heroAircraft.speedY = 0;
            heroAircraft.hp = 10000;
            heroAircraft.setStrategy(new StraightShootStrategy());
            heroAircraft.isValid = true;
            Game.score=0;
        }
    }


    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过forward函数移动
    }

    @Override
    /**
     * 通过射击产生子弹
     * @return 射击出的子弹List
     */
    public List<BaseBullet> shoot(){
        return heroAircraft.executeStrategy(this.getLocationX(), this.getLocationY(), speedX, speedY, direction);
    }

    public List<AbstractProp> dropProps(){
        return new LinkedList<AbstractProp>();
    }

}
