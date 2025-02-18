import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class Window {


    private static final JFrame frame = new JFrame("minesweeper");

    private static boolean gameover;

    private static TileButton[][] buttons;

    private static Game game;

    private static final JPanel header = new JPanel();

    private static final JLabel counter = new JLabel();
    private static final JPanel playfield = new JPanel();

    private static final JButton resetButton = new JButton();

    private static final JButton solveButton = new JButton();

    private static final JPopupMenu generatorChoiceMenu = new JPopupMenu();

    static int width;
    static int heigth;
    static int tilewidth;
    static int tabsize;
    static int minecount;
    static int headersize;

    static boolean firstClick;
    public static void main(String[] args){
        startGame(10,10,20);

    }

    public static void makeWindow(int varx, int vary, int varminecount){
        frame.setSize(tilewidth*(varx)+tilewidth/2, tilewidth* vary + tabsize + headersize);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(header);
        frame.add(playfield);

        header.setBounds(0,0,tilewidth*(varx +1), headersize);
        playfield.setBounds(0,headersize,tilewidth*(varx +1), tilewidth* vary);

        resetButton.setBounds(tilewidth*(varx -2), 10, tilewidth*3/2, headersize/2);
        resetButton.setText("Reset");
        resetButton.addActionListener(e -> {
            playfield.removeAll();
            header.removeAll();
            startGame(varx,vary,varminecount);

            updateBoard();

            header.repaint();
            header.revalidate();

            playfield.repaint();
            playfield.revalidate();
        });

        solveButton.setBounds(tilewidth*(varx -4), 10, tilewidth*3/2, headersize/2);
        solveButton.setText("Solve");
        solveButton.addActionListener(e -> {
            System.out.println("waddap");
            AutomatonSolver automatonSolver = new AutomatonSolver();
            boolean solve = automatonSolver.solve(game);
            System.out.println("Solved - " + solve);
            updateBoard();
        });

        createMenu();

        counter.setBounds(10,10, 100,30);
        counter.setText("Mines left - " + minecount);
        header.add(counter);
        header.add(resetButton);
        header.add(solveButton);

        header.setLayout(null);
        playfield.setLayout(null);
        header.setVisible(true);
        playfield.setVisible(true);

        // using no layout managers
        frame.setLayout(null);

        // making the frame visible
        frame.setVisible(true);
    }
    public static void startGame(int varx, int vary, int varminecount){
        width = varx;
        heigth = vary;
        minecount = varminecount;
        tilewidth = 60;
        tabsize = 40;
        headersize = 60;
        gameover = false;
        firstClick = true;

        buttons = new TileButton[vary][varx];

        if (game != null) {
            System.out.println("olemas" + game.getChosenGenerator());
            game = new Game(new Board(new int[vary][varx]), 0, 0, 0, game.getChosenGenerator());
        } else {
            System.out.println("uus");
            game = new Game(new Board(new int[vary][varx]), 0, 0, 0);
        }
        createGrid(varx, vary, tilewidth);
        makeWindow(varx, vary, varminecount);
    }

    public static void createMenu(){
        generatorChoiceMenu.removeAll();
        generatorChoiceMenu.setLocation(130, 10);

        JLabel text = new JLabel(game.getChosenGenerator().toString());
        text.setBounds(130, 10, 200, 30);
        header.add(text);
        text.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                generatorChoiceMenu.show(header, 130, 10);
            }
        });

        for (GeneratorInterface item : game.getGeneratorOptions()){
            JMenuItem menuitem = new JMenuItem(item.toString());
            menuitem.addActionListener(e -> {
                System.out.println("reset nupp " + item);
                text.setText(menuitem.getText());
                game.setChosenGenerator(item);
            });
            generatorChoiceMenu.add(menuitem);
        }

        header.add(generatorChoiceMenu);
    }


    public static void createGrid(int x, int y, int tilewidth){
        for (int j = 0; j < y; j++) {
            for (int i = 0; i < x; i++) {
                buttons[j][i] = new TileButton(game.getBoard().board[j][i]);

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

                if (firstClick){
                    firstClick = false;
                    buttons = new TileButton[heigth][width];
                    playfield.removeAll();
                    GeneratorInterface valitud = game.getChosenGenerator();

                    game = new Game(width, heigth,minecount, button.tile.x, button.tile.y, valitud);
                    System.out.println("aso " + game.getChosenGenerator());
                    createGrid(width, heigth, tilewidth);
                    printmatrix(game.getBoard().intboard);

                    //mouseReleased(e);
                    buttons[button.tile.y][button.tile.x].tile.revealWithNeighbours();
                    updateBoard();
                    return;
                }

                if (gameover)
                    return;

                if (e.getButton() == MouseEvent.BUTTON3) {
                    if (!button.tile.isRevealed()){
                        button.tile.setFlagged(!button.tile.isFlagged());
                        updateBoard();
                    }
                } else if (e.getButton() == MouseEvent.BUTTON1) {
                    if (e.getClickCount() == 2){
                        revealNeighbours(button);
                        updateBoard();
                    }
                    if (!button.tile.isFlagged()) {
                        button.tile.revealWithNeighbours();
                        System.out.println(button.tile.isRevealed());
                        updateBoard();
                    }
                }
            }
        });

    }

    private static void revealNeighbours(TileButton button) {
        for (Tile neighbour : button.tile.neighbours){
            if (!neighbour.isFlagged()){
                neighbour.setRevealed(true);
            }
        }
    }


    public static void updateBoard(){
        int flaggedCount = 0;
        int revealed = 0;
        for (TileButton[] row : buttons) {
            for (TileButton element : row){
                updateButton(element);
                if (element.tile.isMine() && element.tile.isRevealed()) {
                    gameover = true;
                    counter.setText("Game lost");
                    return;
                }
                if (element.tile.isFlagged())
                    flaggedCount++;
                if (element.tile.isRevealed())
                    revealed++;

            }
        }
        if (revealed == width * heigth -20){
            counter.setText("Game Won");
            gameover = true;
            return;
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
            else {
                button.setText(button.tile.minesInNeighbourhood + "");
                button.setBackground(new Color(255,255,255));
            }
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
