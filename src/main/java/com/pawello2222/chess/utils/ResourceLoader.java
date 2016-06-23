package com.pawello2222.chess.utils;

import com.pawello2222.chess.core.Board;
import com.pawello2222.chess.exception.InvalidResourceException;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Resource loader.
 *
 * @author Pawel Wiszenko
 */
public class ResourceLoader
{
    public static Image loadResource( String resourceName ) throws InvalidResourceException
    {
        URL bgImageURL = Board.class.getClassLoader().getResource( resourceName );
        if ( bgImageURL == null )
            throw new InvalidResourceException( resourceName );
        return new ImageIcon( bgImageURL ).getImage();
    }
}
