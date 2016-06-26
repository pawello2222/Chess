package com.pawello2222.chess.net;

/**
 * Network sender interface.
 *
 * @author Pawel Wiszenko
 */
interface NetworkSender
{
    void sendData( String data );

    void disconnect();
}
