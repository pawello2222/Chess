package com.pawello2222.chess.core;

import com.pawello2222.chess.model.GameState;
import com.pawello2222.chess.utils.ResourceLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Main game frame.
 *
 * @author Pawel Wiszenko
 */
public class Game extends JFrame implements EndOfGameListener
{
    private Board board;

    private ActionListener[] actionListeners;

    private Game()
    {
        setTitle( "Chess" );
        setIconImage( ResourceLoader.loadImageExitOnEx( "ICON.png" ) );

        initActionListeners();
        setJMenuBar( initMenuBar() );
        startNewGame( false );

        setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        setLocationRelativeTo( null );
        setResizable( false );
        setVisible( true );
    }

    private void initActionListeners()
    {
        actionListeners = new ActionListener[ 3 ];
        actionListeners[ 0 ] = event -> startNewGame( false );
        actionListeners[ 1 ] = event -> startNewGame( true );
        actionListeners[ 2 ] = event -> dispose();
    }

    private JMenuBar initMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add( initGameMenu() );
        return menuBar;
    }

    private JMenu initGameMenu()
    {
        JMenu menu = new JMenu( "Game" );
        JMenuItem menuItem;

        menuItem = new JMenuItem( "New game (WHITE)" );
        menuItem.addActionListener( actionListeners[ 0 ] );
        menu.add( menuItem );

        menuItem = new JMenuItem( "New game (BLACK)" );
        menuItem.addActionListener( actionListeners[ 1 ] );
        menu.add( menuItem );

        menu.addSeparator();

        menuItem = new JMenuItem( "Exit" );
        menuItem.addActionListener( actionListeners[ 2 ] );
        menu.add( menuItem );

        return menu;
    }

    private void startNewGame( boolean reversed )
    {
        if ( board != null )
            remove( board );

        board = MainFactory.getBoard( reversed );
        board.addGameEndListener( this );
        add( board );
        pack();
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
            actionListeners[ result ].actionPerformed( null );
    }

    public static void main( String[] args )
    {
        EventQueue.invokeLater( Game::new );
    }
}
