package com.pawello2222.chess.core;

import com.pawello2222.chess.model.*;

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

    private List< GameEndListener > gameEndListeners;

    private Image bgImage;
    private Spot[][] spots;
    private List< Piece > pieces = new ArrayList<>();
    private GameState gameState;

    Board( Image bgImage, Spot[][] spots, List< Piece > pieces )
    {
        this.gameEndListeners = new ArrayList<>();

        this.bgImage = bgImage;
        this.spots = spots;
        this.pieces = pieces;
        this.gameState = GameState.RUNNING_WHITE;

        setPreferredSize( new Dimension( bgImage.getWidth( this ),
                                         bgImage.getHeight( this ) ) );
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

    void endGame()
    {
        gameEndListeners.forEach( GameEndListener::endGame );
    }

    void addGameEndListener( GameEndListener listener )
    {
        gameEndListeners.add( listener );
    }

    void setMoveListener( MoveListenerBase moveListener )
    {
        addMouseListener( moveListener );
        addMouseMotionListener( moveListener );
    }

    GameState getGameState()
    {
        return gameState;
    }

    void setGameState( GameState gameState )
    {
        this.gameState = gameState;
    }
}
