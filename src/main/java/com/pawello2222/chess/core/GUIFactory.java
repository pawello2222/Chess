package com.pawello2222.chess.core;

import com.pawello2222.chess.core.gui.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Graphical user interface factory.
 *
 * @author Pawel Wiszenko
 */
public abstract class GUIFactory
{
    static JFrame getApplicationFrame()
    {
        return new MenuFrame();
    }

    public static JPanel getMenuPanel( Image image, ActionListener[][] actionListeners )
    {
        return new MenuPanel( image, actionListeners );
    }
}
