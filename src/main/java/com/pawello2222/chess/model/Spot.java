package com.pawello2222.chess.model;

/**
 * Spot model.
 *
 * @author Pawel Wiszenko
 */
public class Spot
{
    public static final int SPOT_WIDTH = 50;
    public static final int SPOT_HEIGHT = 50;

    private Piece piece;

    private int row;
    private int column;

    private int x;
    private int y;

    private boolean validMoveFlag;
    private boolean lastMoveFlag;
    private boolean checkFlag;
    private boolean enPassantFlag;

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

    public boolean isValidMoveFlag()
    {
        return validMoveFlag;
    }

    public void setValidMoveFlag( boolean validMoveFlag )
    {
        this.validMoveFlag = validMoveFlag;
    }

    public boolean isLastMoveFlag()
    {
        return lastMoveFlag;
    }

    public void setLastMoveFlag( boolean lastMoveFlag )
    {
        this.lastMoveFlag = lastMoveFlag;
    }

    public boolean isCheckFlag()
    {
        return checkFlag;
    }

    public void setCheckFlag( boolean checkFlag )
    {
        this.checkFlag = checkFlag;
    }

    public boolean isEnPassantFlag()
    {
        return enPassantFlag;
    }

    public void setEnPassantFlag( boolean enPassantFlag )
    {
        this.enPassantFlag = enPassantFlag;
    }
}
