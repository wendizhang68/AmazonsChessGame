package amazons;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * The suite of all Board tests for the amazons package.
 *
 * @author Wendi Zhang
 */

public class BoardTest {

    /**
     * Tests isUnBlockedMove.
     */
    @Test
    public void isUnBlockedMoveTest() {
        Board b = new Board();
        buildBoard(b, ISUNBLOCKEDMOVETESTBOARD);
        assertEquals(false, b.isUnblockedMove(Square.sq(5, 4),
                Square.sq(5, 4), null));
        assertEquals(true, b.isUnblockedMove(Square.sq(5, 4),
                Square.sq(5, 5), null));
        assertEquals(false, b.isUnblockedMove(Square.sq(5, 4),
                Square.sq(5, 6), null));
        assertEquals(false, b.isUnblockedMove(Square.sq(5, 4),
                Square.sq(5, 9), null));
        assertEquals(true, b.isUnblockedMove(Square.sq(5, 4),
                Square.sq(9, 8), Square.sq(9, 8)));
        assertEquals(true, b.isUnblockedMove(Square.sq(5, 4),
                Square.sq(9, 0), Square.sq(6, 3)));
        assertEquals(true, b.isUnblockedMove(Square.sq(7, 7),
                Square.sq(7, 1), Square.sq(7, 3)));
    }

    private void buildBoard(Board b, Piece[][] target) {
        for (int col = 0; col < Board.SIZE; col++) {
            for (int row = Board.SIZE - 1; row >= 0; row--) {
                Piece piece = target[Board.SIZE - row - 1][col];
                b.put(piece, Square.sq(col, row));
            }
        }
        System.out.println(b);
    }

    static final Piece E = Piece.EMPTY;

    static final Piece W = Piece.WHITE;

    static final Piece B = Piece.BLACK;

    static final Piece S = Piece.SPEAR;

    static final Piece[][] ISUNBLOCKEDMOVETESTBOARD =
    {
        {E, E, E, E, E, E, E, E, E, E},
        {E, E, E, E, E, E, E, E, W, W},
        {E, E, E, E, E, E, E, S, E, S},
        {E, E, E, S, S, S, S, E, E, S},
        {E, E, E, S, E, E, E, E, B, E},
        {E, E, E, S, E, W, E, E, B, E},
        {E, E, E, S, S, S, B, W, B, E},
        {E, E, E, E, E, E, E, E, E, E},
        {E, E, E, E, E, E, E, E, E, E},
        {E, E, E, E, E, E, E, E, E, E},
    };
}
