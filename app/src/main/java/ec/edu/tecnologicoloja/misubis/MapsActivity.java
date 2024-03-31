package ec.edu.tecnologicoloja.misubis;

import androidx.fragment.app.FragmentActivity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ec.edu.tecnologicoloja.misubis.database.Sitios;
import ec.edu.tecnologicoloja.misubis.database.SitiosLab;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapNegocios);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Obtener los sitios guardados en la base de datos
        SitiosLab sitiosLab = SitiosLab.get(this);
        List<Sitios> sitiosList = sitiosLab.getSitios();

        // Cargar las imágenes en Bitmap utilizando AsyncTask
        for (Sitios sitio : sitiosList) {
            LoadBitmapTask loadBitmapTask = new LoadBitmapTask(sitio, mMap, sitiosList);
            loadBitmapTask.execute();
            Log.d("Positions", "Adding position: " + sitio.getLatitud() + ", " + sitio.getLongitud());
        }


        // Mover la cámara para centrarse en la primera ubicación
       /* if (!sitiosList.isEmpty()) {
            LatLng firstLocation = new LatLng(sitiosList.get(0).getLatitud(), sitiosList.get(0).getLongitud());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLocation, 12));
        }*/
    }
    private void centerAndZoomMap(List<LatLng> positions) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng position : positions) {
            builder.include(position);
        }
        LatLngBounds bounds = builder.build();

        int padding = 100; // Espacio en píxeles desde los bordes del mapa
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.animateCamera(cu);
    }

    private class LoadBitmapTask extends AsyncTask<Void, Void, Bitmap> {
        private Sitios sitio;
        private GoogleMap map;
        private List<LatLng> positions = new ArrayList<>();
        private List<Sitios> sitiosList;
        ;

        public LoadBitmapTask(Sitios sitio, GoogleMap map, List<Sitios> sitiosList) {
            this.sitio = sitio;
            this.map = map;
            this.sitiosList = sitiosList;
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            try {
                return Glide.with(MapsActivity.this)
                        .asBitmap()
                        .load(sitio.getFoto())
                        .submit()
                        .get();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                // Redimensionar y redondear la imagen
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
                Bitmap roundedBitmap = getRoundedBitmap(resizedBitmap);

                LatLng ubicacion = new LatLng(sitio.getLatitud(), sitio.getLongitud());
                positions.add(ubicacion);
                // Crear un BitmapDescriptor directamente desde el Bitmap redondeado
                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(roundedBitmap);

                MarkerOptions markerOptions = new MarkerOptions()
                        .position(ubicacion)
                        .title(sitio.getNombre())
                        .snippet(sitio.getContacto())
                        .icon(bitmapDescriptor);

                map.addMarker(markerOptions);
            }

            // Verificar si se han procesado todos los marcadores
            if (!sitiosList.isEmpty()) {
                LatLng firstLocation = new LatLng(sitiosList.get(0).getLatitud(), sitiosList.get(0).getLongitud());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLocation, 14));
            }
        }

        private Bitmap getRoundedBitmap(Bitmap bitmap) {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final int color = Color.RED;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(rect);
            final float roundPx = 50; // Radio de la esquina redondeada en píxeles

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

            return output;
        }
    }
}