public class TicTacToeDesign {
    public class TicTacToe {
        private char[][] board;
        private char currentPlayerMark;
        private boolean gameEnd;

        // v2:
        int[] rows;
        int[] cols;
        int diagonal;
        int antiDiagonal;

        public TicTacToe() {
            board = new char[3][3];
            initialize();
        }

        public char getCurrentPlayer() {
            return currentPlayerMark;
        }

        public void initialize() {
            gameEnd = false;
            currentPlayerMark = 'x';

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    board[i][j] = '-';
                }
            }

            // v2:
            rows = new int[3];
            cols = new int[3];
            diagonal = 0;
            antiDiagonal = 0;
        }

        public boolean isBoardFull() {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == '-') {
                        return false;
                    }
                }
            }
            gameEnd = true;
            return true;
        }

        public void changePlayer() {
            if (currentPlayerMark == 'x')
                currentPlayerMark = 'o';
            else
                currentPlayerMark = 'x';

        }

        // v1:
        public void move(int row, int col, char player) throws AlreadyTakenException, GameEndException{
            if (gameEnd) {
                throw new GameEndException();
            }

            if (board[row][col] != '-') {
                throw new AlreadyTakenException();
            }

            board[row][col] = player;

            int cur = player == 'x' ? 1 : -1;
            int n = rows.length;
            rows[row] += cur;
            cols[col] -= cur;
            if (row == col) {
                diagonal += cur;
            }
            if (row + col == n - 1) {
                antiDiagonal += cur;
            }
            if (Math.abs(rows[row]) == n ||
                    Math.abs(cols[col]) == n ||
                    Math.abs(diagonal) == n ||
                    Math.abs(antiDiagonal) == n) {
                gameEnd = true;
                System.out.println(player + "wins");
            }
        }

        // v2:
        // true means this move wins the game, false means otherwise
        public boolean move(int row, int col) throws AlreadyTakenException, GameEndException {

            if (gameEnd) {
                throw new GameEndException();
            }

            if (board[row][col] != '-') {
                throw new AlreadyTakenException();
            }

            board[row][col] = currentPlayerMark;

            boolean win;

            //check row
            win = true;
            for (int i = 0; i < board.length; i++) {
                if (board[row][i] != currentPlayerMark) {
                    win = false;
                    break;
                }
            }

            if (win) {
                gameEnd = true;
                return win;
            }

            //check column
            win = true;
            for (int i = 0; i < board.length; i++) {
                if (board[i][col] != currentPlayerMark) {
                    win = false;
                    break;
                }
            }

            if (win) {
                gameEnd = true;
                return win;
            }

            //check back diagonal
            win = true;
            for (int i = 0; i < board.length; i++) {
                if (board[i][i] != currentPlayerMark) {
                    win = false;
                    break;
                }
            }

            if (win) {
                gameEnd = true;
                return win;
            }

            //check forward diagonal
            win = true;
            for (int i = 0; i < board.length; i++) {
                if (board[i][board.length - i - 1] != currentPlayerMark) {
                    win = false;
                    break;
                }
            }

            if (win) {
                gameEnd = true;
                return win;
            }
            changePlayer();
            return win;
        }
    }


    class GameEndException extends Exception {
        public GameEndException() {
            super("Game has been ended, cannot make any more moves");
        }
    }

    class AlreadyTakenException extends Exception {
        public AlreadyTakenException() {
            super("This place has been taken");
        }
    }
}
