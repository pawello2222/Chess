package com.pawello2222.chess.model;

/**
 * Game state.
 *
 * @author Pawel Wiszenko
 */
public enum GameState
{
    RUNNING_WHITE,
    RUNNING_BLACK,
    CHECKMATE_WIN_WHITE,
    CHECKMATE_WIN_BLACK,
    STALEMATE,
    NETWORK_CLOSE
}
