package amazons;

import static java.lang.Math.*;

import java.util.Iterator;

import static amazons.Piece.*;

/**
 * A Player that automatically generates moves.
 *
 * @author Wendi Zhang
 */
class AI extends Player {

    /**
     * A position magnitude indicating a win (for white if positive, black
     * if negative).
     */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 1;
    /**
     * A magnitude greater than a normal value.
     */
    private static final int INFTY = Integer.MAX_VALUE;

    /**
     * A new AI with no piece or controller (intended to produce
     * a template).
     */
    AI() {
        this(null, null);
    }

    /**
     * A new AI playing PIECE under control of CONTROLLER.
     */
    AI(Piece piece, Controller controller) {
        super(piece, controller);
    }

    @Override
    Player create(Piece piece, Controller controller) {
        return new AI(piece, controller);
    }

    @Override
    String myMove() {
        Move move = findMove();
        _controller.reportMove(move);
        return move.toString();
    }

    /**
     * Return a move for me from the current position, assuming there
     * is a move.
     */
    private Move findMove() {
        Board b = new Board(board());
        if (_myPiece == WHITE) {
            findMove(b, maxDepth(b), true, 1, -INFTY, INFTY);
        } else {
            findMove(b, maxDepth(b), true, -1, -INFTY, INFTY);
        }
        return _lastFoundMove;
    }

    /**
     * The move found by the last call to one of the ...FindMove methods
     * below.
     */
    private Move _lastFoundMove;

    /**
     * Find a move from position BOARD and return its value, recording
     * the move found in _lastFoundMove iff SAVEMOVE. The move
     * should have maximal value or have value > BETA if SENSE==1,
     * and minimal value or value < ALPHA if SENSE==-1. Searches up to
     * DEPTH levels.  Searching at level 0 simply returns a static estimate
     * of the board value and does not set _lastMoveFound.
     */
    private int findMove(Board board, int depth, boolean saveMove, int sense,
                         int alpha, int beta) {
        if (depth == 0 || board.winner() == WHITE || board.winner() == BLACK) {
            return staticScore(board);
        }
        Iterator<Move> allMove = board().legalMoves(_myPiece);
        if (sense == 1) {
            int maxBestSoFar = -INFTY;
            while (allMove.hasNext()) {
                Move maxnext = allMove.next();
                if (board().isLegal(maxnext)) {
                    board().makeMove(maxnext);
                }
                int response = findMove(board, depth - 1,
                        false, -1, alpha, beta);
                if (response >= maxBestSoFar) {
                    maxBestSoFar = response;
                    alpha = max(alpha, response);
                    if (saveMove) {
                        _lastFoundMove = maxnext;
                    }
                    if (beta <= alpha) {
                        break;
                    }
                } else {
                    board().undo();
                }
            }
            return maxBestSoFar;
        } else if (sense == -1) {
            int minBestSoFar = INFTY;
            while (allMove.hasNext()) {
                Move minnext = allMove.next();
                if (board().isLegal(minnext)) {
                    board().makeMove(minnext);
                }
                int response = findMove(board, depth - 1,
                        false, 1, alpha, beta);
                if (response <= minBestSoFar) {
                    minBestSoFar = response;
                    beta = min(beta, response);
                    if (saveMove) {
                        _lastFoundMove = minnext;
                    }
                    if (beta <= alpha) {
                        break;
                    }
                } else {
                    board().undo();
                }
            }
            return minBestSoFar;
        } else {
            throw new IllegalArgumentException("sense can only be 1 or -1");
        }
    }

    /**
     * Limit set for maxdepth.
     */
    static final int LIMIT = 80;

    /**
     * Return a heuristically determined maximum search depth
     * based on characteristics of BOARD.
     */
    private int maxDepth(Board board) {
        int limit = LIMIT;
        int N = board.numMoves();
        if (N < 10) {
            return 1;
        } else if (N < limit) {
            return 2;
        } else {
            return 3;
        }
    }


    /**
     * Return a heuristic value for BOARD.
     */
    private int staticScore(Board board) {
        Piece winner = board.winner();
        if (winner == BLACK) {
            return -WINNING_VALUE;
        } else if (winner == WHITE) {
            return WINNING_VALUE;
        }
        int countWhite = 0;
        int countBlack = 0;
        Iterator whiteIter = board.legalMoves(WHITE);
        Iterator blackIter = board.legalMoves(BLACK);
        while (whiteIter.hasNext()) {
            countWhite++;
            whiteIter.next();
        }
        while (blackIter.hasNext()) {
            countBlack++;
            blackIter.next();
        }
        return countWhite - countBlack;
    }
}
