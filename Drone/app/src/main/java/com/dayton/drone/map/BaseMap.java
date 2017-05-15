package com.dayton.drone.map;

import android.os.Bundle;
import android.view.View;

/**
 * Created by med on 17/5/15.
 */

public interface BaseMap {
    void onCreate(Bundle savedInstanceState);

    void onResume();

    void onDestroy();

    void onPause();

    void onSaveInstanceState(Bundle outState);

    View getMap();
}
