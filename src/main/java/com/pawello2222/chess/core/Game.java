package com.pawello2222.chess.core;

import com.pawello2222.chess.model.GameState;
import com.pawello2222.chess.model.NetworkGame;
import com.pawello2222.chess.model.Piece;
import com.pawello2222.chess.model.Spot;
import com.pawello2222.chess.utils.ResourceLoader;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static com.pawello2222.chess.core.MainFactory.*;

/**
 * Main game frame.
 *
 * @author Pawel Wiszenko
 */
class Game extends GameBase
{
    private MainMenu mainMenu;
    private GameHandlerBase gameHandler;
    private JPanel board;
    private Spot[][] spots;
    private List< Piece > pieces;

    private Game()
    {
        setTitle( "Chess" );
        setIconImage( ResourceLoader.loadImageExitOnEx( "ICON.png" ) );

        setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        setLocationRelativeTo( null );
        setResizable( false );
        setVisible( true );
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
        spots = getSpots( reversed );
        pieces = getPieces( spots );
        board = getBoard( image, spots, pieces );
        gameHandler = getGameHandler( this, spots, pieces );

        MoveListenerBase moveListener = getMoveListener( gameHandler, spots );
        board.addMouseListener( moveListener );
        board.addMouseMotionListener( moveListener );
    }

    private void initNetwork( NetworkGame networkGame, String[] params )
    {

    }

    @Override
    public void endOfGame( GameState gameState )
    {
        Object[] options = { "Yes (White)", "Yes (Black)", "No" };
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

        displayMessage( title, message );
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
    }
}
