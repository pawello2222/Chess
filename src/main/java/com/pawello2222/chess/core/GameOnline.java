package com.pawello2222.chess.core;

import com.pawello2222.chess.model.GameState;
import com.pawello2222.chess.model.GameType;
import com.pawello2222.chess.net.NetworkHandler;

import javax.swing.*;

/**
 * Online game.
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
        closeNetwork();
        super.end( gameState );
    }

    @Override
    void close()
    {
        closeNetwork();
        closeGame();
    }

    private void closeNetwork()
    {
        gameHandler.setNetworkSender( null );
        networkHandler.setNetworkReceiver( null );
        networkHandler.stop();
    }

    @Override
    public void exception( String message )
    {
        closeNetwork();
        message( "Error", message );
        closeGame();
    }





    //todo: refactor

    String getInput( String message )
    {
//        return "MBA-PW";
        return JOptionPane.showInputDialog( this, message, "New game", JOptionPane.PLAIN_MESSAGE );
    }

    int getPort()
    {
        String result = getInput( "Specify port number:" );

        return 2222;
    }

    int getTimeout()
    {
        String result = getInput( "Specify timeout:" );

        return 6000;
    }
}
