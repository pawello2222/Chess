package com.pawello2222.chess.core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Menu panel.
 *
 * @author Pawel Wiszenko
 */
class MenuPanel extends JPanel
{
    private Image bgImage;

    MenuPanel( Image bgImage, ActionListener[][] actionListeners )
    {
        this.bgImage = bgImage;

        setPreferredSize( new Dimension( bgImage.getWidth( this ),
                                         bgImage.getHeight( this ) ) );
    }

    @Override
    protected void paintComponent( Graphics graphics )
    {
        graphics.drawImage( bgImage, 0, 0, null );
    }

//    private JPanel localGamePanel()
//    {
//        JPanel panel = new JPanel();
//        panel.setBorder( BorderFactory.createTitledBorder( "New game (local)" ) );
//
//        JButton button = new JButton( "White" );
//        button.addActionListener( actionListeners[ 0 ][ 0 ] );
//        panel.add( button );
//
//        button = new JButton( "Black" );
//        button.addActionListener( actionListeners[ 0 ][ 1 ] );
//        panel.add( button );
//
//        return panel;
//    }
//
//    private JPanel onlineGamePanel()
//    {
//        JPanel panel = new JPanel();
//        panel.setBorder( BorderFactory.createTitledBorder( "New game (online)" ) );
//
//        JButton button = new JButton( "Host" );
//        button.addActionListener( actionListeners[ 1 ][ 0 ] );
//        panel.add( button );
//
//        button = new JButton( "Join" );
//        button.addActionListener( actionListeners[ 1 ][ 1 ] );
//        panel.add( button );
//
//        return panel;
//    }
}
