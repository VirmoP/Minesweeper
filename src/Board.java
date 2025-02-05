public class Board {
    int width;

    int heigth;

    int[][] intboard;

    Tile[][] board;

    public Board(int[][] board) {
        this.width = board.length;
        //board has to be atleast 1x1
        this.heigth = board[0].length;
        this.intboard = board;
        this.board = intMatToTileMat(board);
        setNeighbours();
    }

    public static Tile[][] intMatToTileMat(int[][] board){
        Tile[][] tiles = new Tile[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                tiles[i][j] = new Tile(board[i][j]);
            }
        }
        return tiles;
    }

    void setNeighbours(){
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[0].length; x++) {
                for (int i = -1; i < 2; i++) {
                    if ((y == 0 && i == -1) || (y == board.length - 1 && i == 1))
                        continue;
                    for (int j = -1; j < 2; j++) {
                        if ((x == 0 && j == -1) || (x == board[0].length - 1 && j == 1))
                            continue;
                        board[y][x].neighbours.add(board[y+i][x+j]);
                    }
                }
            }
        }
    }
}
