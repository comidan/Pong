package pong;
import MGui.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
/**
 *
 * @author daniele&emil
 */
public final class MainMenu extends MFrame implements ActionListener,KeyListener
{
    private final int SCREEN_WIDTH = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width, SCREEN_HEIGHT = SCREEN_WIDTH/16*10;
    private final int BTN_WIDTH = 100, BTN_HEIGHT = 40;
    private int width, height,x,y;
    private String title;
    private int speed=0;
    private boolean isSet=false;
    
    public MainMenu(int WIDTH, int HEIGHT, String TITLE){      
        super.setCanvasBackground(Color.BLACK);
        super.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        super.setVisible(true);
        super.setTitle(TITLE);
        super.setResizable(true); 
        super.setLocationRelativeTo(null);
        super.setDefaultCloseOperation(MFrame.EXIT_ON_CLOSE);
        setExtendedState(MFrame.MAXIMIZED_BOTH);
        addKeyListener(this);
        this.width = SCREEN_WIDTH;
        this.height = SCREEN_HEIGHT;
        this.title = TITLE;
        Timer timer=new Timer(250,this); 
        timer.start();
        File sourceimage;
        Image immagineIc;
        try {
            sourceimage=new java.io.File("src\\icon\\pong.gif");
            Image image = ImageIO.read(sourceimage);
            immagineIc=Toolkit.getDefaultToolkit().getImage(sourceimage.toString());
            super.setIconImage(image);
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void mpaint ( Graphics2D g2 ){
        GraphSet.setColor(g2, 255, 255, 255);
        Rectangle2D.Double r1=new Rectangle2D.Double(width/2-60,height/2,150,30),r2=new Rectangle2D.Double(width/2-60,r1.getY()+50,150,30),
                           r3=new Rectangle2D.Double(width/2-60,r2.getY()+50,150,30),r4=new Rectangle2D.Double(width/2-60,r3.getY()+50,150,30),
                           r5=new Rectangle2D.Double(width/2-60,r4.getY()+50,150,30);
        g2.draw(r1);
        g2.draw(r2);
        g2.draw(r3);
        g2.draw(r4);
        g2.draw(r5);
        g2.setFont(new Font("8BIT WONDER", Font.BOLD, 10));
        g2.drawString("1 Play P vs AI",width/2-50,height/2+20);
        g2.drawString("2 Play P v P",width/2-50,(int)r3.getY()+20);
        g2.drawString("4 Play AI vs AI",width/2-50,(int)r2.getY()+20);
        g2.drawString("Set speed",width/2-50,(int)r4.getY()+20);
        g2.drawString("Exit",width/2-50,(int)r5.getY()+20);
        g2.setFont(new Font("8BIT WONDER", Font.BOLD,90));
        g2.drawString("Pong",width/2-150,100);
        g2.setFont(new Font("Serif",Font.BOLD,20));
        /*String credits="Developed By : \n- Daniele Comi superdani96@gmail.com\n- Emil Osterhed emilost96@gmail.com";
        int textLine=height-200;
        for (String line : credits.split("\n"))
        {
            g2.drawString(line,width-400,textLine+=g2.getFontMetrics().getHeight());
        }*/
        Rectangle2D.Double[] r=new Rectangle2D.Double[100];
        for(int i=0;i<r.length;i++)
        {
            r[i]=new Rectangle2D.Double(new Random().nextInt(width)+1,new Random().nextInt(height)+1,5,5);
            g2.draw(r[i]);
            g2.fill(r[i]);
        }
        if(r1.contains(new Point2D.Double(x,y)))
            start(0);
        else if(r2.contains(new Point2D.Double(x,y)))
            start(1);
        else if(r3.contains(new Point2D.Double(x,y)))
            start(2);
        else if(r4.contains(new Point2D.Double(x,y)))
        {
            if(!isSet&&speed==0)
            {
                isSet=true;
                try
                {
                    speed=Integer.parseInt(JOptionPane.showInputDialog("Insert speed (px/movement)"));
                }
                catch(NumberFormatException exc)
                {
                    speed=12;
                }
            }
        }
        else if(r5.contains(new Point2D.Double(x,y)))
            exit();
    }
    
    @Override
    public void mouseClicked (MouseEvent e) {
        x = e.getX();
        y = e.getY();
        mrepaint();
    }
    
    
    
    void start(int choice)
    {
        dispose();
        if(speed==0)
            speed=8;
        if(choice==0)
            new Game(width,height,title,speed);
        else if(choice==1)
            new AIGame(width,height,title,speed);
        else if(choice==2)
            new PvPGame(width,height,title,speed);
    }
    
    void exit()
    {
        dispose();
        System.exit(0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mrepaint();
    }

    @Override
    public void keyTyped(KeyEvent e)
    {
        if(e.getKeyCode()==KeyEvent.VK_1||e.getKeyCode()==KeyEvent.VK_NUMPAD1)
        {
             if(speed==0)
                    speed=7;
             new Game(width,height,title,speed);
        }
        else if(e.getKeyCode()==KeyEvent.VK_2||e.getKeyCode()==KeyEvent.VK_NUMPAD2)
        {
             if(speed==0)
                    speed=7;
             new PvPGame(width,height,title,speed);
        }
        else if(e.getKeyCode()==KeyEvent.VK_4||e.getKeyCode()==KeyEvent.VK_NUMPAD4)
            {
             if(speed==0)
                    speed=7;
             new AIGame(width,height,title,speed);
        }    
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        if(e.getKeyCode()==KeyEvent.VK_1||e.getKeyCode()==KeyEvent.VK_NUMPAD1)
        {
             if(speed==0)
                    speed=7;
             dispose();
             new Game(width,height,title,speed);
        }
        else if(e.getKeyCode()==KeyEvent.VK_2||e.getKeyCode()==KeyEvent.VK_NUMPAD2)
        {
             if(speed==0)
                    speed=7;
             dispose();
             new PvPGame(width,height,title,speed);
        }
        else if(e.getKeyCode()==KeyEvent.VK_4||e.getKeyCode()==KeyEvent.VK_NUMPAD4)
            {
             if(speed==0)
                    speed=7;
             dispose();
             new AIGame(width,height,title,speed);
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        if(e.getKeyCode()==KeyEvent.VK_1||e.getKeyCode()==KeyEvent.VK_NUMPAD1)
        {
             if(speed==0)
                    speed=7;
             new Game(width,height,title,speed);
        }
        else if(e.getKeyCode()==KeyEvent.VK_2||e.getKeyCode()==KeyEvent.VK_NUMPAD2)
        {
             if(speed==0)
                    speed=7;
             new PvPGame(width,height,title,speed);
        }
        else if(e.getKeyCode()==KeyEvent.VK_4||e.getKeyCode()==KeyEvent.VK_NUMPAD4)
            {
             if(speed==0)
                    speed=7;
             new AIGame(width,height,title,speed);
        }
    }
}
