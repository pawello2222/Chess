package com.pawello2222.chess.core;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Board creator interface.
 *
 * @author Pawel Wiszenko
 */
interface IBoardCreator
{
    static Image loadResource( String resourceName ) throws InvalidResourceException
    {
        URL bgImageURL = Board.class.getClassLoader().getResource( resourceName );
        if ( bgImageURL == null )
            throw new InvalidResourceException( resourceName );
        return new ImageIcon( bgImageURL ).getImage();
    }

    void initializeBoard( String bgImageName, boolean reversed ) throws InvalidResourceException;
}
