package generators;

import utils.Board;

import java.util.Random;

public class FirstSafeRandomGenerator extends AbstractGenerator {

    @Override
    public Board generate(int x, int y, int minecount, int startx, int starty) {

        //Generates a board based on starting location and make sure no mines around start
        Random rand = new Random();
        int[][] board = new int[y][x];
        if (minecount > x*y-9){
            return new Board(board);
        }

        for (int i = 0; i < minecount; i++) {
            while (true){
                int xCoordinate = rand.nextInt(x);
                int yCoordinate = rand.nextInt(y);
                if (board[yCoordinate][xCoordinate] != 9 && (Math.abs(xCoordinate-startx) > 1 || Math.abs(yCoordinate-starty) > 1)){
                    board[yCoordinate][xCoordinate] = 9;
                    add1ToNeighbours(board, xCoordinate, yCoordinate);
                    break;
                }
            }
        }
        return new Board(board);
    }
}
