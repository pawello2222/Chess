package com.pawello2222.chess.util;

/**
 * Network utilities.
 *
 * @author Pawel Wiszenko
 */
public abstract class NetworkUtils
{
    public static int validateInt( String input, int min, int max )
    {
        int result = getInt( input );

        if ( result < min || result > max )
            result = -1;

        return result;
    }

    private static int getInt( String input )
    {
        int result;

        try
        {
            result = Integer.parseInt( input );
        }
        catch ( NumberFormatException e )
        {
            result = -1;
        }

        return result;
    }
}
