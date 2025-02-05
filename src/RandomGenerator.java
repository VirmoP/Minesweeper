import java.sql.SQLOutput;
import java.util.Random;
public class RandomGenerator extends GeneratorInterface{

    @Override
    public Board generate(int x, int y, int minecount) {
        Random rand = new Random();
        int[][] board = new int[y][x];
        for (int i = 0; i < minecount; i++) {
            while (true){
                int xCoordinate = rand.nextInt(x);
                int yCoordinate = rand.nextInt(y);
                if (board[yCoordinate][xCoordinate] != 9){
                    board[yCoordinate][xCoordinate] = 9;
                    addToNeighbours(board, xCoordinate, yCoordinate);
                    break;
                }
            }
        }
        return new Board(board);
    }
}
