package utils;

public class Board {
    public int width;

    public int heigth;

    public int[][] intboard;

    public Tile[][] board;

    public Board(int[][] board) {
        this.width = board.length;
        //board has to be atleast 1x1
        this.heigth = board[0].length;
        this.intboard = board;
        this.board = intMatToTileMat(board);
        setNeighbours();
    }

    /**
     * Method that creates a matrix of TileButtons from matrix of integers where numbers show how many neighbours are mines, 9 means tile itself is mine.
     * @param board int[][] board
     * @return utils.Tile[][] from given int[][]
     */
    public static Tile[][] intMatToTileMat(int[][] board){
        Tile[][] tiles = new Tile[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                tiles[i][j] = new Tile(board[i][j], j, i);
            }
        }
        return tiles;
    }

    /**
     * Method that sets neighbours of every utils.Tile.
     */
    void setNeighbours(){
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[0].length; x++) {
                for (int i = -1; i < 2; i++) {
                    if ((y == 0 && i == -1) || (y == board.length - 1 && i == 1))
                        continue;
                    for (int j = -1; j < 2; j++) {
                        if ((x == 0 && j == -1) || (x == board[0].length - 1 && j == 1))
                            continue;
                        if (i==0 && j==0)
                            continue;
                        board[y][x].neighbours.add(board[y+i][x+j]);
                    }
                }
            }
        }
    }
}
