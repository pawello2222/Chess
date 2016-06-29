package com.pawello2222.chess.net.impl;

import com.pawello2222.chess.core.EventHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;

/**
 * Network server class.
 *
 * @author Pawel Wiszenko
 */
public class NetworkServer extends NetworkHandlerImpl
{
    /**
     * Variables
     */
    private volatile ServerSocket serverSocket;
    private boolean connect;

    public NetworkServer( EventHandler eventHandler )
    {
        super( eventHandler );
    }

    @Override
    public void start( int port, int timeout )
    {
        if ( !isReceiver() )
            exception( "Unable to start server. Connection failed." );

        initServer( port, timeout );
        if ( serverSocket != null )
            connect();
    }

    @Override
    public void stop()
    {
        connect = false;

        close( serverSocket );

        super.stop();
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
            exception( "Unable to create a server socket for port " + port + "." );
        }
    }

    private void connect()
    {
        connect = true;

        Runnable serverTask = () ->
        {
            try
            {
                socket = serverSocket.accept();
                inputStream = new DataInputStream( socket.getInputStream() );
                outputStream = new DataOutputStream( socket.getOutputStream() );

                send( "1" );
                listen();
            }
            catch ( IOException e )
            {
                if ( connect )
                    exception( "Unable to connect to opponent." );
            }
        };

        Thread serverThread = new Thread( serverTask );
        serverThread.start();
    }
}
