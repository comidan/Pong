package pong;
import java.awt.geom.Rectangle2D;
/**
 *
 * @author Emil
 */
public class AIPaddle extends Rectangle2D.Double implements Runnable
{
    private final int gameHeight,gameWidth;
    private final double WIDTH=10,HEIGHT=120,SPEED;
    private final boolean right;
    private double ballX,ballY;
    private Thread t;
    public AIPaddle(int x,int y,double speed,int gameWidth,int gameHeight,boolean right){
        SPEED=speed;
        this.x=x;
        this.y=y;
        this.height=HEIGHT;
        this.width=WIDTH;
        this.gameHeight=gameHeight;
        this.gameWidth=gameWidth;
        this.right=right;
        t=new Thread(this);
        t.start();
    }
    
   public void moveAi(double ballX, double ballY)
   {
        if(right)
        {
            if(ballX >= gameWidth/1.5)
            {    
                if(ballY < y && y>=2)
                    y-=SPEED;
                if(ballY >y && y+height <=gameHeight+10)
                    y+=SPEED;
            }
        }
        else 
        {        
            if(ballX < gameWidth/6)
            {
                 if(ballY < y && y>=2)
                     y-=SPEED;
                 if(ballY >y && y+height <=gameHeight+10)
                     y+=SPEED;
            }
        } 
    }

    public void setBallX(double ballX) 
    {
        this.ballX = ballX;
    }

    public void setBallY(double ballY) 
    {
        this.ballY = ballY;
    }
   
   public void moveAi()
   {
        if(right)
        {
            if(ballX >= gameWidth/1.5){    
                if(ballY < y && y>=2)
                    y-=SPEED;
                if(ballY >y && y+height <=gameHeight+10)
                    y+=SPEED;
            }
        }
        else 
        {        
            if(ballX < gameWidth/3){
                 if(ballY < y && y>=2)
                     y-=SPEED;
                 if(ballY >y && y+height <=gameHeight+10)
                     y+=SPEED;
            }
        } 
    }

    @Override
    public void run() {
        while(true)
        {
            moveAi();
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                
            }
        }
    }
}