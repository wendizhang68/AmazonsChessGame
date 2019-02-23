package amazons;

import java.util.ArrayList;
import java.util.Stack;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;

import static amazons.Piece.*;
import static amazons.Move.mv;
import static amazons.Square.sq;


/**
 * The state of an Amazons Game.
 *
 * @author Wendi Zhang
 */
class Board {

    /**
     * The number of squares on a side of the board.
     */
    static final int SIZE = 10;

    /**
     * Initializes a game board with SIZE squares on a side in the
     * initial position.
     */
    Board() {
        init();
    }

    /**
     * Initializes a copy of MODEL.
     */
    Board(Board model) {
        copy(model);
    }

    /**
     * Copies MODEL into me.
     */
    void copy(Board model) {
        if (model != this) {
            this._board = new Piece[SIZE * SIZE];
            for (int i = 0; i < model._board.length; i += 1) {
                this._board[i] = model._board[i];
            }
            this._turn = model._turn;
            this._winner = model._winner;
            this._move = new Stack<>();
            ArrayList<Move> move = new ArrayList<>();
            for (int i = 0; i < model._move.size(); i += 1) {
                move.add(model._move.pop());
            }
            for (int i = move.size() - 1; i >= 0; i -= 1) {
                this._move.push(move.get(i));
                model._move.push(move.get(i));
            }
        }
    }

    /**
     * Clears the board to the initial position.
     */
    void init() {

        Arrays.fill(_board, EMPTY);

        put(WHITE, sq("a4"));
        put(WHITE, sq("d1"));
        put(WHITE, sq("g1"));
        put(WHITE, sq("j4"));
        put(BLACK, sq("a7"));
        put(BLACK, sq("d10"));
        put(BLACK, sq("g10"));
        put(BLACK, sq("j7"));

        _move.clear();
        _turn = WHITE;
        _winner = null;
    }

    /**
     * Return the Piece whose move it is (WHITE or BLACK).
     */
    Piece turn() {
        return _turn;
    }

    /**
     * Return the number of moves (that have not been undone) for this
     * board.
     */
    int numMoves() {
        return _move.size();
    }

    /**
     * Return the winner in the current position, or null if the game is
     * not yet finished.
     */
    Piece winner() {
        if (!legalMoves().hasNext()) {
            _winner = turn().opponent();
        }
        return _winner;
    }

    /**
     * Return the contents the square at S.
     */
    final Piece get(Square s) {
        return _board[s.index()];
    }

    /**
     * Return the contents of the square at (COL, ROW), where
     * 0 <= COL, ROW < 9.
     */
    final Piece get(int col, int row) {
        return _board[sq(col, row).index()];
    }

    /**
     * Return the contents of the square at COL ROW.
     */
    final Piece get(char col, char row) {
        return get(col - 'a', row - '1');
    }

    /**
     * Set square S to P.
     */
    final void put(Piece p, Square s) {
        _board[s.index()] = p;
    }

    /**
     * Set square (COL, ROW) to P.
     */
    final void put(Piece p, int col, int row) {
        _board[sq(col, row).index()] = p;
    }

    /**
     * Set square COL ROW to P.
     */
    final void put(Piece p, char col, char row) {
        put(p, col - 'a', row - '1');
    }

    /**
     * Return true iff FROM - TO is an unblocked queen move on the current
     * board, ignoring the contents of ASEMPTY, if it is encountered.
     * For this to be true, FROM-TO must be a queen move and the
     * squares along it, other than FROM and ASEMPTY, must be
     * empty. ASEMPTY may be null, in which case it has no effect.
     */
    boolean isUnblockedMove(Square from, Square to, Square asEmpty) {
        List<Square> path = new ArrayList<>();
        if (from == null || !from.isQueenMove(to)) {
            return false;
        }
        int dir = from.direction(to);
        for (int i = 1; i < SIZE; i += 1) {
            if (from.queenMove(dir, i) == null
                    || from.queenMove(dir, i - 1) == to) {
                break;
            } else {
                path.add(from.queenMove(dir, i));
            }
        }
        for (Square s : path) {
            if (!get(s).equals(EMPTY) && s != asEmpty) {
                return false;
            }
        }
        return true;
    }

    /**
     * Return true iff FROM is a valid starting square for a move.
     */
    boolean isLegal(Square from) {
        if (from == null
                || _board[from.index()] == null) {
            return false;
        } else if (turn() == WHITE) {
            return get(from) == WHITE;
        } else {
            return get(from) == BLACK;
        }
    }

    /**
     * Return true iff FROM-TO is a valid first part of move, ignoring
     * spear throwing.
     */
    boolean isLegal(Square from, Square to) {
        if (isLegal(from) && to != null
                && _board[to.index()] == EMPTY
                && isUnblockedMove(from, to, null)) {
            return true;
        }
        return false;
    }

    /**
     * Return true iff FROM-TO(SPEAR) is a legal move in the current
     * position.
     */
    boolean isLegal(Square from, Square to, Square spear) {
        if (spear != null && _board[spear.index()] == SPEAR
                && isLegal(from, to)
                && isUnblockedMove(to, spear, from)) {
            return true;
        }
        return false;
    }

    /**
     * Return true iff FROM-TO(SPEAR) is a legal move in the current
     * position.
     */
    boolean isLegalBefore(Square from, Square to, Square spear) {
        if (isLegal(from, to)
                && (_board[spear.index()] == EMPTY
                || _board[spear.index()] == get(from))
                && isUnblockedMove(to, spear, from)) {
            return true;
        }
        return false;
    }

    /**
     * Return true iff MOVE is a legal move in the current
     * position.
     */
    boolean isLegalBefore(Move move) {
        return isLegalBefore(move.from(), move.to(), move.spear());
    }

    /**
     * Return true iff MOVE is a legal move in the current
     * position.
     */
    boolean isLegal(Move move) {
        return isLegal(move.from(),
                move.to(),
                move.spear());
    }

    /**
     * Move FROM-TO(SPEAR), assuming this is a legal move.
     */
    void makeMove(Square from, Square to, Square spear) {
        makeMove(Move.mv(from, to, spear));
    }

    /**
     * Move according to MOVE, assuming it is a legal move.
     */
    void makeMove(Move move) {
        if (move != null) {
            Piece sp = _board[move.from().index()];
            this.put(EMPTY, move.from());
            this.put(sp, move.to());
            this.put(SPEAR, move.spear());
            _move.push(move);
            _turn = _turn.opponent();
        }
    }

    /**
     * Undo one move.  Has no effect on the initial board.
     */
    void undo() {
        if (!_move.empty()) {
            Move lastMove = _move.pop();
            Piece lastPiece = get(lastMove.to());
            this.put(EMPTY, lastMove.spear());
            this.put(EMPTY, lastMove.to());
            this.put(lastPiece, lastMove.from());
            _turn = _turn.opponent();
        }
    }

    /**
     * Return an Iterator over the Squares that are reachable by an
     * unblocked queen move from FROM. Does not pay attention to what
     * piece (if any) is on FROM, nor to whether the game is finished.
     * Treats square ASEMPTY (if non-null) as if it were EMPTY.  (This
     * feature is useful when looking for Moves, because after moving a
     * piece, one wants to treat the Square it came from as empty for
     * purposes of spear throwing.)
     */
    Iterator<Square> reachableFrom(Square from, Square asEmpty) {
        return new ReachableFromIterator(from, asEmpty);
    }

    /**
     * Return an Iterator over all legal moves on the current board.
     */
    Iterator<Move> legalMoves() {
        return new LegalMoveIterator(_turn);
    }

    /**
     * Return an Iterator over all legal moves on the current board for
     * SIDE (regardless of whose turn it is).
     */
    Iterator<Move> legalMoves(Piece side) {
        return new LegalMoveIterator(side);
    }

    /**
     * An iterator used by reachableFrom.
     */
    private class ReachableFromIterator implements Iterator<Square> {

        /**
         * Iterator of all squares reachable by queen move from FROM,
         * treating ASEMPTY as empty.
         */
        ReachableFromIterator(Square from, Square asEmpty) {
            _from = from;
            _dir = -1;
            _steps = 0;
            _asEmpty = asEmpty;
            toNext();
        }

        @Override
        public boolean hasNext() {
            return _dir < 8;
        }

        @Override
        public Square next() {
            Square to = _to;
            toNext();
            return to;
        }

        /**
         * Advance _dir and _steps, so that the next valid Square is
         * _steps steps in direction _dir from _from.
         */
        private void toNext() {
            while (_dir < 8 && (_from == null
                    || _from.queenMove(_dir, _steps) == null
                    || (!get(_from.queenMove(_dir, _steps)).equals(EMPTY)
                    && _from.queenMove(_dir, _steps) != _asEmpty))) {
                _steps = 1;
                _dir += 1;
            }
            if (_from != null) {
                _to = _from.queenMove(_dir, _steps);
                _steps += 1;
            }
        }

        /**
         * Starting square.
         */
        private Square _from;
        /**
         * Current direction.
         */
        private int _dir;
        /**
         * Current distance.
         */
        private int _steps;
        /**
         * Square treated as empty.
         */
        private Square _asEmpty;
        /**
         * Square move to.
         */
        private Square _to;
    }

    /**
     * An iterator used by legalMoves.
     */
    private class LegalMoveIterator implements Iterator<Move> {
        /**
         * All legal moves for SIDE (WHITE or BLACK).
         */
        LegalMoveIterator(Piece side) {
            _startingSquares = Square.iterator();
            _spearThrows = NO_SQUARES;
            _pieceMoves = NO_SQUARES;
            _fromPiece = side;
            while (_startingSquares.hasNext()) {
                Square next = _startingSquares.next();
                Piece toPiece = get(next);
                if (toPiece == _fromPiece) {
                    _queens.add(next);
                }
            }
            _queenIterator = _queens.iterator();
            toNext();
        }

        @Override
        public boolean hasNext() {
            if (!_spearThrows.hasNext()
                    && !_pieceMoves.hasNext()
                    && !_queenIterator.hasNext()) {
                return false;
            }
            return true;
        }

        @Override
        public Move next() {
            Move move = mv(_start, _nextSquare, _spearThrows.next());
            toNext();
            return move;
        }

        /**
         * Advance so that the next valid Move is
         * _start-_nextSquare(sp), where sp is the next value of
         * _spearThrows.
         */
        private void toNext() {
            if (!_spearThrows.hasNext()) {
                if (!_pieceMoves.hasNext()) {
                    if (!_queenIterator.hasNext()) {
                        return;
                    }
                    _start = _queenIterator.next();
                    _pieceMoves = reachableFrom(_start, null);
                    toNext();
                } else {
                    _nextSquare = _pieceMoves.next();
                    _spearThrows = reachableFrom(_nextSquare, _start);
                    toNext();
                }
            }
        }

        /**
         * Color of side whose moves we are iterating.
         */
        private Piece _fromPiece;
        /**
         * Current starting square.
         */
        private Square _start;
        /**
         * Remaining starting squares to consider.
         */
        private Iterator<Square> _startingSquares;
        /**
         * Current piece's new position.
         */
        private Square _nextSquare;
        /**
         * Remaining moves from _start to consider.
         */
        private Iterator<Square> _pieceMoves;
        /**
         * Remaining spear throws from _piece to consider.
         */
        private Iterator<Square> _spearThrows;

        /**
         * List stores all queens.
         */
        private List<Square> _queens = new ArrayList<>();

        /**
         * Iterator for all queens.
         */
        private Iterator<Square> _queenIterator;
    }

    @Override
    public String toString() {
        StringBuilder printBoard = new StringBuilder();
        for (int i = SIZE * SIZE - SIZE; i >= 0; i -= SIZE) {
            String row = Arrays.toString
                    (Arrays.copyOfRange(_board, i, i + SIZE));
            row = row.replaceAll("[\\[\\],]", "");
            printBoard.append(String.format("   %s%n", row));
        }
        return printBoard.toString();
    }

    /**
     * An empty iterator for initialization.
     */
    private static final Iterator<Square> NO_SQUARES =
            Collections.emptyIterator();

    /**
     * Piece whose turn it is (BLACK or WHITE).
     */
    private Piece _turn;

    /**
     * Cached value of winner on this board, or EMPTY if it has not been
     * computed.
     */
    private Piece _winner;

    /**
     * Initialize the board.
     */
    private Piece[] _board = new Piece[SIZE * SIZE];

    /**
     * A stack store all moves.
     */
    private Stack<Move> _move = new Stack<>();

}
