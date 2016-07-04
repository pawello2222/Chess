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
        EventQueue.invokeLater( AppFactory::getAppFrame );
    }
}

// todo: add verification of null dependencies (everywhere)
// todo: join game -> verify that socket should throw exception