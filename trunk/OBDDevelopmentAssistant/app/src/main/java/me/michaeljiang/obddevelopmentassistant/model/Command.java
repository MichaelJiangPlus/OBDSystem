package me.michaeljiang.obddevelopmentassistant.model;

/**
 * Created by MichaelJiang on 2017/2/27.
 */

public class Command {
    private String commandNumber;
    private String commandInfo;
    private boolean isCheck = false;

    public Command(String number,String info,boolean isCheck){
        this.commandNumber = number;
        this.commandInfo = info;
        this.isCheck = isCheck;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getCommandInfo() {
        return commandInfo;
    }

    public void setCommandInfo(String commandInfo) {
        this.commandInfo = commandInfo;
    }

    public String getCommandNumber() {
        return commandNumber;
    }

    public void setCommandNumber(String commandNumber) {
        this.commandNumber = commandNumber;
    }
}
