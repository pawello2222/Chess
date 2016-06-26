package com.pawello2222.chess.core;

import com.pawello2222.chess.model.*;
import com.pawello2222.chess.net.NetworkSender;

import java.util.List;

import static com.pawello2222.chess.util.BoardUtils.*;

/**
 * Game handler implementation class.
 *
 * @author Pawel Wiszenko
 */
class GameHandlerImpl extends GameHandlerBase
{
    /**
     * Dependencies
     */
    private GameBase game;
    private Spot[][] spots;
    private List< Piece > pieces;

    private NetworkSender networkSender;
    private MoveValidatorBase moveValidator;

    /**
     * Variables
     */
    private GameState gameState;

    GameHandlerImpl( GameBase game, Spot[][] spots, List< Piece > pieces )
    {
        this.game = game;
        this.spots = spots;
        this.pieces = pieces;

        this.moveValidator = MainFactory.getMoveValidator( spots );

        gameState = GameState.RUNNING_WHITE;
    }

    @Override
    public void updateGraphics()
    {
        game.repaint();
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
    public void movePiece( Spot sourceSpot, Spot targetSpot, boolean isOwnMove )
    {
        executeMove( spots, pieces, sourceSpot, targetSpot );
        updateGraphics();

        checkPawnPromotion( game, targetSpot );
        updateGraphics();

        moveValidator.updateFlagsAfterMove( sourceSpot, targetSpot );

        nextTurn();
        if ( networkSender != null && isOwnMove )
            sendMove( sourceSpot, targetSpot );

        //TODO: disable pieces when idle
    }

    private void sendMove( Spot sourceSpot, Spot targetSpot )
    {
        networkSender.sendData( sourceSpot.toString() + targetSpot.toString() );
    }

    @Override
    public void receiveData( String data )
    {
        if ( networkSender != null )
            movePiece( spots[ Integer.parseInt( data.substring( 0, 1 ) ) ]
                               [ Integer.parseInt( data.substring( 1, 2 ) ) ],
                       spots[ Integer.parseInt( data.substring( 2, 3 ) ) ]
                               [ Integer.parseInt( data.substring( 2, 4 ) ) ],
                       false );
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
        if ( gameState == GameState.RUNNING_WHITE )
            gameState = GameState.RUNNING_BLACK;
        else if ( gameState == GameState.RUNNING_BLACK )
            gameState = GameState.RUNNING_WHITE;
    }

    private void endOfGame()
    {
        if ( !isCheckFlagSet( spots ) )
            gameState = GameState.STALEMATE;
        else if ( gameState == GameState.RUNNING_WHITE )
            gameState = GameState.CHECKMATE_WIN_WHITE;
        else if ( gameState == GameState.RUNNING_BLACK )
            gameState = GameState.CHECKMATE_WIN_BLACK;

        for ( Piece piece : pieces )
            piece.setActive( false );

        game.endOfGame( gameState );
        game = null;
    }

    @Override
    public void setNetworkSender( NetworkSender networkSender )
    {
        this.networkSender = networkSender;
    }
}
