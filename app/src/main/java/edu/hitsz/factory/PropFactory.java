package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.prop.AbstractProp;

public interface PropFactory {
    AbstractProp createProp(int locationX, int locationY);
}
