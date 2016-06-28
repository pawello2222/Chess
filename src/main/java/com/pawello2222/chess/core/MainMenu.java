package com.pawello2222.chess.core;

import com.pawello2222.chess.model.GameType;
import com.pawello2222.chess.util.ResourceLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static com.pawello2222.chess.core.MainFactory.getGame;

/**
 * Main menu frame.
 *
 * @author Pawel Wiszenko
 */
public class MainMenu extends JFrame
{
    private ActionListener[][] actionListeners;

    private MainMenu()
    {
        setTitle( "Main menu" );
        setIconImage( ResourceLoader.loadImageExitOnEx( "ICON.png" ) );

        initActionListeners();
        setJMenuBar( initMenuBar() );
        add( localGamePanel() );
        add( onlineGamePanel() );
        pack();

        setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        setLocationRelativeTo( null );
        setResizable( false );
        setVisible( true );
    }

    private void initActionListeners()
    {
        actionListeners = new ActionListener[ 2 ][ 3 ];
        actionListeners[ 0 ][ 0 ] = event -> startNewGame( GameType.LOCAL_WHITE );
        actionListeners[ 0 ][ 1 ] = event -> startNewGame( GameType.LOCAL_BLACK );
        actionListeners[ 0 ][ 2 ] = event -> dispose();
        actionListeners[ 1 ][ 0 ] = event -> startNewGame( GameType.ONLINE_WHITE );
        actionListeners[ 1 ][ 1 ] = event -> startNewGame( GameType.ONLINE_BLACK );
        actionListeners[ 1 ][ 2 ] = event ->
        {
            String hostname;
            try
            {
                hostname = InetAddress.getLocalHost().getHostName();
            }
            catch ( UnknownHostException e )
            {
                hostname = "Cannot retrieve host name. Unknown host.";
            }

            displayMessage( "Host name", hostname );
        };
    }

    private JMenuBar initMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add( initGameMenu() );
        menuBar.add( initNetworkMenu() );
        return menuBar;
    }

    private JMenu initGameMenu()
    {
        JMenu menu = new JMenu( "Game" );
        JMenuItem menuItem;

        menuItem = new JMenuItem( "New game (WHITE)" );
        menuItem.addActionListener( actionListeners[ 0 ][ 0 ] );
        menu.add( menuItem );

        menuItem = new JMenuItem( "New game (BLACK)" );
        menuItem.addActionListener( actionListeners[ 0 ][ 1 ] );
        menu.add( menuItem );

        menu.addSeparator();

        menuItem = new JMenuItem( "Exit" );
        menuItem.addActionListener( actionListeners[ 0 ][ 2 ] );
        menu.add( menuItem );

        return menu;
    }

    private JMenu initNetworkMenu()
    {
        JMenu menu = new JMenu( "Network" );
        JMenuItem menuItem;

        menuItem = new JMenuItem( "Host game (WHITE)" );
        menuItem.addActionListener( actionListeners[ 1 ][ 0 ] );
        menu.add( menuItem );

        menuItem = new JMenuItem( "Join game (BLACK)" );
        menuItem.addActionListener( actionListeners[ 1 ][ 1 ] );
        menu.add( menuItem );

        menu.addSeparator();

        menuItem = new JMenuItem( "Info" );
        menuItem.addActionListener( actionListeners[ 1 ][ 2 ] );
        menu.add( menuItem );

        return menu;
    }

    private JPanel localGamePanel()
    {
        JPanel panel = new JPanel();
        panel.setBorder( BorderFactory.createTitledBorder( "New game (local)" ) );

        JButton button = new JButton( "White" );
        button.addActionListener( actionListeners[ 0 ][ 0 ] );
        panel.add( button );

        button = new JButton( "Black" );
        button.addActionListener( actionListeners[ 0 ][ 1 ] );
        panel.add( button );

        return panel;
    }

    private JPanel onlineGamePanel()
    {
        JPanel panel = new JPanel();
        panel.setBorder( BorderFactory.createTitledBorder( "New game (online)" ) );

        JButton button = new JButton( "Host" );
        button.addActionListener( actionListeners[ 1 ][ 0 ] );
        panel.add( button );

        button = new JButton( "Join" );
        button.addActionListener( actionListeners[ 1 ][ 1 ] );
        panel.add( button );

        return panel;
    }

    private void startNewGame( GameType gameType )
    {
        getGame( this, gameType );
    }

    private void displayMessage( String title, String message )
    {
        JOptionPane.showConfirmDialog( this,
                                       message,
                                       title,
                                       JOptionPane.DEFAULT_OPTION,
                                       JOptionPane.PLAIN_MESSAGE );
    }

    public static void main( String[] args )
    {
        EventQueue.invokeLater( MainMenu::new );
    }
}