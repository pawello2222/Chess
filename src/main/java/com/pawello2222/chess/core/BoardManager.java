package com.pawello2222.chess.core;

import com.pawello2222.chess.model.*;
import com.pawello2222.chess.service.IMoveValidator;
import com.pawello2222.chess.service.MoveValidator;

import javax.swing.*;
import java.awt.*;

/**
 * Board manager class.
 *
 * @author Pawel Wiszenko
 */
class BoardManager implements IBoardManager
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

    @Override
    public void updatePossibleMoves( Spot spot )
    {
        moveValidator.updateValidMoveFlags( spot );
    }

    @Override
    public void movePiece( Spot sourceSpot, Spot targetSpot )
    {
        Piece sourcePiece = sourceSpot.getPiece();

        if ( sourcePiece.isUnmoved() )
            sourcePiece.setUnmoved( false );

        if ( targetSpot.isEnPassantFlag() )
        {
            board.getPieces().remove( spots[ targetSpot.getColumn() ][ sourceSpot.getRow() ].getPiece() );
            spots[ targetSpot.getColumn() ][ sourceSpot.getRow() ].setPiece( null );
        }
        else if ( targetSpot.isSpecialMoveFlag() && targetSpot.getPiece() == null )
        {
            Spot source = spots[ targetSpot.getColumn() == 2 ? 0 : 7 ][ targetSpot.getRow() ];
            Spot target = spots[ targetSpot.getColumn() == 2 ? 3 : 5 ][ targetSpot.getRow() ];

            target.setPiece( source.getPiece() );
            target.getPiece().setCoordinatesToSpot( target );
            source.setPiece( null );
        }
        else if ( targetSpot.getPiece() != null )
            board.getPieces().remove( targetSpot.getPiece() );

        targetSpot.setPiece( sourcePiece );
        targetSpot.getPiece().setCoordinatesToSpot( targetSpot );
        sourceSpot.setPiece( null );

        board.repaint();

        int promotionRow = sourcePiece.getColor() == PieceColor.WHITE ? 0 : 7;
        if ( sourcePiece.getType() == PieceType.PAWN && targetSpot.getRow() == promotionRow )
            promotePawn( targetSpot.getPiece() );

        moveValidator.updateFlagsAfterMove( sourceSpot, targetSpot );
    }

    @Override
    public void nextTurn()
    {
        for ( Piece piece : board.getPieces() )
            piece.setActive( !piece.isActive() );

        if ( moveValidator.getPossibleMovesCount() > 0 )
            switchGameState();
        else
            endGame();
    }

    private void switchGameState()
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

        board.endGame();
    }

    private void promotePawn( Piece piece )
    {
        String chosenType;
        do
            chosenType = getPromotionDialogResult();
        while ( chosenType == null );

        Image pieceImage = IBoardCreator.loadResource( piece.getColor() + "_" + chosenType.toUpperCase() + ".png" );
        piece.setImage( pieceImage );
        piece.setType( PieceType.valueOf( chosenType.toUpperCase() ) );
    }

    private String getPromotionDialogResult()
    {
        Object[] possibilities = { "Knight", "Bishop", "Rook", "Queen" };

        return ( String ) JOptionPane.showInputDialog(
                board.getParent(),
                "Choose promotion",
                "Promotion",
                JOptionPane.PLAIN_MESSAGE,
                null,
                possibilities,
                possibilities[ 3 ] );
    }
}
