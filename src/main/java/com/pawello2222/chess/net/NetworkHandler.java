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
public abstract class NetworkHandler implements NetworkSender
{
    protected ExceptionHandler exceptionHandler;
    private NetworkReceiver networkReceiver;

    volatile Socket socket;
    volatile DataInputStream inputStream;
    volatile DataOutputStream outputStream;

    private boolean listen;

    NetworkHandler( ExceptionHandler exceptionHandler )
    {
        this.exceptionHandler = exceptionHandler;

        socket = null;
        inputStream = null;
        outputStream = null;
    }

    @Override
    public void send( String data )
    {
        try
        {
            outputStream.writeUTF( data );
        }
        catch ( IOException e )
        {
            exception( "Cannot send data. Connection lost." );
        }
    }

    private void receive( String data )
    {
        networkReceiver.receive( data );
    }

    protected void listen()
    {
        listen = true;

        Runnable listenTask = () ->
        {
            while ( listen )
            {
                try
                {
                    String data = inputStream.readUTF();
                    receive( data );
                }
                catch ( IOException e )
                {
                    exception( "Cannot receive data. Connection lost." );
                }
            }
        };

        Thread listenThread = new Thread( listenTask );
        listenThread.start();
    }

    public void stop()
    {
        listen = false;

        try
        {
            if ( socket != null )
            {
                send( "Q" );
                socket.close();
            }
        }
        catch ( IOException e )
        {
            exception( "Cannot close socket" );
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
}
