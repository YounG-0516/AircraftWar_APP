package edu.hitsz.application;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import edu.hitsz.R;
import edu.hitsz.activity.MainActivity;
import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.aircraft.MobEnemy;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.prop.BloodProp;
import edu.hitsz.prop.BombProp;
import edu.hitsz.prop.BulletProp;
import java.util.HashMap;
import java.util.Map;

/**
 * 综合管理图片的加载，访问
 * 提供图片的静态访问方法
 *
 * @author hitsz
 */
public class ImageManager {

    /**
     * 类名-图片 映射，存储各基类的图片 <br>
     * 可使用 CLASSNAME_IMAGE_MAP.get( obj.getClass().getName() ) 获得 obj 所属基类对应的图片
     */
    private static final Map<String, Bitmap> CLASSNAME_IMAGE_MAP = new HashMap<>();

    public static Bitmap BACKGROUND1_IMAGE;
    public static Bitmap BACKGROUND2_IMAGE;
    public static Bitmap BACKGROUND3_IMAGE;
    public static Bitmap HERO_IMAGE;
    public static Bitmap HERO_BULLET_IMAGE;
    public static Bitmap ENEMY_BULLET_IMAGE;
    public static Bitmap MOB_ENEMY_IMAGE;
    public static Bitmap ELITE_IMAGE;
    public static Bitmap BOSS_IMAGE;
    public static Bitmap PROP_BLOOD_IMAGE;
    public static Bitmap PROP_BOMB_IMAGE;
    public static Bitmap PROP_BULLET_IMAGE;

    public static void initImage(Context context){

        ImageManager.BACKGROUND1_IMAGE = BitmapFactory.decodeResource(context.getResources(), R.drawable.bg);
        ImageManager.BACKGROUND2_IMAGE = BitmapFactory.decodeResource(context.getResources(), R.drawable.bg2);
        ImageManager.BACKGROUND3_IMAGE = BitmapFactory.decodeResource(context.getResources(), R.drawable.bg3);
        ImageManager.HERO_IMAGE = BitmapFactory.decodeResource(context.getResources(), R.drawable.hero);
        ImageManager.MOB_ENEMY_IMAGE = BitmapFactory.decodeResource(context.getResources(), R.drawable.mob);
        ImageManager.ELITE_IMAGE = BitmapFactory.decodeResource(context.getResources(), R.drawable.elite);
        ImageManager.BOSS_IMAGE = BitmapFactory.decodeResource(context.getResources(), R.drawable.boss);
        ImageManager.HERO_BULLET_IMAGE = BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet_hero);
        ImageManager.ENEMY_BULLET_IMAGE = BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet_enemy);
        ImageManager.PROP_BULLET_IMAGE = BitmapFactory.decodeResource(context.getResources(), R.drawable.prop_bullet);
        ImageManager.PROP_BLOOD_IMAGE = BitmapFactory.decodeResource(context.getResources(), R.drawable.prop_blood);
        ImageManager.PROP_BOMB_IMAGE = BitmapFactory.decodeResource(context.getResources(), R.drawable.prop_bomb);

        CLASSNAME_IMAGE_MAP.put(HeroAircraft.class.getName(), HERO_IMAGE);
        CLASSNAME_IMAGE_MAP.put(MobEnemy.class.getName(), MOB_ENEMY_IMAGE);
        CLASSNAME_IMAGE_MAP.put(HeroBullet.class.getName(), HERO_BULLET_IMAGE);
        CLASSNAME_IMAGE_MAP.put(EnemyBullet.class.getName(), ENEMY_BULLET_IMAGE);
        CLASSNAME_IMAGE_MAP.put(EliteEnemy.class.getName(), ELITE_IMAGE);
        CLASSNAME_IMAGE_MAP.put(BossEnemy.class.getName(),BOSS_IMAGE);
        CLASSNAME_IMAGE_MAP.put(BloodProp.class.getName(),PROP_BLOOD_IMAGE);
        CLASSNAME_IMAGE_MAP.put(BombProp.class.getName(),PROP_BOMB_IMAGE);
        CLASSNAME_IMAGE_MAP.put(BulletProp.class.getName(),PROP_BULLET_IMAGE);
    }

    public static Bitmap get(String className){
        return CLASSNAME_IMAGE_MAP.get(className);
    }

    public static Bitmap get(Object obj){
        if (obj == null){
            return null;
        }
        return get(obj.getClass().getName());
    }

}
