package com.pawello2222.chess.net;

import com.pawello2222.chess.model.Spot;

/**
 * Network sender interface.
 *
 * @author Pawel Wiszenko
 */
public interface NetworkSender
{
    void sendMove( Spot sourceSpot, Spot targetSpot );
}
