package com.pawello2222.chess.exception;

/**
 * Invalid resource exception class.
 *
 * @author Pawel Wiszenko
 */
public class InvalidResourceException extends RuntimeException
{
    private String resource;

    public InvalidResourceException( String resource )
    {
        this.resource = resource;
    }

    @Override
    public String getMessage()
    {
        return "Cannot open a file: " + resource;
    }
}
