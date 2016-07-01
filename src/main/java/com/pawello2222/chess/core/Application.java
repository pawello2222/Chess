package com.pawello2222.chess.core;

import java.awt.*;

/**
 * Main class.
 *
 * @author Pawel Wiszenko
 */
public abstract class Application
{
    public static void main( String[] args )
    {
        EventQueue.invokeLater( GUIFactory::getApplicationFrame );
    }
}

// todo: add verification of null dependencies (everywhere)
