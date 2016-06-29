package com.pawello2222.chess.core;

import javax.swing.*;
import java.awt.*;

/**
 * Main class.
 *
 * @author Pawel Wiszenko
 */
public abstract class Application extends JFrame
{
    public static void main( String[] args )
    {
        //TODO getAppGUI -> rename
        EventQueue.invokeLater( MainFactory::getAppGUI );
    }
}
