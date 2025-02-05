import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class Window {
    private static final JFrame frame = new JFrame("minesweeper");

    private static boolean gamelost;

    private static TileButton[][] buttons;

    private static Board board;

    private static final JPanel header = new JPanel();

    private static final JLabel counter = new JLabel();
    private static final JPanel playfield = new JPanel();

    static int x;
    static int y;
    static int tilewidth;
    static int tabsize;
    static int minecount;
    static int headersize;
    public static void main(String[] args){
        x = 10;
        y = 10;
        tilewidth = 60;
        tabsize = 40;
        minecount = 20;
        headersize = 60;
        gamelost = false;

        buttons = new TileButton[y][x];

        frame.setSize(tilewidth*(x)+tilewidth/2, tilewidth*y+ tabsize + headersize);

        board = (new RandomGenerator()).generate(x,y,minecount);

        createGrid(x,y, tilewidth);

        printmatrix(board.intboard);

        frame.add(header);
        frame.add(playfield);



        header.setBounds(0,0,tilewidth*(x+1), headersize);
        playfield.setBounds(0,headersize,tilewidth*(x+1), tilewidth*y);

        counter.setBounds(10,10, 100,30);
        counter.setText("Mines left - " + minecount);
        header.add(counter);

        header.setLayout(null);
        playfield.setLayout(null);
        header.setVisible(true);
        playfield.setVisible(true);

        // using no layout managers
        frame.setLayout(null);

        // making the frame visible
        frame.setVisible(true);

    }


    public static void createGrid(int x, int y, int tilewidth){
        for (int j = 0; j < y; j++) {
            for (int i = 0; i < x; i++) {
                buttons[j][i] = new TileButton(board.board[j][i]);

                buttons[j][i].setBounds(i*tilewidth+3,j*tilewidth,tilewidth,tilewidth );

                buttonLogistics(buttons[j][i]);

                playfield.add(buttons[j][i]);
            }
        }
    }

    public static void buttonLogistics(TileButton button){
        button.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseReleased(MouseEvent e) {
                if (gamelost)
                    return;
                if (e.getButton() == MouseEvent.BUTTON3) {
                    System.out.println("button3");
                    if (!button.tile.isRevealed()){
                        button.tile.setFlagged(!button.tile.isFlagged());
                        updateBoard();
                    }
                } else if (e.getButton() == MouseEvent.BUTTON1) {
                    System.out.println("button1");
                    if (!button.tile.isFlagged()) {
                        button.tile.revealWithNeighbours();
                        updateBoard();
                    }
                }
            }
        });

    }


    public static void updateBoard(){
        int flaggedCount = 0;
        for (TileButton[] row : buttons) {
            for (TileButton element : row){
                updateButton(element);
                if (element.tile.isMine() && element.tile.isRevealed()) {
                    gamelost = true;
                    counter.setText("Game lost");
                    return;
                }
                if (element.tile.isFlagged())
                    flaggedCount++;
            }
        }
        counter.setText("Mines left - " + (minecount-flaggedCount));
    }

    public static void updateButton(TileButton button){
        if (!button.tile.isFlagged() && !button.tile.isRevealed())
            button.setIcon(null);
        if (button.tile.isFlagged())
            button.setIcon(new ImageIcon("src/images/flag.png"));
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
