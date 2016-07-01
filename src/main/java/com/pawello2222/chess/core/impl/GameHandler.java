package com.pawello2222.chess.core.impl;

import com.pawello2222.chess.core.GameBase;
import com.pawello2222.chess.core.GameHandlerBase;
import com.pawello2222.chess.core.MoveValidatorBase;
import com.pawello2222.chess.model.*;
import com.pawello2222.chess.net.NetworkSender;

import java.util.List;

import static com.pawello2222.chess.util.BoardUtils.*;

/**
 * Game handler implementation class.
 *
 * @author Pawel Wiszenko
 */
public class GameHandler extends GameHandlerBase
{
    /**
     * Dependencies
     */
    private GameBase game;
    private MoveValidatorBase moveValidator;
    private Spot[][] spots;
    private List< Piece > pieces;

    private NetworkSender networkSender;

    /**
     * Variables
     */
    private GameType gameType;
    private GameState gameState;

    private boolean isOnline;

    public GameHandler( GameBase game, MoveValidatorBase moveValidator,
                        Spot[][] spots, List< Piece > pieces, GameType gameType )
    {
        this.game = game;
        this.moveValidator = moveValidator;
        this.spots = spots;
        this.pieces = pieces;

        this.gameType = gameType;
        gameState = GameState.RUNNING_WHITE;
        isOnline = gameType == GameType.ONLINE_WHITE || gameType == GameType.ONLINE_BLACK;

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
    public void movePiece( Spot sourceSpot, Spot targetSpot, char promotion, boolean isOwnMove )
    {
        executeMove( spots, pieces, sourceSpot, targetSpot );
        updateGraphics();

        promotion = checkPawnPromotion( game, targetSpot, promotion );
        updateGraphics();

        moveValidator.updateFlagsAfterMove( sourceSpot, targetSpot );

        if ( isOnline && isOwnMove && networkSender != null )
            sendMove( sourceSpot, targetSpot, promotion );

        nextTurn();
    }

    private void sendMove( Spot sourceSpot, Spot targetSpot, char promotion )
    {
        networkSender.send( "M" + sourceSpot.toString() + targetSpot.toString() + promotion );
    }

    @Override
    public void receive( String data )
    {
        if ( isOnline && networkSender != null )
        {
            if ( data.charAt( 0 ) == '0' )
                game.end( GameState.NETWORK_CLOSE );
            else if ( data.charAt( 0 ) == '1' )
            {
                if ( gameType == GameType.ONLINE_WHITE )
                {
                    game.setTitle( "Chess - online game (WHITE)" );
                    activatePieces( PieceColor.WHITE, true );
                }
                else
                    game.setTitle( "Chess - online game (BLACK)" );
                game.message( "Success", "Connected to opponent" );
            }
            else if ( data.charAt( 0 ) == 'M' )
                movePiece( spots[ Integer.parseInt( data.substring( 1, 2 ) ) ]
                                   [ Integer.parseInt( data.substring( 2, 3 ) ) ],
                           spots[ Integer.parseInt( data.substring( 3, 4 ) ) ]
                                   [ Integer.parseInt( data.substring( 4, 5 ) ) ],
                           data.charAt( 5 ), false );
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
            activatePieces( PieceColor.BLACK, gameType != GameType.ONLINE_WHITE );
        }
        else
        {
            gameState = GameState.RUNNING_WHITE;
            activatePieces( PieceColor.WHITE, gameType != GameType.ONLINE_BLACK );
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

        isOnline = false;
        game.end( gameState );
    }

    private void activatePieces( PieceColor color, boolean active )
    {
        pieces.stream().filter( p -> p.getColor() == color ).forEach( p -> p.setActive( active ) );
    }

    @Override
    public void setNetworkSender( NetworkSender networkSender )
    {
        this.networkSender = networkSender;
    }

    @Override
    public void setGame( GameBase game )
    {
        this.game = game;
    }
}
