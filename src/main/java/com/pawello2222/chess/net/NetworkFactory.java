package com.pawello2222.chess.net;

import com.pawello2222.chess.core.GameHandlerBase;
import com.pawello2222.chess.core.MessageDisplayer;
import com.pawello2222.chess.model.NetworkGame;

/**
 * << Class Name >>.
 *
 * @author Pawel Wiszenko
 */
public abstract class NetworkFactory
{
    public static boolean initNetworkHandler( MessageDisplayer messageDisplayer,
                                           GameHandlerBase boardHandler,
                                           NetworkGame networkGame,
                                           String[] params )
    {
        if ( networkGame == NetworkGame.DISABLED )
            return false;

        NetworkHandlerBase networkHandler = getNetworkHandler( networkGame );
        networkHandler.setMessageDisplayer( messageDisplayer );
        networkHandler.addNetworkReceiver( boardHandler );
        boardHandler.setNetworkHandler( networkHandler );

        if ( networkGame == NetworkGame.SERVER )
        {
            int port = getInt( params[ 0 ] );
            int timeout = getInt( params[ 1 ] );
            if ( port == -1 || timeout == -1 )
                return false;
            networkHandler.initServer( port, timeout );
        }
        else if ( networkGame == NetworkGame.CLIENT )
        {
            int port = getInt( params[ 1 ] );
            if ( port == -1 )
                return false;
            networkHandler.initClient( params[ 0 ], port );
        }

        return true;
    }

    private static int getInt( String param )
    {
        int result;

        try
        {
            result = Integer.parseInt( param );
        }
        catch ( NumberFormatException e )
        {
            result = -1;
        }

        return result;
    }

    private static NetworkHandlerBase getNetworkHandler( NetworkGame networkGame )
    {
        return new NetworkHandlerImpl( networkGame );
    }
}
