package com.pawello2222.chess.core;

import com.pawello2222.chess.model.NetworkGame;
import com.pawello2222.chess.utils.ResourceLoader;

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
public class MainMenu extends JFrame implements MessageDisplayer
{
    private GameBase currentGame;
    private ActionListener[][] actionListeners;

    private MainMenu()
    {
        setTitle( "Main menu" );
        setIconImage( ResourceLoader.loadImageExitOnEx( "ICON.png" ) );

        initActionListeners();
        setJMenuBar( initMenuBar() );

        JButton button = new JButton( "Local - white" );
        button.addActionListener( event -> startNewGame( false ) );

        add( button );
        pack();

        setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        setLocationRelativeTo( null );
        setResizable( false );
        setVisible( true );
    }

    private void initActionListeners()
    {
        actionListeners = new ActionListener[ 2 ][ 3 ];
        actionListeners[ 0 ][ 0 ] = event -> startNewGame( false );
        actionListeners[ 0 ][ 1 ] = event -> startNewGame( true );
        actionListeners[ 0 ][ 2 ] = event -> dispose();

        actionListeners[ 1 ][ 0 ] = event ->
        {
            String port = JOptionPane.showInputDialog( this,
                                                       "Specify port number:",
                                                       "Host game",
                                                       JOptionPane.PLAIN_MESSAGE );

            String timeout = JOptionPane.showInputDialog( this,
                                                          "Specify timeout:",
                                                          "Host game",
                                                          JOptionPane.PLAIN_MESSAGE );

            String[] params = { port, timeout };
            startNewGame( false, NetworkGame.SERVER, params );
        };
        actionListeners[ 1 ][ 1 ] = event ->
        {
            String serverName = JOptionPane.showInputDialog( this,
                                                             "Specify server name:",
                                                             "Join game",
                                                             JOptionPane.PLAIN_MESSAGE );

            String port = JOptionPane.showInputDialog( this,
                                                       "Specify port number:",
                                                       "Join game",
                                                       JOptionPane.PLAIN_MESSAGE );

            String[] params = { serverName, port };
            startNewGame( true, NetworkGame.CLIENT, params );
        };
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

    private void startNewGame( boolean reversed )
    {
        currentGame = getGame( this, reversed );
    }

    private void startNewGame( boolean reversed, NetworkGame networkGame, String[] params )
    {
        currentGame = getGame( this, reversed, networkGame, params );
    }

    @Override
    public void displayMessage( String message )
    {

    }

    @Override
    public void displayMessage( String title, String message )
    {

    }

    @Override
    public void displayError( String error )
    {

    }

    public static void main( String[] args )
    {
        EventQueue.invokeLater( MainMenu::new );
    }
}
