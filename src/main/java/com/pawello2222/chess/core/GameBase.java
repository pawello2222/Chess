package com.pawello2222.chess.core;

import com.pawello2222.chess.model.GameState;
import com.pawello2222.chess.model.GameType;

import javax.swing.*;

/**
 * Game base class.
 *
 * @author Pawel Wiszenko
 */
public abstract class GameBase extends JFrame implements EventHandler
{
    public abstract void start( GameType gameType );

    public abstract void end( GameState gameState );

    public abstract void close();

    @Override
    public abstract void message( String title, String message );

    @Override
    public abstract void exception( String message );
}
