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

        this.setJMenuBar( initMenuBar() );
        startNewGame( false );

        this.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        this.setLocationRelativeTo( null );
        this.setVisible( true );
    }

    private JMenuBar initMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();

        menuBar.add( initGameMenu() );
        menuBar.add( initLanguageMenu() );

        return menuBar;
    }

    private JMenu initGameMenu()
    {
        JMenu menu = new JMenu( "Game" );
        JMenuItem menuItem;

        menuItem = new JMenuItem( "New game (WHITE)" );
        menuItem.addActionListener( event -> startNewGame( false ) );
        menu.add( menuItem );

        menuItem = new JMenuItem( "New game (BLACK)" );
        menuItem.addActionListener( event -> startNewGame( true ) );
        menu.add( menuItem );

        menu.addSeparator();

        menuItem = new JMenuItem( "Exit" );
        menuItem.addActionListener( event -> this.dispose() );
        menu.add( menuItem );

        return menu;
    }

    private JMenu initLanguageMenu()
    {
        JMenu menu = new JMenu( "Language" );
        JMenuItem menuItem;

        menuItem = new JMenuItem( "Polski (PL)" );
        menuItem.addActionListener( event -> {} );
        menu.add( menuItem );

        menuItem = new JMenuItem( "English (EN)" );
        menuItem.addActionListener( event -> {} );
        menu.add( menuItem );

        return menu;
    }

    private void startNewGame( boolean reversed )
    {
        if ( board != null )
            this.remove( board );
        board = new Board( reversed );
        this.add( board );
        this.pack();
    }

    public static void main( String[] args )
    {
        EventQueue.invokeLater( Game::new );
    }
}
