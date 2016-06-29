package com.pawello2222.chess.core;

import com.pawello2222.chess.core.impl.*;
import com.pawello2222.chess.model.*;

import java.util.List;

/**
 * Main factory.
 *
 * @author Pawel Wiszenko
 */
public abstract class MainFactory
{
    static Application getApplicationGUI()
    {
        return new MainMenu();
    }

    public static GameBase getLocalGame( Application application )
    {
        return new GameLocal( application );
    }

    public static GameBase getServerGame( Application application )
    {
        return new GameServer( application );
    }

    public static GameBase getClientGame( Application application )
    {
        return new GameClient( application );
    }

    public static GameHandlerBase getGameHandler( GameBase game, MoveValidatorBase moveValidator,
                                                  Spot[][] spots, List< Piece > pieces, GameType gameType )
    {
        return new GameHandlerImpl( game, moveValidator, spots, pieces, gameType );
    }

    public static MoveListenerBase getMoveListener( GameHandlerBase gameHandler, Spot[][] spots )
    {
        return new MoveListenerImpl( gameHandler, spots );
    }

    public static MoveValidatorBase getMoveValidator( Spot[][] spots )
    {
        return new MoveValidatorImpl( spots );
    }
}
