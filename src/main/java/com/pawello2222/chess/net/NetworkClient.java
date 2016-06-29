package com.pawello2222.chess.net;

import com.pawello2222.chess.core.EventHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Network client class.
 *
 * @author Pawel Wiszenko
 */
class NetworkClient extends NetworkHandler
{
    NetworkClient( EventHandler eventHandler )
    {
        super( eventHandler );
    }

    @Override
    public void start( String serverName, int port )
    {
        if ( !isReceiver() )
            exception( "Unable to start client. Connection failed." );

        connect( serverName, port );
    }

    private void connect( String serverName, int port )
    {
        try
        {
            socket = new Socket( serverName, port );
            inputStream = new DataInputStream( socket.getInputStream() );
            outputStream = new DataOutputStream( socket.getOutputStream() );

            send( "1" );
            listen();
        }
        catch ( IOException e )
        {
            exception( "Unable to connect to opponent." );
        }
    }
}
