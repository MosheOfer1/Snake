import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class main {
    public static int GreenScore=0;
    public static int RedScore=0;
    public static int score = 0;
    public static int bestScore = 0;
    public static Board board  = new Board();
    public static JFrame frame = new JFrame("Snake");
    public static JLabel score2 = new JLabel();
    public static JLabel bestScore2 = new JLabel();


    public static void main(String[] args) {
        board.setBounds(40,100,810,540);
        board.setVisible(true);

        JButton button = new JButton();
        button.setText("Restart");
        button.setBounds(40,50,150,40);

        JSlider sizeSlider = new JSlider(1,3,2);
        sizeSlider.setBounds(200,50,150,40);
        sizeSlider.setMajorTickSpacing(1);
        sizeSlider.setPaintLabels(true);
        sizeSlider.setPaintTrack(true);

        JLabel sizeLabel = new JLabel("Size");
        sizeLabel.setBounds(260,10,50,40);
        frame.add(sizeLabel);

        JSlider speedSlider = new JSlider(1,3,2);
        speedSlider.setBounds(400,50,150,40);
        speedSlider.setMajorTickSpacing(1);
        speedSlider.setPaintLabels(true);
        speedSlider.setPaintTrack(true);

        JLabel speedLabel = new JLabel("Speed");
        speedLabel.setBounds(460,10,50,40);
        frame.add(speedLabel);

        JLabel scoreL = new JLabel("Score: ");
        scoreL.setBounds(620,10,50,40);
        frame.add(scoreL);

        score2.setBounds(640,40,50,40);

        JLabel bestScoreLab = new JLabel("Best Score: ");
        bestScoreLab.setBounds(720,10,100,40);
        frame.add(bestScoreLab);

        bestScore2.setBounds(750,40,50,40);

        JCheckBox fence = new JCheckBox();
        fence.setBounds(20,10,100,40);
        fence.setText("No fence");
        frame.add(fence);

        JCheckBox twoPlayers = new JCheckBox();
        twoPlayers.setBounds(120,10,100,40);
        twoPlayers.setText("Two players");
        final boolean[] pressed = {false};
        twoPlayers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!pressed[0]) {
                    scoreL.setText("Green");
                    bestScoreLab.setText("Red");
                    pressed[0] =true;
                    score2.setText(String.valueOf(GreenScore));
                    bestScore2.setText(String.valueOf(RedScore));
                }
                else {
                    scoreL.setText("Score: ");
                    bestScoreLab.setText("Best Score: ");
                    pressed[0] =false;
                    score2.setText(String.valueOf(score));
                    bestScore2.setText(String.valueOf(bestScore));
                }
                frame.repaint();
            }
        });
        frame.add(twoPlayers);



        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Board.twoPlayers= twoPlayers.isSelected();
                if (!Board.twoPlayers) {
                    main.score = 0;
                    main.score2.setText(String.valueOf(main.score));
                    main.frame.add(main.score2);
                    main.frame.repaint();
                }
                Board.restart=true;
                Board.SIZE= 15 * sizeSlider.getValue();
                int speedVal = speedSlider.getValue();
                if (speedVal==1)
                    Board.SPEED = 200;
                else if (speedVal==2) {
                    Board.SPEED = 100;
                }
                else {
                    Board.SPEED = 50;

                }

                try {
                    Thread.sleep(Board.SPEED+10);
                } catch (InterruptedException ignored) {
                }
                Thread thread = new Thread(){
                    @Override
                    public void run() {
                        super.run();

                        frame.remove(board);
                        board = new Board();
                        if (fence.isSelected())
                            Board.noFence=true;
                        else
                            Board.noFence=false;



                        board.setBounds(40,100,810,540);
                        frame.add(board);
                        board.setLayout(null);
                        board.setOpaque(false);
                        Board.moveSnake();

                    }
                };
                thread.start();
                button.addKeyListener(board);

            }
        });
        button.setFocusable(true);


        frame.add(sizeSlider);
        frame.add(speedSlider);
        frame.add(board);

        frame.add(button);

        frame.setSize(900, 680);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        board.setLayout(null);
        board.setOpaque(false);
        frame.setVisible(true);

        button.addKeyListener(board);

    }

}
