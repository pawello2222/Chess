package com.pawello2222.chess.net;

/**
 * Network handler base class.
 *
 * @author Pawel Wiszenko
 */
public abstract class NetworkHandlerBase implements NetworkSender
{
    @Override
    public abstract void send( String data );

    public abstract void start( int port, int timeout );

    public abstract void start( String serverName, int port );

    public abstract void stop( boolean notify );

    @Override
    public abstract void setNetworkReceiver( NetworkReceiver networkReceiver );
}
