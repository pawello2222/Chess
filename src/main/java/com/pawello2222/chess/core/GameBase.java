package com.pawello2222.chess.core;

import com.pawello2222.chess.model.GameState;

import javax.swing.*;

/**
 * Game base class.
 *
 * @author Pawel Wiszenko
 */
abstract class GameBase extends JFrame implements EndOfGameListener, MessageDisplayer
{
    @Override
    public abstract void endOfGame( GameState gameState );

    @Override
    public abstract void displayMessage( String message );

    @Override
    public abstract void displayMessage( String title, String message );

    @Override
    public abstract void displayError( String message );
}
