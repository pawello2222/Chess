package com.pawello2222.chess.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

/**
 * Translucent button class.
 *
 * @author Pawel Wiszenko
 */
public class TranslucentButton extends JButton
{
    private static final Color normal1 = new Color( 1f, 1f, 1f, .7f );
    private static final Color normal2 = new Color( 0f, 0f, 0f, .5f );
    private static final Color pressed1 = new Color( 0f, 0f, 0f, .7f );
    private static final Color pressed2 = new Color( 1f, 1f, 1f, .5f );
    private static final Color border = new Color( 0f, 0f, 0f, .6f );

    private static final int radius = 8;

    public TranslucentButton( String text, ActionListener actionListener )
    {
        super( text );

        addActionListener( actionListener );

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
        Shape area = new RoundRectangle2D.Float( 0, 0, getWidth() - 1, getHeight() - 1, radius, radius );

        Color gradient1 = normal1;
        Color gradient2 = normal2;

        if( getModel().isPressed() )
        {
            gradient1 = pressed1;
            gradient2 = pressed2;
        }

        g2.setPaint( new GradientPaint( 0, 0, gradient1, 0, getHeight(), gradient2, true ) );
        g2.fill( area );
        g2.setPaint( border );
        g2.draw( area );
        g2.dispose();

        super.paintComponent( graphics );
    }
}
