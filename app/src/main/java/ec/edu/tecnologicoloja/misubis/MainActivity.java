package ec.edu.tecnologicoloja.misubis;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import ec.edu.tecnologicoloja.misubis.adapter.ListAdapter;
import ec.edu.tecnologicoloja.misubis.database.Sitios;
import ec.edu.tecnologicoloja.misubis.database.SitiosLab;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ListAdapter listItemAdapter;
    private ArrayList<Sitios> listaNegocios = new ArrayList<>();
    private ListView listView;
    private SitiosLab mSitiosLab;
    private LinearLayout btnAddStore, btnChange;

    // Código para manejar el resultado de la actividad addNegocioActivity
    private ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    getAllNegocios(); // Actualizar la lista de negocios después de agregar uno nuevo
                    listItemAdapter.notifyDataSetChanged(); // Notificar al adaptador
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar la base de datos y la lista de negocios
        mSitiosLab = new SitiosLab(this);
        getAllNegocios();

        // Obtener referencias a los elementos de la interfaz de usuario
        listView = findViewById(R.id.contactList);
        btnAddStore = findViewById(R.id.btn_addStore);
        btnChange = findViewById(R.id.btn_change);

        // Configurar listeners para los botones y la lista
        btnAddStore.setOnClickListener(this);
        btnChange.setOnClickListener(this);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

        // Inicializar y establecer el adaptador de la lista de negocios
        listItemAdapter = new ListAdapter(this, listaNegocios);
        listView.setAdapter(listItemAdapter);
    }

    // Función para obtener todos los negocios de la base de datos
    public void getAllNegocios() {
        listaNegocios.clear();
        listaNegocios.addAll(mSitiosLab.getSitios());
        Log.i(TAG, listaNegocios + "");
    }

    @Override
    public void onClick(View v) {
        if (v == btnAddStore) {
            // Iniciar la actividad addNegocioActivity para agregar un nuevo negocio
            Intent intent = new Intent(this, addNegocioActivity.class);
            mStartForResult.launch(intent);
        } else if (v == btnChange) {
            // Iniciar la actividad MapsActivity para cambiar ubicación
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        // Abrir la actividad viewNegociosActivity para ver los detalles de un negocio
        Intent intent = new Intent(this, viewNegociosActivity.class);
        String idd = String.valueOf(listaNegocios.get(position).getId_negocio());
        intent.putExtra("id", idd);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        // Mostrar un diálogo para confirmar la eliminación de un negocio
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar Sitio");
        builder.setMessage("¿Estás seguro de que deseas eliminar este sitio?");
        builder.setPositiveButton("Eliminar", (dialog, which) -> {
            mSitiosLab.deleteSitio(listaNegocios.get(position));
            getAllNegocios(); // Actualizar la lista
            listItemAdapter.notifyDataSetChanged(); // Notificar al adaptador
            dialog.dismiss();
        });
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
        return false;
    }
}
