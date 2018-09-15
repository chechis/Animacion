package com.chechis.multimedia;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.chechis.multimedia.graficos.Renderer;

public class Graficos extends AppCompatActivity {

    private GLSurfaceView lienzo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lienzo = new GLSurfaceView(this);
        lienzo.setRenderer(new Renderer(this));

        this.setContentView(lienzo);
    }
}
