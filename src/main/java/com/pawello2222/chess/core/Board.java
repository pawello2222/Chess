package com.pawello2222.chess.core;

import com.pawello2222.chess.exception.InvalidResourceException;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Board class.
 *
 * @author Pawel Wiszenko
 */
public class Board extends JPanel
{
//    public static final int BOARD_OFFSET_X = 10;
//    public static final int BOARD_OFFSET_Y = 10;
//    public static final int TILE_OFFSET_X = 50;
//    public static final int TILE_OFFSET_Y = 50;

    private Image bgImage;

//    private boolean reversed;

    public Board( boolean reversed )
    {
//        this.reversed = reversed;

        String boardName = "BOARD.png";
        URL bgImageURL = getClass().getClassLoader().getResource( boardName );
        if ( bgImageURL == null )
            throw new InvalidResourceException( boardName );
        bgImage = new ImageIcon( bgImageURL ).getImage();

        setPreferredSize( new Dimension( bgImage.getWidth( this ),
                                         bgImage.getHeight( this ) ) );
    }

    @Override
    protected void paintComponent( Graphics graphics )
    {
        graphics.drawImage( bgImage, 0, 0, null );
    }

//    public boolean isReversed()
//    {
//        return reversed;
//    }
}
