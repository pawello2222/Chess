package com.pawello2222.chess.net;

import com.pawello2222.chess.core.ExceptionHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;

/**
 * << Class Name >>.
 *
 * @author Pawel Wiszenko
 */
class NetworkServer extends NetworkHandler
{
    private volatile ServerSocket serverSocket;
    private volatile Thread serverThread;

    NetworkServer( ExceptionHandler exceptionHandler )
    {
        super( exceptionHandler );
    }

    @Override
    public void start( int port, int timeout )
    {
        initServer( port, timeout );
        connect();
    }

    private void initServer( int port, int timeout )
    {
        try
        {
            serverSocket = new ServerSocket( port );
            serverSocket.setSoTimeout( timeout );
        }
        catch ( IOException e )
        {
            exception( "Cannot create a socket on port " + port + " with timeout: " + timeout + "ms." );
        }
    }

    private void connect()
    {
        Runnable serverTask = () ->
        {
            try
            {
                socket = serverSocket.accept();
                inputStream = new DataInputStream( socket.getInputStream() );
                outputStream = new DataOutputStream( socket.getOutputStream() );

                send( "P" );
                listen();
            }
            catch ( IOException e )
            {
                exception( "Cannot connect with opponent." );
            }
        };

        serverThread = new Thread( serverTask );
        serverThread.start();
    }

    @Override
    public void stop()
    {
        try
        {
            if ( serverSocket != null )
                serverSocket.close();
        }
        catch ( IOException e )
        {
            exception( "Cannot close server" );
        }

        super.stop();
    }
}
