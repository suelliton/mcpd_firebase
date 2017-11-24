package com.tads.eaj.ufrn.mcpd.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tads.eaj.ufrn.mcpd.R;

/**
 * Created by Aluno on 24/11/2017.
 */

public class Galeria {
    String name;
    Bitmap img_01,img_02,img_03,img_04,img_05;

    public Galeria(String name, Context context){
        this.name = name;
        img_01 = BitmapFactory.decodeResource(context.getResources(), R.drawable.a);
        img_02 = BitmapFactory.decodeResource(context.getResources(), R.drawable.b);
        img_03 = BitmapFactory.decodeResource(context.getResources(), R.drawable.c);
        img_04 = BitmapFactory.decodeResource(context.getResources(), R.drawable.d);
        img_05 = BitmapFactory.decodeResource(context.getResources(), R.drawable.e);
    }

    public Bitmap getImage(int img){
        switch (img){
            case 1:
                return img_01;
            case 2:
                return img_02;
            case 3:
                return img_03;
            case 4:
                return img_04;
            case 5:
                return img_05;
            default:
                return img_01;
        }
    }
}
