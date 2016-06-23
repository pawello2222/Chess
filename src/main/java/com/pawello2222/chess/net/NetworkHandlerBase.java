package com.pawello2222.chess.net;

import com.pawello2222.chess.model.Spot;

/**
 * Network handler base class.
 *
 * @author Pawel Wiszenko
 */
public abstract class NetworkHandlerBase implements NetworkSender
{
    @Override
    public abstract void sendMove( Spot sourceSpot, Spot targetSpot );

    public abstract void addNetworkReceiver( NetworkReceiver networkReceiver );
}
