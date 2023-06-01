package edu.hitsz.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

import edu.hitsz.DAO.User;
import edu.hitsz.DAO.UserDao;
import edu.hitsz.activity.LauncherActivity;
import edu.hitsz.activity.MainActivity;

import edu.hitsz.activity.OnlineActivity;
import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.aircraft.MobEnemy;
import edu.hitsz.application.HeroController;
import edu.hitsz.application.ImageManager;

import edu.hitsz.application.MusicService;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.basic.Observer;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.factory.AircraftFactory;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.prop.BloodProp;
import edu.hitsz.prop.BombProp;
import edu.hitsz.prop.BulletProp;

/**
 * 游戏逻辑抽象基类，遵循模板模式，action() 为模板方法
 * 包括：游戏主面板绘制逻辑，游戏执行逻辑。
 * 子类需实现抽象方法，实现相应逻辑
 * @author young
 */
public abstract class Game extends SurfaceView implements SurfaceHolder.Callback, Runnable{

    public static final String TAG = "BaseGame";
    boolean mbLoop = false; //控制绘画线程的标志位
    private SurfaceHolder surfaceHolder;
    private Canvas canvas;  //绘图的画布
    private Paint mPaint;
    private Handler handler;
    private Context context;
    MusicService music;
    UserDao data;
    User user;

    /**
     * 背景图片缓存，可随难度改变
     */
    protected Bitmap backGround;

    //点击屏幕位置
    float clickX = 0, clickY=0;
    private int backGroundTop = 0;

    private final ScheduledExecutorService executorService;
    private boolean gameOverFlag = false;
    public static int score = 0;
    public static boolean needMusic;
    protected boolean bossFlag = false;

    //敌机工厂
    protected AircraftFactory factory;

    protected final HeroAircraft heroAircraft;
    protected final List<AbstractAircraft> enemyAircrafts;
    protected final List<BaseBullet> heroBullets;
    protected final List<BaseBullet> enemyBullets;
    protected final List<AbstractProp> props;
    protected BossEnemy boss;

    /**
     * 时间相关参数
     */
    protected int timeInterval = 40;
    protected int cycleDuration = 600;
    private int cycleTime = 0;
    private int time = 0;

    /**
     * 难度相关参数
     */
    protected int enemyMaxNumber = 4;
    protected double probability;
    protected int threshold;
    protected int BOSS_ENEMY_HP;

    public Game(Context context, Handler handler) throws IOException, ClassNotFoundException {
        super(context);
        this.context = context;
        this.handler = handler;

        mbLoop = true;
        mPaint = new Paint();  //设置画笔
        surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);
        this.setFocusable(true);
        ImageManager.initImage(context);

        // 初始化英雄机
        HeroAircraft.refreshHero();
        heroAircraft = HeroAircraft.getHeroAircraft();

        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        props = new LinkedList<>();

        //Scheduled 线程池，用于定时任务调度
        ThreadFactory gameThread = r -> {
            Thread t =new Thread(r);
            t.setName("game thread");
            return t;
        };
        executorService = new ScheduledThreadPoolExecutor(1, gameThread);

        surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);
        this.setFocusable(true);

        //启动触摸监听
        this.setOnTouchListener(new HeroController(this,heroAircraft));

        music = new MusicService(context);

        if(needMusic){
            music.playBgm();
        }
    }


    /**
     * 游戏启动入口，执行游戏逻辑
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void action() {

        //new Thread(new Runnable() {
        Runnable task = () -> {

            time += timeInterval;

            // 周期性执行（控制频率）
            if (timeCountAndNewCycleJudge()) {
                System.out.println(time);

                // 新敌机产生
                addEnemy();

                // 飞机射出子弹
                shootAction();

            }

            //产生Boss机
            generateBoss();

            // 子弹移动
            bulletsMoveAction();

            // 飞机移动
            aircraftsMoveAction();

            // 道具移动
            propsMoveAction();

            // 撞击检测
            try {
                crashCheckAction();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 后处理
            postProcessAction();

            //每个时刻重绘界面
            //draw();

        };
        new Thread(task).start();
    }

    /**
    public void heroController(){
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                clickX = motionEvent.getX();
                clickY = motionEvent.getY();
                heroAircraft.setLocation(clickX, clickY);

                if ( clickX<0 || clickX> MainActivity.screenWidth || clickY<0 || clickY>MainActivity.screenHeight){
                    // 防止超出边界
                    return false;
                }
                return true;
            }
        });
    }
     */

    /**
     * 控制Boss敌机是否出现
     */
    protected void generateBoss() {
        if(needMusic){
            //music.stopBgm();
            music.playBossBgm();
        }
    }


    protected boolean isBossExist(){
        boolean isInstance = false;
        for (AbstractAircraft enemyAircraft : enemyAircrafts){
            if(enemyAircraft instanceof BossEnemy){
                isInstance = true;
                boss = (BossEnemy) enemyAircraft;
                break;
            }
        }
        return isInstance;
    }

    /**
     * 添加敌机
     */
    protected abstract void addEnemy();


    private boolean timeCountAndNewCycleJudge() {
        cycleTime += timeInterval;
        if (cycleTime >= cycleDuration && cycleTime - timeInterval < cycleTime) {
            // 跨越到新的周期
            cycleTime %= cycleDuration;
            return true;
        } else {
            return false;
        }
    }

    private void shootAction() {
        // 敌机射击
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            enemyBullets.addAll(enemyAircraft.shoot());
        }

        heroBullets.addAll(heroAircraft.shoot());

    }

    private void bulletsMoveAction() {
        for (BaseBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    private void aircraftsMoveAction() {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
    }

    private void propsMoveAction() {
        for (AbstractProp prop : props) {
            prop.forward();
        }
    }

    public static void addScore(int num){
        score = score + num;
    }

    public boolean isGameOverFlag(){
        return gameOverFlag;
    }

    /**
     * 碰撞检测：
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    private void crashCheckAction() throws InterruptedException {
        // 敌机子弹攻击英雄
        for (BaseBullet bullet : enemyBullets) {
            if (bullet.notValid()) {
                continue;
            }
            if (heroAircraft.crash(bullet)) {
                heroAircraft.decreaseHp(bullet.getPower());
                bullet.vanish();
            }
        }

        // 英雄子弹攻击敌机
        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }

            //for (AbstractAircraft enemyAircraft : enemyAircrafts)
            for (int i=0;i<enemyAircrafts.size();i++) {
                if (enemyAircrafts.get(i).notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircrafts.get(i).crash(bullet)) {
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    enemyAircrafts.get(i).decreaseHp(bullet.getPower());

                    bullet.vanish();
                    if (enemyAircrafts.get(i).notValid()) {

                        props.addAll(enemyAircrafts.get(i).dropProps());

                        //分数增加
                        if(enemyAircrafts.get(i) instanceof MobEnemy){
                            addScore(10);
                        }else if(enemyAircrafts.get(i) instanceof EliteEnemy){
                            addScore(20);
                        }else if(enemyAircrafts.get(i) instanceof BossEnemy){
                            addScore(50);

                            if(needMusic){
                                //music.stopBossBgm();
                                music.playBgm();
                            }
                        }

                    }
                }
                // 英雄机 与 敌机 相撞，均损毁
                if (enemyAircrafts.get(i).crash(heroAircraft) || heroAircraft.crash(enemyAircrafts.get(i))) {
                    enemyAircrafts.get(i).vanish();
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
                }
            }
        }

        //我方获得道具，道具生效
        for(AbstractProp prop : props){
            if(prop.notValid()){
                continue;
            }
            if (heroAircraft.crash(prop) || prop.crash(heroAircraft)){
                if(prop instanceof BombProp){

                    for(AbstractAircraft enemy: enemyAircrafts){
                        ((BombProp) prop).addObserver((Observer) enemy);
                    }
                    for(BaseBullet bullet: enemyBullets){
                        ((BombProp) prop).addObserver((Observer) bullet);
                    }

                }

                if (needMusic) {
                    if (prop instanceof BombProp) {
                        music.explosionSP();
                    } else if (prop instanceof BloodProp) {
                        music.supplySP();
                    } else if (prop instanceof BulletProp) {
                        music.hitSP();
                    }
                }

                prop.effect(heroAircraft);
                prop.vanish();
            }
        }

    }

    /**
     * 后处理：
     * 1. 删除无效的子弹
     * 2. 删除无效的敌机
     * 3. 检查英雄机生存
     * <p>
     * 无效的原因可能是撞击或者飞出边界
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        props.removeIf(AbstractFlyingObject::notValid);

//        if (heroAircraft.notValid()) {
//            gameOverFlag = true;
//            mbLoop = false;
//            Log.i(TAG, "heroAircraft is not Valid");
//        }

        // 游戏结束检查英雄机是否存活
        if (heroAircraft.getHp() <= 0) {
            // 游戏结束
            executorService.shutdown();
            gameOverFlag = true;
            System.out.println("Game Over!");

            if(needMusic){
                music.gameOverbgm();
                music.stopBgm();
                music.stopBossBgm();
            }

            if(LauncherActivity.isOnline){
                while(!OnlineActivity.isGameOverFlag()){
                    repaint();
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            }else{
                user = new User();
                user.setScore(score);
                //获取当前时间
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");
                user.setOverTime(formatter.format(date));
            }

            mbLoop = false;

        }

    }


    //***********************
    //      Paint 各部分
    //***********************

    private void repaint() {
        synchronized (surfaceHolder){
            draw();
        }
    }

    public void draw() {
        canvas = surfaceHolder.lockCanvas();
        if(surfaceHolder == null || canvas == null){
            return;
        }

        //绘制背景，图片滚动
        canvas.drawBitmap(backGround,0,this.backGroundTop-backGround.getHeight(),mPaint);
        canvas.drawBitmap(backGround,0,this.backGroundTop,mPaint);
        backGroundTop +=1;
        if (backGroundTop == MainActivity.WINDOW_HEIGHT)
            this.backGroundTop = 0;

        //先绘制子弹，后绘制飞机
        paintImageWithPositionRevised(props); //敌机子弹
        paintImageWithPositionRevised(enemyBullets); //敌机子弹
        paintImageWithPositionRevised(heroBullets);  //英雄机子弹
        paintImageWithPositionRevised(enemyAircrafts);//敌机

        canvas.drawBitmap(ImageManager.HERO_IMAGE,
                heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY()- ImageManager.HERO_IMAGE.getHeight() / 2,
                mPaint);

        //画生命值
        paintScoreAndLife();

        surfaceHolder.unlockCanvasAndPost(canvas);

    }

    private void paintImageWithPositionRevised(List<? extends AbstractFlyingObject> objects) {
        if (objects.size() == 0) {
            return;
        }

        /**
        for (AbstractFlyingObject object : objects) {
            Bitmap image = object.getImage();
            assert image != null : objects.getClass().getName() + " has no image! ";
            canvas.drawBitmap(image, object.getLocationX() - image.getWidth() / 2,
                    object.getLocationY() - image.getHeight() / 2, mPaint);
        }
         */
        for (int i=0; i<objects.size() ;i++) {
            Bitmap image = objects.get(i).getImage();
            assert image != null : objects.getClass().getName() + " has no image! ";
            canvas.drawBitmap(image, objects.get(i).getLocationX() - image.getWidth() / 2,
                    objects.get(i).getLocationY() - image.getHeight() / 2, mPaint);
        }
    }

    private void paintScoreAndLife() {
        int x = 30;
        int y = 60;

        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(55);

        canvas.drawText("SCORE:" + this.score, x, y, mPaint);
        canvas.drawText("LIFE:" + this.heroAircraft.getHp(), x, y + 60, mPaint);
        if(LauncherActivity.isOnline){
            canvas.drawText("OPPONENT NAME:" + OnlineActivity.getOpponentName(), x+450, y, mPaint);
            canvas.drawText("OPPONENT SCORE:" + OnlineActivity.getOpponentScore(), x+450, y+60, mPaint);
        }
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        new Thread(this).start();
        Log.i(TAG, "start surface view thread");
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        MainActivity.WINDOW_WIDTH = i1;
        MainActivity.WINDOW_HEIGHT = i2;
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        mbLoop = false;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void run() {

        while (mbLoop){   //游戏结束停止绘制
            synchronized (surfaceHolder){
                action();
                draw();
            }
        }

        Message message = Message.obtain();
        message.what = 1 ;
        message.obj = user;
        handler.sendMessage(message);
    }
}
