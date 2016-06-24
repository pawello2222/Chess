package com.pawello2222.chess.core;

import com.pawello2222.chess.model.*;
import com.pawello2222.chess.net.NetworkHandlerBase;

import java.util.List;

import static com.pawello2222.chess.utils.BoardUtils.*;

/**
 * Board handler implementation class.
 *
 * @author Pawel Wiszenko
 */
class BoardHandlerImpl extends BoardHandlerBase
{
    private Board board;
    private MoveValidator moveValidator;
    private NetworkHandlerBase networkHandler;
    private Spot[][] spots;
    private List< Piece > pieces;

    BoardHandlerImpl( Board board, MoveValidator moveValidator,
                      Spot[][] spots, List< Piece > pieces )
    {
        this.board = board;
        this.moveValidator = moveValidator;
        this.spots = spots;
        this.pieces = pieces;
    }

    @Override
    public void updateGraphics()
    {
        board.repaint();
    }

    @Override
    public void setFocusOn( Piece piece )
    {
        pieces.remove( piece );
        pieces.add( piece );
    }

    @Override
    public void updatePossibleMoves( Spot spot )
    {
        moveValidator.updateValidMoveFlags( spot );
    }

    @Override
    public void movePiece( Spot sourceSpot, Spot targetSpot )
    {
        executeMove( spots, pieces, sourceSpot, targetSpot );
        updateGraphics();

        checkPawnPromotion( board.getParent(), targetSpot );
        updateGraphics();

        moveValidator.updateFlagsAfterMove( sourceSpot, targetSpot );

        nextTurn();
        if ( networkHandler != null )
            networkHandler.sendMove( sourceSpot, targetSpot );

        //TODO: disable pieces when idle
    }

    @Override
    public void receiveMove( int[] sourceSpot, int[] targetSpot )
    {
        movePiece( spots[ sourceSpot[ 0 ] ][ sourceSpot[ 1 ] ],
                   spots[ targetSpot[ 0 ] ][ targetSpot[ 1 ] ] );
    }

    private void nextTurn()
    {
        for ( Piece piece : pieces )
            piece.setActive( !piece.isActive() );

        if ( moveValidator.getPossibleMovesCount() > 0 )
            switchGameState();
        else
            endOfGame();
    }

    private void switchGameState()
    {
        if ( board.isGameState( GameState.RUNNING_WHITE ) )
            board.setGameState( GameState.RUNNING_BLACK );
        else if ( board.isGameState( GameState.RUNNING_BLACK ) )
            board.setGameState( GameState.RUNNING_WHITE );
    }

    private void endOfGame()
    {
        if ( !isCheckFlagSet( spots ) )
            board.setGameState( GameState.STALEMATE );
        else if ( board.isGameState( GameState.RUNNING_WHITE ) )
            board.setGameState( GameState.CHECKMATE_WIN_WHITE );
        else if ( board.isGameState( GameState.RUNNING_BLACK ) )
            board.setGameState( GameState.CHECKMATE_WIN_BLACK );

        for ( Piece piece : pieces )
            piece.setActive( false );

        board.endOfGame();
    }

    @Override
    public void setNetworkHandler( NetworkHandlerBase networkHandler )
    {
        this.networkHandler = networkHandler;
    }
}
