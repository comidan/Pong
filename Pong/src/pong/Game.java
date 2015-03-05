package pong;
import java.awt.*;
import MGui.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class Game extends MFrame implements Runnable
{
    private File sourceimage;
    private Image immagineIc;
    private int movement,points,_points,score,scorePX,scoreCX,scoreY;
    private final int width,height,speed;
    private Ball ball;
    private final Paddle player;
    private final AIPaddle ai;
    private boolean right,down,up,ballMovement,game=true;
    private boolean[] keys;
    private final URL url,_url; 
    private final AudioClip clipContact,clipScore;
    private final KeyListener listener;
    private final double SPEED_AI;
    private Thread t;
    private Font font;
    
    public Game(int width, int height, String title,int speed) {        
        super(java.awt.Toolkit.getDefaultToolkit().getScreenSize().width,java.awt.Toolkit.getDefaultToolkit().getScreenSize().height);
        width=java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
        height=java.awt.Toolkit.getDefaultToolkit().getScreenSize().height-60;
        super.setTitle(title);
        super.setResizable(true); 
        super.setLocationRelativeTo(null);
        super.setCanvasBackground(Color.BLACK);
        setExtendedState(MFrame.MAXIMIZED_BOTH);
        this.speed=speed;
        try {
            sourceimage=new java.io.File("src\\icon\\pong.gif");
            Image image = ImageIO.read(sourceimage);
            immagineIc=Toolkit.getDefaultToolkit().getImage(sourceimage.toString());
            super.setIconImage(image);
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        listener = new MyKeyListener();
        keys=new boolean[256];
        for(int i=0;i<keys.length;i++)
            keys[i]=false;
	addKeyListener(listener);
	setFocusable(true);
        SPEED_AI=5.5;
        score=0;
        points=0;
        _points=0;
        movement=10;
        scorePX=width/6;
        scoreCX=900;
        scoreY=40;
        String OS=System.getProperty("os.name").toLowerCase();   //uguali ma li tengo per in futuro
        if(OS.startsWith("windows")&&!OS.contains("xp"))
        {
            this.width=width;
            ai=new AIPaddle(width-10,0,speed+2,width,height,true);
        }
        else if(OS.startsWith("linux")||(OS.startsWith("windows")&&OS.contains("xp")))
        {
            this.width=width;
            ai=new AIPaddle(width-10,0,speed+1,width,height,true);
        }
        else
        {
            this.width=width;
            ai=new AIPaddle(width-10,0,speed+1,width,height,true);
        }
        this.height=height;
        ball=new Ball(100,40,20,20);
        ball.setSpeed(speed+2);
        player=new Paddle(0,0,10,120);
        player.setSpeed(speed+2);
        url=Pong.class.getResource("pongLimit.wav");
        _url=Pong.class.getResource("pongScore.wav");
        clipContact=Applet.newAudioClip(url);
        clipScore=Applet.newAudioClip(_url);
        ballMovement=true;
        t=new Thread(this);
        t.start();
    }

    @Override
    public void mpaint (Graphics2D g2)
    {
        GraphSet.setColor(g2,255,255,255);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
        try
        {
            g2.draw(ball);
            g2.fill(ball);
            g2.draw(player);
            g2.fill(player);
            g2.draw(ai);
            g2.fill(ai);
            g2.setFont(new Font("8BIT WONDER", Font.BOLD, 40));        //OLD WILD 8 Bit FONT :D
            setDimension(g2);
            g2.drawString(""+_points,width/2+15,scoreY);
            g2.drawString(""+points,width/2-45,scoreY);
            for(int i=0;i<height+10;i+=10)
            {
                Rectangle2D.Double r=new Rectangle2D.Double(width/2,i,5,5);
                g2.draw(r);
                g2.fill(r);
            }
        }
        catch(NullPointerException exc)
        {
            
        }
        if(score!=0)
        {
            g2.setFont(new Font("8BIT WONDER", Font.BOLD,30));
            if(score==1)
                g2.drawString("You score",scorePX,height/2);
            else
                g2.drawString("Computer score",scoreCX,height/2);
    }
  }
 
    void moveBall()
    {   
        if(points==10)
            jackpot("Game Over\nWanna continue playing?","You win!");
        else if(_points==10)
            jackpot("Game Over\nWanna continue playing?","You lose!");
        
        if(ball.getBounds().intersectsLine(new Line2D.Double(0,height,width,height)))
        {
            movement=0;
            clipContact.play();
        }
        else if(ball.getBounds().intersectsLine(new Line2D.Double(0,0,width,0)))
            {
            movement=1;
            clipContact.play();
        }
        else if(ball.getBounds().intersectsLine(new Line2D.Double(width,0,width,height)))
        {
            if(!ball.getBounds().intersects(ai))
            {
                   printScore();
                   score=1;
                   points++;
                   new GameScore(clipScore).run(); //utilizzo oggetto esterno causa thread. 
                   movement=2;
            }
            else
            {
                if(ball.getBounds().intersectsLine(ai.getX(),ai.getY(),ai.getX(),(ai.getY()+40)))
                    movement=4;
                else if(ball.getBounds().intersectsLine(ai.getX(),ai.getY()+40,ai.getX(),(ai.getY()+80)))
                    movement=5;
                else if(ball.getBounds().intersectsLine(ai.getX(),ai.getY()+80,ai.getX(),(ai.getY()+120)))
                    movement=6;
            }
                   clipContact.play();
               
        }
        else if(ball.getBounds().intersectsLine(new Line2D.Double(-5,0,-5,height)))
        {
               if(!ball.getBounds().intersects(player)) 
               {
                   printScore();
                   score=2;
                   _points++;
                   new GameScore(clipScore).run(); //utilizzo oggetto esterno causa thread.
                   movement=3;
               }
               else
              {
                if(ball.getBounds().intersectsLine(player.getX(),player.getY(),player.getX(),(player.getY()+40)))
                    movement=7;
                else if(ball.getBounds().intersectsLine(player.getX(),player.getY()+40,player.getX(),(player.getY()+80)))
                    movement=8;
                else if(ball.getBounds().intersectsLine(player.getX(),player.getY()+80,player.getX(),(player.getY()+120)))
                    movement=9;
              }
                   clipContact.play(); 
        }
        
        switch(movement)
        {
            case 0: ball.setSpeed(speed+2);ball.moveY(false); ball.moveX(right);  down=false;  break;
            case 1: ball.setSpeed(speed+2);ball.moveY(true); ball.moveX(right);  down=true;   break;
            case 2: ball.setSpeed(speed+2);ball.moveX(false); ball.moveY(down);  right=false; break;
            case 3: ball.setSpeed(speed+2);ball.moveX(true); ball.moveY(down);  right=true; break;
            case 4: right=false; ball.setSpeed(speed+2); if(ball.getX()>=ai.getX()) down=false; ball.moveY(down); ball.moveX(right); break;
            case 5: ball.setSpeed(speed+2);right=false; ball.setSpeed(speed+3); ball.moveX(right);  break;
            case 6: ball.setSpeed(speed+2);right=false; if(ball.getX()>=ai.getX()) down=true; ball.moveY(down); ball.moveX(right); break;
            case 7: ball.setSpeed(speed+2);right=true; if(ball.getX()<=player.getX()) down=false; ball.moveY(down); ball.moveX(right); break;
            case 8: right=true; ball.setSpeed(speed+3); ball.moveX(right); break;
            case 9: ball.setSpeed(speed+2);right=true; if(ball.getX()<=player.getX()) down=true; ball.moveY(down); ball.moveX(right); break;
            default:ball.setSpeed(speed+2);
                    ball.moveX(true);
                    ball.moveY(true);  
                    down=true;
                    right=true;
                    break;
        }
    }

    
    private void printScore()
    {
        ballMovement=false;
    }

    private void reset()
    {
        ball=new Ball(350,80,20,20);
        ball.setSpeed(6);
        points=0;
        _points=0;
        player.setY(0);
        mrepaint();
    }
    
    private void jackpot(String msg,String ttl)
    {
        /*if(JOptionPane.showConfirmDialog(this,msg,ttl,
           JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
              reset();
        else
        {
              dispose();
              System.exit(0);
        }*/
        game=false;
    }
    
    void setDimension(Graphics2D g2)                                 //check the presence of a custom font
    {
        if(!g2.getFont().getName().equals("8BIT WONDER"))
            {
                g2.setFont(new Font("Serif", Font.BOLD, 40));
                scoreY=40;
                scorePX=width/5;
                scoreCX=450;
            }
    }
    
    private void updateStatus()
    {
        if(keys[KeyEvent.VK_C])
            game=false;
        if(player.getY()>=2)
			if(keys[KeyEvent.VK_NUMPAD8]||keys[KeyEvent.VK_8]||keys[KeyEvent.VK_R]||keys[KeyEvent.VK_UP])
                        {
                            player.setMove(true);
                            player.setDir(false);
                        }
        if(player.getY()<=(height-110))
                        if(keys[KeyEvent.VK_NUMPAD2]||keys[KeyEvent.VK_2]||keys[KeyEvent.VK_F]||keys[KeyEvent.VK_DOWN])
                        {
                            player.setMove(true);
                            player.setDir(true);
                        }
    }

    @Override
    public void run() 
    {
       while(game)
       {
           if(ballMovement)
           {
                
                updateStatus();
                ai.setBallX(ball.x);
                ai.setBallY(ball.y);
                if(ballMovement)
                    moveBall();
                mrepaint();
           }
           else
           {
               try 
               {
                    Thread.sleep(1000);
               } 
               catch (InterruptedException ex) 
               {
            
               }
               ballMovement=true;
               score=0;
           }
           try 
           {
                Thread.sleep(10);
           } 
           catch (InterruptedException ex) 
           {
               
           }
       }
       dispose();
       new Pong();
    }
    
    private class MyKeyListener implements KeyListener {
		@Override
		public void keyTyped(KeyEvent e) {
                    keys[e.getKeyCode()]=true;
		}

		@Override
		public void keyPressed(KeyEvent e) {
                    keys[e.getKeyCode()]=true;
		}

		@Override
		public void keyReleased(KeyEvent e) {
                    keys[e.getKeyCode()]=false;
		}
	}

    @Override
    public void mouseClicked(MouseEvent e)
    {
        if(e.getClickCount()==2)
        {
            game=false;
            dispose();
            new Pong();
        }
    }
}
