package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractAircraft;

public interface AircraftFactory {
    AbstractAircraft createAircraft(double speedX, double speedY, int hp);
}
