package com.pawello2222.chess.core.gui;

import com.pawello2222.chess.core.GameBase;
import com.pawello2222.chess.model.GameType;
import com.pawello2222.chess.util.ResourceLoader;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static com.pawello2222.chess.core.AppFactory.getAppPanel;
import static com.pawello2222.chess.core.MainFactory.*;

/**
 * Application frame.
 *
 * @author Pawel Wiszenko
 */
public class AppFrame extends JFrame
{
    private ActionListener[][] actionListeners;

    public AppFrame()
    {
        setTitle( "Main menu" );
        setIconImage( ResourceLoader.loadImageExitOnEx( "ICON.png" ) );

        initActionListeners();
        setJMenuBar( initMenuBar() );
        add( getAppPanel( ResourceLoader.loadImageExitOnEx( "BACKGROUND_ALL.png" ), actionListeners ) );
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

            JOptionPane.showConfirmDialog( this, hostname, "Host name",
                                           JOptionPane.DEFAULT_OPTION,
                                           JOptionPane.PLAIN_MESSAGE );
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

    private void startNewGame( GameType gameType )
    {
        GameBase game = null;

        switch ( gameType )
        {
            case LOCAL_WHITE:
            case LOCAL_BLACK:
                game = getLocalGame( this );
                break;

            case ONLINE_WHITE:
                game = getServerGame( this );
                break;
            case ONLINE_BLACK:
                game = getClientGame( this );
                break;
        }

        game.start( gameType );
    }
}
