package com.pawello2222.chess.core;

import com.pawello2222.chess.core.impl.*;
import com.pawello2222.chess.model.*;

import javax.swing.*;
import java.util.List;

/**
 * Main factory.
 *
 * @author Pawel Wiszenko
 */
public abstract class MainFactory
{
    public static GameBase getLocalGame( JFrame application )
    {
        return new Game( application );
    }

    public static GameBase getServerGame( JFrame application )
    {
        return new OnlineGameServer( application );
    }

    public static GameBase getClientGame( JFrame application )
    {
        return new OnlineGameClient( application );
    }

    public static GameHandlerBase getGameHandler( GameBase game, MoveValidatorBase moveValidator,
                                                  Spot[][] spots, List< Piece > pieces, GameType gameType )
    {
        return new GameHandler( game, moveValidator, spots, pieces, gameType );
    }

    public static MoveListenerBase getMoveListener( GameHandlerBase gameHandler, Spot[][] spots )
    {
        return new MoveListener( gameHandler, spots );
    }

    public static MoveValidatorBase getMoveValidator( Spot[][] spots )
    {
        return new MoveValidator( spots );
    }
}
