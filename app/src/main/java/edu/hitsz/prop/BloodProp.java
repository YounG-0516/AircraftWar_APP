package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractAircraft;


public class BloodProp extends AbstractProp{

    public BloodProp(int locationX, int locationY, double speedX, double speedY){
        super(locationX, locationY, speedX, speedY);
    }

    //加血道具功能，每次增加20点血量
    @Override
    public void effect(AbstractAircraft aircraft){

        System.out.println("BloodSupply active!");
        if(aircraft.getHp() < aircraft.getMaxHp()){
            aircraft.decreaseHp(-20);
        }
    }

}
