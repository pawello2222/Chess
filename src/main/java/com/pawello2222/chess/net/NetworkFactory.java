package com.pawello2222.chess.net;

import com.pawello2222.chess.core.ExceptionHandler;

/**
 * Network factory.
 *
 * @author Pawel Wiszenko
 */
public abstract class NetworkFactory
{
    public static NetworkHandler getNetworkServer( ExceptionHandler exceptionHandler )
    {
        return new NetworkServer( exceptionHandler );
    }

    public static NetworkHandler getNetworkClient( ExceptionHandler exceptionHandler )
    {
        return new NetworkClient( exceptionHandler );
    }
}
