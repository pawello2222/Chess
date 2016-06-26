package com.pawello2222.chess.net;

import com.pawello2222.chess.core.MessageDisplayer;

/**
 * Network handler base class.
 *
 * @author Pawel Wiszenko
 */
public abstract class NetworkHandlerBase implements NetworkSender
{
    @Override
    public abstract void sendData( String data );

    public abstract void initServer( int port, int timeout );

    public abstract void initClient( String serverName, int port );

    public abstract void addNetworkReceiver( NetworkReceiver networkReceiver );

    public abstract void setMessageDisplayer( MessageDisplayer messageDisplayer );
}
