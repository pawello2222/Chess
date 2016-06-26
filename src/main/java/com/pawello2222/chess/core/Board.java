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
class Board extends JPanel
{
    static final int BOARD_OFFSET_X = 10;
    static final int BOARD_OFFSET_Y = 10;

    private Image bgImage;
    private Spot[][] spots;
    private List< Piece > pieces = new ArrayList<>();

    Board( Image bgImage, Spot[][] spots, List< Piece > pieces )
    {
        this.bgImage = bgImage;
        this.spots = spots;
        this.pieces = pieces;

        setPreferredSize( new Dimension( bgImage.getWidth( this ),
                                         bgImage.getHeight( this ) ) );
    }

    @Override
    protected void paintComponent( Graphics graphics )
    {
        graphics.drawImage( bgImage, 0, 0, null );

        for ( Spot[] row : spots )
            for ( Spot spot : row )
            {
                if ( spot.isValidMoveFlag() )
                    drawHighlightedRect( graphics, spot, Color.GREEN, 4 );

                if ( spot.isLastMoveFlag() )
                    drawHighlightedRect( graphics, spot, Color.YELLOW, 2 );

                if ( spot.isCheckFlag() )
                    drawHighlightedRect( graphics, spot, Color.RED, 6 );
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

    void setMoveListener( MoveListenerBase moveListener )
    {
        addMouseListener( moveListener );
        addMouseMotionListener( moveListener );
    }
}
