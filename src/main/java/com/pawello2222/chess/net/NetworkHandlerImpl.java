package com.pawello2222.chess.net;

import com.pawello2222.chess.model.Spot;

import java.util.ArrayList;
import java.util.List;

/**
 * Network handler implementation class.
 *
 * @author Pawel Wiszenko
 */
public class NetworkHandlerImpl extends NetworkHandlerBase
{
    private List< NetworkReceiver > networkReceivers;

    public NetworkHandlerImpl()
    {
        networkReceivers = new ArrayList<>();

    }

    @Override
    public void sendMove( Spot sourceSpot, Spot targetSpot )
    {

    }

    public void receiveMove( Spot sourceSpot, Spot targetSpot )
    {
        for ( NetworkReceiver networkReceiver : networkReceivers )
            networkReceiver.receiveMove( sourceSpot, targetSpot );
    }

    @Override
    public void addNetworkReceiver( NetworkReceiver networkReceiver )
    {
        networkReceivers.add( networkReceiver );
    }
}
