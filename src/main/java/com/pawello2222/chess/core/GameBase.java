package com.pawello2222.chess.core;

import com.pawello2222.chess.model.GameState;
import com.pawello2222.chess.model.GameType;

import javax.swing.*;

/**
 * Game base class.
 *
 * @author Pawel Wiszenko
 */
abstract class GameBase extends JFrame implements EventHandler
{
    public abstract void start( GameType gameType );

    public abstract void end( GameState gameState );

    abstract void close();

    @Override
    public void message( String title, String message )
    {
        JOptionPane.showConfirmDialog( this, message, title,
                                       JOptionPane.DEFAULT_OPTION,
                                       JOptionPane.PLAIN_MESSAGE );
    }

    @Override
    public abstract void exception( String message );
}
