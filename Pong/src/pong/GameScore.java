package pong;

import java.applet.AudioClip;

/**
 *
 * @author daniele
 */
public class GameScore implements Runnable {

    AudioClip clip;
    public GameScore(AudioClip clip)
    {
        this.clip=clip;
    }
    @Override
    public void run() {
        clip.play();
    }
    
    
}
