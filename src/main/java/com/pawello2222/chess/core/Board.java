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

    private List< EndGameListener > listeners = new ArrayList<>();

    private BoardManager boardManager;

    private Spot[][] spots;
    private List< Piece > pieces = new ArrayList<>();

    private GameState gameState;

    private boolean reversed;

    Board( boolean reversed )
    {
        this.reversed = reversed;
        this.gameState = GameState.RUNNING_WHITE;

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

        boardManager = new BoardManager( this );

        MoveListener moveListener = new MoveListener( this );
        this.addMouseListener( moveListener );
        this.addMouseMotionListener( moveListener );
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

    void addListener( EndGameListener newListener )
    {
        listeners.add( newListener );
    }

    void endGame()
    {
        listeners.forEach( EndGameListener::endGame );
    }

    public void movePiece( Spot sourceSpot, Spot targetSpot )
    {
        boardManager.movePiece( sourceSpot, targetSpot );
        boardManager.nextTurn( sourceSpot, targetSpot );
    }

    public void setFocusOnPiece( Piece piece )
    {
        pieces.remove( piece );
        pieces.add( piece );
    }

    public void updateValidMoveFlags( Spot spot )
    {
        boardManager.updateValidMoveFlags( spot );
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

    List< Piece > getPieces()
    {
        return pieces;
    }

    void setPieces( List< Piece > pieces )
    {
        this.pieces = pieces;
    }

    GameState getGameState()
    {
        return gameState;
    }

    void setGameState( GameState gameState )
    {
        this.gameState = gameState;
    }

    boolean isReversed()
    {
        return reversed;
    }
}
