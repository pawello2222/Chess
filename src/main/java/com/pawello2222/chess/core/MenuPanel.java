package com.pawello2222.chess.core;

import com.pawello2222.chess.util.TranslucentButton;

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

        JButton button = new TranslucentButton( "Chess" );
        button.setBounds( bgImage.getWidth( this ) / 4, bgImage.getHeight( this ) / 2, 200, 40 );
//        button.setPreferredSize( new Dimension( 200, 40 ) );
        add( button );

        setLayout( null );

//        setLayout( new FlowLayout() );
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
