package com.pawello2222.chess.core.impl;

import com.pawello2222.chess.model.GameType;

import javax.swing.*;

import static com.pawello2222.chess.net.NetworkFactory.getNetworkClient;

/**
 * Client game implementation class.
 *
 * @author Pawel Wiszenko
 */
public class OnlineGameClient extends OnlineGameBase
{
    public OnlineGameClient( JFrame parentFrame )
    {
        super( parentFrame );
    }

    @Override
    public void start( GameType gameType )
    {
        networkHandler = getNetworkClient( this );

        super.start( gameType );

        String serverName = getInput( "Specify server name:" );
        if ( serverName == null || serverName.equals( "" ) )
            close( "Invalid server name." );
        else
        {
            int port = getPort();
            if ( port == -1 )
                close( "Invalid port number." );
            else
                networkHandler.start( serverName, port );
        }
    }
}
