package ec.edu.tecnologicoloja.misubis.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/*
 * LA SIGUIENTE CLASE ES LA BASE DE DATOS PRINCIPAL PARA LA APLICACIÓN
 * SE DEFINE MEDIANTE LA ANOTACIÓN @Database
 * Y SE ESPECIFICA LA VERSIÓN DE LA BASE DE DATOS Y LAS ENTIDADES (TABLAS) QUE CONTIENE
 *
 * */
@Database(entities = {Sitios.class}, version = 3)
public abstract class SitiosDatabase extends RoomDatabase {

    /*
     * ESTE MÉTODO ABSTRACTO PERMITE ACCEDER AL OBJETO SitiosDao
     * QUE ES UNA INTERFAZ QUE CONTIENE LAS OPERACIONES PARA MANIPULAR LA TABLA "negocios" EN LA BASE DE DATOS
     * AL DECLARAR ESTE MÉTODO COMO ABSTRACTO, INDICAMOS QUE Room SE ENCARGARÁ DE IMPLEMENTARLO EN TIEMPO DE EJECUCIÓN
     * LA CLASE SitiosDatabase DEBE SER ABSTRACTA PORQUE NO TIENE UNA IMPLEMENTACIÓN DIRECTA DE getSitiosDao(),
     * QUE SERÁ PROPORCIONADA POR Room AL COMPILAR EL CÓDIGO
     *
     * */
    public abstract SitiosDao getSitiosDao();
}