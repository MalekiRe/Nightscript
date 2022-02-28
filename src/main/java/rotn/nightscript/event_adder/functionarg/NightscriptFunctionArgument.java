package rotn.nightscript.event_adder.functionarg;

import rotn.nightscript.event_adder.NightscriptFunction;
import rotn.nightscript.parser.NodeToken;

public class NightscriptFunctionArgument {
    //This is basically like a C style union, but without any of the memory savings
    public NightscriptFunction function = null;
    public String stringVariable = null;
    public String variableIdentifier = null;
    public int variableNumber = 0;
    public ArgumentType argumentType;
    public NightscriptFunctionArgument(NightscriptFunction nightscriptFunction) {
        this.function = nightscriptFunction;
        this.argumentType = ArgumentType.FUNCTION;
    }
    public NightscriptFunctionArgument(NodeToken variableToken) {
        switch (variableToken.phrase) {
            case variable: this.variableIdentifier = variableToken.relatedString; this.argumentType = ArgumentType.IDENTIFIER; return;
            case number: this.variableNumber = Integer.parseInt(variableToken.relatedString); this.argumentType = ArgumentType.INT; return;
            case string: this.stringVariable = variableToken.relatedString; this.argumentType = ArgumentType.STRING; return;
        }
        System.err.println("error is not a variable number or string but is supposed to be : " + variableToken.phrase);
        NodeToken.printTree(variableToken, 0);
        System.exit(-1);
    }
    public String toString() {
        switch (argumentType) {
            case FUNCTION: return "function: " + function;
            case IDENTIFIER: return "id: " + variableIdentifier;
            case INT: return "int: " + variableNumber;
            case STRING: return "string: " + stringVariable;
        }
        System.out.println("argument type is : " + argumentType);
        System.exit(-1);
        return null;
    }
}
