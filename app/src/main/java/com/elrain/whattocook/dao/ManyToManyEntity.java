package com.elrain.whattocook.dao;

/**
 * Created by elrain on 05.06.15.
 */
public class ManyToManyEntity {

    private long id;
    private long idFirst;
    private long idSecond;

    public ManyToManyEntity(long id, long idFirst, long idSecond) {
        this.id = id;
        this.idFirst = idFirst;
        this.idSecond = idSecond;
    }

    public long getId() {
        return id;
    }

    public long getIdFirst() {
        return idFirst;
    }

    public long getIdSecond() {
        return idSecond;
    }
}
