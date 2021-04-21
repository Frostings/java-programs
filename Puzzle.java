public class Puzzle {
    int size;
    char[][] grid;
    int blackRow;
    int blackCol;
    
    static final int TOP_LEFT = 0;
    static final int TOP_RIGHT = 1;
    static final int BOTTOM_LEFT = 2;
    static final int BOTTOM_RIGHT = 3;
    
    
    Puzzle(int size) {
        this.size = size;
        grid = new char[size][size];
        blackRow = roll(0, size);
        blackCol = roll(0, size);
        for (char[] col : grid) {
            for (int i = 0; i < size; ++i) {
                col[i] = ' ';
            }
        }
        grid[blackRow][blackCol] = '*';
    }
    
    int roll(int a, int b) {
        return (int)(Math.random() * (b - a) + a);
    }
    
    void print() {
        System.out.print('*');
        for (int i = 0; i < size; ++i) {
            System.out.print('-');
        }
        System.out.println('*');
        
        for (char[] col : grid) {
            System.out.print('|');
            for (char c : col) {
                System.out.print(c);
            }
            System.out.println('|');
        }
        
        System.out.print('*');
        for (int i = 0; i < size; ++i) {
            System.out.print('-');
        }
        System.out.println('*');
    }
    
    void placePiece(int row, int col, int orientation) {
        if (orientation == TOP_LEFT) {
            grid[row][col] = '┌';
            grid[row][col + 1] = '─';
            grid[row + 1][col] = '│';
        } else if (orientation == TOP_RIGHT) {
            grid[row][col] = '┐';
            grid[row][col - 1] = '─';
            grid[row + 1][col] = '│';
        } else if (orientation == BOTTOM_RIGHT) {
            grid[row][col] = '┘';
            grid[row][col - 1] = '─';
            grid[row - 1][col] = '│';
        } else {
            grid[row][col] = '└';
            grid[row][col + 1] = '─';
            grid[row - 1][col] = '│';
        }
    }
    
    void solve() {
        solve(0, 0, size, blackRow, blackCol);
    }
    
    void solve(int row, int col, int size, int blackRow, int blackCol) {
        int blackSquare = (blackRow < (row + size / 2)) ? ((blackCol < (col + size / 2)) ? TOP_LEFT : TOP_RIGHT)  : ((blackCol < (col + size / 2)) ? BOTTOM_LEFT : BOTTOM_RIGHT);
        if (size == 2) {
            if (blackSquare == TOP_LEFT) {
                placePiece(row + 1, col + 1, BOTTOM_RIGHT);
            } else if (blackSquare == TOP_RIGHT) {
                placePiece(row + 1, col, BOTTOM_LEFT);
            } else if (blackSquare == BOTTOM_LEFT) {
                placePiece(row, col + 1, TOP_RIGHT);
            } else {
                placePiece(row, col, TOP_LEFT);
            }
        } else {
            if (blackSquare == TOP_LEFT) {
                solve(row, col, size / 2, blackRow, blackCol);
                solve(row, col + size / 2, size / 2, row + size / 2 - 1, col + size / 2);
                solve(row + size / 2, col, size / 2, row + size / 2, col + size / 2 - 1);
                solve(row + size / 2, col + size / 2, size / 2, row + size / 2, col + size / 2);
                placePiece(row + size / 2, col + size / 2, BOTTOM_RIGHT);
            } else if (blackSquare == TOP_RIGHT) {
                solve(row, col, size / 2, row + size / 2 - 1, col + size / 2 - 1);
                solve(row, col + size / 2, size / 2, blackRow, blackCol);
                solve(row + size / 2, col, size / 2, row + size / 2, col + size / 2 - 1);
                solve(row + size / 2, col + size / 2, size / 2, row + size / 2, col + size / 2);
                placePiece(row + size / 2, col + size / 2 - 1, BOTTOM_LEFT);
            } else if (blackSquare == BOTTOM_LEFT) {
                solve(row, col, size / 2, row + size / 2 - 1, col + size / 2 - 1);
                solve(row, col + size / 2, size / 2, row + size / 2 - 1, col + size / 2);
                solve(row + size / 2, col, size / 2, blackRow, blackCol);
                solve(row + size / 2, col + size / 2, size / 2, row + size / 2, col + size / 2);
                placePiece(row + size / 2 - 1, col + size / 2, TOP_RIGHT);
            } else {
                solve(row, col, size / 2, row + size / 2 - 1, col + size / 2 - 1);
                solve(row, col + size / 2, size / 2, row + size / 2 - 1, col + size / 2);
                solve(row + size / 2, col, size / 2, row + size / 2, col + size / 2 - 1);
                solve(row + size / 2, col + size / 2, size / 2, blackRow, blackCol);
                placePiece(row + size / 2 - 1, col + size / 2 - 1, TOP_LEFT);
            }
        }
    }
    
    public static void main(String[] args) {
	    Puzzle p = new Puzzle(64);
	    p.solve();
	    p.print();
	}
}
