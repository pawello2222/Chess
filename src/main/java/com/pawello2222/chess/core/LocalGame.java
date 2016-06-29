package com.pawello2222.chess.core;

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

import static com.pawello2222.chess.core.MainFactory.*;

/**
 * Game frame.
 *
 * @author Pawel Wiszenko
 */
class LocalGame extends GameBase
{
    private Application application;
    GameHandlerBase gameHandler;
    private MoveListenerBase moveListener;
    private JPanel board;

    LocalGame( Application application )
    {
        this.application = application;

        setTitle( "Chess - local game" );
        setIconImage( ResourceLoader.loadImageExitOnEx( "ICON.png" ) );

        setResizable( false );
        setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );

        addWindowListener( new WindowAdapter()
        {
            @Override
            public void windowClosing( WindowEvent e )
            {
                int confirm = JOptionPane.showOptionDialog( LocalGame.this,
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

        setLocationRelativeTo( application );
        setVisible( true );
        application.setVisible( false );
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
        close();
    }

    @Override
    void close()
    {
        closeGame();
    }

    void closeGame()
    {
        board.removeMouseListener( moveListener );
        board.removeMouseMotionListener( moveListener );
        remove( board );

        moveListener.setGameHandler( null );
        gameHandler.setGame( null );

        application.setVisible( true );

        dispose();
    }

    @Override
    public void exception( String message )
    {
        message( "Error", message );
        close();
    }
}