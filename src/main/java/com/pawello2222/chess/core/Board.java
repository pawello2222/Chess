package com.pawello2222.chess.core;

import com.pawello2222.chess.exception.InvalidResourceException;
import com.pawello2222.chess.model.Piece;
import com.pawello2222.chess.model.Spot;
import com.pawello2222.chess.service.MoveListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Board class.
 *
 * @author Pawel Wiszenko
 */
public class Board extends JPanel
{
    public static final int BOARD_OFFSET_X = 10;
    public static final int BOARD_OFFSET_Y = 10;

    private Image bgImage;

    private Spot[][] spots;
    private List< Piece > pieces = new ArrayList<>();

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
    }

    @Override
    protected void paintComponent( Graphics graphics )
    {
        graphics.drawImage( bgImage, 0, 0, null );

        for ( Piece piece : pieces )
            graphics.drawImage( piece.getImage(), piece.getX(), piece.getY(), null );
    }

    public boolean isReversed()
    {
        return reversed;
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
}
