package com.boom3.mines;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;

import static java.lang.Math.abs;
import static java.lang.Math.floor;

public class GameScreen implements Screen, GestureDetector.GestureListener {
    final MainActivity game;
    OrthographicCamera camera;
    SpriteBatch batch;
    Minesweeper field;
    Texture mine;
    Texture back;
    Texture fld, checkedfld;
    Texture i0,i1,i2,i3,i4,i5,i6,i7,i8,i9;
    Texture imgButtonLeft;
    Texture imgButtonRight;
    Texture failFlag, flag, checkedflag;
    Array<Field> rects;
    Field tempField;
    Rectangle buttonLeft;
    Rectangle buttonRight;
    BitmapFont font;
    boolean firstTouch = true;
    boolean isCreateField = true;
    boolean isButton = false;
    boolean lose = false;
    boolean win = false;
    Vector3 touchPosition;
    final int sizeMines;
    final int mines;
    final int width = 720;
    final int height = 1200;
    final int xMines;
    final int yMines;
    final double ZOOM_DELTA = 0.01f;
    final double ZOOM_MAX = 1;
    final double ZOOM_MIN = 0.2;
    final int UP = 1;
    final int RIGHT = 2;
    final int DOWN = 3;
    final int LEFT = 4;
    final int UPRIGHT = 5;
    final int DOWNRIGHT = 6;
    final int DOWNLEFT = 7;
    final int UPLEFT = 8;

    public GameScreen(MainActivity gameScreen) {
        this.game = gameScreen;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, width, height);
        mines = MainMenuScreen.pMines;
        switch (mines){
            case 10:
                sizeMines = 92;
                break;
            case 20:
                sizeMines = 73;
                break;
            case 30:
                sizeMines = 65;
                break;
            case 40:
                sizeMines = 60;
                break;
            case 50:
                sizeMines = 58;
                break;
            case 60:
                sizeMines = 55;
                break;
            case 70:
                sizeMines = 52;
                break;
            case 80:
                sizeMines = 50;
                break;
            case 90:
                sizeMines = 48;
                break;
            case 100:
                sizeMines = 45;
                break;
            default:
                sizeMines = 10;
                break;
        }
        xMines = width / sizeMines;
        yMines = height / sizeMines;
        field = new Minesweeper(xMines,yMines,mines, 0, 0);
        font = new BitmapFont();
        back = new Texture("back.jpg");
        mine = new Texture("mine.png");
        fld = new Texture("rect.png");
        checkedfld = new Texture("checkedrect.png");
        i0 = new Texture("0.png");
        i1 = new Texture("1.png");
        i2 = new Texture("2.png");
        i3 = new Texture("3.png");
        i4 = new Texture("4.png");
        i5 = new Texture("5.png");
        i6 = new Texture("6.png");
        i7 = new Texture("7.png");
        i8 = new Texture("8.png");
        i9 = new Texture("9.png");
        imgButtonLeft = new Texture("buttonleft.png");
        imgButtonRight = new Texture("buttonright.png");
        flag = new Texture("flag.png");
        checkedflag = new Texture("checkedflag.png");
        failFlag = new Texture("failflag.png");
        rects = new Array<Field>();
        for(int i = 0; i < xMines; i++){
            for(int j = 0; j < yMines; j++){
                rects.add(new Field(field.getField()[i][j],i * width / xMines, j * height / yMines, width / xMines, height / yMines));
            }
        }
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        camera.zoom = 1;

        touchPosition = new Vector3();
        Gdx.input.setInputProcessor(new GestureDetector(this));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(247, 247, 245, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for(Field f : rects){
            if(f.view ) {
                openFields(f, rects);
            } else {
                if(!f.flag) {
                    batch.draw(fld, f.rect.x, f.rect.y, f.rect.width, f.rect.height);
                } else {
                    if((f.value != -1) && (win || lose)) {
                        batch.draw(failFlag, f.rect.x, f.rect.y, f.rect.width, f.rect.height);
                    } else {
                        batch.draw(flag, f.rect.x, f.rect.y, f.rect.width, f.rect.height);
                    }
                }
            }
        }
        if(isButton){
            if(tempField.flag){
                batch.draw(checkedflag, tempField.rect.x, tempField.rect.y, tempField.rect.width, tempField.rect.height);
            } else {
                batch.draw(checkedfld, tempField.rect.x, tempField.rect.y, tempField.rect.width, tempField.rect.height);
            }
            batch.draw(imgButtonLeft, buttonLeft.x, buttonLeft.y, buttonLeft.width, buttonLeft.height);
            batch.draw(imgButtonRight, buttonRight.x, buttonRight.y, buttonRight.width, buttonRight.height);
        }

        for(Field f : rects){
            win = true;
            if(f.value != -1){
                if(!f.view){
                    win = false;
                    break;
                }
            }
        }
        if(win || lose){
            font.getData().setScale(4, 4);
            if(win) {
                font.draw(batch, "You win.", width / 2, height / 2);
            }
            if(lose) {
                font.draw(batch, "Game over.", width / 2, height / 2);
            }
            openAllField();
            if (Gdx.input.isTouched(0))
                Timer.schedule(new Timer.Task(){
                    @Override
                    public void run() {
                        game.setScreen(new MainMenuScreen(game));
                        dispose();
                    }
                }, 0.5f);
        }

        batch.end();

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
    public void dispose () {
        batch.dispose();
        mine.dispose();
        back.dispose();
    }


    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        if(firstTouch) {
            firstTouch = false;
        } else {
            System.out.println("Tap");
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(0), Gdx.input.getY(0), 0);
            camera.unproject(touchPos);

            if(!isButton) {
                for (Field r : rects) {
                    if (r.rect.contains(touchPos.x, touchPos.y) && !r.view && !(win || lose)) {
                        //r.view = true;

                        if(isCreateField){
                            System.out.println("RECREATE: " + (rects.indexOf(r, true) / yMines) + " " + (rects.indexOf(r, true) % yMines));
                            field = new Minesweeper(xMines, yMines, mines, rects.indexOf(r, true) / yMines, rects.indexOf(r, true) % yMines);
                            field.print();
                            int num = rects.indexOf(r, true);
                            rects.removeRange(0, rects.size - 1);
                            for(int i = 0; i < xMines; i++){
                                for(int j = 0; j < yMines; j++){
                                    rects.add(new Field(field.getField()[i][j],i * width / xMines, j * height / yMines, width / xMines, height / yMines));
                                }
                            }
                            r = rects.get(num);
                            isCreateField = false;
                        }


                        System.out.println("TapRect");
                        int xL = (int) (r.rect.x - r.rect.width / 2);
                        if(xL < 0)
                            xL = 0;
                        while(xL + 2 * r.rect.width > width)
                            xL -= r.rect.width / 2;
                        int yL = (int) (r.rect.y + r.rect.height);
                        if(yL < 0)
                            yL = 0;
                        while(yL + r.rect.height > height)
                            yL -= r.rect.height;
                        buttonLeft = new Rectangle(xL, yL, r.rect.width, r.rect.height);
                        buttonRight = new Rectangle(xL + r.rect.width, yL, r.rect.width, r.rect.height);;
                        isButton = true;
                        tempField = r;
                        System.out.println("TapRectoff");
                    }
                }
            } else {
                System.out.println("Tap2");

                if(buttonLeft.contains(touchPos.x, touchPos.y)){
                        tempField.view = true;
                        if (tempField.value == -1) {
                            lose = true;
                        }
                }

                if(buttonRight.contains(touchPos.x, touchPos.y)){
                    if(tempField.flag)
                        tempField.flag = false;
                    else
                        tempField.flag = true;
                }
                isButton = false;

            }
            System.out.println(touchPos.x + "  tch  " + touchPos.y + " " + xMines + " " + yMines);
        }
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
        if(camera.zoom < ZOOM_MAX) {
            if(((camera.position.x - (deltaX + width / 2) * camera.zoom  > 0) && (camera.position.x - (deltaX - width / 2) * camera.zoom < width))
                && ((camera.position.y + (deltaY - height / 2) * camera.zoom > 0) && (camera.position.y + (deltaY + height / 2) * camera.zoom < height))) {
                camera.translate(-deltaX * camera.zoom, deltaY * camera.zoom);
                camera.update();
            }
        }
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        if (initialDistance >= distance) {
            if(camera.zoom < ZOOM_MAX){
                camera.zoom += ZOOM_DELTA; //уменьшение
                if(camera.position.x + (width / 2)* camera.zoom  >= width)
                    camera.position.x = width * (-0.3755f * camera.zoom + 0.875f);
                if(camera.position.y + (height / 2)* camera.zoom  >= width)
                    camera.position.y = height * (-0.3755f * camera.zoom + 0.875f);
                if(camera.position.x - (width / 2)* camera.zoom <= 0)
                    camera.position.x = width * (-0.3755f * camera.zoom + 0.875f);
                if(camera.position.y - (height / 2)* camera.zoom  <= 0)
                    camera.position.y = height * (-0.3755f * camera.zoom + 0.875f);
            }
        } else {
            if(camera.zoom > ZOOM_MIN) {
                camera.zoom -= ZOOM_DELTA; //увеличение
            }
        }
        camera.update();
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }


    void openFields(Field f, Array<Field> rects){
        switch (f.value){
            case -1:
                batch.draw(mine, f.rect.x, f.rect.y, f.rect.width, f.rect.height);
                break;
            case 0:
                batch.draw(i0, f.rect.x, f.rect.y, f.rect.width, f.rect.height);
                int index = rects.indexOf(f, true);
                int y = index % yMines;
                int x =  index / yMines;
                if(y != 0)
                    open(index - 1, rects);
                if(y != yMines - 1)
                    open(index + 1, rects);
                if(y != 0 )
                    open(index - yMines - 1, rects);
                open(index - yMines, rects);
                if(y != yMines - 1)
                    open(index - yMines + 1, rects);
                if(y != 0)
                    open(index + yMines - 1, rects);
                open(index + yMines, rects);
                if(y != yMines - 1)
                    open(index + yMines + 1, rects);
                break;
            case 1:
                batch.draw(i1, f.rect.x, f.rect.y, f.rect.width, f.rect.height);
                break;
            case 2:
                batch.draw(i2, f.rect.x, f.rect.y, f.rect.width, f.rect.height);
                break;
            case 3:
                batch.draw(i3, f.rect.x, f.rect.y, f.rect.width, f.rect.height);
                break;
            case 4:
                batch.draw(i4, f.rect.x, f.rect.y, f.rect.width, f.rect.height);
                break;
            case 5:
                batch.draw(i5, f.rect.x, f.rect.y, f.rect.width, f.rect.height);
                break;
            case 6:
                batch.draw(i6, f.rect.x, f.rect.y, f.rect.width, f.rect.height);
                break;
            case 7:
                batch.draw(i7, f.rect.x, f.rect.y, f.rect.width, f.rect.height);
                break;
            case 8:
                batch.draw(i8, f.rect.x, f.rect.y, f.rect.width, f.rect.height);
                break;
            case 9:
                batch.draw(i9, f.rect.x, f.rect.y, f.rect.width, f.rect.height);
                break;

        }
    }

    void open(int i, Array<Field> rect){
        if((i > 0) && (i < rect.size))
            if(rect.get(i) != null){
                if((rect.get(i).value != -1) && !(rect.get(i).flag))
                    rect.get(i).view = true;
            }
    }

    void openAllField(){
        for(Field f : rects){
            if(!f.flag) {
                f.view = true;
            }
        }
    }
}
