package net.danilovms.rcloud.server;

import net.danilovms.rcloud.common.Objects.User;

public class ManagerDB {

    public static boolean Authorization(User user){
        boolean result = false;
        String login = user.getLogin();
        String pass = new String(user.getPassword());

        if (login.equals("admin") && pass.equals("admin")){
            result = true;
        }

        return result;
    }
}
