package pong;
import java.awt.geom.Ellipse2D;
/**
 *
 * @author daniele
 */
public class Ball extends Ellipse2D.Double {
    
    private int speed;
    
    public Ball(int x,int y,int width,int height)
    {
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;
    }
    
    boolean setSpeed(int speed)
    {
        this.speed=speed;
        return true;
    }
    
    void moveX(boolean right)
    {
        if(right)
            x+=speed;
        else x-=speed;
    }
    
    void moveY(boolean down)
    {
        if(down)
            y+=speed;
        else y-=speed;
    }
    
    boolean setX(int x)
    {
        if(x<width)
            this.x=x;
        return x<width;
    }
    
    boolean setY(int y)
    {
        if(y<height)
            this.y=y;
        return y<height;
    }
    

}
