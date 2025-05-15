import generators.AbstractGenerator;
import solvers.*;
import utils.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class Window {


    private static final JFrame frame = new JFrame("Minesweeper");

    private static boolean gameover;

    private static TileButton[][] buttons;

    private static Game game;

    private static final JPanel header = new JPanel();

    private static final JLabel counter = new JLabel();
    private static final JPanel playfield = new JPanel();

    private static final JButton resetButton = new JButton();

    private static final JButton solveButton = new JButton();

    private static JComboBox<Object> dropdownMenu = new JComboBox<>();
    private static final JSpinner Choosey = new JSpinner();
    private static final JSpinner Choosex = new JSpinner();
    private static final JSpinner Chooseminecount = new JSpinner();

    static int width;
    static int heigth;
    static int tilewidth;
    static int tabsize;
    static int minecount;
    static int headersize;

    static boolean firstClick;
    public static void main(String[] args){
        startGame(30,16,99);
    }

    /**
     * Method that builds a GUI with java swift with a size compatible to given vairables.
     * @param varx board width
     * @param vary board height
     */
    public static void makeWindow(int varx, int vary){
        //main frame
        frame.setSize(tilewidth*(varx+2), tilewidth* vary + tabsize + headersize);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //adding containers
        frame.add(header);
        frame.add(playfield);

        //setting container sizes
        header.setBounds(0,0,tilewidth*(varx +1), headersize);
        playfield.setBounds(0,headersize,tilewidth*(varx +1), tilewidth* vary);

        //build a reset button that starts a new game
        resetButton.setPreferredSize(new Dimension(tilewidth*3, headersize/2));
        resetButton.setText("Reset");
        resetButton.addActionListener(e -> {
            resetButton.removeActionListener(resetButton.getActionListeners()[0]);
            solveButton.removeActionListener(solveButton.getActionListeners()[0]);

            game.setChosenGenerator((AbstractGenerator) dropdownMenu.getSelectedItem());

            int temp = dropdownMenu.getSelectedIndex();
            playfield.removeAll();
            header.removeAll();
            //startGame(varx,vary,varminecount);

            startGame((int) Choosex.getValue(),(int) Choosey.getValue(),(int) Chooseminecount.getValue());
            dropdownMenu.setSelectedIndex(temp);
            updateBoard();
        });

        //build solve button that solves the given board
        solveButton.setPreferredSize(new Dimension(tilewidth*3, headersize/2));
        solveButton.setText("Solve");
        solveButton.addActionListener(e -> {
            SolverInterface[] solvers = {new CSPSolverSubsets()};

            SolverInterface solver = solvers[0];
            SolveInfo solveinfo = solver.solve(game);

            System.out.println("Solved - " + solveinfo.solved);
            System.out.println(solveinfo.constraints);
            updateBoard();
        });

        //builds a dropdown menu for choosing a generating algorithm
        createMenu();

        counter.setBounds(10,10, 100,30);
        counter.setText("Mines left - " + minecount);
        header.add(counter);
        header.add(resetButton);
        header.add(solveButton);

        header.setLayout(new FlowLayout());
        playfield.setLayout(new GridLayout(vary, varx));
        header.setVisible(true);
        playfield.setVisible(true);

        // using no layout managers
        frame.setLayout(null);

        // making the frame visible
        frame.setVisible(true);
    }

    /**
     * Method that sets up a utils.Game object for the GUI
     * @param varx game width
     * @param vary game heigth
     * @param varminecount minecount
     */
    public static void startGame(int varx, int vary, int varminecount){
        width = varx;
        heigth = vary;
        minecount = varminecount;
        tilewidth = 30;
        tabsize = 40;
        headersize = 60;
        gameover = false;
        firstClick = true;

        //Buttons for game tiles
        buttons = new TileButton[vary][varx];

        //if a generator is already chosen we build a new game with the chosen generator
        if (game != null) {
            game = new Game(new Board(new int[vary][varx]), 0, 0, 0, game.getChosenGenerator());
        } else {
            game = new Game(new Board(new int[vary][varx]), 0, 0, 0);
        }
        //generating the graphical board
        createGrid(varx, vary, tilewidth);
        makeWindow(varx, vary);

    }

    /**
     * Menu that builds a dropdown menu with every generation algorithm as an option
     */
    public static void createMenu(){

        header.add(new JLabel("x: "));
        header.add(Choosex);
        Choosex.setValue(width);
        header.add(new JLabel("y: "));
        header.add(Choosey);
        Choosey.setValue(heigth);
        header.add(new JLabel("Mines: "));
        header.add(Chooseminecount);
        Chooseminecount.setValue(minecount);

        Chooseminecount.addChangeListener(e -> {
            int val = (int) Chooseminecount.getValue();
            if (val < 1)
                Chooseminecount.setValue(1);
            if (val > ((int) Choosey.getValue()) * ((int) Choosex.getValue()))
                Chooseminecount.setValue(((int) Choosey.getValue()) * ((int) Choosex.getValue()));
        });

        Choosey.addChangeListener(e -> {
            int val = (int) Choosey.getValue();
            if (val < 10)
                Choosey.setValue(10);
            if ((int) Chooseminecount.getValue() > ((int) Choosey.getValue()) * ((int) Choosex.getValue()))
                Chooseminecount.setValue(((int) Choosey.getValue()) * ((int) Choosex.getValue()));
        });

        Choosex.addChangeListener(e -> {
            int val = (int) Choosex.getValue();
            if (val < 10)
                Choosex.setValue(10);
            if ((int) Chooseminecount.getValue() > ((int) Choosey.getValue()) * ((int) Choosex.getValue()))
                Chooseminecount.setValue(((int) Choosey.getValue()) * ((int) Choosex.getValue()));
        });

        AbstractGenerator[] generators = game.getGeneratorOptions();

        dropdownMenu = new JComboBox<>(generators);

        dropdownMenu.setSelectedItem(game.getChosenGenerator());

        header.add(dropdownMenu);

    }

    /**
     * Method that builds a grid, of given size and a preferred tile size, of TileButtons
     * @param x horisontal size
     * @param y verical size
     * @param tilewidth pixel size of one tile
     */
    public static void createGrid(int x, int y, int tilewidth){
        for (int j = 0; j < y; j++) {
            for (int i = 0; i < x; i++) {
                buttons[j][i] = new TileButton(game.getBoard().board[j][i]);

                buttons[j][i].setPreferredSize(new Dimension(tilewidth, tilewidth));
                //buttons[j][i].setBackground(new Color(23,93,207));

                buttons[j][i].setMargin(new Insets(0,0,0,0));

                buttonLogistics(buttons[j][i]);

                playfield.add(buttons[j][i]);
            }
        }
    }

    /**
     * Method that gives a button a listener that has the necessary logic for that button
     * @param button button to give logic for
     */
    public static void buttonLogistics(TileButton button){
        button.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseReleased(MouseEvent e) {
                //if this is the first click of the game a new board will need to be built with the appropriate starting position.
                if (firstClick){
                    firstClick = false;
                    buttons = new TileButton[heigth][width];
                    playfield.removeAll();
                    AbstractGenerator valitud = game.getChosenGenerator();

                    game = new Game(width, heigth,minecount, button.tile.getX(), button.tile.getY(), valitud);
                    
                    createGrid(width, heigth, tilewidth);
                    printmatrix(game.getBoard().intboard);

                    //buttons[button.tile.y][button.tile.x].tile.setRevealed(true);
                    buttons[button.tile.getY()][button.tile.getX()].tile.revealWithNeighbours();

                    updateBoard();
                    return;
                }

                //if game is over only the info of given button is printed for debugging reasons
                if (gameover) {
                    System.out.println(button.tile);
                    return;
                }

                //right click flags a tile
                if (e.getButton() == MouseEvent.BUTTON3) {
                    if (!button.tile.isRevealed()){
                        button.tile.setFlagged(!button.tile.isFlagged());
                        updateBoard();
                    }
                    //left click reveals tile unless flagged
                } else if (e.getButton() == MouseEvent.BUTTON1) {
                    //double click reveals neighbours of a revealed tile
                    if (e.getClickCount() == 2 && flagsPresent(button)){
                        revealNeighbours(button);
                        updateBoard();
                    }
                    //on click just reveals tile if not flagged, if reveals 0 it also reveals neighbours
                    if (!button.tile.isFlagged()) {
                        button.tile.revealWithNeighbours();
                        updateBoard();
                    }
                }
            }
        });

    }

    /**
     * Method that checks if a tile has amount of neighbours equal to mines.
     * @param button given utils.TileButton
     * @return boolean
     */
    private static boolean flagsPresent(TileButton button) {
        int flags = 0;
        for (Tile tile: button.tile.getNeighbours()){
            if (tile.isFlagged())
                flags++;
        }
        return flags == button.tile.getMinesInNeighbourhood();
    }

    /**
     * Method that checks if a buttons neighbours are flagged, if not then reveals them
     * @param button given utils.TileButton
     */
    private static void revealNeighbours(TileButton button) {
        for (Tile neighbour : button.tile.getNeighbours()){
            if (!neighbour.isFlagged()){
                neighbour.revealWithNeighbours();
            }
        }
    }

    /**
     * If all safe tiles are revealed the rest are flagged automatically.
     */
    public static void unrevealedToMines(){
        for (TileButton[] row : buttons){
            for (TileButton element : row){
                if (!element.tile.isRevealed()) {
                    element.tile.setFlagged(true);
                    element.setIcon(new ImageIcon("src/images/flag2.png"));
                }
            }
        }
    }


    /**
     * Method that loops through entire board and checks if game is lost or won,
     * calls updateButton on every tile.
     */
    public static void updateBoard(){
        int flaggedCount = 0;
        int revealed = 0;
        for (TileButton[] row : buttons) {
            for (TileButton element : row){
                updateButton(element);
                if (element.tile.isMine() && element.tile.isRevealed()) {
                    gameover = true;
                    counter.setText("Game lost");
                    revealAllMines();
                }
                if (element.tile.isFlagged())
                    flaggedCount++;
                if (element.tile.isRevealed())
                    revealed++;

            }
        }

        counter.setText("Mines left - " + (minecount-flaggedCount));

        if (revealed == width * heigth - minecount){
            counter.setText("Game Won");
            gameover = true;
            unrevealedToMines();
        }

    }

    public static void revealAllMines(){
        for (TileButton[] row : buttons){
            for (TileButton button : row){
                if (button.tile.isMine()){
                    button.tile.setRevealed(true);
                    updateButton(button);
                }
            }
        }
    }


    /**
     * Updates the visuals of a utils.TileButton
     * @param button utils.TileButton
     */
    public static void updateButton(TileButton button){
        if (!button.tile.isFlagged() && !button.tile.isRevealed())
            button.setIcon(null);
        if (button.tile.isFlagged())
            button.setIcon(new ImageIcon("src/images/flag2.png"));
        if (button.tile.isRevealed()){

            if (button.tile.isMine())
                button.setIcon(new ImageIcon("src/images/mine2.png"));
            else {
                button.setText(button.tile.getMinesInNeighbourhood() + "");
                button.setBackground(new Color(255,255,255));
                if (button.tile.getMinesInNeighbourhood() == 0){
                    button.setText("");
                }
            }
        }

    }


    /**
     * Utility to print a matrix to console.
     * @param matrix int[][]
     */
    public static void printmatrix(int[][] matrix){
        // Loop through all rows
        for (int[] ints : matrix) {
            System.out.println();
            // Loop through all elements of current row
            for (int anInt : ints) System.out.print(anInt + " ");
        }
        System.out.println();
    }
}
