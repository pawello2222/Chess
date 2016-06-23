package com.pawello2222.chess.core;

import com.pawello2222.chess.exception.ConnectionException;
import com.pawello2222.chess.exception.SocketCreationException;
import com.pawello2222.chess.model.GameState;
import com.pawello2222.chess.model.NetworkGame;
import com.pawello2222.chess.utils.ResourceLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Main game frame.
 *
 * @author Pawel Wiszenko
 */
public class Game extends JFrame implements EndOfGameListener
{
    private Board board;

    private ActionListener[][] actionListeners;

    private Game()
    {
        setTitle( "Chess" );
        setIconImage( ResourceLoader.loadImageExitOnEx( "ICON.png" ) );

        initActionListeners();
        setJMenuBar( initMenuBar() );
        startNewGame( false, NetworkGame.DISABLED, "", "" );

        setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        setLocationRelativeTo( null );
        setResizable( false );
        setVisible( true );
    }

    private void initActionListeners()
    {
        actionListeners = new ActionListener[ 2 ][ 3 ];
        actionListeners[ 0 ][ 0 ] = event -> startNewGame( false, NetworkGame.DISABLED, "", "" );
        actionListeners[ 0 ][ 1 ] = event -> startNewGame( true, NetworkGame.DISABLED, "", "" );
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

            startNewGame( false, NetworkGame.SERVER, port, timeout );
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

            startNewGame( true, NetworkGame.CLIENT, serverName, port );
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

            JOptionPane.showConfirmDialog( this,
                                         hostname,
                                         "Host name",
                                         JOptionPane.DEFAULT_OPTION );
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

    private void startNewGame( boolean reversed, NetworkGame networkGame, String param1, String param2 )
    {
        setEnabled( false );
        if ( board != null )
            remove( board );

        try
        {
            board = MainFactory.initBoard( reversed, networkGame, param1, param2 );
            board.addGameEndListener( this );
        }
        catch ( ConnectionException e )
        {
            displayError( e.getMessage() );
        }
        catch ( SocketCreationException e )
        {
            displayError( e.getMessage() );
        }

        add( board );
        pack();
        setEnabled( true );
    }

    private int getEndGameDialogResult()
    {
        Object[] options = { "Yes (White)", "Yes (Black)", "No" };
        String title = "Stalemate";
        String message = "It's a draw.";

        if ( board.isGameState( GameState.CHECKMATE_WIN_WHITE ) )
        {
            title = "Checkmate";
            message = "White player wins!";
        }
        else if ( board.isGameState( GameState.CHECKMATE_WIN_BLACK ) )
        {
            title = "Checkmate";
            message = "Black player wins!";
        }

        return JOptionPane.showOptionDialog(
                board.getParent(),
                message + " Do you want to start a new game?",
                title,
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[ 0 ] );
    }

    @Override
    public void endOfGame()
    {
        int result = getEndGameDialogResult();
        if ( result >= 0 )
            actionListeners[ 0 ][ result ].actionPerformed( null );
    }

    private void displayError( String message )
    {
        JOptionPane.showConfirmDialog( this,
                                       message,
                                       "Error",
                                       JOptionPane.DEFAULT_OPTION,
                                       JOptionPane.WARNING_MESSAGE );
    }

    public static void main( String[] args )
    {
        EventQueue.invokeLater( Game::new );
    }
}
