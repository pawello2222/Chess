package com.pawello2222.chess.core.gui;

import com.pawello2222.chess.util.TranslucentButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Main menu panel.
 *
 * @author Pawel Wiszenko
 */
public class AppPanel extends JPanel
{
    private static final int buttonWidth = 200;
    private static final int buttonHeight = 40;

    private Image bgImage;

    private ActionListener[][] actionListeners;

    public AppPanel( Image bgImage, ActionListener[][] actionListeners )
    {
        this.bgImage = bgImage;
        this.actionListeners = actionListeners;

        setOpaque( false );
        setPreferredSize( new Dimension( bgImage.getWidth( this ),
                                         bgImage.getHeight( this ) ) );

        setLayout( new BorderLayout() );

        JPanel panel = new JPanel( new GridBagLayout() );
        panel.setOpaque( false );

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets( 40, 30, 0, 30 );

        String[] labels1 = { "Local game - WHITE", "Host game - WHITE" };
        panel.add( initPanel( gbc, labels1, 0 ) );
        String[] labels2 = { "Local game - BLACK", "Join game - BLACK" };
        panel.add( initPanel( gbc, labels2, 1 ) );
        add( panel );
    }

    private JPanel initPanel( GridBagConstraints gbc, String[] labels, int black )
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        panel.setOpaque( false );

        for ( int i = 0; i < 2; i++ )
        {
            JButton button = new TranslucentButton( labels[ i ], actionListeners[ i ][ black ] );
            button.setPreferredSize( new Dimension( buttonWidth, buttonHeight ) );
            panel.add( button, gbc );
        }

        return panel;
    }

    @Override
    protected void paintComponent( Graphics graphics )
    {
        graphics.drawImage( bgImage, 0, 0, null );
    }
}
