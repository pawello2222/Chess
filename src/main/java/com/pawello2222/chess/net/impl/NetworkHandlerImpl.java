package com.pawello2222.chess.net.impl;

import com.pawello2222.chess.core.EventHandler;
import com.pawello2222.chess.net.NetworkHandler;
import com.pawello2222.chess.net.NetworkReceiver;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Network handler implementation class.
 *
 * @author Pawel Wiszenko
 */
class NetworkHandlerImpl extends NetworkHandler
{
    /**
     * Dependencies
     */
    private EventHandler eventHandler;
    private NetworkReceiver networkReceiver;

    /**
     * Variables
     */
    volatile Socket socket;
    volatile DataInputStream inputStream;
    volatile DataOutputStream outputStream;

    private boolean listen;

    NetworkHandlerImpl( EventHandler eventHandler )
    {
        this.eventHandler = eventHandler;
    }

    @Override
    public void send( String data )
    {
        try
        {
            if ( outputStream != null )
                outputStream.writeUTF( data );
        }
        catch ( IOException e )
        {
            exception( "Connection failed." );
        }
    }

    @Override
    public void start( int port, int timeout ) { }

    @Override
    public void start( String serverName, int port ) { }

    private void receive( String data )
    {
        networkReceiver.receive( data );
    }

    void listen()
    {
        listen = true;

        Runnable listenTask = () ->
        {
            while ( listen )
            {
                try
                {
                    String data = inputStream.readUTF();
                    if ( listen )
                        receive( data );
                }
                catch ( IOException e )
                {
                    if ( listen )
                        exception( "Connection failed." );
                }
            }
        };

        Thread listenThread = new Thread( listenTask );
        listenThread.start();
    }

    @Override
    public void stop( boolean notify )
    {
        listen = false;

        if ( notify )
            send( "0" );

        close( outputStream );
        close( inputStream );
        close( socket );

        eventHandler = null;
    }

    void close( Closeable closeable )
    {
        try
        {
            if ( closeable != null )
                closeable.close();
        }
        catch ( IOException e )
        {
            exception( "Cannot close network module: " + closeable.toString() + "." );
        }
    }

    void exception( String exception )
    {
        eventHandler.exception( exception );
    }

    @Override
    public void setNetworkReceiver( NetworkReceiver networkReceiver )
    {
        this.networkReceiver = networkReceiver;
    }

    boolean isReceiver()
    {
        return networkReceiver != null;
    }
}
