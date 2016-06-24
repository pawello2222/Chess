package com.pawello2222.chess.net;

import com.pawello2222.chess.core.MessageDisplayer;
import com.pawello2222.chess.exception.NetworkException;
import com.pawello2222.chess.model.NetworkGame;
import com.pawello2222.chess.model.Spot;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Network handler implementation class.
 *
 * @author Pawel Wiszenko
 */
class NetworkHandlerImpl extends NetworkHandlerBase
{
    private List< NetworkReceiver > networkReceivers;
    private MessageDisplayer messageDisplayer;

    private NetworkGame networkGame;

    private volatile Thread activeThread;
    private volatile Thread listenThread;
    private volatile ServerSocket serverSocket;
    private volatile Socket socket;
    private volatile DataInputStream inputStream;
    private volatile DataOutputStream outputStream;

    NetworkHandlerImpl( NetworkGame networkGame )
    {
        networkReceivers = new ArrayList<>();
        this.networkGame = networkGame;
    }

    @Override
    public void sendMove( Spot sourceSpot, Spot targetSpot )
    {
        try
        {
            outputStream.writeUTF( "1113" );
        }
        catch ( IOException e )
        {
            messageDisplayer.displayError( "Output error" );
        }
    }

    private void initServerSocket( int port, int timeout ) throws NetworkException
    {
        try
        {
            serverSocket = new ServerSocket( port );
            serverSocket.setSoTimeout( timeout );
        }
        catch ( IOException e )
        {
            throw new NetworkException( "Cannot create a socket on port " + port + " with timeout: " + timeout );
        }
    }

    private void closeServerSocket() throws NetworkException
    {
        try
        {
            if ( serverSocket != null )
                serverSocket.close();
        }
        catch ( IOException e )
        {
            throw new NetworkException( "Cannot close server socket" );
        }
    }

    private void closeSocket() throws NetworkException
    {
        try
        {
            if ( socket != null )
                socket.close();
        }
        catch ( IOException e )
        {
            throw new NetworkException( "Cannot close socket" );
        }
    }

    @Override
    public void initServer( int port, int timeout )
    {
        try
        {
            initServerSocket( port, timeout );
        }
        catch ( NetworkException e )
        {
            messageDisplayer.displayError( e.getMessage() );
        }

        startServer();
    }

    private void startServer()
    {
        Thread.UncaughtExceptionHandler exceptionHandler = ( thread, e ) ->
        {
            messageDisplayer.displayError( e.getMessage() );
            stopServer();
        };

        Runnable serverTask = () ->
        {
            try
            {
                socket = serverSocket.accept();
                inputStream = new DataInputStream( socket.getInputStream() );
                outputStream = new DataOutputStream( socket.getOutputStream() );

                messageDisplayer.displayMessage( "Connected with opponent" );
                startListening();
            }
            catch ( IOException e )
            {
                throw new NetworkException( "Cannot connect with opponent" );
            }
        };

        activeThread = new Thread( serverTask );
        activeThread.setUncaughtExceptionHandler( exceptionHandler );
        activeThread.start();
    }

    private void stopServer()
    {
        stopListening();
        closeSocket();
        closeServerSocket();
    }

    @Override
    public void initClient( String serverName, int port )
    {
        try
        {
            startClient( serverName, port );
        }
        catch ( NetworkException e )
        {
            messageDisplayer.displayError( e.getMessage() );
            stopClient();
        }
    }

    private void startClient( String serverName, int port )
    {
        try
        {
            socket = new Socket( serverName, port );
            inputStream = new DataInputStream( socket.getInputStream() );
            outputStream = new DataOutputStream( socket.getOutputStream() );

            messageDisplayer.displayMessage( "Connected with opponent" );
            startListening();
        }
        catch ( IOException e )
        {
            throw new NetworkException( "Cannot connect with opponent" );
        }
    }

    private void stopClient()
    {
        stopListening();
        closeSocket();
    }

    private void startListening()
    {
        Runnable listenTask = () ->
        {
            try
            {
                receiveMove();
            }
            catch ( NetworkException e )
            {
                messageDisplayer.displayError( e.getMessage() );
            }
        };

        listenThread = new Thread( listenTask );
        listenThread.start();
    }

    private void stopListening()
    {
        if ( listenThread != null )
            listenThread.interrupt();
    }

    private void receiveMove()
    {
        String input = null;
        try
        {
            input = inputStream.readUTF();
        }
        catch ( IOException e )
        {
            throw new NetworkException( "Input error" );
        }

        int[] sourceSpot = { 1, 1 };
        int[] targetSpot = { 1, 2 };

        for ( NetworkReceiver networkReceiver : networkReceivers )
            networkReceiver.receiveMove( sourceSpot, targetSpot );
    }

    @Override
    public void addNetworkReceiver( NetworkReceiver networkReceiver )
    {
        networkReceivers.add( networkReceiver );
    }

    @Override
    public void setMessageDisplayer( MessageDisplayer messageDisplayer )
    {
        this.messageDisplayer = messageDisplayer;
    }
}
