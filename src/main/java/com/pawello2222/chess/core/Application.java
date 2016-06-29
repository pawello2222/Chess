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
        EventQueue.invokeLater( MainFactory::getApplicationGUI );
    }
}

// todo: add board base class
// todo: add verification of null dependencies (everywhere)
