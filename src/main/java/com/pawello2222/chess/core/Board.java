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
class Board extends JPanel
{
    private Image bgImage;

    Board()
    {
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
}
