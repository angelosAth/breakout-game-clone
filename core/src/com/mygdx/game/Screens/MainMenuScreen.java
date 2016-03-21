package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.mygdx.game.MyGame;
import static com.mygdx.game.Util.Constants.*;


/**
 * Created by angel on 6/3/2016.
 */
public class MainMenuScreen implements Screen {
    //reference to our game
    private MyGame myGame;
    OrthographicCamera cam;

    public MainMenuScreen(MyGame myGame) {
        this.myGame = myGame;
        //create the camera
        cam = new OrthographicCamera(HEIGHT, WIDTH);
        cam.setToOrtho(false, HEIGHT, WIDTH);

    }

    @Override
    public void render(float delta) {
        // clear the screen with
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // tell the camera to update its matrices.
        cam.update();
        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        myGame.batch.setProjectionMatrix(cam.combined);

        // begin a new batch and draw the welcome
        // words
        myGame.batch.begin();
        myGame.font.draw(myGame.batch, "Welcome to My Game!!! ", 300, 380);
        myGame.font.draw(myGame.batch, "Tap anywhere to begin!", 300, 300);
        myGame.batch.end();
        // process user input
        if (Gdx.input.isTouched()) {
            myGame.setScreen(new GameScreen(myGame));
            dispose();
        }
    }


    @Override
    public void show() {

    }


    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
