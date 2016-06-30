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

        initButtons( actionListeners );
    }

    private void initButtons( ActionListener[][] actionListeners )
    {
        int width = 200;
        int height = 40;
        int gap = 10;

        int bgWidth = bgImage.getWidth( this );
        int bgHeight = bgImage.getHeight( this );

        setLayout( new GridBagLayout() );
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(150, 30, 30, 30);

        JPanel panel = new JPanel( new GridBagLayout() );
        panel.setOpaque( false );

        JButton button = new TranslucentButton( "Local game - WHITE", actionListeners[ 0 ][ 0 ] );
        button.setPreferredSize( new Dimension( width, height ) );
        panel.add(button, gbc);
        button = new TranslucentButton( "Online game - WHITE", actionListeners[ 0 ][ 0 ] );
        button.setPreferredSize( new Dimension( width, height ) );
        panel.add(button, gbc);

        add( panel );

        panel = new JPanel( new GridBagLayout() );
        panel.setOpaque( false );

        button = new TranslucentButton( "Local game - BLACK", actionListeners[ 0 ][ 0 ] );
        button.setPreferredSize( new Dimension( width, height ) );
        panel.add(button, gbc);
        button = new TranslucentButton( "Online game - BLACK", actionListeners[ 0 ][ 0 ] );
        button.setPreferredSize( new Dimension( width, height ) );
        panel.add(button, gbc);

        add( panel );

//        JPanel panel = new JPanel();
//        panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS ) );
//
//        JButton button = new TranslucentButton( "Local game - WHITE", actionListeners[ 0 ][ 0 ] );
//        button.setPreferredSize( new Dimension( width, height ) );
//        panel.add( button );
//        button = new TranslucentButton( "Online game - WHITE", actionListeners[ 1 ][ 0 ] );
//        button.setPreferredSize( new Dimension( width, height ) );
//        panel.add( button );
//
//        add( panel );

//
//        panel.add(new JLabel("FirstName:"));
//        panel.add(new JTextField(10));
//        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//        add( panel );
//
//        panel = new JPanel();
//        panel.add(new JLabel("LastName:"));
//        panel.add(new JTextField(10));
//        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//        add( panel );

//        JButton button = new TranslucentButton( "Local game - WHITE" );
//        button.setPreferredSize( new Dimension( width, height ) );
////        button.setBounds( bgWidth / 4, bgHeight / 10 * 4 - height / 2, width, height );
//        button.addActionListener( actionListeners[ 0 ][ 0 ] );
//        add( button );
//
//        button = new TranslucentButton( "Local game - BLACK" );
//        button.setBounds( bgWidth / 4 * 3, bgHeight / 10 * 4 - height / 2, width, height );
//        button.addActionListener( actionListeners[ 0 ][ 1 ] );
//        add( button );
//
//        button = new TranslucentButton( "Online game - WHITE" );
//        button.setBounds( bgWidth / 4, bgHeight / 10 * 5, width, height );
//        button.addActionListener( actionListeners[ 1 ][ 0 ] );
//        add( button );
//
//        button = new TranslucentButton( "Online game - BLACK" );
//        button.setBounds( bgWidth / 4 * 3, bgHeight / 10 * 5, width, height );
//        button.addActionListener( actionListeners[ 1 ][ 1 ] );
//        add( button );
    }

    @Override
    protected void paintComponent( Graphics graphics )
    {
        graphics.drawImage( bgImage, 0, 0, null );
    }
}
