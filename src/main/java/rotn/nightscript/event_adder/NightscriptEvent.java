package rotn.nightscript.event_adder;

import org.lwjgl.Sys;
import rotn.nightscript.parser.NodeToken;
import rotn.nightscript.parser.Phrase;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import static rotn.nightscript.parser.Phrase.*;

public class NightscriptEvent {
    public Set<NightscriptEventArgument> eventArguments = new HashSet<>();
    public Vector<NightscriptFunction> nightscriptFunctions = new Vector<NightscriptFunction>();
    public NightscriptEvent(NodeToken eventToken) {
        System.out.println("adding new nightscript event");
        NodeToken eventArgsToken = eventToken.childTokens.get(0);
        for(int i = 0; eventArgsToken.phrase != Phrase.EVENT_ARGS_LIST; i++) {
            eventArgsToken = eventToken.childTokens.get(i);
        }
        processEventArgsListToken(eventArgsToken);
        for(NodeToken token : eventToken.childTokens) {
            if(token.phrase == MULTI_FUNCTION) {
                processMultiFunction(token);
            }
        }
        Collections.reverse(nightscriptFunctions);
        printRep();
    }
    public void processMultiFunction(NodeToken multifunction) {
        for(NodeToken token : multifunction.childTokens) {
            if(token.phrase == MULTI_FUNCTION) {
                processMultiFunction(token);
            }
            if(token.phrase == FUNCTION) {
                processFunction(token);
            }
        }
    }
    public void processFunction(NodeToken function) {
        nightscriptFunctions.add(new NightscriptFunction(function));
    }
    public void processEventArgsListToken(NodeToken eventArgsListToken) {
        for(int i = 0; i < eventArgsListToken.childTokens.size(); i++) {
            if(eventArgsListToken.childTokens.get(i).phrase == EVENT_ARGS) {
                processEventArgsToken(eventArgsListToken.childTokens.get(i));
            }
        }
    }
    public void processEventArgsToken(NodeToken eventArgsToken) {
        for(int i = 0; i < eventArgsToken.childTokens.size(); i++) {
            if(eventArgsToken.childTokens.get(i).phrase == EVENT_ARG) {
                processEventArgToken(eventArgsToken.childTokens.get(i));
            }
        }
    }
    public void processEventArgToken(NodeToken eventArg) {
        String classType;
        String variable;
        if(eventArg.childTokens.get(0).phrase == Phrase.variable) {
            variable = eventArg.childTokens.get(0).relatedString;
            classType = eventArg.childTokens.get(1).relatedString;
        } else {
            classType = eventArg.childTokens.get(0).relatedString;
            variable = eventArg.childTokens.get(1).relatedString;
        }
        eventArguments.add(new NightscriptEventArgument(classType, variable));
    }
    public void printRep() {
        for(NightscriptEventArgument nightscriptEventArgument : eventArguments) {
            System.out.println("event arg : " + nightscriptEventArgument);
        }
        for(NightscriptFunction function : nightscriptFunctions) {
            System.out.println("function : " + function);
        }
    }

}
