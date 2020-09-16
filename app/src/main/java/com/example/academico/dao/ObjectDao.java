package com.example.academico.dao;

/**
 * Created by Giovany on 12/08/2015.
 */
// Classe privada e abstrata usada apenas para que todos os Dao tenham um DataBaseCreator
abstract class ObjectDao {
    private DataBaseCreator creator;

    public ObjectDao(DataBaseCreator creator) {
        this.creator = creator;
    }

    protected DataBaseCreator getCreator() {
        return creator;
    }
}
