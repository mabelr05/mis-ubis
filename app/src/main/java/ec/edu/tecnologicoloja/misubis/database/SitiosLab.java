package ec.edu.tecnologicoloja.misubis.database;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.room.Room;

import java.util.List;

/*
 * LA SIGUIENTE CLASE ES EL LABORATORIO DE SITIOS
 * SE ENCARGA DE UTILIZAR LOS MÉTODOS DE LA INTERFAZ SitiosDao PARA INTERACTUAR CON LA BASE DE DATOS
 * */

public class SitiosLab {
    @SuppressLint("StaticFieldLeak")
    private static SitiosLab sSitiosLab;
    private SitiosDao mSitiosDao;

    /*
     * EL CONSTRUCTOR DE LA CLASE RECIBE EL CONTEXTO DE LA APLICACIÓN
     * PARA PODER OBTENER EL CONTEXT DE LA ACTIVIDAD Y CONSTRUIR LA BASE DE DATOS
     * */
    public SitiosLab(Context context) {
        Context appContext = context.getApplicationContext();
        SitiosDatabase database = Room.databaseBuilder
                        (appContext, SitiosDatabase.class,
                                "negocios")
                .allowMainThreadQueries().build();
        mSitiosDao = database.getSitiosDao();
    }

    /*
     * EL MÉTODO get ES UN MÉTODO ESTÁTICO QUE PERMITE ACCEDER A LA INSTANCIA DEL SITIOSLAB
     * SI LA INSTANCIA ES NULA, SE CREA UNA NUEVA INSTANCIA Y SE DEVUELVE
     * */
    public static SitiosLab get(Context context) {
        if (sSitiosLab == null) {
            sSitiosLab = new SitiosLab(context);
        }
        return sSitiosLab;
    }

    /*
     * EL MÉTODO getSitios RETORNA UNA LISTA DE OBJETOS DE LA CLASE Sitios
     * ESTA LISTA CONTIENE TODOS LOS SITIOS DE LA BASE DE DATOS
     * */
    public List<Sitios> getSitios() {
        return mSitiosDao.getSitios();
    }

    /*
     * EL MÉTODO getSitio RECIBE UN IDENTIFICADOR (ID) Y RETORNA UN OBJETO DE LA CLASE Sitios
     * QUE CORRESPONDE AL SITIO CON EL ID ESPECIFICADO
     * */
    public Sitios getSitio(String id) {
        return mSitiosDao.getSitio(id);
    }

    /*
     * EL MÉTODO addSitio RECIBE UN OBJETO DE LA CLASE Sitios Y LO AGREGA A LA BASE DE DATOS
     * */
    public void addSitio(Sitios sitio) {
        mSitiosDao.addSitio(sitio);
    }

    /*
     * EL MÉTODO updateSitio RECIBE UN OBJETO DE LA CLASE Sitios Y ACTUALIZA SUS DATOS EN LA BASE DE DATOS
     * */
    public void updateSitio(Sitios sitio) {
        mSitiosDao.updateSitio(sitio);
    }

    /*
     * EL MÉTODO deleteSitio RECIBE UN OBJETO DE LA CLASE Sitios Y LO ELIMINA DE LA BASE DE DATOS
     * */
    public void deleteSitio(Sitios sitio) {
        mSitiosDao.deleteSitio(sitio);
    }

    /*
     * EL MÉTODO deleteAllSitios ELIMINA TODOS LOS SITIOS DE LA BASE DE DATOS
     * */
    public void deleteAllSitios() {
        mSitiosDao.deleteAllSitio();
    }
}