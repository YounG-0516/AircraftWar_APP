package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.strategy.ScatterShootStrategy;
import edu.hitsz.strategy.StraightShootStrategy;
public class BulletProp extends AbstractProp{

    private boolean effectCalled = false;
    public BulletProp(int locationX, int locationY, double speedX, double speedY){
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public synchronized void effect(AbstractAircraft aircraft){
        System.out.println("BulletSupply active!");
        if(!effectCalled){
            effectCalled = true;
            //定义新线程
            Runnable r = ()->{
                aircraft.setStrategy(new ScatterShootStrategy());

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                aircraft.setStrategy(new StraightShootStrategy());
            };

            //启动新线程
            new Thread(r).start();
        }
    }
}
