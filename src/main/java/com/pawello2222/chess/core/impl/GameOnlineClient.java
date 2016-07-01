package com.pawello2222.chess.core.impl;

import com.pawello2222.chess.core.Application;
import com.pawello2222.chess.model.GameType;

import static com.pawello2222.chess.net.NetworkFactory.getNetworkClient;

/**
 * Client game implementation class.
 *
 * @author Pawel Wiszenko
 */
public class GameOnlineClient extends GameOnlineBase
{
    public GameOnlineClient( Application application )
    {
        super( application );
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
                close( "Invalid timeout." );
            else
                networkHandler.start( serverName, port );
        }
    }
}
