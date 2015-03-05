package pong;
import java.awt.geom.Rectangle2D;
/**
 *
 * @author daniele
 */
public class Paddle extends Rectangle2D.Double implements Runnable {
    
    private int speed;
    private boolean dir,move;
    Thread t;
    public Paddle(int x,int y,int width,int height)
    {
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;
        t=new Thread(this);
        t.start();
    }
    
    boolean setSpeed(int speed)
    {
        if(speed<0||speed>16)
            return false;
        this.speed=speed;
        return true;
    }

    public void setMove(boolean move) {
        this.move = move;
    }

    public void setDir(boolean dir) 
    {
        this.dir=dir;
        move=true;
    }
    
    boolean setY(int y)
    {
        if(y<height)
            this.y=y;
        return y<height;
    }
    
    void move(boolean dir)
    {
          if(dir)
              y+=speed;
          else
              y-=speed;
    }
    
    void move()
    {
        if(move)
        {
          if(dir)
              y+=speed;
          else
              y-=speed;
        }
    }

    @Override
    public void run() {
        while(true)
        {
            move();
            move=false;
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
               
            }
        }
    }
    
    
}
