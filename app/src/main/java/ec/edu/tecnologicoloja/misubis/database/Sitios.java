package ec.edu.tecnologicoloja.misubis.database;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

// Indicamos que la clase es una entidad de base de datos y se almacenará en una tabla llamada "negocios"
@Entity(tableName = "negocios")
// Utilizamos un convertidor personalizado para el tipo de dato Bitmap (que no es nativo de Room)
@TypeConverters(BitmapTypeConverter.class)
public class Sitios {


    // Definimos el campo de la clave primaria y que se genere automáticamente al insertar un nuevo registro
    @PrimaryKey (autoGenerate = true)
    @NonNull
    private int id_negocio;

    // Definimos los atributos de la tabla y mapeamos sus nombres de columna
    @ColumnInfo (name="nombre_sitio")
    @NonNull
    private String nombre;

    @ColumnInfo (name="latitude")
    private Double latitud;

    @ColumnInfo (name="longitude")
    private Double longitud;

    @ColumnInfo (name="propietario")
    @NonNull
    private String propietario;

    @ColumnInfo (name="contacto")
    @NonNull
    private String contacto;

    @ColumnInfo (name="direccion")
    @NonNull
    private String direccion;

    @ColumnInfo (name="foto")
    private String foto;

    @ColumnInfo (name="email")
    @NonNull
    private String email;



    /********Getter ans Setters*******/

    public int getId_negocio() {
        return id_negocio;
    }

    public void setId_negocio(int id_negocio) {
        this.id_negocio = id_negocio;
    }

    @NonNull
    public String getNombre() {
        return nombre;
    }

    public void setNombre(@NonNull String nombre) {
        this.nombre = nombre;
    }

    @NonNull
    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(@NonNull Double latitud) {
        this.latitud = latitud;
    }

    @NonNull
    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(@NonNull Double longitud) {
        this.longitud = longitud;
    }

    @NonNull
    public String getPropietario() {
        return propietario;
    }

    public void setPropietario(@NonNull String propietario) {
        this.propietario = propietario;
    }

    @NonNull
    public String getContacto() {
        return contacto;
    }

    public void setContacto(@NonNull String contacto) {
        this.contacto = contacto;
    }

    @NonNull
    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(@NonNull String direccion) {
        this.direccion = direccion;
    }


    @NonNull
    public String getFoto() {
        return foto;
    }

    public void setFoto(@NonNull String foto) {
        this.foto = foto;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

}