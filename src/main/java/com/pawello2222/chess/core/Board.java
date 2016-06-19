package com.pawello2222.chess.core;

import com.pawello2222.chess.exception.InvalidResourceException;
import com.pawello2222.chess.model.*;
import com.pawello2222.chess.service.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Board panel.
 *
 * @author Pawel Wiszenko
 */
public class Board extends JPanel
{
    static final int BOARD_OFFSET_X = 10;
    static final int BOARD_OFFSET_Y = 10;

    private Image bgImage;

    private IMoveValidator moveValidator;

    private Spot[][] spots;
    private List< Piece > pieces = new ArrayList<>();

    private GameState gameState;

    private boolean reversed;

    Board( boolean reversed )
    {
        this.reversed = reversed;

        BoardCreator boardCreator = new BoardCreator( this );

        try
        {
            boardCreator.initializeBoard( "BOARD.png" );
        }
        catch ( InvalidResourceException e )
        {
            System.out.println( e.getMessage() );
            System.exit( -1 );
        }

        MoveListener moveListener = new MoveListener( this );
        this.addMouseListener( moveListener );
        this.addMouseMotionListener( moveListener );

        moveValidator = new MoveValidator( spots );
    }

    @Override
    protected void paintComponent( Graphics graphics )
    {
        graphics.drawImage( bgImage, 0, 0, null );

        for ( int column = 0; column < 8; column++ )
            for ( int row = 0; row < 8; row++ )
            {
                if ( spots[ column ][ row ].isValidMoveFlag() )
                    drawHighlightedRect( graphics, spots[ column ][ row ], Color.GREEN, 4 );

                if ( spots[ column ][ row ].isLastMoveFlag() )
                    drawHighlightedRect( graphics, spots[ column ][ row ], Color.YELLOW, 2 );

                if ( spots[ column ][ row ].isCheckFlag() )
                    drawHighlightedRect( graphics, spots[ column ][ row ], Color.RED, 6 );
            }

        for ( Piece piece : pieces )
            graphics.drawImage( piece.getImage(), piece.getX(), piece.getY(), null );
    }

    private void drawHighlightedRect( Graphics graphics, Spot spot, Color color, int offset )
    {
        graphics.setColor( color );
        graphics.drawRoundRect( spot.getX() + offset, spot.getY() + offset,
                                Spot.SPOT_WIDTH - 2 * offset, Spot.SPOT_HEIGHT - 2 * offset, 10, 10 );
    }

    public void clearAllFlags()
    {
        clearFlags( FlagType.VALID_MOVE );
        clearFlags( FlagType.LAST_MOVE );
        clearFlags( FlagType.CHECK );
        clearFlags( FlagType.EN_PASSANT );
    }

    private void clearFlags( FlagType flagType )
    {
        for ( int column = 0; column < 8; column++ )
            for ( int row = 0; row < 8; row++ )
                switch( flagType )
                {
                    case VALID_MOVE:
                        spots[ column ][ row ].setValidMoveFlag( false );
                        break;

                    case LAST_MOVE:
                        spots[ column ][ row ].setLastMoveFlag( false );
                        break;

                    case CHECK:
                        spots[ column ][ row ].setCheckFlag( false );
                        break;

                    case EN_PASSANT:
                        spots[ column ][ row ].setEnPassantFlag( false );
                        break;
                }
    }

    public void updatePossibleMoves( Spot spot )
    {
        clearFlags( FlagType.VALID_MOVE );
        moveValidator.validateMovesForSpot( spot );
    }

    public void nextTurn()
    {
        moveValidator.updateCheckFlag();

        for ( Piece piece : this.getPieces() )
            piece.setActive( !piece.isActive() );

        int possibleMoves = 0;

        for ( int column = 0; column < 8; column++ )
            for ( int row = 0; row < 8; row++ )
            {
                possibleMoves += moveValidator.countMovesForSpot( spots[ column ][ row ] );
                clearFlags( FlagType.VALID_MOVE );
            }

        if ( possibleMoves > 0 )
            switchPlayer();
        else
            endGame();
    }

    private void switchPlayer()
    {
        if ( gameState == GameState.RUNNING_WHITE )
            gameState = GameState.RUNNING_BLACK;
        else if ( gameState == GameState.RUNNING_BLACK )
            gameState = GameState.RUNNING_WHITE;
    }

    private void endGame()
    {
        if ( gameState == GameState.RUNNING_WHITE )
            gameState = GameState.CHECKMATE_WIN_WHITE;
        else if ( gameState == GameState.RUNNING_BLACK )
            gameState = GameState.CHECKMATE_WIN_BLACK;

        for ( Piece piece : this.getPieces() )
            piece.setActive( false );

        System.out.println( "END" );
    }

    void setBgImage( Image bgImage )
    {
        this.bgImage = bgImage;
    }

    public Spot[][] getSpots()
    {
        return spots;
    }

    void setSpots( Spot[][] spots )
    {
        this.spots = spots;
    }

    public List< Piece > getPieces()
    {
        return pieces;
    }

    void setPieces( List< Piece > pieces )
    {
        this.pieces = pieces;
    }

    public GameState getGameState()
    {
        return gameState;
    }

    public void setGameState( GameState gameState )
    {
        this.gameState = gameState;
    }

    boolean isReversed()
    {
        return reversed;
    }
}
