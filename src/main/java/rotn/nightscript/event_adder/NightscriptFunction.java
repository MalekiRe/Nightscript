package rotn.nightscript.event_adder;

import rotn.nightscript.event_adder.functionarg.NightscriptFunctionArgument;
import rotn.nightscript.functionalstuff.FuncArgPair;
import rotn.nightscript.functionalstuff.Memo;
import rotn.nightscript.functionalstuff.NonMemo;
import rotn.nightscript.parser.NodeToken;
import rotn.nightscript.parser.Pair;
import rotn.nightscript.parser.Phrase;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.*;

import static rotn.nightscript.event_adder.MainEventsClass.*;

public class NightscriptFunction {
    public ArrayList<NightscriptFunctionArgument> functionArguments = new ArrayList<>();
    public String functionIdentifier = null;
    public Memo lazyEvaluationFunction;
    enum FunctionType {
        NORMAL_FUNCTION,
        EVENT_LAMBDA_FUNCTION
    }
    boolean isBuiltInFunc = false;
    FunctionType functionType = null;
    Set<Pair<Method, Memo>> matchingLengthArgsSet = new HashSet<>();
    public NightscriptFunction(NodeToken functionToken) {
        NodeToken.printTree(functionToken, 0);
        System.out.println(functionToken.childTokens.get(0).phrase);
        if(functionToken.childTokens.get(0).phrase == Phrase.NORMAL_FUNCTION) {
            System.out.println("is decided to be normal function");
            functionType = FunctionType.NORMAL_FUNCTION;
        } else {
            System.out.println("is decided to be normal function");
            functionType = FunctionType.EVENT_LAMBDA_FUNCTION;
        }
        functionToken = functionToken.childTokens.get(0);
        for(NodeToken token : functionToken.childTokens) {
            if(token.phrase == Phrase.identifier) {
                this.functionIdentifier = token.relatedString;
                if(this.functionIdentifier.charAt(0) == '@') {
                    this.isBuiltInFunc = true;
                }
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
            if(!this.isBuiltInFunc) {
                System.out.println("trying to get : " + this.functionIdentifier + " from memoset");
                System.out.println("we got: " + memoSet.get(this.functionIdentifier));
                this.lazyEvaluationFunction = memoSet.get(this.functionIdentifier);
            } else {
                //We delay until the event has gone through and added stuff to the set.
            }
        }

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
            //System.out.println("isn't lambda type");
            //System.out.println("function is : " + this.lazyEvaluationFunction);
            if(!isBuiltInFunc) {
                return new FuncArgPair(this.lazyEvaluationFunction, (list).toArray());
            } else {
                if(matchingLengthArgsSet.size() == 0) {
                    for (Pair<Method, Memo> temp : autoGenMethods.get(this.functionIdentifier).second) {
                        //We have the minus one cause the first parameter is the variable upon which we invoke the function.
                        //TODO::remember to make all this stuff work for static functions too.
                        if (temp.first.getParameters().length == (functionArguments.size() - 1) ||
                                (Modifier.isStatic(temp.first.getModifiers()) && temp.first.getParameters().length == functionArguments.size())) {
                            matchingLengthArgsSet.add(temp);
                        }
                    }
                }
                ArrayList<List> listOfLists = new ArrayList<>();
                return new FuncArgPair(new NonMemo((event, args) -> {
                    List list1 = (List) evaluateOrGetThings(args, event);
                    listOfLists.add(list1);
                    for(Pair<Method, Memo> methodMemoPair : matchingLengthArgsSet) {
                        boolean flag = false;
                        int offset = 1;
                        if(Modifier.isStatic(methodMemoPair.first.getModifiers())) {
                            list1.add(0, null);
                        }
                        for(int i = 0; i < methodMemoPair.first.getParameters().length; i++) {
                            if(list1.size() == i+offset) {
                                flag = true;
                                break;
                            }
                            if(!methodMemoPair.first.getParameterTypes()[i].isInstance(list1.get(i+offset))) {
                                //System.out.println("paramater : " + methodMemoPair.first.getParameterTypes()[i] + " is not instance of : " + list1.get(i+1));
                                flag = true;
                                break;
                            }
                        }
                        if(flag) {
                            continue;
                        }
                        return methodMemoPair.second.eval(event, list1);
                    }
                    System.err.println("Error in nightscript, tried but could not find a matching builtinFunction for " + matchingLengthArgsSet);
                    return null;
                }), list.toArray());

            }
        }
    }



    public ArrayList<Object> evaluateAndReturnAllFunctionArguments(Map<String, Memo> eventFunctionsMap) {
        ArrayList<Object> returnList = new ArrayList<>();
        for(NightscriptFunctionArgument functionArgument : this.functionArguments) {
            switch (functionArgument.argumentType) {
//                case IDENTIFIER: returnList.add(eventFuncArgsMap.get(functionArgument.variableIdentifier)); break;
                case STRING: returnList.add(functionArgument.stringVariable.replace("\"", "")); break;
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
