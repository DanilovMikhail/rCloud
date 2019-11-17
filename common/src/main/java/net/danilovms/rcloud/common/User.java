package net.danilovms.rcloud.common;

public class User {
    private String login;
    private char[] password;

    public User(String login, char[] password){
        this.login = login;
        this.password =  password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public char[] getPassword() {
        return password;
    }
}
