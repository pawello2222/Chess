package com.pawello2222.chess.net;

import com.pawello2222.chess.core.MessageDisplayer;

/**
 * Network factory.
 *
 * @author Pawel Wiszenko
 */
public abstract class NetworkFactory
{
    public static NetworkHandlerBase getNetworkHandler( MessageDisplayer messageDisplayer )
    {
        return new NetworkHandlerImpl( messageDisplayer );
    }
}
