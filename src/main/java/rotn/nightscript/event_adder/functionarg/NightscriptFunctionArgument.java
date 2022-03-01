package rotn.nightscript.event_adder.functionarg;

import rotn.nightscript.event_adder.NightscriptFunction;
import rotn.nightscript.parser.NodeToken;

public class NightscriptFunctionArgument {
    //This is basically like a C style union, but without any of the memory savings
    public NightscriptFunction function = null;
    public String stringVariable = null;
    public String variableIdentifier = null;
    public int variableInteger = 0;
    public float variableFloat = 0f;
    public double variableDouble = 0.0;
    public boolean variableBoolean = false;
    public String eventLambdaIdentifier = null;
    public ArgumentType argumentType;
    public NightscriptFunctionArgument(NightscriptFunction nightscriptFunction) {
        this.function = nightscriptFunction;
        this.argumentType = ArgumentType.FUNCTION;
    }
    public NightscriptFunctionArgument(NodeToken variableToken) {
        switch (variableToken.phrase) {
            case variable: this.variableIdentifier = variableToken.relatedString; this.argumentType = ArgumentType.IDENTIFIER; return;
            case integer: this.variableInteger = Integer.parseInt(variableToken.relatedString); this.argumentType = ArgumentType.INT; return;
            case string: this.stringVariable = variableToken.relatedString; this.argumentType = ArgumentType.STRING; return;
            case double_var: this.variableDouble = Double.parseDouble(variableToken.relatedString); this.argumentType = ArgumentType.DOUBLE; return;
            case float_var: this.variableFloat = Float.parseFloat(variableToken.relatedString); this.argumentType = ArgumentType.FLOAT; return;
            case bool: this.variableBoolean = Boolean.parseBoolean(variableToken.relatedString); this.argumentType = ArgumentType.BOOL; return;
            //case event_lambda: this.eventLambdaIdentifier = variableToken.relatedString.replace("#", ""); this.argumentType = ArgumentType.EVENT_LAMBDA; return;
        }
        System.err.println("error is not a variable number or string but is supposed to be : " + variableToken.phrase);
        NodeToken.printTree(variableToken, 0);
        System.exit(-1);
    }
    public String toString() {
        switch (argumentType) {
            case FUNCTION: return "function: " + function;
            case IDENTIFIER: return "id: " + variableIdentifier;
            case INT: return "int: " + variableInteger;
            case STRING: return "string: " + stringVariable;
            //case EVENT_LAMBDA: return "event lambda: " + eventLambdaIdentifier;
            case FLOAT: return "float: " + variableFloat;
            case DOUBLE: return "double: " + variableDouble;
            case BOOL: return "bool: " + variableBoolean;
        }
        System.out.println("argument type is : " + argumentType);
        System.exit(-1);
        return null;
    }
}
