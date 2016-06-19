package com.mygdx.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.screens.GameScreen;

/**
 * Created by angel on 8/3/2016.
 */
public class Paddle{

    private GameScreen screen;
    private Rectangle paddle;


    public Paddle (GameScreen screen){
        this.screen = screen;
        paddle = new Rectangle();
        //load the paddle texture
        screen.paddleImage = new Texture(Gdx.files.internal("sprites/paddle.png"));
        //initial position and the dimension of the paddle
        paddle.x = 800 / 2 - 64 / 2;
        paddle.y = 20;
        paddle.width = 64;
        paddle.height = 16;
    }

    public Rectangle getPaddle(){
        return paddle;
    }
}