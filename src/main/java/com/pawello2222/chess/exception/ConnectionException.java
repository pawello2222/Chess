package com.pawello2222.chess.exception;

/**
 * Connection exception class.
 *
 * @author Pawel Wiszenko
 */
public class ConnectionException extends RuntimeException
{
    @Override
    public String getMessage()
    {
        return "Connection broken";
    }
}