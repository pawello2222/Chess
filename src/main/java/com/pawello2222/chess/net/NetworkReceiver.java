package com.pawello2222.chess.net;

import com.pawello2222.chess.model.Spot;

/**
 * Network receiver interface.
 *
 * @author Pawel Wiszenko
 */
public interface NetworkReceiver
{
    void receiveMove( Spot sourceSpot, Spot targetSpot );
}
