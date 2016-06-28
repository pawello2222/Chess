package com.pawello2222.chess.net;

/**
 * Network sender interface.
 *
 * @author Pawel Wiszenko
 */
public interface NetworkSender
{
    void send( String data );

    void setNetworkReceiver( NetworkReceiver networkReceiver );
}
