package rotn.nightscript.event_adder;

public class NightscriptEventArgument {
    public String classType;
    public String variableIdentifier;
    public NightscriptEventArgument(String classType, String variableIdentifier) {
        this.classType = classType;
        this.variableIdentifier = variableIdentifier;
    }
    public String toString() {
        return "classType: " + classType + ", variableIdentifier: " + variableIdentifier;
    }
}
