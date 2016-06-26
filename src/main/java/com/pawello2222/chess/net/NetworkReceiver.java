package com.pawello2222.chess.net;

/**
 * Network receiver interface.
 *
 * @author Pawel Wiszenko
 */
public interface NetworkReceiver
{
    void receiveData( String data );

    void setNetworkSender( NetworkSender networkSender );
}
