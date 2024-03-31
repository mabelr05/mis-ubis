package ec.edu.tecnologicoloja.misubis.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/*
 * LA SIGUIENTE INTERFACE ES DONDE SE IMPLEMENTA LAS SETENCIA DE ROOM'
 * PARA MANIPULAR LA BASE DE DATOS
 * COMO LAS CONSULTAS, EL INSERTAR, EL BORRAR
 *
 * */

@Dao
public interface SitiosDao {

    // Consulta para obtener todos los registros de la tabla "negocios"
    @Query("SELECT * FROM negocios")
    List<Sitios> getSitios();

    // Consulta para obtener un registro específico de la tabla "negocios" por su id_negocio
    @Query("SELECT * FROM negocios WHERE id_negocio== :uuid")
    Sitios getSitio(String uuid);

    // Inserción de un nuevo registro en la tabla "negocios"
    @Insert
    void addSitio(Sitios p);

    // Eliminación de un registro específico de la tabla "negocios"
    @Delete
    void deleteSitio(Sitios p);

    // Actualización de un registro específico de la tabla "negocios"
    @Update
    void updateSitio(Sitios p);

    // Consulta para eliminar todos los registros de la tabla "negocios"
    @Query("DELETE FROM negocios")
    void deleteAllSitio();

}
