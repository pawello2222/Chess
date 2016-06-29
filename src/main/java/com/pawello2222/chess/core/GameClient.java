package com.pawello2222.chess.core;

import com.pawello2222.chess.model.GameType;

import static com.pawello2222.chess.net.NetworkFactory.getNetworkClient;

/**
 * Client game.
 *
 * @author Pawel Wiszenko
 */
class GameClient extends GameOnline
{
    GameClient( Application application )
    {
        super( application );
    }

    @Override
    public void start( GameType gameType )
    {
        networkHandler = getNetworkClient( this );

        super.start( gameType );

        networkHandler.start( getInput( "Specify server name:" ), getPort() );
    }
}
