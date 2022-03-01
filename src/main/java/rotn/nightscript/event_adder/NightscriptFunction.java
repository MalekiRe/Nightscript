package rotn.nightscript.event_adder;

import rotn.nightscript.event_adder.functionarg.NightscriptFunctionArgument;
import rotn.nightscript.functionalstuff.FuncArgPair;
import rotn.nightscript.functionalstuff.Memo;
import rotn.nightscript.parser.NodeToken;
import rotn.nightscript.parser.Pair;
import rotn.nightscript.parser.Phrase;

import java.util.*;

import static rotn.nightscript.event_adder.MainEventsClass.memoSet;

public class NightscriptFunction {
    public ArrayList<NightscriptFunctionArgument> functionArguments = new ArrayList<>();
    public String functionIdentifier = null;
    ActionFunction actionFunction = null;
    public Memo lazyEvaluationFunction;
    public FuncArgPair funcArgPair;
    enum FunctionType {
        NORMAL_FUNCTION,
        EVENT_LAMBDA_FUNCTION
    }
    FunctionType functionType = null;
    public NightscriptFunction(NodeToken functionToken) {
        NodeToken.printTree(functionToken, 0);
        if(functionToken.childTokens.get(0).phrase == Phrase.NORMAL_FUNCTION) {
            functionType = FunctionType.NORMAL_FUNCTION;
        } else {
            functionType = FunctionType.EVENT_LAMBDA_FUNCTION;
        }
        functionToken = functionToken.childTokens.get(0);
        for(NodeToken token : functionToken.childTokens) {
            if(token.phrase == Phrase.identifier) {
                this.functionIdentifier = token.relatedString;
            }
            if(token.phrase == Phrase.event_lambda) {
                this.functionIdentifier = token.relatedString.replace("#", "");
            }
            if(token.phrase == Phrase.ARGS) {
                processArgs(token);
            }
        }
        Collections.reverse(functionArguments);
        if(functionType == FunctionType.NORMAL_FUNCTION) {
            this.lazyEvaluationFunction = memoSet.get(this.functionIdentifier);
        }
//        for(ActionFunction actionFunction1 : MainEventsClass.actionFunctionSet) {
//            if(actionFunction1.actionFunctionName.equals(functionIdentifier)) {
//                if(actionFunction1.argumentList.size() == functionArguments.size()) {
//                    this.actionFunction = actionFunction1;
//                    break;
//                }
//            }
//        }
    }
    public FuncArgPair getLazyFunction(Map<String, Memo> eventFunctionsMap) {
        List list = this.evaluateAndReturnAllFunctionArguments(eventFunctionsMap);
        //System.out.println("func args for : " + functionIdentifier);
        /*for(Object object : list) {
            System.out.println(object);
        }*/
        if(this.functionType == FunctionType.EVENT_LAMBDA_FUNCTION) {
            //System.out.println("is lambda type");
            //System.out.println("gotten is : " + eventFunctionsMap.get(this.functionIdentifier) + " from : " + this.functionIdentifier);
            return new FuncArgPair(eventFunctionsMap.get(this.functionIdentifier), (list).toArray());
        } else {
            return new FuncArgPair(this.lazyEvaluationFunction, (list).toArray());
        }
    }
//    public Object runFunction(Map<String, Object> eventFuncArgsMap, Map<String, Memo> eventFunctionsMap) {
//
//        List list = this.evaluateAndReturnAllFunctionArguments(eventFuncArgsMap, eventFunctionsMap);
//        if(this.functionType == FunctionType.EVENT_LAMBDA_FUNCTION) {
//            return eventFunctionsMap.get(this.functionIdentifier).eval(Collections.addAll(list));
//        } else {
//            return this.lazyEvaluationFunction.eval(Collections.addAll(list));
//        }
        //return this.getActionFunction().functionDo.accept(Collections.addAll(this.evaluateAndReturnAllFunctionArguments(eventFuncArgsMap, eventFunctionsMap)));
//        ArrayList<Object> objectArrayList = new ArrayList<>();
//        for(NightscriptFunctionArgument functionArgument : this.functionArguments) {
//            switch (functionArgument.argumentType) {
//                case IDENTIFIER: objectArrayList.add(eventFuncArgsMap.get(functionArgument.variableIdentifier)); break;
//                case STRING: objectArrayList.add(functionArgument.stringVariable); break;
//                case INT: objectArrayList.add(functionArgument.variableInteger); break;
//                case FUNCTION:
//                    if(functionArgument.function.functionType == FunctionType.NORMAL_FUNCTION) {
//                        objectArrayList.add(Pair.of(functionArgument.function, eventFuncArgsMap));
//                        break;
//                    }
//                    else {
//                        //If its actually a real function then we actually just run it and then add its return value to the thing.
//                        //So we need to determine all of the functions arguments and pass them in.
//                        objectArrayList.add(Pair.of(eventFunctionsMap.get(functionArgument.function.functionIdentifier).accept(
//                                functionArgument.function.evaluateAndReturnAllFunctionArguments(eventFuncArgsMap, eventFunctionsMap)
//                        ));
//                        break;
//                    }
//                case FLOAT: objectArrayList.add(functionArgument.variableFloat); break;
//                case DOUBLE: objectArrayList.add(functionArgument.variableDouble); break;
//                //case EVENT_LAMBDA: objectArrayList.add(Pair.of(functionArgument.eventLambdaIdentifier, eventFuncArgsMap)); break;
//            }
//        }
//        //System.out.println("running function : " + this.functionIdentifier);
//        return this.getActionFunction().functionDo.accept(objectArrayList);

    //}

    public ArrayList<Object> evaluateAndReturnAllFunctionArguments(Map<String, Memo> eventFunctionsMap) {
        ArrayList<Object> returnList = new ArrayList<>();
        for(NightscriptFunctionArgument functionArgument : this.functionArguments) {
            switch (functionArgument.argumentType) {
//                case IDENTIFIER: returnList.add(eventFuncArgsMap.get(functionArgument.variableIdentifier)); break;
                case STRING: returnList.add(functionArgument.stringVariable); break;
                case INT: returnList.add(functionArgument.variableInteger); break;
                case DOUBLE: returnList.add(functionArgument.variableDouble); break;
                case FLOAT: returnList.add(functionArgument.variableFloat); break;
                case BOOL: returnList.add(functionArgument.variableBoolean); break;
                case FUNCTION: {
                    returnList.add(functionArgument.function.getLazyFunction(eventFunctionsMap));
                }
            }
        }
        return returnList;
    }
//    public ActionFunction getActionFunction()  {
//        return actionFunction;
//    }
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
