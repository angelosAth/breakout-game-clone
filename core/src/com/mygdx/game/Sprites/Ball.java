package com.mygdx.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.mygdx.game.Screens.GameScreen;

/**
 * Created by angel on 8/3/2016.
 */
public class Ball{
    private GameScreen screen;
    public Circle ball;


    public Ball (GameScreen screen){
        this.screen = screen;

        ball = new Circle();

        // load the image for the ball
        screen.ballImage = new Texture(Gdx.files.internal("ball.png"));
        // initial position and radius of the ball
        initial();
    }

    public void initial(){
        ball.x = 400;
        ball.y = 300;
        ball.setRadius(4);
    }

}
