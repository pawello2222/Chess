package com.pawello2222.chess.core.impl;

import com.pawello2222.chess.core.*;
import com.pawello2222.chess.model.GameState;
import com.pawello2222.chess.model.GameType;
import com.pawello2222.chess.model.Piece;
import com.pawello2222.chess.model.Spot;
import com.pawello2222.chess.util.ResourceLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import static com.pawello2222.chess.core.BoardFactory.*;
import static com.pawello2222.chess.core.MainFactory.*;

/**
 * Game implementation class.
 *
 * @author Pawel Wiszenko
 */
public class Game extends GameBase
{
    /**
     * Dependencies
     */
    GameHandlerBase gameHandler;
    private MoveListenerBase moveListener;
    private JFrame parentFrame;
    private JPanel boardPanel;

    public Game( JFrame parentFrame )
    {
        this.parentFrame = parentFrame;

        setTitle( "Chess - local game" );
        setIconImage( ResourceLoader.loadImageExitOnEx( "ICON.png" ) );

        setResizable( false );
        setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );

        addWindowListener( new WindowAdapter()
        {
            @Override
            public void windowClosing( WindowEvent e )
            {
                int confirm = JOptionPane.showOptionDialog( Game.this,
                        "Are you sure you want to quit this game?", "Quit game",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                        null, null, null );
                if ( confirm == 0 )
                    close();
            }
        } );
    }

    @Override
    public void start( GameType gameType )
    {
        initGame( gameType );

        setLocationRelativeTo( parentFrame );
        setVisible( true );
        parentFrame.setVisible( false );
    }

    private void initGame( GameType gameType )
    {
        boolean reversed = gameType == GameType.LOCAL_BLACK
                           || gameType == GameType.ONLINE_BLACK;

        Image image = ResourceLoader.loadImageExitOnEx( "BOARD.png" );
        Spot[][] spots = getSpots( reversed );
        List< Piece > pieces = getPieces( spots );
        boardPanel = getBoardPanel( image, spots, pieces );

        MoveValidatorBase moveValidator = getMoveValidator( spots );
        gameHandler = getGameHandler( this, moveValidator,spots, pieces, gameType );

        moveListener = getMoveListener( gameHandler, spots );
        boardPanel.addMouseListener( moveListener );
        boardPanel.addMouseMotionListener( moveListener );

        add( boardPanel );
        pack();
    }

    @Override
    public void end( GameState gameState )
    {
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

        message( title, message );
        closeGame();
    }

    @Override
    public void close()
    {
        closeGame();
    }

    void close ( String message )
    {
        message( "Error", message );
        closeGame();
    }

    void closeGame()
    {
        boardPanel.removeMouseListener( moveListener );
        boardPanel.removeMouseMotionListener( moveListener );
        remove( boardPanel );

        moveListener.setGameHandler( null );
        gameHandler.setGame( null );

        parentFrame.setVisible( true );

        dispose();
    }

    @Override
    public void message( String title, String message )
    {
        JOptionPane.showConfirmDialog( this, message, title,
                                       JOptionPane.DEFAULT_OPTION,
                                       JOptionPane.PLAIN_MESSAGE );
    }

    @Override
    public void exception( String message )
    {
        message( "Error", message );
        close();
    }
}
