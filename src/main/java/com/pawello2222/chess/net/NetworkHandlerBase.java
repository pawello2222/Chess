package com.pawello2222.chess.net;

import com.pawello2222.chess.core.MessageDisplayer;
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

    public abstract void initServer( int port, int timeout );

    public abstract void initClient( String serverName, int port );

    public abstract void addNetworkReceiver( NetworkReceiver networkReceiver );

    public abstract void setMessageDisplayer( MessageDisplayer messageDisplayer );
}
