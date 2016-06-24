package com.pawello2222.chess.core;

import com.pawello2222.chess.model.Piece;
import com.pawello2222.chess.model.Spot;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

/**
 * Move listener implementation class.
 *
 * @author Pawel Wiszenko
 */
class MoveListenerImpl extends MoveListenerBase
{
    private BoardHandlerBase boardHandler;

    private Spot[][] spots;
    private Spot sourceSpot;
    private Piece dragPiece;

    MoveListenerImpl( BoardHandlerBase boardHandler, Spot[][] spots )
    {
        this.boardHandler = boardHandler;
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
                    boardHandler.setFocusOn( dragPiece );
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
            boardHandler.updateGraphics();
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
                boardHandler.movePiece( sourceSpot, targetSpot, true );

            sourceSpot = null;
            dragPiece = null;

            mouseMoved( e );
        }
    }

    @Override
    public void mouseMoved( MouseEvent e )
    {
        if( dragPiece == null )
        {
            boardHandler.updatePossibleMoves( getSpotFromXY( e.getPoint().x, e.getPoint().y ) );
            boardHandler.updateGraphics();
        }
    }

    private Spot getSpotFromXY( int x, int y )
    {
        for ( Spot[] row : spots )
            for ( Spot spot : row )
                if( isMouseOverSpot( spot, x, y ) )
                    return spot;

        return null;
    }

    private boolean isMouseOverSpot( Spot spot, int x, int y )
    {
        return spot.getX() <= x
               && spot.getX() + Spot.SPOT_WIDTH >= x
               && spot.getY() <= y
               && spot.getY() + Spot.SPOT_HEIGHT >= y;
    }
}
