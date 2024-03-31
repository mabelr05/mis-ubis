package ec.edu.tecnologicoloja.misubis;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import ec.edu.tecnologicoloja.misubis.database.Sitios;
import ec.edu.tecnologicoloja.misubis.database.SitiosLab;

public class viewNegociosActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout btn_back;
    private TextView txt_Name, txt_Address, txt_Email, txt_Phone, txt_propie;
    private ImageView img_photo;
    private SitiosLab mSitiosLab;
    private Sitios mSitios;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_negocios);

        mSitiosLab = new SitiosLab(this);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        mSitios = mSitiosLab.getSitio(id);

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);

        txt_Name = findViewById(R.id.txtName);
        txt_Address = findViewById(R.id.txtDir);
        txt_Email = findViewById(R.id.txtEmail);
        txt_Phone = findViewById(R.id.txtPhone);
        txt_propie = findViewById(R.id.txtPropietario);

        img_photo = findViewById(R.id.img_profile);

        if (mSitios == null) {
            Toast.makeText(this, "Negocio no Existe", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            txt_Name.setText(mSitios.getNombre());
            txt_Address.setText(mSitios.getDireccion());
            txt_Email.setText(mSitios.getEmail());
            txt_Phone.setText(mSitios.getContacto());
            txt_propie.setText(mSitios.getPropietario());

            if (mSitios == null) {
                Toast.makeText(this, "Negocio no Existe", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                txt_Name.setText(mSitios.getNombre());
                txt_Address.setText(mSitios.getDireccion());
                txt_Email.setText(mSitios.getEmail());
                txt_Phone.setText(mSitios.getContacto());
                txt_propie.setText(mSitios.getPropietario());

                // Configurar imagen con iniciales incluso si la foto no existe
                if (mSitios.getFoto() == null) {
                    String nombre = mSitios.getNombre();
                    if (nombre.length() >= 2) {
                        String initials = nombre.substring(0, 2);

                        int backgroundColor = Color.parseColor("#FFC107"); // Color de fondo amarillo
                        int textColor = Color.WHITE; // Color del texto blanco
                        Bitmap initialsImage = generateInitialsImage(initials, 50, 50, backgroundColor, textColor);
                        img_photo.setImageBitmap(initialsImage);
                    }
                } else {
                    // Si la foto existe, configura la imagen de la foto
                    // Cargar imagen desde la dirección almacenada usando Glide
                    Glide.with(this)
                            .load(mSitios.getFoto()) // Cargar la imagen desde la dirección
                            .into(img_photo); // Establecer la imagen en el ImageView

                }
            }


        }


    }

    @Override
    public void onClick(View v) {
        if (btn_back == v) {
            finish();
        }
    }

    private Bitmap generateInitialsImage(String text, int width, int height, int backgroundColor, int textColor) {
        Bitmap imageBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(imageBitmap);

        Paint backgroundPaint = new Paint();
        backgroundPaint.setColor(backgroundColor);
        canvas.drawPaint(backgroundPaint);

        Paint textPaint = new Paint();
        textPaint.setColor(textColor);
        textPaint.setTextSize(24); // Tamaño del texto en píxeles
        textPaint.setAntiAlias(true);

        // Centra el texto en el canvas
        Rect textBounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), textBounds);
        float x = (canvas.getWidth() - textBounds.width()) / 2;
        float y = (canvas.getHeight() + textBounds.height()) / 2;

        canvas.drawText(text, x, y, textPaint);

        return imageBitmap;
    }

}