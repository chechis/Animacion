package com.chechis.multimedia;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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

    private Bitmap bitmap = null;
    private ImageView imageView;
    private Button camara;
    private String nombreArchivo = "";
    private static final int REQUEST_CODE = 1;
    private int numMostrar = 0;

    private static final String FOLDER = "/Examen/";
    private static final String FORMATO = ".jpg";

    private TextView rutaTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagen);


        imageView = (ImageView) findViewById(R.id.image_imagen);
        camara = (Button) findViewById(R.id.btn_imagen_iniciar);
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
                startActivityForResult(i,2);
            }
        });
    }

    protected  void onActivityResult(int requestCode, int resultCode, Intent Data){
        super.onActivityResult(requestCode,resultCode,Data);

        if(requestCode ==2 && resultCode==RESULT_OK && null !=Data){
            Uri imagenseleccionada = Data.getData();
            String[] path = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(imagenseleccionada,path,null,null,null);
            cursor.moveToFirst();
            int columna = cursor.getColumnIndex(path[0]);
            String pathimagen = cursor.getString(columna);
            cursor.close();
            bitmap = BitmapFactory.decodeFile(pathimagen);
            BitmapFactory.Options options= new BitmapFactory.Options();
            int height= bitmap.getHeight();
            int width=bitmap.getWidth();
            float scaleA =((float)(width/2))/width;
            float scaleB =((float)(height/2))/height;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleA,scaleB);
            Bitmap nuevaimagen= Bitmap.createBitmap(bitmap,0,0,width,height,matrix,true);
            imageView.setImageBitmap(nuevaimagen);

            String ruta = Environment.getExternalStorageDirectory()+ FOLDER+
                    nombreArchivo+ FORMATO;
            rutaTxt.setText(ruta);


        }else {
            Toast.makeText(this, "Ocurrió un error", Toast.LENGTH_SHORT).show();
        }

    }

    private void guardarFoto (){
        nombreArchivo = "foto";
        Intent camaraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File imagenFolder = new File(Environment.getExternalStorageDirectory(), FOLDER);
        imagenFolder.mkdirs();

        File imagen = new File(imagenFolder, nombreArchivo + FORMATO);
        Uri uriImagen = Uri.fromFile(imagen);

        camaraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriImagen);
        startActivityForResult(camaraIntent, REQUEST_CODE);

    }

}
