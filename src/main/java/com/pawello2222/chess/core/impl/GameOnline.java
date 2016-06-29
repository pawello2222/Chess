package com.pawello2222.chess.core.impl;

import com.pawello2222.chess.core.Application;
import com.pawello2222.chess.model.GameState;
import com.pawello2222.chess.model.GameType;
import com.pawello2222.chess.net.NetworkHandler;

import javax.swing.*;

import static com.pawello2222.chess.util.NetworkUtils.validateInt;

/**
 * Online game base class.
 *
 * @author Pawel Wiszenko
 */
abstract class GameOnline extends GameLocal
{
    /**
     * Dependencies
     */
    NetworkHandler networkHandler;

    GameOnline( Application application )
    {
        super( application );

        setTitle( "Chess - waiting for connection..." );
    }

    @Override
    public void start( GameType gameType )
    {
        super.start( gameType );
        initNetwork();
    }

    private void initNetwork()
    {
        gameHandler.setNetworkSender( networkHandler );
        networkHandler.setNetworkReceiver( gameHandler );
    }

    @Override
    public void end( GameState gameState )
    {
        closeNetwork( gameState == GameState.NETWORK_CLOSE );
        super.end( gameState );
    }

    @Override
    public void close()
    {
        closeNetwork( true );
        closeGame();
    }

    private void closeNetwork( boolean notify )
    {
        gameHandler.setNetworkSender( null );
        networkHandler.setNetworkReceiver( null );
        networkHandler.stop( notify );
    }

    @Override
    public void exception( String message )
    {
        closeNetwork( true );
        message( "Error", message );
        closeGame();
    }

    String getInput( String message )
    {
        return JOptionPane.showInputDialog( this, message, "New game", JOptionPane.PLAIN_MESSAGE );
    }

    int getPort()
    {
        int min = 1024;
        int max = 65535;

        return validateInt( getInput( "Specify port number (range: " + min + "-" + max + "):" ), min, max );
    }

    int getTimeout()
    {
        int min = 1;
        int max = 30;

        int result = validateInt( getInput( "Specify timeout in seconds (range: " + min + "-" + max + "):" ), min, max );
        if ( result != -1 )
            result *= 1000;

        return result;
    }
}
