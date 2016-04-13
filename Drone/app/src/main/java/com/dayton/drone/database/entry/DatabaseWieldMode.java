package com.dayton.drone.database.entry;

import net.medcorp.library.ble.util.Optional;

import java.util.List;

/**
 * Created by boy on 2016/4/12.
 */
public interface DatabaseWieldMode<T> {

    public Optional<T> add(T Object);

    public boolean upData(T Object);

    public boolean remove(int rid);

    public List<Optional<T>> get(int rid);

    public List<Optional<T>> getAll();

    public List<T> convertToNormalList(List<Optional<T>> optionalList);

}
