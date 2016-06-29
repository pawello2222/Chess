package com.pawello2222.chess.net;

import com.pawello2222.chess.core.EventHandler;

/**
 * Network factory.
 *
 * @author Pawel Wiszenko
 */
public abstract class NetworkFactory
{
    public static NetworkHandler getNetworkServer( EventHandler eventHandler )
    {
        return new NetworkServer( eventHandler );
    }

    public static NetworkHandler getNetworkClient( EventHandler eventHandler )
    {
        return new NetworkClient( eventHandler );
    }
}
