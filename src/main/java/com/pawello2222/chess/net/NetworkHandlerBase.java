package com.pawello2222.chess.net;

import com.pawello2222.chess.exception.ConnectionException;
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

    public abstract void startServer( int port, int timeout ) throws ConnectionException, InterruptedException;

    public abstract void startClient( String hostname, int port ) throws ConnectionException;

    public abstract void addNetworkReceiver( NetworkReceiver networkReceiver );
}
