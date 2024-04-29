import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.*;

//What you are trying to do is to animate the GUI drawing, incrementally updating the image. There are several steps involved in solving this
//
//Use an animation or "game" loop to drive the animation, and this is most easily achieved using a Swing Timer
//Inside the animation loop, draw onto a BufferedImage, and call repaint()
//Draw that image within a paintComponent method using the Graphics#drawImage(...) method.
//https://stackoverflow.com/questions/60377750/how-to-see-lines-drawn-in-real-time-java-swing

public class SimpleAnimation extends JPanel {
    private static final int IMG_W = 800;
    private static final int IMG_H = IMG_W;
    private static final int TIMER_DELAY = 40;
    public static final Stroke STROKE = new BasicStroke(6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    public static final Color DRAW_COLOR = Color.RED;
    public static final Color BACKGROUND_COLOR = Color.white;
    public static final double DELTA = 8;
    private BufferedImage img = new BufferedImage(IMG_W, IMG_H, BufferedImage.TYPE_INT_ARGB);
    private Timer animationTimer = null;
    private int myX = 0;
    private int myY = 0;

    public SimpleAnimation() {
        setBackground(Color.WHITE);
        JButton drawButton = new JButton("Draw!");
        drawButton.addActionListener(e -> draw());
        add(drawButton);
    }

    private void draw() {
        // if timer currently running, stop it
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
        myX = 0;
        myY = 0;
        img = new BufferedImage(IMG_W, IMG_H, BufferedImage.TYPE_INT_ARGB);
        animationTimer = new Timer(TIMER_DELAY, new TimerListener());
        animationTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (img != null) {
            g.drawImage(img, 0, 0, this);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(IMG_W, IMG_H);
    }

    private class TimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (img == null) {
                return;
            }
            Graphics2D g2 = img.createGraphics();
            g2.setStroke(STROKE);
            g2.setColor(DRAW_COLOR);
            g2.setBackground(BACKGROUND_COLOR);
            //clear the current image
            g2.clearRect(0, 0, 800, 800);
            //int x = myX + (int) (DELTA * Math.random());
            //int y = myY + (int) (DELTA * Math.random());
            int x = myX + (int)DELTA;
            int y = myY + (int)DELTA;
            g2.drawLine(x, y, myX, myY);
            g2.dispose();
            myX = x;
            myY = y;

            if (myX > IMG_W || myY > IMG_H) {
                ((Timer) e.getSource()).stop();
            }
            repaint();
        }
    }

    private static void createAndShowGui() {
        SimpleAnimation mainPanel = new SimpleAnimation();

        JFrame frame = new JFrame("SimpleAnimation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGui());
    }
}