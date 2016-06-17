package com.pawello2222.chess.service;

import com.pawello2222.chess.core.Board;
import com.pawello2222.chess.model.Piece;
import com.pawello2222.chess.model.Spot;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

/**
 * Piece move listener.
 *
 * @author Pawel Wiszenko
 */
public class MoveListener implements MouseListener, MouseMotionListener
{
    private Board board;
    private Spot[][] spots;
    private List< Piece > pieces;

    private Spot sourceSpot;
    private Spot targetSpot;
    private Piece dragPiece;

    public MoveListener( Board board )
    {
        this.board = board;
        this.spots = board.getSpots();
        this.pieces = board.getPieces();
    }

    @Override
    public void mouseClicked( MouseEvent e )
    {

    }

    @Override
    public void mousePressed( MouseEvent e )
    {
        switch ( e.getModifiers() )
        {
            case InputEvent.BUTTON1_MASK:
                Spot spot = getSpotFromXY( e.getPoint().x, e.getPoint().y );

                if ( spot != null && spot.getPiece() != null && spot.getPiece().isActive() )
                {
                    sourceSpot = spot;
                    dragPiece = spot.getPiece();
                    pieces.remove( dragPiece );
                    pieces.add( dragPiece );
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void mouseReleased( MouseEvent e )
    {

    }

    @Override
    public void mouseEntered( MouseEvent e )
    {

    }

    @Override
    public void mouseExited( MouseEvent e )
    {

    }

    @Override
    public void mouseDragged( MouseEvent e )
    {
        if( dragPiece != null )
        {
            dragPiece.setX( e.getPoint().x - Spot.SPOT_WIDTH / 2 );
            dragPiece.setY( e.getPoint().y - Spot.SPOT_HEIGHT / 2 );
            board.repaint();
        }
    }

    @Override
    public void mouseMoved( MouseEvent e )
    {

    }

    private boolean isMouseOverSpot( Spot spot, int x, int y )
    {
        return spot.getX() <= x
               && spot.getX() + Spot.SPOT_WIDTH >= x
               && spot.getY() <= y
               && spot.getY() + Spot.SPOT_HEIGHT >= y;
    }

    private Spot getSpotFromXY( int x, int y )
    {
        for ( int column = 0; column < 8; column++ )
            for ( int row = 0; row < 8; row++ )
                if( isMouseOverSpot( spots[ column ][ row ], x, y ) )
                    return spots[ column ][ row ];

        return null;
    }
}