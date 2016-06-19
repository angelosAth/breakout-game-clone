package com.mygdx.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.mygdx.game.screens.GameScreen;

/**
 * Created by angel on 8/3/2016.
 */
public class Ball{
    private GameScreen screen;
    private Circle ball;

    public Ball (GameScreen screen){
        this.screen = screen;
        ball = new Circle();
        // load the image for the ball
        screen.ballImage = new Texture(Gdx.files.internal("sprites/ball.png"));
        // initial position and radius of the ball
        initial();
    }

    public void initial(){
        ball.x = 400;
        ball.y = 300;
        ball.setRadius(4);
    }

    public Circle getBall(){
        return ball;
    }
}