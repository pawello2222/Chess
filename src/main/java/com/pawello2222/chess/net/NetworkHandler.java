package com.pawello2222.chess.net;

import com.pawello2222.chess.core.ExceptionHandler;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Network handler base class.
 *
 * @author Pawel Wiszenko
 */
public abstract class NetworkHandler implements NetworkSender
{
    /**
     * Dependencies
     */
    private ExceptionHandler exceptionHandler;
    private NetworkReceiver networkReceiver;

    /**
     * Variables
     */
    volatile Socket socket;
    volatile DataInputStream inputStream;
    volatile DataOutputStream outputStream;

    private boolean listen;

    NetworkHandler( ExceptionHandler exceptionHandler )
    {
        this.exceptionHandler = exceptionHandler;
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

    public void start( int port, int timeout ) { }

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

    public void stop()
    {
        listen = false;

        send( "0" );

        close( outputStream );
        close( inputStream );
        close( socket );

        exceptionHandler = null;
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
        exceptionHandler.exception( exception );
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
