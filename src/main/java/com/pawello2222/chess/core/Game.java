package com.pawello2222.chess.core;

import com.pawello2222.chess.model.GameState;
import com.pawello2222.chess.model.NetworkGame;
import com.pawello2222.chess.model.Piece;
import com.pawello2222.chess.model.Spot;
import com.pawello2222.chess.net.NetworkHandlerBase;
import com.pawello2222.chess.util.ResourceLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import static com.pawello2222.chess.core.MainFactory.*;
import static com.pawello2222.chess.net.NetworkFactory.getNetworkHandler;

/**
 * Main game frame.
 *
 * @author Pawel Wiszenko
 */
class Game extends GameBase
{
    private MainMenu mainMenu;
    private GameHandlerBase gameHandler;
    private NetworkHandlerBase networkHandler;
    private JPanel board;

    private Game()
    {
        setTitle( "Chess" );
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
                    quit();
            }
        } );
    }

    Game( MainMenu mainMenu, boolean reversed )
    {
        this();

        this.mainMenu = mainMenu;
        initGame( reversed );
    }

    Game( MainMenu mainMenu, boolean reversed, NetworkGame networkGame, String[] params )
    {
        this( mainMenu, reversed );

        initNetwork( networkGame, params );
    }

    private void initGame( boolean reversed )
    {
        Image image = ResourceLoader.loadImageExitOnEx( "BOARD.png" );
        Spot[][] spots = getSpots( reversed );
        List< Piece > pieces = getPieces( spots );
        board = getBoard( image, spots, pieces );

        gameHandler = getGameHandler( this, spots, pieces );

        MoveListenerBase moveListener = getMoveListener( gameHandler, spots );
        board.addMouseListener( moveListener );
        board.addMouseMotionListener( moveListener );

        add( board );
        pack();
        setLocationRelativeTo( null );
        setVisible( true );
    }

    private void initNetwork( NetworkGame networkGame, String[] params )
    {
        networkHandler = getNetworkHandler( this );

        gameHandler.setNetworkSender( networkHandler );
        networkHandler.setNetworkReceiver( gameHandler );

        networkHandler.start( networkGame, params );
    }

    @Override
    public void endOfGame( GameState gameState )
    {
        String title = "Stalemate";
        String message = "It's a draw.";

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

        quit();
        displayMessage( title, message );
        mainMenu.setVisible( true );
        dispose();
    }

    private void quit()
    {
        if ( networkHandler != null )
        {
            networkHandler.stop();
            networkHandler = null;
        }

        gameHandler = null;
        remove( board );
    }

    @Override
    public void displayMessage( String message )
    {
        displayMessage( "Message", message );
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

    @Override
    public void displayError( String message )
    {
        JOptionPane.showConfirmDialog( this,
                                       message,
                                       "Error",
                                       JOptionPane.DEFAULT_OPTION,
                                       JOptionPane.ERROR_MESSAGE );

        quit();
    }
}
