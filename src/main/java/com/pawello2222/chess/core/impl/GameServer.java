package com.pawello2222.chess.core.impl;

import com.pawello2222.chess.core.Application;
import com.pawello2222.chess.model.GameType;

import static com.pawello2222.chess.net.NetworkFactory.getNetworkServer;

/**
 * Server game implementation class.
 *
 * @author Pawel Wiszenko
 */
public class GameServer extends GameOnline
{
    public GameServer( Application application )
    {
        super( application );
    }

    @Override
    public void start( GameType gameType )
    {
        networkHandler = getNetworkServer( this );

        super.start( gameType );

        int port = getPort();
        if ( port == -1 )
            close( "Invalid port number." );
        else
        {
            int timeout = getTimeout();
            if ( timeout == -1 )
                close( "Invalid timeout." );
            else
                networkHandler.start( port, timeout );
        }
    }
}
