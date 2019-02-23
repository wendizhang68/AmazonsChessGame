package amazons;

import ucb.gui2.Pad;

import java.io.IOException;

import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import static amazons.Piece.*;
import static amazons.Square.sq;

/**
 * A widget that displays an Amazons game.
 *
 * @author Wendi Zhang
 */
class BoardWidget extends Pad {

    /* Parameters controlling sizes, speeds, colors, and fonts. */

    /**
     * Colors of empty squares and grid lines.
     */
    static final Color
            SPEAR_COLOR = new Color(200, 100, 100),
            LIGHT_SQUARE_COLOR = new Color(230, 200, 161),
            DARK_SQUARE_COLOR = new Color(200, 130, 60),
            CLICK_COLOR = new Color(20, 100, 200);

    /**
     * Locations of images of white and black queens.
     */
    private static final String
            WHITE_QUEEN_IMAGE = "ph.png",
            BLACK_QUEEN_IMAGE = "jh.png",
            SPEAR_IMAGE = "jd.png";

    /**
     * Size parameters.
     */
    private static final int
            SQUARE_SIDE = 30,
            BOARD_SIDE = SQUARE_SIDE * 10;

    /**
     * A graphical representation of an Amazons board that sends commands
     * derived from mouse clicks to COMMANDS.
     */
    BoardWidget(ArrayBlockingQueue<String> commands) {
        _commands = commands;
        setMouseHandler("click", this::mouseClicked);
        setPreferredSize(BOARD_SIDE, BOARD_SIDE);

        try {
            _whiteQueen = ImageIO.read(Utils.getResource(WHITE_QUEEN_IMAGE));
            _blackQueen = ImageIO.read(Utils.getResource(BLACK_QUEEN_IMAGE));
            _spear = ImageIO.read(Utils.getResource(SPEAR_IMAGE));
        } catch (IOException excp) {
            System.err.println("Could not read queen images.");
            System.exit(1);
        }
        _acceptingMoves = false;
    }

    /**
     * Draw the bare board G.
     */
    private void drawGrid(Graphics2D g) {
        g.setColor(LIGHT_SQUARE_COLOR);
        g.fillRect(0, 0, BOARD_SIDE, BOARD_SIDE);
        g.setColor(DARK_SQUARE_COLOR);
        for (int x = 0; x < 10; x += 2) {
            for (int y = 0; y < 10; y += 2) {
                g.fillRect(cx(x), cx(y), SQUARE_SIDE, SQUARE_SIDE);

            }
        }
        for (int x = 1; x < 10; x += 2) {
            for (int y = 1; y < 10; y += 2) {
                g.fillRect(cx(x), cx(y), SQUARE_SIDE, SQUARE_SIDE);

            }
        }
    }

    @Override
    public synchronized void paintComponent(Graphics2D g) {
        drawGrid(g);
        Iterator<Square> squareIterator = Square.iterator();
        while (squareIterator.hasNext()) {
            Square nextSquare = squareIterator.next();
            if (_board.get(nextSquare) == WHITE) {
                drawQueen(g, nextSquare, WHITE);
            } else if (_board.get(nextSquare) == BLACK) {
                drawQueen(g, nextSquare, BLACK);
            } else if (_board.get(nextSquare) == SPEAR) {
                //drawQueen(g, nextSquare, SPEAR);
                g.drawImage(_spear, cx(nextSquare.col()), cy(nextSquare.row()), null);
//                g.fillRect(cx(nextSquare.col()),
//                        cy(nextSquare.row()), SQUARE_SIDE, SQUARE_SIDE);
            }
        }
        if (_click[0] != null) {
            g.setColor(CLICK_COLOR);
            g.fillRect(cx(_click[0].col()),
                    cy(_click[0].row()), SQUARE_SIDE, SQUARE_SIDE);
        } else if (_click[1] != null) {
            g.setColor(CLICK_COLOR);
            g.fillRect(cx(_click[1].col()),
                    cy(_click[1].row()), SQUARE_SIDE, SQUARE_SIDE);
        }
    }

    /**
     * Draw a queen for side PIECE at square S on G.
     */
    private void drawQueen(Graphics2D g, Square s, Piece piece) {
        g.drawImage(piece == WHITE ? _whiteQueen : _blackQueen,
                cx(s.col()) + 2, cy(s.row()) + 4, null);
    }

    /**
     * Handle a click on S.
     */
    private void click(Square s) {
        if (_click[0] == null && _board.isLegal(s)) {
            _click[0] = s;
        } else if (_click[1] == null && _board.isLegal(_click[0], s)) {
            _click[1] = s;
        } else if (_board.isLegalBefore(_click[0], _click[1], s)) {
            _commands.offer(_click[0].toString()
                    + " " + _click[1].toString() + " " + s.toString());
            _click = new Square[2];
        }
    }

    /**
     * Handle mouse click event E.
     */
    private synchronized void mouseClicked(String unused, MouseEvent e) {
        int xpos = e.getX(), ypos = e.getY();
        int x = xpos / SQUARE_SIDE,
                y = (BOARD_SIDE - ypos) / SQUARE_SIDE;
        if (_acceptingMoves
                && x >= 0 && x < Board.SIZE && y >= 0 && y < Board.SIZE) {
            click(sq(x, y));
        }
    }

    /**
     * Revise the displayed board according to BOARD.
     */
    synchronized void update(Board board) {
        _board.copy(board);
        repaint();
    }

    /**
     * Turn on move collection iff COLLECTING, and clear any current
     * partial selection.   When move collection is off, ignore clicks on
     * the board.
     */
    void setMoveCollection(boolean collecting) {
        _acceptingMoves = collecting;
        repaint();
    }

    /**
     * Return x-pixel coordinate of the left corners of column X
     * relative to the upper-left corner of the board.
     */
    private int cx(int x) {
        return x * SQUARE_SIDE;
    }

    /**
     * Return y-pixel coordinate of the upper corners of row Y
     * relative to the upper-left corner of the board.
     */
    private int cy(int y) {
        return (Board.SIZE - y - 1) * SQUARE_SIDE;
    }

    /**
     * Return x-pixel coordinate of the left corner of S
     * relative to the upper-left corner of the board.
     */
    private int cx(Square s) {
        return cx(s.col());
    }

    /**
     * Return y-pixel coordinate of the upper corner of S
     * relative to the upper-left corner of the board.
     */
    private int cy(Square s) {
        return cy(s.row());
    }

    /**
     * Queue on which to post move commands (from mouse clicks).
     */
    private ArrayBlockingQueue<String> _commands;
    /**
     * Board being displayed.
     */
    private final Board _board = new Board();

    /**
     * Image of white queen.
     */
    private BufferedImage _whiteQueen;
    /**
     * Image of black queen.
     */
    private BufferedImage _blackQueen;
    /**
     * Image of spear.
     */
    private BufferedImage _spear;

    /**
     * True iff accepting moves from user.
     */
    private boolean _acceptingMoves;

    /**
     * An Array storing all clicks.
     */
    private Square[] _click = new Square[2];
}
