package com.pawello2222.chess.core;

import com.pawello2222.chess.model.GameType;

import static com.pawello2222.chess.net.NetworkFactory.getNetworkServer;

/**
 * Server game.
 *
 * @author Pawel Wiszenko
 */
class GameServer extends GameOnline
{
    GameServer( Application application )
    {
        super( application );
    }

    @Override
    public void start( GameType gameType )
    {
        networkHandler = getNetworkServer( this );

        super.start( gameType );

        networkHandler.start( getPort(), getTimeout() );
    }
}
