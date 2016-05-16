package com.mygdx.game.Screens;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGame;
import com.mygdx.game.Sprites.Ball;
import com.mygdx.game.Sprites.Bricks;
import com.mygdx.game.Sprites.Paddle;
import static com.mygdx.game.Util.Constants.*;


/**
 * Created by angel on 6/3/2016.
 */
public class GameScreen implements Screen {

    private MyGame myGame;
    private ShapeRenderer shapeRenderer;
    private Bricks[] bricks;
    private Array<Bricks> brickses;
    private Paddle gamePaddle;
    private Ball gameBall;
    private float ballXmove;
    private float ballYmove;
    //the number of player lives
    private int lives = GAME_LIVES;
    //the number of destroyed bricks
    private int bricksDestroyed;
    //division of the paddle to three parts
    private int aPaddlePart;
    private int bPaddlePart;
    private int cPaddlePart;
    private int numOfBricks;

    public Texture paddleImage;
    public Texture brickImage;
    public Texture ballImage;

    Sound hitSound;
    Sound loseSound;
    Sound hitPaddleSound;
    Music music;

    OrthographicCamera cam;


    public GameScreen (MyGame myGame){
        this.myGame = myGame;
        gamePaddle = new Paddle(this);
        gameBall = new Ball(this);

        bricks = new Bricks[NUM_OF_BRICKS];
        numOfBricks = 0;
        brickses = new Array<Bricks>();
        //division of the paddle to three parts
        aPaddlePart = (int)gamePaddle.paddle.x;
        bPaddlePart = (int)gamePaddle.paddle.x + 21;
        cPaddlePart = (int)gamePaddle.paddle.x + 40;


        // load the drop sound effect and the background music
        hitSound = Gdx.audio.newSound(Gdx.files.internal("hit.wav"));
        hitPaddleSound = Gdx.audio.newSound(Gdx.files.internal("pop.wav"));
        loseSound = Gdx.audio.newSound(Gdx.files.internal("lose.wav"));
        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        music.setLooping(true);

        // create the camera
        cam = new OrthographicCamera(HEIGHT, WIDTH);
        cam.setToOrtho(false, HEIGHT, WIDTH);

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);

        ballXmove = MathUtils.random(-300, 300);
        ballYmove = 300;

        makeBricks();

    }


    @Override
    public void show() {
        // start the playback of the background music
        // when the screen is shown
        music.play();
    }

    @Override
    public void render (float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell the camera to update its matrices.
        cam.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        myGame.batch.setProjectionMatrix(cam.combined);

        // begin a new batch and draw the paddle, ball and
        // all bricks
        myGame.batch.begin();
        myGame.font.draw(myGame.batch, "Bricks Destroyed: " + bricksDestroyed, 0, 480);
        myGame.font.draw(myGame.batch, "Lives Left: " + lives, 700, 480);
        myGame.batch.draw(paddleImage, gamePaddle.paddle.x, gamePaddle.paddle.y);
        for (int i = 0; i < NUM_OF_BRICKS; i++) {
            if (!bricks[i].isDestroyed()) {
                myGame.batch.draw(brickImage, bricks[i].brick.x, bricks[i].brick.y);
            }
        }
        myGame.batch.draw(ballImage, gameBall.ball.x, gameBall.ball.y);
        myGame.batch.end();


        ApplicationType appType = Gdx.app.getType();

        // should work also with Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer)
        if (appType == ApplicationType.Android || appType == ApplicationType.iOS) {
            gamePaddle.paddle.x += Gdx.input.getAccelerometerY() * PADDLE_SPEED * delta;
        } else {
            // process user input
            if (Gdx.input.isTouched()) {
                Vector3 touchPos = new Vector3();
                touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                cam.unproject(touchPos);
                gamePaddle.paddle.x = touchPos.x - 64 / 2;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
                gamePaddle.paddle.x -= PADDLE_SPEED * delta;
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
                gamePaddle.paddle.x += PADDLE_SPEED * delta;
        }

        //if we lose all game lives go back to main screen
        if (lives < 0){
            myGame.setScreen(new MainMenuScreen(myGame));
            music.stop();
        }
        //we destroyed all the bricks so we put true for the boolean in the constructor
        if (numOfBricks < 1)
            myGame.setScreen(new MainMenuScreen(myGame, true));


        ballWallCollision();
        ballMovement(delta);
        paddleScreenBounds();
        debugRenderer();
        ballHitBrick();
        ballHitPaddle();
        checkBallSpeed();
    }

    //the direction of the ball after it hits the game borders
    //if goes below the paddle we lose a life, we
    //hear the lose sound and the ball starts over
    //the initial position
    private void ballWallCollision () {
        if (gameBall.ball.x < 0)
            ballXmove *= -1;
        if (gameBall.ball.x > 790)
            ballXmove *= -1;
        if (gameBall.ball.y < 0) {
            gameBall.initial();
            loseSound.play();
            lives--;
        }
        if (gameBall.ball.y > 470)
            ballYmove *= -1;
    }


    private void ballMovement(float delta){
        gameBall.ball.x += ballXmove * delta;
        gameBall.ball.y += ballYmove * delta;
    }

    private void paddleScreenBounds(){
        // make sure the paddle stays within the screen bounds
        if (gamePaddle.paddle.x < 1)
            gamePaddle.paddle.x = 1 ;
        if (gamePaddle.paddle.x > 800 - 64)
            gamePaddle.paddle.x = 800 - 64;
    }

    private void makeBricks(){
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 10; j++) {
                bricks[numOfBricks] = new Bricks(this, j * 70 + 60, i * 20 + 360);
                numOfBricks++;
            }
        }
    }

    private void debugRenderer(){
        // Tells shapeRenderer to draw an outline of the following shapes
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        // Chooses RGB Color of 255, 109, 120 at full opacity
        shapeRenderer.setColor(255 / 255.0f, 109 / 255.0f, 120 / 255.0f, 1);

        for (int i=0; i < NUM_OF_BRICKS; i++) {
            //Draws the brick if it is not destroyed
            if (!bricks[i].isDestroyed()) {
                shapeRenderer.rect(bricks[i].brick.x, bricks[i].brick.y,
                        bricks[i].brick.width, bricks[i].brick.height);
            }
        }
        shapeRenderer.end();
    }

    //the direction of the ball when hits a brick
    //the brick dies and we hear the crash sound
    private void ballHitBrick(){
        for (int i = 0; i < NUM_OF_BRICKS; i++) {
            if (Intersector.overlaps(gameBall.ball, bricks[i].brick)) {
                if (!bricks[i].isDestroyed()) {
                    bricks[i].setDestroyed();
                    bricksDestroyed++;
                    ballYmove *= -1;
                    hitSound.play();
                    numOfBricks--;
                }
            }
        }
    }

    // the direction of the ball when it hits the paddle
    //which depends on the part of the paddle it hits
    private void ballHitPaddle(){
        if (Intersector.overlaps(gameBall.ball, gamePaddle.paddle)) {
            if ((gameBall.ball.x) >= aPaddlePart && (gameBall.ball.x) < bPaddlePart){
                ballYmove *= -1;
                ballXmove -=150;
            }
            else if ((gameBall.ball.x) >= bPaddlePart && (gameBall.ball.x) < cPaddlePart){
                ballYmove *= -1;
            }
            else{
                ballYmove *= -1;
                ballXmove +=150;
            }
            hitPaddleSound.play();
        }
    }

    // limits the ball speed
    private void checkBallSpeed(){
        if (ballYmove > BALL_SPEED)
            ballYmove = BALL_SPEED;
        if (ballYmove < -BALL_SPEED)
            ballYmove = -BALL_SPEED;

        if (ballXmove > BALL_SPEED)
            ballXmove = BALL_SPEED;
        if (ballXmove < BALL_SPEED)
            ballXmove = -BALL_SPEED;

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
        ballImage.dispose();
        paddleImage.dispose();
        brickImage.dispose();
        hitSound.dispose();
        music.dispose();

    }


}
