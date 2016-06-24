package com.pawello2222.chess.net;

/**
 * Network receiver interface.
 *
 * @author Pawel Wiszenko
 */
public interface NetworkReceiver
{
    void receiveMove( int[] sourceSpot, int[] targetSpot );

    void dispatchReceiver();
}
