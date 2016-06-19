package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.screens.MainMenuScreen;

public class MyGame extends Game {

    // for all the screens
    public SpriteBatch batch;
    public BitmapFont font;

    private boolean paused;

    @Override
    public void create () {
        batch = new SpriteBatch();
        //Use LibGDX's default Arial font.
        font = new BitmapFont();
        paused = false;
        setScreen(new MainMenuScreen(this));
    }

    @Override
    public void render () {
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) paused = paused ? false : true;

        if(!paused) {
            super.render();
        }
        else {
            this.batch.begin();
            this.font.draw(this.batch, "PAUSED", 370, 250);
            this.batch.end();
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        font.dispose();
    }
}