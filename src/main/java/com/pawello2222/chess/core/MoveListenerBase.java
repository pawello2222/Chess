package com.pawello2222.chess.core;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Move listener base class.
 *
 * @author Pawel Wiszenko
 */
abstract class MoveListenerBase implements MouseListener, MouseMotionListener
{
    @Override
    public abstract void mousePressed( MouseEvent e );

    @Override
    public abstract void mouseDragged( MouseEvent e );

    @Override
    public abstract void mouseReleased( MouseEvent e );

    @Override
    public abstract void mouseMoved( MouseEvent e );

    @Override
    public void mouseClicked( MouseEvent e )
    {

    }

    @Override
    public void mouseEntered( MouseEvent e )
    {

    }

    @Override
    public void mouseExited( MouseEvent e )
    {

    }
}
