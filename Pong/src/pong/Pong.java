package pong;

/**
 *
 * @author daniele&emil
 */
public class Pong {

    public Pong()
    {
        main(new String[]{});
    }

    public static void main(String[] args) { 
            final int WIDTH = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
            final int HEIGHT = WIDTH / 16 * 9;
            final String TITLE = "Pong";
            MainMenu a=new MainMenu(WIDTH, HEIGHT, TITLE);
    }
}
