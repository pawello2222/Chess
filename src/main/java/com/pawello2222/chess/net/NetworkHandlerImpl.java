package com.pawello2222.chess.net;

import com.pawello2222.chess.core.MessageDisplayer;
import com.pawello2222.chess.exception.NetworkException;
import com.pawello2222.chess.model.NetworkGame;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Network handler implementation class.
 *
 * @author Pawel Wiszenko
 */
class NetworkHandlerImpl extends NetworkHandlerBase
{
    private MessageDisplayer messageDisplayer;
    private NetworkReceiver networkReceiver;

    private volatile Thread listenThread;
    private volatile ServerSocket serverSocket;
    private volatile Socket socket;
    private volatile DataInputStream inputStream;
    private volatile DataOutputStream outputStream;

    NetworkHandlerImpl( MessageDisplayer messageDisplayer )
    {
        this.messageDisplayer = messageDisplayer;
    }

    @Override
    public void start( NetworkGame networkGame, String[] params )
    {
        try
        {
            initNetwork( networkGame, params );
        }
        catch ( NetworkException e )
        {
            displayError( e.getMessage() );
        }
    }

    @Override
    public void stop()
    {
//        messageDisplayer = null;
        networkReceiver.setNetworkSender( null );
//        networkReceiver = null;

        if ( listenThread != null )
            listenThread.interrupt();

        closeSocket();
        closeServerSocket();
    }

    private void initNetwork( NetworkGame networkGame, String[] params )
    {
        if ( networkGame == NetworkGame.SERVER )
        {
            int port = getInt( params[ 0 ] );
            int timeout = getInt( params[ 1 ] );
            if ( port == -1 || timeout == -1 )
                throw new NetworkException( "Invalid connection parameters" );
            else
                initServer( port, timeout );
        }
        else if ( networkGame == NetworkGame.CLIENT )
        {
            int port = getInt( params[ 1 ] );
            if ( port == -1 )
                throw new NetworkException( "Invalid connection parameters" );
            else
                initClient( params[ 0 ], port );
        }
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

    private void initServerSocket( int port, int timeout )
    {
        try
        {
            serverSocket = new ServerSocket( port );
            serverSocket.setSoTimeout( timeout );
        }
        catch ( IOException e )
        {
            throw new NetworkException( "Cannot create a socket on port " + port + " with timeout: " + timeout + "ms." );
        }
    }

    private void closeServerSocket()
    {
        try
        {
            if ( serverSocket != null )
                serverSocket.close();
        }
        catch ( IOException e )
        {
            throw new NetworkException( "Cannot close server socket." );
        }
    }

    private void closeSocket()
    {
        try
        {
            if ( socket != null )
                socket.close();
        }
        catch ( IOException e )
        {
            throw new NetworkException( "Cannot close socket." );
        }
    }

    private void initServer( int port, int timeout ) throws NetworkException
    {
        initServerSocket( port, timeout );
        startServer();
    }

    private void startServer()
    {
        Thread.UncaughtExceptionHandler exceptionHandler = ( thread, e ) -> displayError( e.getMessage() );

        Runnable serverTask = () ->
        {
            try
            {
                socket = serverSocket.accept();
                inputStream = new DataInputStream( socket.getInputStream() );
                outputStream = new DataOutputStream( socket.getOutputStream() );

                messageDisplayer.displayMessage( "Connected with opponent." );
            }
            catch ( IOException e )
            {
                throw new NetworkException( "Cannot connect with opponent." );
            }
        };

        Thread thread = new Thread( serverTask );
        thread.setUncaughtExceptionHandler( exceptionHandler );
        thread.start();
    }

    private void initClient( String serverName, int port ) throws NetworkException
    {
        startClient( serverName, port );
    }

    private void startClient( String serverName, int port )
    {
        try
        {
            socket = new Socket( serverName, port );
            inputStream = new DataInputStream( socket.getInputStream() );
            outputStream = new DataOutputStream( socket.getOutputStream() );

            messageDisplayer.displayMessage( "Connected with opponent." );
            startListening();
        }
        catch ( IOException e )
        {
            throw new NetworkException( "Cannot connect with opponent." );
        }
    }

    @Override
    public void sendData( String data )
    {
        try
        {
            outputStream.writeUTF( data );
            startListening();
        }
        catch ( IOException e )
        {
            displayError( "Cannot send move. Connection lost." );
        }
    }

    private void startListening()
    {
//        Thread.UncaughtExceptionHandler exceptionHandler = ( thread, e ) -> displayError( e.getMessage() );

        Runnable listenTask = () ->
        {
            try
            {
                receiveMove();
            }
            catch ( NetworkException e )
            {
                displayError( e.getMessage() );
            }
        };

        listenThread = new Thread( listenTask );
//        listenThread.setUncaughtExceptionHandler( exceptionHandler );
        listenThread.start();
    }

    private void receiveMove()
    {
        String data;
        try
        {
            data = inputStream.readUTF();
            networkReceiver.receiveData( data );
        }
        catch ( IOException e )
        {
            throw new NetworkException( "Cannot receive opponent move. Connection lost." );
        }
    }

    private void displayError( String error )
    {
        messageDisplayer.displayError( error );
        stop();
    }

    @Override
    public void setNetworkReceiver( NetworkReceiver networkReceiver )
    {
        this.networkReceiver = networkReceiver;
    }
}
