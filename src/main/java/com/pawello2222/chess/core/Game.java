package com.pawello2222.chess.core;

import com.pawello2222.chess.exception.InvalidResourceException;

import javax.swing.*;
import java.awt.*;

/**
 * Main game frame.
 *
 * @author Pawel Wiszenko
 */
public class Game extends JFrame
{
    private Board board;

    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem[] menuItems;

    private Game()
    {
        this.setTitle( "Chess" );
        try
        {
            this.setIconImage( BoardCreator.loadResource( "ICON.png" ) );
        }
        catch ( InvalidResourceException e )
        {
            System.out.println( e.getMessage() );
            System.exit( -1 );
        }

        menuBar = new JMenuBar();
        menuItems = new JMenuItem[ 6 ];
        menu = new JMenu( "Game" );
        menuBar.add( menu );
        this.setJMenuBar( menuBar );

        board = new Board( false );
        this.add( board );
        this.pack();

        this.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        this.setLocationRelativeTo( null );
        this.setVisible( true );
    }

    public static void main( String[] args )
    {
        EventQueue.invokeLater( new Runnable()
        {
            public void run()
            {
                new Game();
            }
        } );
    }
}
