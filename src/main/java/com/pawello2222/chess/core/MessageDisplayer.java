package com.pawello2222.chess.core;

import java.awt.Component;

/**
 * Message displayer interface.
 *
 * @author Pawel Wiszenko
 */
public interface MessageDisplayer
{
    void displayMessage( String message );

    void displayMessage( String title, String message );

    void displayError( String error );

    Component getComponent();
}
