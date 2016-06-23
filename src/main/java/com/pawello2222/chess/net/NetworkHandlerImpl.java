package com.pawello2222.chess.net;

import com.pawello2222.chess.exception.ConnectionException;
import com.pawello2222.chess.exception.SocketCreationException;
import com.pawello2222.chess.model.NetworkGame;
import com.pawello2222.chess.model.Spot;

import javax.swing.*;
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
public class NetworkHandlerImpl extends NetworkHandlerBase
{
    private List< NetworkReceiver > networkReceivers;

    private NetworkGame networkGame;

    private volatile Thread activeThread;
    private ServerSocket serverSocket;
    private volatile Socket socket;
    private volatile DataInputStream inputStream;
    private volatile DataOutputStream outputStream;

    public NetworkHandlerImpl( NetworkGame networkGame )
    {
        networkReceivers = new ArrayList<>();
        this.networkGame = networkGame;
    }

    @Override
    public void sendMove( Spot sourceSpot, Spot targetSpot )
    {

    }

    @Override
    public void startServer( int port, int timeout ) throws ConnectionException, InterruptedException
    {
        initServerSocket( port, timeout );

        Runnable serverTask = () ->
        {
            try
            {
                socket = serverSocket.accept();
                inputStream = new DataInputStream( socket.getInputStream() );
                outputStream = new DataOutputStream( socket.getOutputStream() );
            }
            catch ( IOException e )
            {
//                JOptionPane.showConfirmDialog( null, "Unable to connect with opponent!",
//                                               "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE );
            }
        };

        activeThread = new Thread( serverTask );
        activeThread.start();
        activeThread.join();
    }

    @Override
    public void startClient( String hostname, int port )
    {

    }

    private void initServerSocket( int port, int timeout ) throws SocketCreationException
    {
        try
        {
            serverSocket = new ServerSocket( port );
            serverSocket.setSoTimeout( timeout );
        }
        catch ( IOException e )
        {
            throw new SocketCreationException( port, timeout );
        }
    }

    private Thread startClient2( String serverName, int port )
    {
        Runnable clientTask = () ->
        {
            try
            {
                socket = new Socket( serverName, port );
                inputStream = new DataInputStream( socket.getInputStream() );
                outputStream = new DataOutputStream( socket.getOutputStream() );
            }
            catch ( IOException e )
            {
                throw new ConnectionException();
            }
        };

        Thread clientThread = new Thread( clientTask );

        clientThread.start();

        return clientThread;
    }

    private void stop()
    {

    }

    private void receiveMove()
    {
        Spot sourceSpot = null;
        Spot targetSpot = null;

        for ( NetworkReceiver networkReceiver : networkReceivers )
            networkReceiver.receiveMove( sourceSpot, targetSpot );
    }

    @Override
    public void addNetworkReceiver( NetworkReceiver networkReceiver )
    {
        networkReceivers.add( networkReceiver );
    }
}
