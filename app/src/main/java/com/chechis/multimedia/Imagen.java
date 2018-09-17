package com.chechis.multimedia;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class Imagen extends AppCompatActivity {

    private static final String[] PERMISOS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private Bitmap bitmap = null;
    private ImageView imageView;
    private static final int REQUEST_CODE = 1;


    private String nombreArchivo = "foto";

    private int numMostrar = 0;

    private static final String FOLDER = "/Examen/";
    private static final String FORMATO = ".jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        int leer = ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(leer == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, PERMISOS, REQUEST_CODE);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagen);


        imageView = (ImageView) findViewById(R.id.image_imagen);
        Button camara = (Button) findViewById(R.id.btn_imagen_iniciar);
        Button button = (Button) findViewById(R.id.btn_imagen_abrir);

        camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarFoto();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i,REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            //mostrarFoto();
            Toast.makeText(this, "Se ha guardado la imagen", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Ocurrio un error al guardar la imagen", Toast.LENGTH_SHORT).show();
        }

    }

    private void guardarFoto (){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File imagenFolder = new File(Environment.getExternalStorageDirectory(), "CamaraFolder");
        imagenFolder.mkdirs();

        File imagen = new File(imagenFolder, "captura.jpg");

        Uri uriImagen = Uri.fromFile(imagen);

        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriImagen);

        startActivityForResult(cameraIntent, REQUEST_CODE);

    }

    private void mostrarFoto (){

        String ruta = Environment.getExternalStorageDirectory()+
                FOLDER + nombreArchivo + FORMATO;

        Toast.makeText(this, "Se ha guardado la imagen:\n" + ruta, Toast.LENGTH_LONG).show();

        Bitmap bitmap = BitmapFactory.decodeFile(ruta);

        int height = bitmap.getHeight();
        int width = bitmap.getWidth();

        float scaleA = ((float)(width/2))/width;
        float scaleB = ((float)(height/2))/height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleA,scaleB);

        Bitmap nuevaImagen = Bitmap.createBitmap(bitmap,0,0,width,height,matrix,true);


        imageView.setImageBitmap(nuevaImagen);


    }
}
