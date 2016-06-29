package com.pawello2222.chess.net;

import com.pawello2222.chess.core.EventHandler;
import com.pawello2222.chess.net.impl.NetworkClient;
import com.pawello2222.chess.net.impl.NetworkServer;

/**
 * Network factory.
 *
 * @author Pawel Wiszenko
 */
public abstract class NetworkFactory
{
    public static NetworkHandlerBase getNetworkServer( EventHandler eventHandler )
    {
        return new NetworkServer( eventHandler );
    }

    public static NetworkHandlerBase getNetworkClient( EventHandler eventHandler )
    {
        return new NetworkClient( eventHandler );
    }
}
