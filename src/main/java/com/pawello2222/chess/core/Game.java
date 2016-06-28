package com.pawello2222.chess.core;

import com.pawello2222.chess.model.GameState;
import com.pawello2222.chess.model.GameType;
import com.pawello2222.chess.model.Piece;
import com.pawello2222.chess.model.Spot;
import com.pawello2222.chess.net.NetworkHandler;
import com.pawello2222.chess.util.ResourceLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import static com.pawello2222.chess.core.MainFactory.*;
import static com.pawello2222.chess.net.NetworkFactory.*;

/**
 * Main game frame.
 *
 * @author Pawel Wiszenko
 */
class Game extends GameBase
{
    private MainMenu mainMenu;
    private GameHandlerBase gameHandler;
    private MoveListenerBase moveListener;
    private NetworkHandler networkHandler;
    private JPanel board;

    private Game()
    {
        setIconImage( ResourceLoader.loadImageExitOnEx( "ICON.png" ) );

        setResizable( false );
        setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
        final Game game = this;

        addWindowListener( new WindowAdapter()
        {
            @Override
            public void windowClosing( WindowEvent e )
            {
                int confirm = JOptionPane.showOptionDialog(
                        game,
                        "Are you sure you want to quit this game?",
                        "Quit game",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        null,
                        null );
                if ( confirm == 0 )
                {
                    closeNetwork();
                    quit();
                }
            }
        } );
    }

    Game( MainMenu mainMenu, GameType gameType )
    {
        this();
        this.mainMenu = mainMenu;

        initGame( gameType );
        setVisible( true );
        mainMenu.setVisible( false );

        switch ( gameType )
        {
            case LOCAL_WHITE:
            case LOCAL_BLACK:
                setTitle( "Chess - local game" );
                break;

            case ONLINE_WHITE:
                setTitle( "Chess - waiting for connection..." );
                networkHandler = getNetworkServer( this );
                initNetwork();
                networkHandler.start( getPort(), getTimeout() );
                break;

            case ONLINE_BLACK:
                setTitle( "Chess - waiting for connection..." );
                networkHandler = getNetworkClient( this );
                initNetwork();
                networkHandler.start( getInput( "Specify server name:" ), getPort() );
                break;
        }
    }

    private String getInput( String message )
    {
        return "MBA-PW";
//        return JOptionPane.showInputDialog( this, message, "New game", JOptionPane.PLAIN_MESSAGE );
    }

    private int getPort()
    {
        String result = getInput( "Specify port number:" );

        return 2222;
    }

    private int getTimeout()
    {
        String result = getInput( "Specify timeout:" );

        return 6000;
    }

    private void initGame( GameType gameType )
    {
        Image image = ResourceLoader.loadImageExitOnEx( "BOARD.png" );
        boolean reversed = gameType == GameType.LOCAL_BLACK || gameType == GameType.ONLINE_BLACK;
        Spot[][] spots = getSpots( reversed );
        List< Piece > pieces = getPieces( spots );
        board = getBoard( image, spots, pieces );

        gameHandler = getGameHandler( this, spots, pieces, gameType );

        moveListener = getMoveListener( gameHandler, spots );
        board.addMouseListener( moveListener );
        board.addMouseMotionListener( moveListener );

        add( board );
        pack();
        setLocationRelativeTo( mainMenu );
    }

    private void initNetwork()
    {
        gameHandler.setNetworkSender( networkHandler );
        networkHandler.setNetworkReceiver( gameHandler );
    }

    private void closeGame()
    {
        moveListener.setGameHandler( null );

        board.removeMouseListener( moveListener );
        board.removeMouseMotionListener( moveListener );
        remove( board );

        gameHandler.setGame( null );

        mainMenu.setVisible( true );
    }

    private void closeNetwork()
    {
        if ( networkHandler != null )
        {
            gameHandler.setNetworkSender( null );
            networkHandler.setNetworkReceiver( null );
            networkHandler.stop();
        }
    }

    @Override
    public void endOfGame( GameState gameState )
    {
        closeNetwork();

        String title = "Error";
        String message = "Opponent disconnected.";

        if ( gameState == GameState.CHECKMATE_WIN_WHITE )
        {
            title = "Checkmate";
            message = "White player wins!";
        }
        else if ( gameState == GameState.CHECKMATE_WIN_BLACK )
        {
            title = "Checkmate";
            message = "Black player wins!";
        }
        else if ( gameState == GameState.STALEMATE )
        {
            title = "Stalemate";
            message = "It's a draw.";
        }

        displayMessage( title, message );
        quit();
    }

    @Override
    public void quit()
    {
        closeGame();
        dispose();
    }

    @Override
    public void exception( String message )
    {
        displayMessage( "Exception occurred", message );
        closeNetwork();
        quit();
    }

    @Override
    public void displayMessage( String title, String message )
    {
        JOptionPane.showConfirmDialog( this,
                                       message,
                                       title,
                                       JOptionPane.DEFAULT_OPTION,
                                       JOptionPane.PLAIN_MESSAGE );
    }
}
