package com.pawello2222.chess.core;

/**
 * Exception handler interface.
 *
 * @author Pawel Wiszenko
 */
public interface EventHandler
{
    void message( String title, String message );

    void exception( String message );
}
