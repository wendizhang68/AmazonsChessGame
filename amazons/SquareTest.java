package amazons;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * The suite of all JUnit tests for the Permutation class.
 *
 * @author Wendi Zhang
 */
public class SquareTest {

    private Square square1 = Square.sq(0);
    private Square square2 = Square.sq(15);
    private Square square3 = Square.sq("e", "7");
    private Square square4 = Square.sq("i5");
    private Square square5 = Square.sq(99);

    @Test
    public void checkSetUp() {
        assertEquals(0, square1.col());
        assertEquals(5, square2.col());
        assertEquals(1, square2.row());
        assertEquals(6, square3.row());
        assertEquals(8, square4.col());
        assertEquals("f2", square2.toString());
        assertEquals("j10", square5.toString());
    }

    @Test
    public void checkMove() {
        Square move1 = square1.queenMove(0, 4);
        assertEquals(true, square1.isQueenMove(move1));
        assertEquals("a5", move1.toString());
        assertEquals(0, square1.direction(move1));
        assertEquals(false, square2.isQueenMove(square3));
    }
}

