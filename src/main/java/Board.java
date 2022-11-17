import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board extends JPanel implements KeyListener{
    public static int SIZE;
    public static int SPEED;
    public static final int UP = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;
    public static final int RIGHT = 4;

    public static int direction;
    public static int direction2;

    public static boolean noFence=false;
    public static boolean twoPlayers=false;
    public static boolean greenWon;
    public static boolean greenAte;
    private static int food_kind=0;


    private static final List<Node> snake = new ArrayList<>();
    private static final List<Node> snake2 = new ArrayList<>();

    private static food food;
    public static boolean restart=false;

    public Board() {
        food = new food(SIZE*2,SIZE*2);
        snake.clear();
        snake2.clear();

        direction = LEFT;
        for (int i = 0; i < 6; i++) {
            snake.add(new Node(270+(i*SIZE),90+(SIZE)));
        }
        if (twoPlayers){
            direction2 = RIGHT;
            for (int i = 0; i < 6; i++) {
                snake2.add(new Node(90 - (i * SIZE), 270 + (SIZE)));
            }
        }
    }


    public static void moveSnake(){
        restart=false;

        int last = snake.size();
        int last2 = snake2.size();
        while (!touchedBorder1() && !touchedBorder2() && !touchedItself1() && !touchedItself2() && !restart){
            main.board.repaint();


            if (last==0){
                last=snake.size()-1;
            }
            if (last2==0){
                last2=snake2.size()-1;
            }
            if (ateFood1() || ateFood2()){
                if (!twoPlayers) {
                    if (noFence)
                        main.score += ((200 - SPEED) / 10) + SIZE;
                    else
                        main.score += 1.5 * (((200 - SPEED) / 10) + SIZE);
                    main.score2.setText(String.valueOf(main.score));
                    main.frame.add(main.score2);
                    main.frame.repaint();
                }
                Random random = new Random() ;
                int x = random.nextInt(810);
                x=x-x%SIZE;
                int y = random.nextInt(540);
                y=y-y%SIZE;
                food_kind = random.nextInt(2);
                for (int i = 0; i < snake.size(); i++) {
                    if (((x==snake.get(i).getX()) && (y==snake.get(i).getY()))){
                        x = random.nextInt(810);
                        x = x - x % SIZE;
                        y = random.nextInt(540);
                        y = y - y % SIZE;
                        i = 0;
                    }
                }
                if (twoPlayers){
                    for (int i = 0; i < snake2.size(); i++) {
                        if (((x==snake2.get(i).getX()) && (y==snake2.get(i).getY()))) {
                            x = random.nextInt(810);
                            x = x - x % SIZE;
                            y = random.nextInt(540);
                            y = y - y % SIZE;
                            i = 0;
                        }
                    }
                }

                if (greenAte || !twoPlayers)
                    snake.add(new Node(snake.get(last).getX(),snake.get(last).getY()));
                else
                    snake2.add(new Node(snake2.get(last2).getX(),snake2.get(last2).getY()));

                food.setX(x);
                food.setY(y);
                if ((snake.size()+snake2.size())%4==0)
                    SPEED-=(double)SPEED*0.15;
            }

            last=move(last);
            if (twoPlayers)
                last2=move2(last2);
            try {
                if (!restart)Thread.sleep(Board.SPEED);
            } catch (InterruptedException ignored) {
            }
        }
        if (twoPlayers){
            if (!greenWon) {
                main.RedScore += 5;
                main.bestScore2.setText(String.valueOf(main.RedScore));
                main.frame.add(main.bestScore2);
            }
            else {
                main.GreenScore+=5;
                main.score2.setText(String.valueOf(main.GreenScore));
                main.frame.add(main.score2);
            }
            main.frame.repaint();
        }
        else {
            if (main.bestScore < main.score) {
                main.bestScore = main.score;
                main.bestScore2.setText(String.valueOf(main.bestScore));
                main.frame.add(main.bestScore2);
                main.frame.repaint();

            }
        }




    }

    private static int move(int last) {
        last = (last - 1)%(snake.size()-1);

        if (last==0){
            last=snake.size()-1;
        }

        int headX = snake.get(0).getX();
        int headY = snake.get(0).getY();
        if (direction == UP){
            int newLoc=snake.get(0).getY()-SIZE;
            if (noFence && newLoc%540<0)
                newLoc = 540 + newLoc;
            snake.get(0).setY(newLoc);
            snake.get(last).setY(headY);
            snake.get(last).setX(headX);

        }

        else if (direction == DOWN) {
            int newLoc = (snake.get(0).getY()+SIZE);
            if (noFence)
                newLoc = (snake.get(0).getY()+SIZE)%540;
            snake.get(0).setY(newLoc);
            snake.get(last).setY(headY);
            snake.get(last).setX(headX);
        }

        else if (direction == LEFT) {
            int newLoc = (snake.get(0).getX()-SIZE);
            if (noFence && newLoc%810<0)
                newLoc = 810 + newLoc;
            snake.get(0).setX(newLoc);
            snake.get(last).setX(headX);
            snake.get(last).setY(headY);
        }

        else {
            int newLoc=(snake.get(0).getX()+SIZE);
            if (noFence)
                newLoc=(snake.get(0).getX()+SIZE)%810;
            snake.get(0).setX(newLoc);
            snake.get(last).setX(headX);
            snake.get(last).setY(headY);
        }
        return last;
    }
    private static int move2(int last) {
        last = (last - 1)%(snake2.size()-1);

        if (last==0){
            last=snake2.size()-1;
        }

        int headX = snake2.get(0).getX();
        int headY = snake2.get(0).getY();
        if (direction2 == UP){
            int newLoc=snake2.get(0).getY()-SIZE;
            if (noFence && newLoc%540<0)
                newLoc = 540 + newLoc;
            snake2.get(0).setY(newLoc);
            snake2.get(last).setY(headY);
            snake2.get(last).setX(headX);

        }

        else if (direction2 == DOWN) {
            int newLoc = (snake2.get(0).getY()+SIZE);
            if (noFence)
                newLoc = (snake2.get(0).getY()+SIZE)%540;
            snake2.get(0).setY(newLoc);
            snake2.get(last).setY(headY);
            snake2.get(last).setX(headX);
        }

        else if (direction2 == LEFT) {
            int newLoc = (snake2.get(0).getX()-SIZE);
            if (noFence && newLoc%810<0)
                newLoc = 810 + newLoc;
            snake2.get(0).setX(newLoc);
            snake2.get(last).setX(headX);
            snake2.get(last).setY(headY);
        }

        else {
            int newLoc=(snake2.get(0).getX()+SIZE);
            if (noFence)
                newLoc=(snake2.get(0).getX()+SIZE)%810;
            snake2.get(0).setX(newLoc);
            snake2.get(last).setX(headX);
            snake2.get(last).setY(headY);
        }
        return last;
    }

    private static boolean ateFood1() {
        int headX = snake.get(0).getX();
        int headY = snake.get(0).getY();
        if (headX== food.getX() && headY== food.getY()){
            if (twoPlayers){
                greenAte=true;
                main.GreenScore++;
                main.score2.setText(String.valueOf(main.GreenScore));
                main.frame.add(main.score2);
                main.frame.repaint();
            }
            return true;
        }
        return false;
    }
    private static boolean ateFood2() {
        if (!twoPlayers)
            return false;
        int headX = snake2.get(0).getX();
        int headY = snake2.get(0).getY();
        if (headX== food.getX() && headY== food.getY()){
            greenAte=false;
            main.RedScore++;
            main.bestScore2.setText(String.valueOf(main.RedScore));
            main.frame.add(main.bestScore2);
            main.frame.repaint();
            return true;
        }
        return false;
    }

    private static boolean touchedItself1() {
        int headX = snake.get(0).getX();
        int headY = snake.get(0).getY();
        for (int i = 1; i < snake.size(); i++) {
            if (snake.get(i).getX()==headX && snake.get(i).getY()==headY){
                greenWon = false;
                return true;
            }
        }
        if (twoPlayers) {
            for (Node node : snake2) {
                if (node.getX() == headX && node.getY() == headY) {
                    greenWon = false;
                    return true;
                }
            }
        }
        return false;
    }
    private static boolean touchedItself2() {
        if (!twoPlayers)
            return false;
        int headX = snake2.get(0).getX();
        int headY = snake2.get(0).getY();
        for (int i = 1; i < snake2.size(); i++) {
            if (snake2.get(i).getX()==headX && snake2.get(i).getY()==headY){
                greenWon = true;
                return true;
            }
        }


        for (Node node : snake) {
            if (node.getX() == headX && node.getY() == headY) {
                greenWon = true;
                return true;
            }
        }

        return false;
    }

    private static boolean touchedBorder1() {
        if (noFence)
            return false;
        int headX = snake.get(0).getX();
        int headY = snake.get(0).getY();
        if (headX<0 || headX>main.board.getWidth()-SIZE
        || headY<0 || headY>main.board.getHeight()-SIZE) {
            greenWon = false;
            return true;
        }
        return false;
    }
    private static boolean touchedBorder2() {
        if (noFence || !twoPlayers)
            return false;
        int headX = snake2.get(0).getX();
        int headY = snake2.get(0).getY();
        if (headX<0 || headX>main.board.getWidth()-SIZE
                || headY<0 || headY>main.board.getHeight()-SIZE) {
            greenWon = true;
            return true;
        }

        return false;
    }

    @Override
    public void paint(Graphics g) {

        super.paint(g);
        if (!noFence) {
            g.setColor(Color.black);
            g.drawRect(0, 0, this.getWidth(), this.getHeight() - 1);
        }
        g.setColor(Color.white);
        g.fillRect(1,1,this.getWidth()-2,this.getHeight()-2);

        Image head = null;
        try {
        if (direction==DOWN){
            head = ImageIO.read(new File("src/pics/head.png"));
        } else if (direction==UP) {
            head = ImageIO.read(new File("src/pics/head2.png"));
        } else if (direction==LEFT) {
            head = ImageIO.read(new File("src/pics/head3.png"));
        }else {
            head = ImageIO.read(new File("src/pics/head4.png"));
        }
        } catch (IOException ignored) {
        }

        Image apple = null;
        try {
            if (food_kind==1)
                apple = ImageIO.read(new File("src/pics/red.png"));
            else
                apple = ImageIO.read(new File("src/pics/cherry.png"));

        } catch (IOException ignored) {
        }
        g.drawImage(apple,food.getX(),food.getY(),SIZE,SIZE,this);

        g.setColor(new Color(144, 176, 49));

        for (int i = 1; i < snake.size(); i++) {
            g.fillOval(snake.get(i).getX(),snake.get(i).getY(),SIZE,SIZE);
            //g.setColor(Color.black);
            //g.drawString(String.valueOf(i), (int) (snake.get(i).getX()+(double)SIZE*.5), (int) (snake.get(i).getY()+(double)SIZE*.5));
        }

        g.drawImage(head,snake.get(0).getX(),snake.get(0).getY(), SIZE, SIZE,this);


        if (twoPlayers){
            Image head2 = null;
            try {
                if (direction2==DOWN){
                    head2 = ImageIO.read(new File("src/pics/rhead.png"));
                } else if (direction2==UP) {
                    head2 = ImageIO.read(new File("src/pics/rhead2.png"));
                } else if (direction2==LEFT) {
                    head2 = ImageIO.read(new File("src/pics/rhead3.png"));
                }else {
                    head2 = ImageIO.read(new File("src/pics/rhead4.png"));
                }
            } catch (IOException e) {
            }
            g.setColor(new Color(215, 67, 54));

            for (int i = 1; i < snake2.size(); i++) {
                g.fillOval(snake2.get(i).getX(),snake2.get(i).getY(),SIZE,SIZE);
                //g.setColor(Color.black);
                //g.drawString(String.valueOf(i), (int) (snake.get(i).getX()+(double)SIZE*.5), (int) (snake.get(i).getY()+(double)SIZE*.5));
            }

            g.drawImage(head2,snake2.get(0).getX(),snake2.get(0).getY(), SIZE, SIZE,this);
        }

        }
    @Override
    public void keyTyped(KeyEvent e) {


    }
    private Thread t = new Thread();
    @Override
    public void keyPressed(KeyEvent e) {
        if (!t.isAlive()) {
            t = new Thread() {
                @Override
                public void run() {
                    super.run();
                    if (e.getKeyCode() == KeyEvent.VK_LEFT && direction != RIGHT) {
                        direction = LEFT;
                    } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && direction != LEFT) {
                        direction = RIGHT;
                    } else if (e.getKeyCode() == KeyEvent.VK_UP && direction != DOWN) {
                        direction = UP;
                    } else if (e.getKeyCode() == KeyEvent.VK_DOWN && direction != UP) {
                        direction = DOWN;
                    }

                    if (e.getKeyCode() == KeyEvent.VK_A && direction2 != RIGHT) {
                        if (twoPlayers)
                            direction2 = LEFT;
                        else if (direction != RIGHT)
                            direction = LEFT;
                    } else if (e.getKeyCode() == KeyEvent.VK_D && direction2 != LEFT) {
                        if (twoPlayers)
                            direction2 = RIGHT;
                        else if (direction != LEFT)
                            direction = RIGHT;
                    } else if (e.getKeyCode() == KeyEvent.VK_W && direction2 != DOWN) {
                        if (twoPlayers)
                            direction2 = UP;
                        else if (direction != DOWN)
                            direction = UP;
                    } else if (e.getKeyCode() == KeyEvent.VK_S ) {
                        if (twoPlayers && direction2 != UP  )
                            direction2 = DOWN;
                        else if (direction != UP)
                            direction = DOWN;
                    }
                    try {
                        Thread.sleep((long) (0.3*SPEED));
                    } catch (InterruptedException ignored) {
                    }
                }
            };
            t.start();
        }


    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
