package amazons;

import org.junit.Test;

import static org.junit.Assert.*;

import ucb.junit.textui;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Junit tests for our Board iterators.
 *
 * @author Wendi Zhang
 */
public class IteratorTests {

    /**
     * Run the JUnit tests in this package.
     */
    public static void main(String[] ignored) {
        textui.runClasses(IteratorTests.class);
    }

    /**
     * Tests reachableFromIterator to make sure it returns all reachable
     * Squares. This method may need to be changed based on
     * your implementation.
     */
    @Test
    public void testReachableFrom() {
        Board b = new Board();
        buildBoard(b, REACHEABLEFROM);
        int numSquares = 0;
        Set<Square> squares = new HashSet<>();
        Iterator<Square> reachableFrom = b.reachableFrom(Square.sq(5, 4), null);
        while (reachableFrom.hasNext()) {
            Square s = reachableFrom.next();
            System.out.println(s);
            assertTrue(REACHABLEFROMSQ1.contains(s));
            numSquares += 1;
            squares.add(s);
        }
        assertEquals(REACHABLEFROMSQ1.size(), numSquares);
        assertEquals(REACHABLEFROMSQ1.size(), squares.size());
    }

    @Test
    public void testReachableFrom1() {
        Board b = new Board();
        buildBoard(b, REACHABLEFROM1);
        int numSquares = 0;
        Set<Square> squares = new HashSet<>();
        Iterator<Square> reachableFrom = b.reachableFrom(Square.sq(1, 1), null);
        while (reachableFrom.hasNext()) {
            Square s = reachableFrom.next();
            System.out.println(s);
            assertTrue(REACHABLEFROMSQTEST1.contains(s));
            numSquares += 1;
            squares.add(s);
        }
        assertEquals(REACHABLEFROMSQTEST1.size(), numSquares);
        assertEquals(REACHABLEFROMSQTEST1.size(), squares.size());
    }

    @Test
    public void testReachableFrom2() {
        Board b = new Board();
        buildBoard(b, REACHABLEFROM2);
        int numSquares = 0;
        Set<Square> squares = new HashSet<>();
        Iterator<Square> reachableFrom = b.reachableFrom(Square.sq(2, 6), null);
        while (reachableFrom.hasNext()) {
            Square s = reachableFrom.next();
            System.out.println(s);
            assertTrue(REACHABLEFROMSQ2.contains(s));
            numSquares += 1;
            squares.add(s);
        }
        assertEquals(REACHABLEFROMSQ2.size(), numSquares);
        assertEquals(REACHABLEFROMSQ2.size(), squares.size());
    }

    @Test
    public void testReachableFrom3() {
        Board b = new Board();
        buildBoard(b, REACHABLEFROM3);
        int numSquares = 0;
        Set<Square> squares = new HashSet<>();
        Iterator<Square> reachableFrom = b.reachableFrom(Square.sq(0, 0), null);
        while (reachableFrom.hasNext()) {
            Square s = reachableFrom.next();
            System.out.println(s);
            assertTrue(REACHABLEFROMSQ3.contains(s));
            numSquares += 1;
            squares.add(s);
        }
        assertEquals(REACHABLEFROMSQ3.size(), numSquares);
        assertEquals(REACHABLEFROMSQ3.size(), squares.size());
    }

    @Test
    public void testReachableFromasEmpty() {
        Board b = new Board();
        buildBoard(b, REACHABLEFROMEMPTY);
        int numSquares = 0;
        Set<Square> squares = new HashSet<>();
        Iterator<Square> reachableFrom =
                b.reachableFrom(Square.sq(5, 4), Square.sq(5, 3));
        while (reachableFrom.hasNext()) {
            Square s = reachableFrom.next();
            System.out.println(s);
            assertTrue(REACHABLEFROMEMPTY1.contains(s));
            numSquares += 1;
            squares.add(s);
        }
        assertEquals(REACHABLEFROMEMPTY1.size(), numSquares);
        assertEquals(REACHABLEFROMEMPTY1.size(), squares.size());
    }

    /**
     * Tests legalMovesIterator to make sure it returns all legal Moves.
     * This method needs to be finished and may need to be changed
     * based on your implementation.
     */
    @Test
    public void testLegalMoves() {
        Board b = new Board();
        buildBoard(b, LEGALMOVETEST);
        int numMoves = 0;
        Set<Move> moves = new HashSet<>();
        Iterator<Move> legalMoves = b.legalMoves(Piece.WHITE);
        while (legalMoves.hasNext()) {
            Move m = legalMoves.next();
            assertTrue(LEGALMOVETESTMOVES.contains(m));
            numMoves += 1;
            moves.add(m);
        }
        assertEquals(LEGALMOVETESTMOVES.size(), numMoves);
        assertEquals(LEGALMOVETESTMOVES.size(), moves.size());
    }

    @Test
    public void testInitialLegalMoves() {
        Board b = new Board();
        buildBoard(b, LEGALMOVETEST1);
        int numMoves = 0;
        Set<Move> moves = new HashSet<>();
        Iterator<Move> legalMoves = b.legalMoves(Piece.BLACK);
        while (legalMoves.hasNext()) {
            Move m = legalMoves.next();
            numMoves += 1;
            moves.add(m);
        }
        assertEquals(2176, numMoves);
        assertEquals(2176, moves.size());
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

    static final Piece[][] REACHEABLEFROM =
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

    static final Piece[][] REACHABLEFROM1 =
    {
            {E, E, E, E, E, E, E, E, E, E},
            {E, E, E, E, E, E, E, E, E, E},
            {E, E, E, E, E, E, E, S, E, S},
            {E, E, E, S, S, S, S, E, E, S},
            {E, E, E, S, E, E, E, E, B, E},
            {E, E, E, S, E, E, E, E, B, E},
            {E, E, E, S, S, S, B, E, B, E},
            {S, S, E, E, E, E, E, E, E, E},
            {S, B, S, E, E, E, E, E, E, E},
            {W, S, S, E, E, E, E, E, E, E},
    };

    static final Piece[][] REACHABLEFROM2 =
    {
            {E, E, E, E, E, E, E, E, E, E},
            {E, W, E, E, E, E, E, E, E, E},
            {E, E, E, E, E, E, E, S, E, S},
            {E, E, E, S, S, S, S, E, E, S},
            {S, E, E, S, E, E, E, E, B, E},
            {W, B, S, S, W, E, E, E, B, E},
            {E, E, E, S, S, S, B, E, B, E},
            {S, S, S, E, E, E, E, E, E, E},
            {E, W, S, E, E, E, E, E, E, E},
            {E, B, S, E, E, E, E, E, E, E},
    };

    static final Piece[][] REACHABLEFROM3 =
    {
            {E, E, E, E, E, E, E, E, E, E},
            {E, E, E, E, E, E, E, E, E, E},
            {E, E, E, E, E, E, E, S, E, S},
            {E, E, E, S, S, S, S, E, E, S},
            {E, E, E, S, E, E, E, E, B, E},
            {E, E, E, S, E, E, E, E, B, E},
            {E, E, E, S, S, S, B, E, B, E},
            {S, S, S, E, E, E, E, E, E, E},
            {E, E, S, E, E, E, E, E, E, E},
            {W, B, S, E, E, E, E, E, E, E},
    };

    static final Piece[][] REACHABLEFROMEMPTY =
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

    static final Set<Square> REACHABLEFROMSQ1 =
            new HashSet<>(Arrays.asList(
                    Square.sq(5, 5),
                    Square.sq(4, 5),
                    Square.sq(4, 4),
                    Square.sq(6, 4),
                    Square.sq(7, 4),
                    Square.sq(6, 5),
                    Square.sq(7, 6),
                    Square.sq(8, 7)));

    static final Set<Square> REACHABLEFROMSQTEST1 =
            new HashSet<>(Arrays.asList(
                    Square.sq(2, 2)));

    static final Set<Square> REACHABLEFROMSQ2 =
            new HashSet<>(Arrays.asList(
                    Square.sq(2, 5),
                    Square.sq(1, 5),
                    Square.sq(1, 6),
                    Square.sq(1, 7),
                    Square.sq(0, 6),
                    Square.sq(0, 8),
                    Square.sq(2, 7),
                    Square.sq(2, 8),
                    Square.sq(2, 9),
                    Square.sq(3, 7),
                    Square.sq(4, 8),
                    Square.sq(5, 9)));

    static final Set<Square> REACHABLEFROMSQ3 =
            new HashSet<>(Arrays.asList(
                    Square.sq(0, 1),
                    Square.sq(1, 1)));

    static final Set<Square> REACHABLEFROMEMPTY1 =
            new HashSet<>(Arrays.asList(
                    Square.sq(5, 5),
                    Square.sq(5, 3),
                    Square.sq(5, 2),
                    Square.sq(5, 1),
                    Square.sq(5, 0),
                    Square.sq(4, 5),
                    Square.sq(4, 4),
                    Square.sq(6, 4),
                    Square.sq(7, 4),
                    Square.sq(6, 5),
                    Square.sq(7, 6),
                    Square.sq(8, 7)));

    static final Piece[][] LEGALMOVETEST =
    {
            {E, E, E, E, E, E, E, E, E, E},
            {E, E, E, E, E, E, E, E, E, E},
            {E, E, E, E, E, E, E, S, E, S},
            {E, E, E, S, S, S, S, E, E, S},
            {E, E, E, S, E, E, E, E, B, E},
            {E, E, E, S, E, E, E, E, B, E},
            {E, E, E, S, S, S, B, E, B, E},
            {S, S, S, E, E, E, E, E, E, E},
            {E, E, S, E, E, E, E, E, E, E},
            {W, B, S, E, E, E, E, E, E, E},
    };

    static final Piece[][] LEGALMOVETEST1 =
    {
            {E, E, E, B, E, E, B, E, E, E},
            {E, E, E, E, E, E, E, E, E, E},
            {E, E, E, E, E, E, E, E, E, E},
            {B, E, E, E, E, E, E, E, E, B},
            {E, E, E, E, E, E, E, E, E, E},
            {E, E, E, E, E, E, E, E, E, E},
            {W, E, E, E, E, E, E, E, E, W},
            {E, E, E, E, E, E, E, E, E, E},
            {E, E, E, E, E, E, E, E, E, E},
            {E, E, E, W, E, E, W, E, E, E},
    };

    static final Set<Move> LEGALMOVETESTMOVES =
            new HashSet<>(Arrays.asList(
                    Move.mv(Square.sq(0, 0),
                            Square.sq(0, 1), Square.sq(1, 1)),
                    Move.mv(Square.sq(0, 0),
                            Square.sq(0, 1), Square.sq(0, 0)),
                    Move.mv(Square.sq(0, 0),
                            Square.sq(1, 1), Square.sq(0, 0)),
                    Move.mv(Square.sq(0, 0),
                            Square.sq(1, 1), Square.sq(0, 1))));
}
