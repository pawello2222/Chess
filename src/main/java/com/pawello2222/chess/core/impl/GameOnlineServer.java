package com.pawello2222.chess.core.impl;

import com.pawello2222.chess.model.GameType;

import javax.swing.*;

import static com.pawello2222.chess.net.NetworkFactory.getNetworkServer;

/**
 * Server game implementation class.
 *
 * @author Pawel Wiszenko
 */
public class GameOnlineServer extends GameOnlineBase
{
    public GameOnlineServer( JFrame parentFrame )
    {
        super( parentFrame );
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
