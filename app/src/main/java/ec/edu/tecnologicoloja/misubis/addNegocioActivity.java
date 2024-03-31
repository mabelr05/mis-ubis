package ec.edu.tecnologicoloja.misubis;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import ec.edu.tecnologicoloja.misubis.adapter.ListAdapter;
import ec.edu.tecnologicoloja.misubis.database.Sitios;
import ec.edu.tecnologicoloja.misubis.database.SitiosLab;

public class addNegocioActivity extends AppCompatActivity implements View.OnClickListener,OnMapReadyCallback  {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 2;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private MapView mapView;
    private GoogleMap googleMap;
    private Marker centerMarker;

    private double selectedLatitude;
    private double selectedLongitude;



    // Declaraciones de variables para elementos de la interfaz de usuario
    private LinearLayout btnBack, btnSave;
    private Sitios mSitios;
    private SitiosLab mSitiosLab;
    private ListAdapter listItemAdapter;
    private ListView listView;
    private ImageView img_photo;
    private EditText etNombre, etPhone, etAddress, etEmail, etPropietario;

    // Variables para almacenar la ruta de la foto actual y el objeto Bitmap actual
    private String currentPhotoPath;
    private Bitmap currentBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_negocio);

        // Obtener referencias a los elementos de la interfaz de usuario
        btnBack = findViewById(R.id.btn_back);
        btnSave = findViewById(R.id.btn_save);
        etNombre = findViewById(R.id.et_name);
        etPhone = findViewById(R.id.et_phone);
        etAddress = findViewById(R.id.et_address);
        etEmail = findViewById(R.id.et_email);
        etPropietario = findViewById(R.id.et_propietario);
        img_photo = findViewById(R.id.img_profile);

        // Configurar listeners para los botones y la imagen
        btnSave.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        img_photo.setOnClickListener(this);

        // Inicializar el adaptador con una lista vacía
        listItemAdapter = new ListAdapter(this, new ArrayList<>());

        // Establecer el foco inicial en el campo de nombre
        etNombre.requestFocus();

        mapView = findViewById(R.id.mapView2);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        Log.e(TAG, "onMapReady: "+map.getCameraPosition());

        // Configura el botón de mi ubicación
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);

            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        //googleMap.addMarker(new google.maps.Marker);

        // Centra el mapa en la posición actual del GPS al abrir la actividad
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);

            return;
        }
        Location lastKnownLocation = locationManager.getLastKnownLocation(Objects.requireNonNull(locationManager.getBestProvider(criteria, false)));

        if (lastKnownLocation != null) {
            LatLng currentLatLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
        }

        // Configura el listener para obtener la dirección en movimiento del mapa
        googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                LatLng centerLatLng = googleMap.getCameraPosition().target;
                updateAddressFromLatLng(centerLatLng);
                //googleMap.addMarker(new MarkerOptions().position(centerLatLng));
                selectedLatitude = centerLatLng.latitude;
                selectedLongitude = centerLatLng.longitude;
                if (centerMarker != null) {
                    centerMarker.remove();
                }
                centerMarker = googleMap.addMarker(new MarkerOptions().position(centerLatLng));
            }
        });
    }

    // Resto de los métodos y funciones

    private void updateAddressFromLatLng(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            assert addresses != null;
            if (!addresses.isEmpty()) {
                String addressLine = addresses.get(0).getAddressLine(0);
                etAddress.setText(addressLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        if (btnBack == v) {
            finish(); // Cierra la actividad actual y vuelve a la actividad anterior
            clearCampos(); // Limpia los campos de entrada
        } else if (btnSave == v) {
            insertNegocio(); // Llama al método para agregar un nuevo negocio
        } else if (img_photo == v) {
            checkCameraPermission(); // Verifica los permisos de la cámara y captura una foto
        }
    }

    // Función para limpiar los campos de entrada
    public void clearCampos() {
        etPropietario.setText("");
        etEmail.setText("");
        etNombre.setText("");
        etAddress.setText("");
        etPropietario.setText("");
    }

    // Función para agregar un nuevo negocio a la base de datos
    public void insertNegocio() {
        // Crea un nuevo objeto Sitios con los valores ingresados en los campos
        mSitios = new Sitios();
        mSitios.setNombre(etNombre.getText().toString());
        mSitios.setContacto("+593" + etPhone.getText().toString());
        mSitios.setDireccion(etAddress.getText().toString());
        mSitios.setEmail(etEmail.getText().toString());
        mSitios.setPropietario(etPropietario.getText().toString());
        mSitios.setFoto(currentPhotoPath);
        mSitios.setLatitud(selectedLatitude);
        mSitios.setLongitud(selectedLongitude);

        // Obtiene la instancia de SitiosLab para interactuar con la base de datos
        mSitiosLab = SitiosLab.get(this);

        // Agrega el negocio a la base de datos
        mSitiosLab.addSitio(mSitios);

        // Notifica al adaptador que los datos han cambiado
        listItemAdapter.notifyDataSetChanged();

        // Prepara el resultado para enviar de vuelta a MainActivity
        Intent resultIntent = new Intent();
        resultIntent.putExtra("newNegocioAdded", true);
        setResult(RESULT_OK, resultIntent); // Establece el resultado de la actividad

        // Limpia los campos de entrada y finaliza la actividad actual
        clearCampos();
        finish();
    }

    // Función para verificar los permisos de la cámara
    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Si no se tienen los permisos, se solicitan
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            // Si se tienen los permisos, se captura una foto
            dispatchTakePictureIntent();
        }
    }

    // Función para capturar una foto utilizando la cámara
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                // Crea un archivo donde se guardará la foto
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e("Error", ex.getMessage());
            }
            if (photoFile != null) {
                currentPhotoPath = photoFile.getAbsolutePath();
                // Obtiene la URI del archivo utilizando un FileProvider
                Uri photoURI = FileProvider.getUriForFile(this, "ec.edu.tecnologicoloja.listApplication.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    // Función para crear un archivo de imagen
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_QuickDev";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(storageDir, imageFileName + ".jpg");
    }

    // Función que se llama cuando se ha capturado una foto
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Carga la foto capturada en el ImageView utilizando Glide
            Glide.with(this).load(currentPhotoPath).into(img_photo);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume(); // Importante: manejar el ciclo de vida del MapView
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause(); // Importante: manejar el ciclo de vida del MapView
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy(); // Importante: manejar el ciclo de vida del MapView
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory(); // Importante: manejar el ciclo de vida del MapView
    }
}