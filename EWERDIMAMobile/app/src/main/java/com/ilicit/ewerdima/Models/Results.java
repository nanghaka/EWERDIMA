package com.ilicit.ewerdima.Models;

import java.util.List;

/**
 * Created by Shaffic on 5/13/15.
 */
public class Results {

    public List<MyUsers> getUsers_Circles() {
        return Users_Circles;
    }

    public void setUsers_Circles(List<MyUsers> registered_Users) {
        Users_Circles = registered_Users;
    }

    List<MyUsers> Users_Circles;
}
