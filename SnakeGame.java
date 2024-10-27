import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {

    private final int TILE_SIZE = 25;
    private final int SCREEN_WIDTH = 600;
    private final int SCREEN_HEIGHT = 600;
    private final int TOTAL_TILES = (SCREEN_WIDTH * SCREEN_HEIGHT) / (TILE_SIZE * TILE_SIZE);

    private final int[] x = new int[TOTAL_TILES];
    private final int[] y = new int[TOTAL_TILES];

    private int bodyParts = 3;
    private int foodX;
    private int foodY;
    private int score = 0;

    private char direction = 'R';
    private boolean running = false;
    private Timer timer;
    private Random random;

    private JButton resetButton;

    public SnakeGame() {
        random = new Random();
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        if (direction != 'R') {
                            direction = 'L';
                        }
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (direction != 'L') {
                            direction = 'R';
                        }
                        break;
                    case KeyEvent.VK_UP:
                        if (direction != 'D') {
                            direction = 'U';
                        }
                        break;
                    case KeyEvent.VK_DOWN:
                        if (direction != 'U') {
                            direction = 'D';
                        }
                        break;
                }
            }
        });
        resetButton = new JButton("Reset");
        resetButton.setFocusable(false);
        resetButton.addActionListener(e -> resetGame());
        add(resetButton);
        startGame();
    }

    public void startGame() {
        resetButton.setVisible(false);
        bodyParts = 3;
        score = 0;
        direction = 'R';
        x[0] = 0;
        y[0] = 0;
        spawnFood();
        running = true;
        timer = new Timer(100, this);
        timer.start();
    }

    public void resetGame() {
        startGame();
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            g.setColor(Color.RED);
            g.fillOval(foodX, foodY, TILE_SIZE, TILE_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                } else {
                    g.setColor(new Color(45, 180, 0));
                }
                g.fillRect(x[i], y[i], TILE_SIZE, TILE_SIZE);
            }

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + score, (SCREEN_WIDTH - metrics.stringWidth("Score: " + score)) / 2,
                    g.getFont().getSize());
        } else {
            gameOver(g);
        }
    }

    public void spawnFood() {
        foodX = random.nextInt((int) (SCREEN_WIDTH / TILE_SIZE)) * TILE_SIZE;
        foodY = random.nextInt((int) (SCREEN_HEIGHT / TILE_SIZE)) * TILE_SIZE;
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U':
                y[0] -= TILE_SIZE;
                break;
            case 'D':
                y[0] += TILE_SIZE;
                break;
            case 'L':
                x[0] -= TILE_SIZE;
                break;
            case 'R':
                x[0] += TILE_SIZE;
                break;
        }
    }

    public void checkFood() {
        if ((x[0] == foodX) && (y[0] == foodY)) {
            bodyParts++;
            score++;
            spawnFood();
        }
    }

    public void checkCollisions() {
        // Check if the snake collides with itself
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }

        // Check if the snake collides with the borders
        if (x[0] < 0 || x[0] >= SCREEN_WIDTH || y[0] < 0 || y[0] >= SCREEN_HEIGHT) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        metrics = getFontMetrics(g.getFont());
        g.drawString("Score: " + score, (SCREEN_WIDTH - metrics.stringWidth("Score: " + score)) / 2,
                g.getFont().getSize() + 50);

        resetButton.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkFood();
            checkCollisions();
        }
        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        SnakeGame game = new SnakeGame();
        frame.add(game);
        frame.setResizable(false);
        frame.pack();
        frame.setTitle("Snake Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
