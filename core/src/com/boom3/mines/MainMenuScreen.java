package com.boom3.mines;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Nai on 20.08.2017.
 */

public class MainMenuScreen implements Screen, GestureDetector.GestureListener {
    public static int pMines = 30;
    final MainActivity game;
    OrthographicCamera camera;
    Texture menu;
    Rectangle startRect, helpRect;
    Rectangle lArrowRect, rArrowRect;
    String difficult = Integer.toString(pMines);
    int width = 720;
    int height = 1200;
    public MainMenuScreen(final MainActivity gam) {
        game = gam;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, width, height);


        menu = new Texture("menu.png");
        startRect = new Rectangle(36, 406, 654, 158);
        lArrowRect = new Rectangle(66, 620, 186, 166);
        rArrowRect = new Rectangle(478, 620, 186, 166);
        helpRect = new Rectangle(36, 188, 654, 158);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(247, 247, 245, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.getData().setScale(4, 4);
        game.batch.draw(menu, 0, 0, width, height);
        game.font.draw(game.batch, difficult, width / 2 - 40, lArrowRect.y + 100);
        game.batch.end();

    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new GestureDetector(this));
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        Vector3 touchPos = new Vector3();
        touchPos.set(Gdx.input.getX(0), Gdx.input.getY(0), 0);
        camera.unproject(touchPos);
        System.out.println(touchPos.x+ " x " + touchPos.y);
        if(startRect.contains(touchPos.x, touchPos.y)) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
        if(helpRect.contains(touchPos.x, touchPos.y)) {
            game.setScreen(new HelpScreen(game));
            dispose();
        }
        if(lArrowRect.contains(touchPos.x,touchPos.y)){
            pMines -= 10;
            if(pMines <= 0){
                pMines = 100;
            }
        }
        if(rArrowRect.contains(touchPos.x,touchPos.y)){
            pMines += 10;
            if(pMines > 100){
                pMines = 20;
            }
        }

        difficult = Integer.toString(pMines);
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }
}