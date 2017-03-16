package org.jenkinsci.plugins.nomad.Api;

public class Constraints {
    private String LTarget;
    private String RTarget;
    private String Operand;

    public Constraints(String ltarget, String rtarget, String operand) {
        LTarget = ltarget;
        RTarget = rtarget;
        Operand = operand;
    }

    public String getLTarget() {
        return LTarget;
    }

    public void setLTarget(String ltarget) {
        LTarget = ltarget;
    }

    public String getRTarget() {
        return RTarget;
    }

    public void setRTarget(String rtarget) {
        RTarget = rtarget;
    }

    public String getOperand() {
        return Operand;
    }

    public void setOperand(String operand) {
        Operand = operand;
    }

}
