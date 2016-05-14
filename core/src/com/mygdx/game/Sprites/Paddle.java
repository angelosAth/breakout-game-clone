package com.mygdx.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Screens.GameScreen;

/**
 * Created by angel on 8/3/2016.
 */
public class Paddle{

    private GameScreen screen;
    public Rectangle paddle;


    public Paddle (GameScreen screen){
        this.screen = screen;

        paddle = new Rectangle();

        //load the paddle texture
        screen.paddleImage = new Texture(Gdx.files.internal("paddle.png"));

        //initial position and the dimension of the paddle
        paddle.x = 800 / 2 - 64 / 2;
        paddle.y = 20;
        paddle.width = 64;
        paddle.height = 16;
    }
}
