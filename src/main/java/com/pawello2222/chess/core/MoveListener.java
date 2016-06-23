package com.pawello2222.chess.core;

import com.pawello2222.chess.model.Piece;
import com.pawello2222.chess.model.Spot;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Piece move listener class.
 *
 * @author Pawel Wiszenko
 */
public class MoveListener implements MouseListener, MouseMotionListener
{
    private IMoveHandler moveHandler;
    private IGraphicsHandler graphicsHandler;

    private Spot[][] spots;
    private Spot sourceSpot;
    private Piece dragPiece;

    public MoveListener( BoardHandler boardHandler, Spot[][] spots )
    {
        this.moveHandler = boardHandler;
        this.graphicsHandler = boardHandler;
        this.spots = spots;
    }

    @Override
    public void mousePressed( MouseEvent e )
    {
        if ( dragPiece != null )
            return;

        switch ( e.getModifiers() )
        {
            case InputEvent.BUTTON1_MASK:
                Spot spot = getSpotFromXY( e.getPoint().x, e.getPoint().y );

                if ( spot != null && spot.getPiece() != null && spot.getPiece().isActive() )
                {
                    sourceSpot = spot;
                    dragPiece = spot.getPiece();
                    graphicsHandler.setFocusOnPiece( dragPiece );
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void mouseDragged( MouseEvent e )
    {
        if( dragPiece != null )
        {
            dragPiece.setX( e.getPoint().x - dragPiece.getWidth() / 2 );
            dragPiece.setY( e.getPoint().y - dragPiece.getHeight() / 2 );
            graphicsHandler.updateGraphics();
        }
    }

    @Override
    public void mouseReleased( MouseEvent e )
    {
        if( dragPiece != null )
        {
            Spot targetSpot = getSpotFromXY( e.getPoint().x, e.getPoint().y );
            if ( targetSpot == null || !targetSpot.isValidMoveFlag() || targetSpot == sourceSpot )
                dragPiece.setCoordinatesToSpot( sourceSpot );
            else
                moveHandler.movePiece( sourceSpot, targetSpot );

            sourceSpot = null;
            dragPiece = null;

            mouseMoved( e );
        }
    }

    @Override
    public void mouseClicked( MouseEvent e )
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
    public void mouseMoved( MouseEvent e )
    {
        if( dragPiece == null )
        {
            moveHandler.updatePossibleMoves( getSpotFromXY( e.getPoint().x, e.getPoint().y ) );
            graphicsHandler.updateGraphics();
        }
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
