package com.pawello2222.chess.util;

/**
 * Invalid resource exception class.
 *
 * @author Pawel Wiszenko
 */
class InvalidResourceException extends RuntimeException
{
    private String resource;

    InvalidResourceException( String resource )
    {
        this.resource = resource;
    }

    @Override
    public String getMessage()
    {
        return "Cannot open a file: " + resource;
    }
}
