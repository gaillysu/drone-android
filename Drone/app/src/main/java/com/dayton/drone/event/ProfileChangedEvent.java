package com.dayton.drone.event;

import android.support.annotation.NonNull;

import com.dayton.drone.model.User;

/**
 * Created by med on 16/5/27.
 */
public class ProfileChangedEvent {
   private final User user;
    public ProfileChangedEvent(@NonNull User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
