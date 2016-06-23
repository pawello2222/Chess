package com.pawello2222.chess.core;

import com.pawello2222.chess.exception.ConnectionException;
import com.pawello2222.chess.exception.SocketCreationException;
import com.pawello2222.chess.model.*;
import com.pawello2222.chess.net.NetworkHandlerBase;
import com.pawello2222.chess.net.NetworkHandlerImpl;
import com.pawello2222.chess.utils.ResourceLoader;
import javafx.util.Pair;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Main factory.
 *
 * @author Pawel Wiszenko
 */
abstract class MainFactory
{
    static Board initBoard( boolean reversed, NetworkGame networkGame, String param1, String param2 )
            throws ConnectionException, SocketCreationException
    {
        NetworkHandlerBase networkHandler = getNetworkHandler( networkGame );

        if ( networkHandler != null && networkGame == NetworkGame.SERVER )
        {
            int port = Integer.parseInt( param1 );
            int timeout = Integer.parseInt( param2 );

            // TODO: validation
            try
            {
                networkHandler.startServer( port, timeout );
            }
            catch ( ConnectionException e )
            {
                System.out.println( "conn" );
            }
            catch ( InterruptedException e )
            {
                System.out.println( "interrupt" );
            }
        }
        else if ( networkHandler != null && networkGame == NetworkGame.CLIENT )
        {
            int port = Integer.parseInt( param2 );

            // TODO: validation
            networkHandler.startClient( param1, port );
        }

        Pair< Board, BoardHandlerBase > pair = getBoardWithHandler( reversed, networkHandler );
        if ( networkHandler != null )
            networkHandler.addNetworkReceiver( pair.getValue() );

        return pair.getKey();
    }

    private static Pair< Board, BoardHandlerBase > getBoardWithHandler( boolean reversed,
                                                                        NetworkHandlerBase networkHandler )
    {
        Image image = ResourceLoader.loadImageExitOnEx( "BOARD.png" );
        Spot[][] spots = initializeSpots( reversed );
        List< Piece > pieces = initializePieces( spots );

        Board board =  getBoard( image, spots, pieces );

        MoveValidator moveValidator = getMoveValidator( spots );
        BoardHandlerBase boardHandler = getBoardHandler( board, moveValidator, networkHandler, spots, pieces );
        MoveListenerBase moveListener = getMoveListener( boardHandler, spots );

        board.setMoveListener( moveListener );

        return new Pair<>( board, boardHandler );
    }

    private static Board getBoard( Image image, Spot[][] spots, List< Piece > pieces )
    {
        return new Board( image, spots, pieces );
    }

    private static BoardHandlerBase getBoardHandler( Board board, MoveValidator moveValidator,
                                                     NetworkHandlerBase networkHandler,
                                                     Spot[][] spots, List< Piece > pieces )
    {
        return new BoardHandlerImpl( board, moveValidator, networkHandler, spots, pieces );
    }

    private static MoveListenerBase getMoveListener( BoardHandlerBase boardHandler, Spot[][] spots )
    {
        return new MoveListenerImpl( boardHandler, spots );
    }

    private static MoveValidator getMoveValidator( Spot[][] spots )
    {
        return new MoveValidatorImpl( spots );
    }

    private static NetworkHandlerBase getNetworkHandler( NetworkGame networkGame )
    {
        if ( networkGame == NetworkGame.DISABLED )
            return null;

        return new NetworkHandlerImpl( networkGame );
    }

    private static Spot[][] initializeSpots( boolean reversed )
    {
        Spot[][] spots = new Spot[ 8 ][ 8 ];

        for ( int column = 0; column < 8; column++ )
            for ( int row = 0; row < 8; row++ )
            {
                int x = reversed ? 7 - column : column;
                int y = reversed ? 7 - row : row;

                spots[ column ][ row ] = new Spot();
                spots[ column ][ row ].setX( Board.BOARD_OFFSET_X + Spot.SPOT_WIDTH * x );
                spots[ column ][ row ].setY( Board.BOARD_OFFSET_Y + Spot.SPOT_HEIGHT * y );
                spots[ column ][ row ].setRow( row );
                spots[ column ][ row ].setColumn( column );
            }

        return spots;
    }

    private static List< Piece > initializePieces( Spot[][] spots )
    {
        List< Piece > pieces = new ArrayList<>();

        for ( int j = 0; j < 2; j++ )
        {
            int row = j == 0 ? 6 : 1;
            PieceColor color = j == 0 ? PieceColor.WHITE : PieceColor.BLACK;

            for ( int i = 0; i < 8; i++ )
                pieces.add( createPiece( spots, row, i, color, PieceType.PAWN ) );

            row = j == 0 ? 7 : 0;
            pieces.add( createPiece( spots, row, 0, color, PieceType.ROOK ) );
            pieces.add( createPiece( spots, row, 1, color, PieceType.KNIGHT ) );
            pieces.add( createPiece( spots, row, 2, color, PieceType.BISHOP ) );
            pieces.add( createPiece( spots, row, 3, color, PieceType.QUEEN ) );
            pieces.add( createPiece( spots, row, 4, color, PieceType.KING ) );
            pieces.add( createPiece( spots, row, 5, color, PieceType.BISHOP ) );
            pieces.add( createPiece( spots, row, 6, color, PieceType.KNIGHT ) );
            pieces.add( createPiece( spots, row, 7, color, PieceType.ROOK ) );
        }

        return pieces;
    }

    private static Piece createPiece( Spot[][] spots, int row, int column, PieceColor color, PieceType type )
    {
        Image pieceImage = ResourceLoader.loadImageExitOnEx( color + "_" + type + ".png" );
        Piece piece = new Piece( pieceImage, color, type, color == PieceColor.WHITE );
        spots[ column ][ row ].setPiece( piece );
        piece.setCoordinatesToSpot( spots[ column ][ row ] );

        return piece;
    }
}
