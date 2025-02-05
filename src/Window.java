import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import  java.io.*;
import javax.swing.*;

public class Window {
    private static final JFrame frame = new JFrame("minesweeper");

    private static TileButton[][] buttons;

    private static Board board;

    public static void main(String[] args){
        int x = 10;
        int y = 10;
        int tilewidth = 60;
        int headersize = 40;
        int minecount = 20;

        buttons = new TileButton[y][x];

        frame.setSize(tilewidth*(x+1), tilewidth*y+ headersize);

        board = (new RandomGenerator()).generate(x,y,minecount);

        createGrid(x,y, tilewidth);

        printmatrix(board.intboard);

        // using no layout managers
        frame.setLayout(null);

        // making the frame visible
        frame.setVisible(true);

    }

    //TODO integrate buttons and actual tiles click reveals jadajada
    public static void createGrid(int x, int y, int tilewidth){
        for (int j = 0; j < y; j++) {
            for (int i = 0; i < x; i++) {
                buttons[j][i] = new TileButton(board.board[j][i]);

                buttons[j][i].setBounds(i*tilewidth,j*tilewidth,tilewidth,tilewidth );

                buttonLogistics(buttons[j][i]);

                frame.add(buttons[j][i]);
            }
        }
    }

    public static void buttonLogistics(TileButton button){
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                button.tile.revealWithNeighbours();
                updateBoard();
            }
        });

    }

    public static void updateBoard(){
        for (TileButton[] row : buttons) {
            for (TileButton element : row){
                updateButton(element);
            }
        }
    }

    public static void updateButton(TileButton button){
        if (button.tile.isRevealed()){
            if (button.tile.isMine())
                button.setIcon(new ImageIcon("src/images/mine.png"));
            else
                button.setText(button.tile.minesInNeighbourhood+"");
        }
    }
    public static void printmatrix(int[][] matrix){
        // Loop through all rows
        for (int[] ints : matrix) {
            System.out.println();
            // Loop through all elements of current row
            for (int anInt : ints) System.out.print(anInt + " ");
        }
    }
}
