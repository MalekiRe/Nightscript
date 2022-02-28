package rotn.nightscript.event_adder;

import rotn.nightscript.event_adder.functionarg.NightscriptFunctionArgument;
import rotn.nightscript.parser.NodeToken;
import rotn.nightscript.parser.Pair;
import rotn.nightscript.parser.Phrase;

import java.util.*;

public class NightscriptFunction {
    public ArrayList<NightscriptFunctionArgument> functionArguments = new ArrayList<>();
    public String functionIdentifier = null;
    ActionFunction actionFunction = null;
    public NightscriptFunction(NodeToken functionToken) {
        NodeToken.printTree(functionToken, 0);
        for(NodeToken token : functionToken.childTokens) {
            if(token.phrase == Phrase.identifier) {
                this.functionIdentifier = token.relatedString;
            }
            if(token.phrase == Phrase.ARGS) {
                processArgs(token);
            }
        }
        Collections.reverse(functionArguments);
        for(ActionFunction actionFunction1 : MainEventsClass.actionFunctionSet) {
            if(actionFunction1.actionFunctionName.equals(functionIdentifier)) {
                if(actionFunction1.argumentList.size() == functionArguments.size()) {
                    this.actionFunction = actionFunction1;
                    break;
                }
            }
        }
    }
    public Object runFunction(Map<String, Object> eventFuncArgsMap) {

        ArrayList<Object> objectArrayList = new ArrayList<>();
        for(NightscriptFunctionArgument functionArgument : this.functionArguments) {
            switch (functionArgument.argumentType) {
                case IDENTIFIER: objectArrayList.add(eventFuncArgsMap.get(functionArgument.variableIdentifier)); break;
                case STRING: objectArrayList.add(functionArgument.stringVariable); break;
                case INT: objectArrayList.add(functionArgument.variableNumber); break;
                case FUNCTION: objectArrayList.add(Pair.of(functionArgument.function, eventFuncArgsMap)); break;
            }
        }
        //System.out.println("running function : " + this.functionIdentifier);
        return this.getActionFunction().functionDo.accept(objectArrayList);

    }
    public ActionFunction getActionFunction()  {
        return actionFunction;
    }
    public void processArgs(NodeToken argsToken) {
        for(NodeToken token : argsToken.childTokens) {
            if(token.phrase == Phrase.ARGS) {
                processArgs(token);
            }
            if(token.phrase == Phrase.FUNCTION_ARG) {
                processFuncArgs(token);
            }
        }
    }
    public void processFuncArgs(NodeToken funcArgsToken) {
        if (funcArgsToken.childTokens.get(0).phrase == Phrase.FUNCTION) {
            functionArguments.add(new NightscriptFunctionArgument(new NightscriptFunction(funcArgsToken.childTokens.get(0))));
        } else {
            functionArguments.add(new NightscriptFunctionArgument(funcArgsToken.childTokens.get(0)));
        }
    }
    public String toString() {
        String s = "functionIdentifier: " + functionIdentifier + "[";
        for(int i = 0; i < functionArguments.size(); i++) {
            if(i != 0) {
                s += ", ";
            }
            s += functionArguments.get(i);
        }
        s += "]";
        return s;
    }
}
