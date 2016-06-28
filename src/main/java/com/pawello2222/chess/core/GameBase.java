package com.pawello2222.chess.core;

import com.pawello2222.chess.model.GameState;

import javax.swing.*;

/**
 * Game base class.
 *
 * @author Pawel Wiszenko
 */
public abstract class GameBase extends JFrame implements ExceptionHandler
{
    public abstract void setVisible();

    public abstract void endOfGame( GameState gameState );

    public abstract void quit();

    public abstract void displayMessage( String title, String message );

    @Override
    public abstract void exception( String message );
}
