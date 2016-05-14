package com.mygdx.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Screens.GameScreen;

/**
 * Created by angel on 8/3/2016.
 */
public class Bricks{

    private GameScreen screen;
    private boolean destroyed = false;

    public Rectangle brick;


    public Bricks (GameScreen screen, int x, int y){
        this.screen = screen;

        brick = new Rectangle();

        //load the brick texture
        screen.brickImage = new Texture(Gdx.files.internal("brick.png"));

        //the position and the dimensions of the brick
        brick.x = x;
        brick.y = y;
        brick.width = 64;
        brick.height = 16;
    }

    public boolean isDestroyed(){
        return destroyed;
    }
    //boolean variable for the destruction of the brick
    public void setDestroyed(){
        destroyed = true;

    }
}
