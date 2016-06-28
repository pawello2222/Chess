package com.pawello2222.chess.net;

import com.pawello2222.chess.core.ExceptionHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * << Class Name >>.
 *
 * @author Pawel Wiszenko
 */
class NetworkClient extends NetworkHandler
{

    NetworkClient( ExceptionHandler exceptionHandler )
    {
        super( exceptionHandler );
    }

    @Override
    public void start( String serverName, int port )
    {
        connect( serverName, port );
    }

    private void connect( String serverName, int port )
    {
        try
        {
            socket = new Socket( serverName, port );
            inputStream = new DataInputStream( socket.getInputStream() );
            outputStream = new DataOutputStream( socket.getOutputStream() );

            send( "P" );
            listen();
        }
        catch ( IOException e )
        {
            exception( "Cannot connect with opponent." );
        }
    }
}
