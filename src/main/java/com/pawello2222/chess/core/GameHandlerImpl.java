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
    private GameType gameType;
    private GameState gameState;
    private boolean isOnline;
    private boolean isPlayerWhite;

    GameHandlerImpl( GameBase game, Spot[][] spots, List< Piece > pieces, GameType gameType )
    {
        this.game = game;
        this.spots = spots;
        this.pieces = pieces;

        this.moveValidator = MainFactory.getMoveValidator( spots );

        this.gameType = gameType;
        gameState = GameState.RUNNING_WHITE;
        isOnline = gameType == GameType.ONLINE_WHITE || gameType == GameType.ONLINE_BLACK;
        isPlayerWhite = gameType == GameType.LOCAL_WHITE || gameType == GameType.ONLINE_WHITE;

        if ( !isOnline )
            activatePieces( PieceColor.WHITE, true );
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

        if ( isOnline && isOwnMove )
            sendMove( sourceSpot, targetSpot );

        nextTurn();
    }

    private void sendMove( Spot sourceSpot, Spot targetSpot )
    {
        networkSender.send( "M" + sourceSpot.toString() + targetSpot.toString() );
    }

    @Override
    public void receive( String data )
    {
        if ( isOnline )
        {
            if ( data.charAt( 0 ) == 'Q' )
                game.endOfGame( GameState.NETWORK_ERROR );
            else if ( data.charAt( 0 ) == 'P' )
            {
                if ( gameType == GameType.ONLINE_WHITE )
                {
                    game.setTitle( "Chess - online game (WHITE)" );
                    activatePieces( PieceColor.WHITE, true );
                }
                else
                    game.setTitle( "Chess - online game (BLACK)" );
                game.displayMessage( "Success", "Connected with opponent" );
            }
            else if ( data.charAt( 0 ) == 'M' )
                movePiece( spots[ Integer.parseInt( data.substring( 1, 2 ) ) ]
                                   [ Integer.parseInt( data.substring( 2, 3 ) ) ],
                           spots[ Integer.parseInt( data.substring( 3, 4 ) ) ]
                                   [ Integer.parseInt( data.substring( 4, 5 ) ) ],
                           false );
        }
    }

    private void nextTurn()
    {
        if ( gameState == GameState.RUNNING_WHITE )
        {
            activatePieces( PieceColor.WHITE, false );
            activatePieces( PieceColor.BLACK, true );
        }
        else
        {
            activatePieces( PieceColor.WHITE, true );
            activatePieces( PieceColor.BLACK, false );
        }

        if ( moveValidator.getPossibleMovesCount() > 0 )
            switchGameState();
        else
            endOfGame();
    }

    private void switchGameState()
    {
        if ( gameState == GameState.RUNNING_WHITE )
        {
            gameState = GameState.RUNNING_BLACK;
            activatePieces( PieceColor.WHITE, false );
            activatePieces( PieceColor.BLACK, !isPlayerWhite );
        }
        else
        {
            gameState = GameState.RUNNING_WHITE;
            activatePieces( PieceColor.WHITE, isPlayerWhite );
            activatePieces( PieceColor.BLACK, false );
        }
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
    }

    private void activatePieces( PieceColor pieceColor, boolean active )
    {
        pieces.stream().filter( piece -> piece.getColor() == pieceColor ).forEach( piece -> piece.setActive( active ) );
    }

    @Override
    public void setGame( GameBase game )
    {
        this.game = game;
    }

    @Override
    public void setNetworkSender( NetworkSender networkSender )
    {
        this.networkSender = networkSender;
    }
}