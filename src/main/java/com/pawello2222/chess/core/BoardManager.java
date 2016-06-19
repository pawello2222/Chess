package com.pawello2222.chess.core;

import com.pawello2222.chess.model.FlagType;
import com.pawello2222.chess.model.GameState;
import com.pawello2222.chess.model.Piece;
import com.pawello2222.chess.model.Spot;
import com.pawello2222.chess.service.IMoveValidator;
import com.pawello2222.chess.service.MoveValidator;

/**
 * Board manager class.
 *
 * @author Pawel Wiszenko
 */
class BoardManager
{
    private Board board;
    private IMoveValidator moveValidator;

    private Spot[][] spots;

    BoardManager( Board board )
    {
        this.board = board;
        this.spots = board.getSpots();

        moveValidator = new MoveValidator( spots );
    }

    void nextTurn( Spot sourceSpot, Spot targetSpot )
    {
        clearAllFlags();
        moveValidator.updateLastMoveFlags( sourceSpot, targetSpot );
        moveValidator.updateCheckFlag();
        moveValidator.updateEnPassantFlag( sourceSpot, targetSpot );

        for ( Piece piece : board.getPieces() )
            piece.setActive( !piece.isActive() );

        int possibleMoves = 0;

        for ( int column = 0; column < 8; column++ )
            for ( int row = 0; row < 8; row++ )
            {
                possibleMoves += countValidMoveFlags( spots[ column ][ row ] );
                clearFlagsByType( FlagType.VALID_MOVE );
            }

        if ( possibleMoves > 0 )
            switchPlayer();
        else
            endGame();
    }

    private void switchPlayer()
    {
        if ( board.getGameState() == GameState.RUNNING_WHITE )
            board.setGameState( GameState.RUNNING_BLACK );
        else if ( board.getGameState() == GameState.RUNNING_BLACK )
            board.setGameState( GameState.RUNNING_WHITE );
    }

    private void endGame()
    {
        if ( board.getGameState() == GameState.RUNNING_WHITE )
            board.setGameState( GameState.CHECKMATE_WIN_WHITE );
        else if ( board.getGameState() == GameState.RUNNING_BLACK )
            board.setGameState( GameState.CHECKMATE_WIN_BLACK );

        for ( Piece piece : board.getPieces() )
            piece.setActive( false );

        System.out.println( "END" );
    }

    void updateValidMoveFlags( Spot spot )
    {
        clearFlagsByType( FlagType.VALID_MOVE );
        moveValidator.updateValidMoveFlags( spot );
    }

    private void clearAllFlags()
    {
        clearFlagsByType( FlagType.VALID_MOVE );
        clearFlagsByType( FlagType.LAST_MOVE );
        clearFlagsByType( FlagType.CHECK );
        clearFlagsByType( FlagType.EN_PASSANT );
    }

    private void clearFlagsByType( FlagType flagType )
    {
        for ( int column = 0; column < 8; column++ )
            for ( int row = 0; row < 8; row++ )
                switch( flagType )
                {
                    case VALID_MOVE:
                        spots[ column ][ row ].setValidMoveFlag( false );
                        break;

                    case LAST_MOVE:
                        spots[ column ][ row ].setLastMoveFlag( false );
                        break;

                    case CHECK:
                        spots[ column ][ row ].setCheckFlag( false );
                        break;

                    case EN_PASSANT:
                        spots[ column ][ row ].setEnPassantFlag( false );
                        break;
                }
    }

    private int countValidMoveFlags( Spot spot )
    {
        int count = 0;

        if ( spot == null || spot.getPiece() == null || !spot.getPiece().isActive() )
            return 0;

        moveValidator.updateValidMoveFlags( spot );

        for ( int column = 0; column < 8; column++ )
            for ( int row = 0; row < 8; row++ )
                if ( spots[ column ][ row ].isValidMoveFlag() )
                    count++;

        return count;
    }
}