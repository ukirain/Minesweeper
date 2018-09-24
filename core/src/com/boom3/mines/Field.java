package com.boom3.mines;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Олег on 27.10.2017.
 */

public class Field {
    int value;
    Rectangle rect;
    boolean view = false;
    boolean flag = false;
    Field(int Value, int x, int y, int width, int height){
        rect = new Rectangle(x, y, width, height);
        value = Value;
    }
}
