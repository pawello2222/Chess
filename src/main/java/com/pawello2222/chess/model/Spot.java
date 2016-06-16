package com.pawello2222.chess.model;

/**
 * Spot model.
 *
 * @author Pawel Wiszenko
 */
public class Spot
{
    private Piece piece;

    private int row;
    private int column;

    private int x;
    private int y;

    public Spot()
    {

    }

    public Piece getPiece()
    {
        return piece;
    }

    public void setPiece( Piece piece )
    {
        this.piece = piece;
    }

    public int getRow()
    {
        return row;
    }

    public void setRow( int row )
    {
        this.row = row;
    }

    public int getColumn()
    {
        return column;
    }

    public void setColumn( int column )
    {
        this.column = column;
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
