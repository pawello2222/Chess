package com.pawello2222.chess.core;

import com.pawello2222.chess.core.gui.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Main GUI factory.
 *
 * @author Pawel Wiszenko
 */
public abstract class AppFactory
{
    static JFrame getApplicationFrame()
    {
        return new AppFrame();
    }

    public static JPanel getMenuPanel( Image image, ActionListener[][] actionListeners )
    {
        return new AppPanel( image, actionListeners );
    }
}
