package com.pawello2222.chess.model;

import com.pawello2222.chess.core.Board;

import java.awt.*;

/**
 * Piece model.
 *
 * @author Pawel Wiszenko
 */
public class Piece
{
    private Board board;
    private Spot spot;
    private Image image;

    private PieceColor color;
    private PieceType type;

    private int x;
    private int y;

    boolean active;

    public Piece( Board board, Spot spot, Image image, PieceColor color, PieceType type, boolean active )
    {
        this.board = board;
        this.spot = spot;
        this.image = image;
        this.color = color;
        this.type = type;
        this.active = active;
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
