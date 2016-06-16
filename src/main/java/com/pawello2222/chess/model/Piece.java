package com.pawello2222.chess.model;

import com.pawello2222.chess.core.Board;

import java.awt.Image;

/**
 * Piece model.
 *
 * @author Pawel Wiszenko
 */
public class Piece
{
    private Spot spot;
    private Image image;

    private PieceColor color;
    private PieceType type;

    private int x;
    private int y;

    boolean boardReversed;
    boolean active;

    public Piece( Spot spot, Image image, PieceColor color, PieceType type, boolean active, boolean boardReversed )
    {
        this.spot = spot;
        this.image = image;
        this.color = color;
        this.type = type;
        this.active = active;

        resetCoordinatesToSpot( spot );
    }

    public void resetCoordinatesToSpot( Spot spot )
    {
        int a = boardReversed ? 7 - spot.getColumn() : spot.getColumn();
        int b = boardReversed ? 7 - spot.getRow() : spot.getRow();

        x = Board.BOARD_OFFSET_X + Board.TILE_OFFSET_X * a;
        y = Board.BOARD_OFFSET_Y + Board.TILE_OFFSET_Y * b;
    }

    public Image getImage()
    {
        return image;
    }

    public void setImage( Image image )
    {
        this.image = image;
    }

    public int getWidth()
    {
        return image.getHeight( null );
    }

    public int getHeight()
    {
        return image.getHeight( null );
    }

    public PieceColor getColor()
    {
        return color;
    }

    public PieceType getType()
    {
        return type;
    }

    public void setType( PieceType type )
    {
        this.type = type;
    }

    public int getX()
    {
        return x;
    }

    public void setX( int x )
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY( int y )
    {
        this.y = y;
    }
}
