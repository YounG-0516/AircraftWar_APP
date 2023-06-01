package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.basic.Observer;

import java.util.ArrayList;
import java.util.List;

public class BombProp extends AbstractProp{

    public BombProp(int locationX, int locationY, double speedX, double speedY){
        super(locationX, locationY, speedX, speedY);
    }

    //观察者列表
    private List<Observer> observerLists = new ArrayList<>();

    //添加观察者
    public void addObserver(Observer observer){
        observerLists.add(observer);
    }

    //通知所有观察者
    public void notifyAllFlyings(){
        for(Observer observerList : observerLists){
            observerList.update();
        }
    }

    @Override
    public void effect(AbstractAircraft aircraft) {

        System.out.println("BombSupply active!");
        notifyAllFlyings();
    };

}
