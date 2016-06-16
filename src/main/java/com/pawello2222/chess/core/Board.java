package com.pawello2222.chess.core;

import com.pawello2222.chess.exception.InvalidResourceException;
import com.pawello2222.chess.model.Piece;
import com.pawello2222.chess.model.Spot;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
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
    public static final int TILE_OFFSET_X = 50;
    public static final int TILE_OFFSET_Y = 50;

    private BoardCreator boardCreator;

    private Image bgImage;

    private Spot[][] spots;
    private List< Piece > pieces = new ArrayList< Piece >();

    private boolean reversed;

    public Board( boolean reversed )
    {
        this.reversed = reversed;

        this.boardCreator = new BoardCreator( this.reversed );
        bgImage = loadResource( "BOARD.png" );
        this.setPreferredSize( new Dimension( bgImage.getWidth( this ),
                                               bgImage.getHeight( this ) ) );

        spots = new Spot[ 8 ][ 8 ];
        boardCreator.initializeSpots( spots );
        boardCreator.addPieces( spots, pieces );
    }

    @Override
    protected void paintComponent( Graphics graphics )
    {
        graphics.drawImage( bgImage, 0, 0, null );

        for ( Piece piece : pieces )
            graphics.drawImage( piece.getImage(), piece.getX(), piece.getY(), null );
    }

    public static Image loadResource( String resource ) throws InvalidResourceException
    {
        URL bgImageURL = Board.class.getClassLoader().getResource( resource );
        if ( bgImageURL == null )
            throw new InvalidResourceException( resource );
        return new ImageIcon( bgImageURL ).getImage();
    }

    public boolean isReversed()
    {
        return reversed;
    }

    public Image getBgImage()
    {
        return bgImage;
    }

    public void setBgImage( Image bgImage )
    {
        this.bgImage = bgImage;
    }
}
