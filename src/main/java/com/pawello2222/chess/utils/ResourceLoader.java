package com.pawello2222.chess.utils;

import com.pawello2222.chess.exception.InvalidResourceException;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Resource loader.
 *
 * @author Pawel Wiszenko
 */
public abstract class ResourceLoader
{
    private static Image loadImage( String imageName ) throws InvalidResourceException
    {
        URL bgImageURL = ResourceLoader.class.getClassLoader().getResource( imageName );
        if ( bgImageURL == null )
            throw new InvalidResourceException( imageName );

        return new ImageIcon( bgImageURL ).getImage();
    }

    public static Image loadImageExitOnEx( String imageName )
    {
        Image image = null;

        try
        {
            image = ResourceLoader.loadImage( imageName );
        }
        catch ( InvalidResourceException e )
        {
            System.out.println( e.getMessage() );
            System.exit( -1 );
        }

        return image;
    }
}
