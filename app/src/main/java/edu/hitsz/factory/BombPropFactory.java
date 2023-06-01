package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.prop.BombProp;

public class BombPropFactory implements PropFactory{
    @Override
    public AbstractProp createProp(int locationX, int locationY) {
        return new BombProp(locationX, locationY ,0,5);
    }
}
