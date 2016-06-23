package com.pawello2222.chess.exception;

/**
 * Socket creation exception class.
 *
 * @author Pawel Wiszenko
 */
public class SocketCreationException extends RuntimeException
{
    private int port;
    private int timeout;

    public SocketCreationException( int port, int timeout )
    {
        this.port = port;
        this.timeout = timeout;
    }

    @Override
    public String getMessage()
    {
        return "Cannot create a socket on port " + port + " with timeout: " + timeout;
    }
}