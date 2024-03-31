package ec.edu.tecnologicoloja.misubis.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import ec.edu.tecnologicoloja.misubis.R;
import ec.edu.tecnologicoloja.misubis.database.Sitios;

public class ListAdapter extends ArrayAdapter<Sitios> {

    private final Context context;  // El contexto de la aplicación
    private final ArrayList<Sitios> list;  // Lista de objetos Sitios que se mostrarán en la vista

    // Constructor de la clase ListAdapter
    public ListAdapter(Context context, ArrayList<Sitios> list) {
        super(context, R.layout.content_list);  // Llama al constructor de ArrayAdapter con el layout de los elementos
        this.context = context;  // Inicializa el contexto
        this.list = list;  // Inicializa la lista de objetos Sitios
    }

    // Devuelve la cantidad de elementos en la lista
    @Override
    public int getCount() {
        return list.size();
    }

    // Método que se llama para obtener la vista de un elemento en una posición
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            // Si no hay vista reutilizable, se infla el layout de contenido de lista
            convertView = LayoutInflater.from(context).inflate(R.layout.content_list, parent, false);

            // Se crea un nuevo ViewHolder y se asocia a los elementos de la vista
            viewHolder = new ViewHolder();
            viewHolder.vItemName = convertView.findViewById(R.id.textNombre);
            viewHolder.vItemImage = convertView.findViewById(R.id.img);
            viewHolder.vItemPhone = convertView.findViewById(R.id.textDireccion);
            convertView.setTag(viewHolder);  // Se almacena el ViewHolder en la vista para reutilización
        } else {
            viewHolder = (ViewHolder) convertView.getTag();  // Si la vista se puede reutilizar, se obtiene el ViewHolder
        }

        Sitios sitio = list.get(position);  // Obtiene el objeto Sitios en la posición actual
        viewHolder.vItemName.setText(sitio.getNombre());  // Asigna el nombre del sitio al TextView
        viewHolder.vItemPhone.setText(sitio.getDireccion());  // Asigna la dirección del sitio al TextView

        String fotoUri = sitio.getFoto();  // Obtiene la URI de la foto del sitio
        if (fotoUri != null && !fotoUri.isEmpty()) {
            // Si la URI de la foto no es nula ni está vacía, carga la imagen usando Glide
            Glide.with(context)
                    .load(fotoUri)
                    .into(viewHolder.vItemImage);
        } else {
            // Si la URI de la foto es nula o está vacía, crea una imagen de iniciales y la asigna al ImageView
            String nombre = sitio.getNombre();
            if (nombre.length() >= 2) {
                String initials = nombre.substring(0, 2);
                Bitmap initialsImage = generateInitialsImage(initials, 50, 50, Color.parseColor("#FFC107"), Color.WHITE);
                viewHolder.vItemImage.setImageBitmap(initialsImage);
            }
        }
        return convertView;  // Devuelve la vista para mostrar en la lista
    }

    // Clase ViewHolder para mantener referencias a los elementos de la vista
    static class ViewHolder {
        TextView vItemName;
        ImageView vItemImage;
        TextView vItemPhone;
    }

    // Método para generar una imagen de iniciales en un Bitmap
    private Bitmap generateInitialsImage(String text, int width, int height, int backgroundColor, int textColor) {
        Bitmap imageBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(imageBitmap);

        Paint backgroundPaint = new Paint();
        backgroundPaint.setColor(backgroundColor);
        canvas.drawPaint(backgroundPaint);

        Paint textPaint = new Paint();
        textPaint.setColor(textColor);
        textPaint.setTextSize(24);
        textPaint.setAntiAlias(true);

        Rect textBounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), textBounds);
        float x = (canvas.getWidth() - textBounds.width()) / 2;
        float y = (canvas.getHeight() + textBounds.height()) / 2;

        canvas.drawText(text, x, y, textPaint);

        return imageBitmap;
    }
}