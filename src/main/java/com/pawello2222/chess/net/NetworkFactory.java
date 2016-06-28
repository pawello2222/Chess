package com.pawello2222.chess.net;

import com.pawello2222.chess.core.GameBase;

/**
 * Network factory.
 *
 * @author Pawel Wiszenko
 */
public abstract class NetworkFactory
{
    public static NetworkHandler getNetworkServer( GameBase game, int port, int timeout )
    {
        return new NetworkServer( game, port, timeout );
    }

    public static NetworkHandler getNetworkClient( GameBase game, String serverName, int port )
    {
        return new NetworkClient( game, serverName, port );
    }
}
