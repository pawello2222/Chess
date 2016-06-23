package com.pawello2222.chess.core;

import com.pawello2222.chess.exception.InvalidResourceException;
import com.pawello2222.chess.model.*;
import com.pawello2222.chess.utils.ResourceLoader;

import javax.swing.*;
import java.util.List;

/**
 * Board handler class implementation.
 *
 * @author Pawel Wiszenko
 */
class BoardHandlerImpl extends BoardHandlerBase
{
    private Board board;
    private MoveValidator moveValidator;
    private Spot[][] spots;
    private List< Piece > pieces;

    BoardHandlerImpl( Board board, MoveValidator moveValidator, Spot[][] spots, List< Piece > pieces )
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
        executeMove( sourceSpot, targetSpot );
        nextTurn();
    }

    private void executeMove( Spot sourceSpot, Spot targetSpot )
    {
        Piece sourcePiece = sourceSpot.getPiece();

        if ( sourcePiece.isUnmoved() )
            sourcePiece.setUnmoved( false );

        if ( targetSpot.isEnPassantFlag() )
        {
            pieces.remove( spots[ targetSpot.getColumn() ][ sourceSpot.getRow() ].getPiece() );
            spots[ targetSpot.getColumn() ][ sourceSpot.getRow() ].setPiece( null );
        }
        else if ( targetSpot.isSpecialMoveFlag() && targetSpot.isEmpty() )
        {
            Spot source = spots[ targetSpot.getColumn() == 2 ? 0 : 7 ][ targetSpot.getRow() ];
            Spot target = spots[ targetSpot.getColumn() == 2 ? 3 : 5 ][ targetSpot.getRow() ];

            target.setPiece( source.getPiece() );
            target.getPiece().setCoordinatesToSpot( target );
            source.setPiece( null );
        }
        else if ( targetSpot.getPiece() != null )
            pieces.remove( targetSpot.getPiece() );

        targetSpot.setPiece( sourcePiece );
        targetSpot.getPiece().setCoordinatesToSpot( targetSpot );
        sourceSpot.setPiece( null );

        updateGraphics();

        int promotionRow = sourcePiece.getColor() == PieceColor.WHITE ? 0 : 7;
        if ( sourcePiece.getType() == PieceType.PAWN && targetSpot.getRow() == promotionRow )
            promotePawn( targetSpot.getPiece() );

        updateGraphics();

        moveValidator.updateFlagsAfterMove( sourceSpot, targetSpot );
    }

    private void promotePawn( Piece piece )
    {
        String chosenType;
        do
            chosenType = getPromotionDialogResult();
        while ( chosenType == null );

        try
        {
            piece.setImage( ResourceLoader.loadResource( piece.getColor() + "_" + chosenType.toUpperCase() + ".png" ) );
        }
        catch ( InvalidResourceException e )
        {
            System.out.println( e.getMessage() );
            System.exit( -1 );
        }

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
        if ( board.isGameState( GameState.RUNNING_WHITE ) )
            board.setGameState( GameState.CHECKMATE_WIN_WHITE );
        else if ( board.isGameState( GameState.RUNNING_BLACK ) )
            board.setGameState( GameState.CHECKMATE_WIN_BLACK );

        for ( Piece piece : pieces )
            piece.setActive( false );

        board.endOfGame();
    }
}
