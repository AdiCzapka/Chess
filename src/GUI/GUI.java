package GUI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.*;
import Board.*;

public class GUI {
    CardLayout cardLayout;
    JFrame frame;
    JPanel boardPanel;
    JPanel promotionPanel;
    JPanel colourPanel;
    JPanel sidePanel;
    JPanel deck;
    BlockingQueue<GUISignal> blockingQueue;
    public JButton[][] squares = new JButton[8][8];
    public JButton[] promotions = new JButton[4];
    public JButton[] colours = new JButton[2];
    public boolean whiteDown = true;
    boolean whitePerspective = true;
    public GUI(BlockingQueue<GUISignal> blockingQueue) {
        this.cardLayout = new CardLayout();
        this.frame = new JFrame("Chess");
        this.boardPanel = new JPanel();
        this.promotionPanel = new JPanel();
        this.colourPanel = new JPanel();
        this.deck = new JPanel(cardLayout);
        this.sidePanel = new JPanel();
        this.blockingQueue = blockingQueue;
    }

    public void initialiseGUI(Board board) {
        deck.setLayout(cardLayout);
        //Create the board gui (panel)
        createBoardPanel(board);
        //Create the promotion gui (panel)
        createPromotionPanel();
        //Create the colour selection panel
        createColourSelectionPanel();
        //Add all 3 panels to the card layout
        deck.add(boardPanel, "Board");
        deck.add(promotionPanel, "Promotion");
        deck.add(colourPanel, "Colour");
        //Create side panel with the restart and flip buttons
        createSidePanel();
        //Add deck to frame in centre, and side panel to frame on the right
        frame.add(deck, BorderLayout.CENTER);
        frame.add(sidePanel, BorderLayout.EAST);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Make everything visible
        frame.setVisible(true);
    }
    public void restartGUI(Board board) {
        SwingUtilities.invokeLater(() -> {
            updatePiecesGUI(board);
            removeAttackSquares(board);
            cardLayout.show(deck, "Board");
        });
    }

    public void createBoardPanel(Board board) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                squares[i][j] = new JButton();
                if (((i+j)%2 == 0)) {
                    squares[i][j].setBackground(new Color (255, 240, 200));
                }
                else {
                    squares[i][j].setBackground(new Color(150, 100, 0));
                }
                squares[i][j].setOpaque(true);
                squares[i][j].setBorderPainted(false);
                boardPanel.add(squares[i][j]);
                setSquareSymbol(i,j,board, squares);
                final int row = i;
                final int col = j;
                squares[i][j].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        blockingQueue.offer(new SquareSignal(new Square(7-row, col)));
                    }
                });
                squares[i][j].setFont(new Font("SansSerif", Font.BOLD, 35));
            }
        }
        boardPanel.setLayout(new GridLayout(8,8));
    }
    public void createPromotionPanel() {
        for (int i = 0; i < 4; i++) {
            promotions[i] = new JButton();
            promotions[i].setBackground(Color.WHITE);
            promotions[i].setOpaque(true);
            promotions[i].setBorderPainted(false);
            promotionPanel.add(promotions[i]);
            switch (i) {
                case 0:
                    promotions[i].setText("♞");
                    break;
                case 1:
                    promotions[i].setText("♝");
                    break;
                case 2:
                    promotions[i].setText("♜");
                    break;
                case 3:
                    promotions[i].setText("♛");
                    break;
            }
            final int buttonNumber = i;
            promotions[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    switch (buttonNumber) {
                        case 0:
                            blockingQueue.offer(new PromotionSignal('N'));
                            break;
                        case 1:
                            blockingQueue.offer(new PromotionSignal('B'));
                            break;
                        case 2:
                            blockingQueue.offer(new PromotionSignal('R'));
                            break;
                        case 3:
                            blockingQueue.offer(new PromotionSignal('Q'));
                            break;
                    }

                }
            });
            promotions[i].setFont(new Font("SansSerif", Font.BOLD, 35));
        }
        promotionPanel.setLayout(new GridLayout(1,4));
    }
    public void createColourSelectionPanel() {
        for (int i = 0; i < 2; i++) {
            colours[i] = new JButton();
            colours[i].setBackground(Color.WHITE);
            colours[i].setOpaque(true);
            colours[i].setBorderPainted(false);
            colourPanel.add(colours[i]);
            switch (i) {
                case 0:
                    colours[i].setText("♔");
                    break;
                case 1:
                    colours[i].setText("♚");
                    break;
            }
            final int buttonNumber = i;
            colours[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    switch (buttonNumber) {
                        case 0:
                            blockingQueue.offer(new ColourSignal("White"));
                            break;
                        case 1:
                            blockingQueue.offer(new ColourSignal("Black"));
                            break;
                    }
                }
            });
            colours[i].setFont(new Font("SansSerif", Font.BOLD, 35));
        }
        colourPanel.setLayout(new GridLayout(1,2));
    }


    public void createSidePanel() {
        //Create buttons
        JButton replayButton = createReplayButton();
        JButton flipButton = createFlipButton();
        sidePanel.add(replayButton);
        sidePanel.add(flipButton);
        sidePanel.setLayout(new GridLayout(2,1));
    }
    public JButton createReplayButton() {
        JButton replayButton = new JButton();
        replayButton.setBackground(Color.LIGHT_GRAY);
        replayButton.setOpaque(true);
        replayButton.setBorderPainted(false);
        replayButton.setText("Replay");
        replayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Somehow make main restart
                blockingQueue.offer(new ReplaySignal());
            }
        });
        replayButton.setFont(new Font("SansSerif", Font.BOLD, 35));
        return replayButton;
    }
    public JButton createFlipButton() {
        JButton flipButton = new JButton();
        flipButton.setBackground(Color.LIGHT_GRAY);
        flipButton.setOpaque(true);
        flipButton.setBorderPainted(false);
        flipButton.setText("Flip Board");
        flipButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                whitePerspective = !whitePerspective;
                changeWhiteDown();
                addButtonsToBoardPanel();
            }
        });
        flipButton.setFont(new Font("SansSerif", Font.BOLD, 35));
        return  flipButton;
    }

    public void addButtonsToBoardPanel() {
        boardPanel.removeAll();
        if  (whiteDown) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    boardPanel.add(squares[i][j]);
                }
            }
        }
        else {
            for (int i = 7; i >= 0; i--) {
                for (int j = 7; j >= 0; j--) {
                    boardPanel.add(squares[i][j]);
                }
            }
        }
        boardPanel.revalidate();
        boardPanel.repaint();
    }
    public void updatePiecesGUI(Board board) {
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    setSquareSymbol(i, j, board, squares);
                }
            }
        });
    }
    public void removeAttackSquares(Board board){
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (((i + j) % 2 == 0)) {
                        squares[i][j].setBackground(new Color(255, 240, 200));
                    } else {
                        squares[i][j].setBackground(new Color(150, 100, 0));
                    }
                }
            }
        });
    }
    public void setSquareSymbol(int squaresRow, int squaresCol, Board board, JButton[][] squares) {
//        //Funky
        int boardRow = squaresRow;
        int boardCol = squaresCol;

        char pieceSymbol = board.getPiece(7-squaresRow, squaresCol);
        switch(pieceSymbol) {
            case 'P':
                squares[squaresRow][squaresCol].setText("♙");
                break;
            case 'p':
                squares[squaresRow][squaresCol].setText("♟");
                break;
            case 'N':
                squares[squaresRow][squaresCol].setText("♘");
                break;
            case 'n':
                squares[squaresRow][squaresCol].setText("♞");
                break;
            case 'R':
                squares[squaresRow][squaresCol].setText("♖");
                break;
            case 'r':
                squares[squaresRow][squaresCol].setText("♜");
                break;
            case 'B':
                squares[squaresRow][squaresCol].setText("♗");
                break;
            case 'b':
                squares[squaresRow][squaresCol].setText("♝");
                break;
            case 'Q':
                squares[squaresRow][squaresCol].setText("♕");
                break;
            case 'q':
                squares[squaresRow][squaresCol].setText("♛");
                break;
            case 'K':
                squares[squaresRow][squaresCol].setText("♔");
                break;
            case 'k':
                squares[squaresRow][squaresCol].setText("♚");
                break;
            default:
                squares[squaresRow][squaresCol].setText("");
                break;
        }
    }
    public void highlightSquare(Square square) {
        SwingUtilities.invokeLater(() -> {
            squares[7 - square.getRow()][square.getCol()].setBackground(new Color(235, 135, 100));
        });
    }
    public void changeWhiteDown(){
        this.whiteDown = !this.whiteDown;
    }


    public Square getClickedSquare() throws InterruptedException {
        while (!blockingQueue.isEmpty()) {
            GUISignal guisignal = blockingQueue.take();
            if (guisignal instanceof ReplaySignal) {
                return new Square(-1,-1);
            }
        }
        while (true) {
            GUISignal guisignal = blockingQueue.take();
            if (guisignal instanceof SquareSignal) {
                return ((SquareSignal) guisignal).getSquare();
            }
            else if (guisignal instanceof PromotionSignal) {}
            else if (guisignal instanceof ColourSignal) {}
            else if (guisignal instanceof ReplaySignal) {
                return new Square (-1, -1);
            }
        }
    }
    public char getPromotionOption() throws InterruptedException {
        while (!blockingQueue.isEmpty()) {
            GUISignal guisignal = blockingQueue.take();
            if (guisignal instanceof ReplaySignal) {
                return 'x';
            }
        }
        SwingUtilities.invokeLater(() -> {
            cardLayout.show(deck, "Promotion");
        });
        char promotionOption = 'x';
        while (true) {
            GUISignal guisignal = blockingQueue.take();
            if (guisignal instanceof PromotionSignal) {
                promotionOption = ((PromotionSignal) guisignal).getPromotionSymbol();
                break;
            }
            else if (guisignal instanceof SquareSignal) {}
            else if (guisignal instanceof ColourSignal) {}
            else if (guisignal instanceof ReplaySignal) {
                promotionOption = 'x';
                break;
            }
        }
        SwingUtilities.invokeLater(() -> {
            cardLayout.show(deck, "Board");
        });
        return promotionOption;
    }
    public String getColourOption() throws InterruptedException {
        while (!blockingQueue.isEmpty()) {
            GUISignal guisignal = blockingQueue.take();
            if (guisignal instanceof ReplaySignal) {
                return "x";
            }
        }
        SwingUtilities.invokeLater(() -> {
            cardLayout.show(deck, "Colour");
        });
        String colourOption = "x";
        while (true) {
            GUISignal guisignal = blockingQueue.take();
            if (guisignal instanceof ColourSignal) {
                colourOption = ((ColourSignal) guisignal).getColourOption();
                break;
            }
            else if (guisignal instanceof SquareSignal) {}
            else if (guisignal instanceof PromotionSignal) {}
            else if (guisignal instanceof ReplaySignal) {
                colourOption = "x";
                break;
            }
        }
        SwingUtilities.invokeLater(() -> {
            cardLayout.show(deck, "Board");
        });
        return colourOption;
    }
    public void getRestartSignal() throws InterruptedException {
        while (true) {
            GUISignal guisignal = blockingQueue.take();
            if (guisignal instanceof ReplaySignal) {
                break;
            }
        }
    }
    public void setWhitePerspective(boolean whitePerspective) {this.whitePerspective = whitePerspective;}
    public boolean getWhitePerspective() {
        return whitePerspective;
    }

}
