package net.danilovms.rcloud.common.Objects;

public class ErrorMessage extends AbstractMessage{
    private String textError;

    public ErrorMessage(String textError){this.textError = textError;}

    public void setTextError(String textError){this.textError = textError;}

    public String getTextError(){return this.textError;}

}
