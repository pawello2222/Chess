package com.pawello2222.chess.net;

import com.pawello2222.chess.model.NetworkGame;

/**
 * Network handler base class.
 *
 * @author Pawel Wiszenko
 */
public abstract class NetworkHandlerBase implements NetworkSender
{
    @Override
    public abstract void sendData( String data );

    @Override
    public abstract void setNetworkReceiver( NetworkReceiver networkReceiver );

    public abstract void start( NetworkGame networkGame, String[] params );

    public abstract void stop();
}
