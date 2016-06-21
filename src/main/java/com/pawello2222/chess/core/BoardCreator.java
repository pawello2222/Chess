package com.pawello2222.chess.core;

import com.pawello2222.chess.exception.InvalidResourceException;
import com.pawello2222.chess.model.Piece;
import com.pawello2222.chess.model.PieceColor;
import com.pawello2222.chess.model.PieceType;
import com.pawello2222.chess.model.Spot;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Board creator.
 *
 * Helper class to initialize and populate board.
 *
 * @author Pawel Wiszenko
 */
class BoardCreator
{
    private Board board;

    BoardCreator( Board board )
    {
        this.board = board;
    }

    static Image loadResource( String resourceName ) throws InvalidResourceException
    {
        URL bgImageURL = Board.class.getClassLoader().getResource( resourceName );
        if ( bgImageURL == null )
            throw new InvalidResourceException( resourceName );
        return new ImageIcon( bgImageURL ).getImage();
    }

    void initializeBoard( String bgImageName, boolean reversed ) throws InvalidResourceException
    {
        Image bgImage = BoardCreator.loadResource( bgImageName );
        board.setBgImage( bgImage );
        board.setPreferredSize( new Dimension( bgImage.getWidth( board ),
                                              bgImage.getHeight( board ) ) );

        Spot[][] spots = initializeSpots( reversed );
        board.setSpots( spots );

        List< Piece > pieces = initializePieces( spots );
        board.setPieces( pieces );
    }

    private Spot[][] initializeSpots( boolean reversed )
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

    private List< Piece > initializePieces( Spot[][] spots ) throws InvalidResourceException
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

    private Piece createPiece( Spot[][] spots, int row, int column, PieceColor color, PieceType type )
            throws InvalidResourceException
    {
        Image pieceImage = loadResource( color + "_" + type + ".png" );
        Piece piece = new Piece( pieceImage, color, type, color == PieceColor.WHITE );
        spots[ column ][ row ].setPiece( piece );
        piece.setCoordinatesToSpot( spots[ column ][ row ] );

        return piece;
    }
}
