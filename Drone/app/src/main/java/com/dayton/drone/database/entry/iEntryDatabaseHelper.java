package com.dayton.drone.database.entry;


import net.medcorp.library.ble.util.Optional;

import java.util.Date;
import java.util.List;

/**
 * Created by karl-john on 17/11/15.
 */
interface iEntryDatabaseHelper<T> {

     Optional<T> add(T object);
     boolean update(T object);

     boolean remove(String userId, Date date);
     List<Optional<T>> get(String userId);
     Optional<T> get(String userId, Date date);
     List<Optional<T>> getAll(String userId);

     List<T> convertToNormalList(List<Optional<T>> optionalList);

}
