package com.pawello2222.chess.util;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Translucent button class.
 *
 * @author Pawel Wiszenko
 */
public class TranslucentButton extends JButton
{
    private static final Color interiorHover = new Color( 1f, 1f, 1f, .9f );
    private static final Color borderHover = new Color( 0f, 0f, 0f, .6f );
    private static final Color interiorPressed = new Color( 0f, 0f, 0f, .1f );
    private static final Color borderPressed = new Color( 1f, 1f, 1f, .4f );

    public TranslucentButton( String text )
    {
        super( text );

        setBorder( BorderFactory.createEmptyBorder() );

        setContentAreaFilled( false );
        setFocusPainted( false );
        setOpaque( false );
    }

    @Override
    protected void paintComponent( Graphics graphics )
    {
        Graphics2D g2 = ( Graphics2D ) graphics.create();

        g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
        int radius = 8;
        Shape area = new RoundRectangle2D.Float( 0, 0, getWidth() - 1, getHeight() - 1, radius, radius );

        Color tileColor = interiorHover;
        Color borderColor = borderHover;

        if( getModel().isPressed() )
        {
            tileColor = interiorPressed;
            borderColor = borderPressed;
        }

        g2.setPaint( new GradientPaint( 0, 0, tileColor, 0, getHeight(), borderColor, true ) );
        g2.fill( area );

        if( getModel().isPressed() )
            g2.setPaint( borderHover );
        else
            g2.setPaint( borderColor );

        g2.draw( area );
        g2.dispose();

        super.paintComponent( graphics );
    }
}
