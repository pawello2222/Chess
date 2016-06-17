package com.pawello2222.chess.model;

import java.awt.Image;

/**
 * Piece model.
 *
 * @author Pawel Wiszenko
 */
public class Piece
{
    private Image image;

    private PieceColor color;
    private PieceType type;

    private int x;
    private int y;

    private boolean active;

    private int offset_x;
    private int offset_y;

    public Piece( Image image, PieceColor color, PieceType type, boolean active )
    {
        this.image = image;
        this.color = color;
        this.type = type;
        this.active = active;

        this.offset_x = ( Spot.SPOT_WIDTH - this.getWidth() ) / 2;
        this.offset_y = ( Spot.SPOT_HEIGHT - this.getHeight() ) / 2;
    }

    public void setCoordinatesToSpot( Spot spot )
    {
        this.x = spot.getX() + offset_x;
        this.y = spot.getY() + offset_y;
    }

    public Image getImage()
    {
        return image;
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

    public boolean isActive()
    {
        return active;
    }

    public void setActive( boolean active )
    {
        this.active = active;
    }
}
