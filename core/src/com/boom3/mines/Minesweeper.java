package com.boom3.mines;

import static com.badlogic.gdx.math.MathUtils.random;
import static java.lang.Math.abs;

/**
 * Created by Олег on 22.10.2017.
 */

public class Minesweeper {
    private int[][] field = new int[100][100];
    final int MINE = -1;
    private int sizeX;
    private int sizeY;
    private int numMines;
    Minesweeper(int x, int y, int mines, int touchX, int touchY){
        sizeX = x;
        sizeY = y;
        numMines = mines;
        System.out.println(touchX + " sss " + (touchY));
        if((sizeX < 100) && (sizeY < 100)){
            pledge(sizeX, sizeY, numMines, touchX, touchY);
            //print();
            scatter(sizeX, sizeY);
        }
    }

    private void pledge(int sizeX, int sizeY, int numMines, int touchX, int touchY){
        int xMine, yMine;
        int mines = 0;
        for(mines = 0; mines < numMines; mines++) {
            xMine = random(0, sizeX - 1);
            yMine = random(0, sizeY - 1);
            if(abs(touchX - xMine) < 2 && abs(touchY - yMine) < 2) {
                System.out.println(xMine + " www " + yMine);
                mines--;
                continue;
            }
            if (field[xMine][yMine] != MINE) {
                field[xMine][yMine] = MINE;
                //System.out.println(xMine + " " + yMine);
            } else {
                mines--;
            }
        }
    }

    private void scatter(int sizeX, int sizeY){
        int encirclement = 0;
        for(int x = 0; x < sizeX; x++){
            for(int y = 0; y < sizeY; y++){
                if(field[x][y] != MINE) {
                    field[x][y] = minesAround(x, y, sizeX, sizeY);
                    //System.out.println(x + " " + y + " " + field[x][y]);
                }
            }
        }
    }

    private int minesAround(int x, int y, int sizeX, int sizeY){
        int around = 0;
        for(int i = x - 1; i <= x + 1; i++){
            for(int j = y - 1; j <= y + 1; j++){
                if(!((i == x) && (j == y))){
                    if(checkMine(i, j, sizeX, sizeY)){
                        around++;
                    }
                }
            }
        }
        return around;
    }

    private boolean checkMine(int x, int y, int sizeX, int sizeY){
        if((x >= 0) && (y >= 0) && (x < sizeX) &&(y < sizeY)) {
            if (field[x][y] == MINE) {
                return true;
            }
        }
        return false;
    }

    public void print(){
        for(int y = sizeY - 1; y >= 0; y--) {
            for (int x = 0; x < sizeX; x++) {
                System.out.print(field[x][y] + " ");
            }
            System.out.println();
        }
    }

    public int[][] getField() {
        return field;
    }
}

