package net.danilovms.rcloud.common.Objects;

public class User extends AbstractMessage {
    private String login;
    private char[] password;
    private boolean authorised;

    public User(String login, char[] password){
        this.login      = login;
        this.password   =  password;
        this.authorised = false;
    }

    public void setLogin(String login) {this.login = login;}

    public void setPassword(char[] password) {this.password = password;}

    public void setAuthorised(boolean authorised){this.authorised = authorised;}

    public String getLogin() {return this.login;}

    public char[] getPassword() {return this.password;}

    public boolean getAuthorised(){return this.authorised;}
}
