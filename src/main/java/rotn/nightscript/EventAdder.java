package rotn.nightscript;

import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import rotn.nightscript.event_adder.NightscriptEvent;
import rotn.nightscript.event_adder.NightscriptEventArgument;
import rotn.nightscript.event_adder.NightscriptFunction;
import rotn.nightscript.functionalstuff.Memo;
import rotn.nightscript.functionalstuff.NonMemo;
import rotn.nightscript.parser.NodeToken;
import rotn.nightscript.parser.Pair;
import rotn.nightscript.parser.Phrase;

import java.lang.reflect.Method;
import java.util.*;

import static rotn.nightscript.events.AllEvents.*;

public class EventAdder {
    public static Set<NightscriptEvent> blockBreakEvents = new HashSet<>();
    public static Set<NightscriptEvent> playerInteractWithBlock = new HashSet<>();
    public static Set<NightscriptEvent> bonemealEvents = new HashSet<>();
    public static Set<NightscriptEvent> playerAttack = new HashSet<>();

    public static Map<Class<? extends Event>, Map<String, Memo>> eventFunctionsMap = new HashMap<>();


    public static void addNodeTokensToEvent(NodeToken token) {
        NodeToken.printTree(token, 0);
        if (token.phrase != Phrase.EVENT) {
            System.out.println("phrase : " + token.phrase + " isn't event getting children");
            for (NodeToken token1 : token.childTokens) {
                addNodeTokensToEvent(token1);
            }
            return;
        }
        NodeToken identifierToken = token.childTokens.get(0);
        for(int i = 0; i < token.childTokens.size(); i++) {
            if(identifierToken.phrase == Phrase.identifier) {
                break;
            }
            identifierToken = token.childTokens.get(i);
        }
        if(!nightscriptEventsMap.containsKey(identifierToken.relatedString)) {
            nightscriptEventsMap.put(identifierToken.relatedString, new HashSet<>());
        }
        nightscriptEventsMap.get(identifierToken.relatedString).add(new NightscriptEvent(token));
        System.out.println("identifier is : " + identifierToken.relatedString);
    }
    public static Map<String, Memo> getEventFunctions(Event event) {
        if(!eventFunctionsMap.containsKey(event.getClass())) {
            System.out.println("doesn't contain event : " + event.getClass());
            addEventFunctionsToMap(event);
        }
        return eventFunctionsMap.get(event.getClass());
    }
    public static void addEventFunctionsToMap(Event event) {
        Map<String, Memo> myMap = new HashMap<>();
        Class myClass = event.getClass();
        while(myClass != Event.class) {
            for(Method method : myClass.getDeclaredMethods()) {
                if(method.getParameters().length == 0) {
                    myMap.put(method.getName(), new Memo((event1, args) -> method.invoke(event1, ((Collection)args).toArray())));
                } else {
                    myMap.put(method.getName(), new NonMemo((event1, args) -> method.invoke(event1, ((Collection) args).toArray())));
                }
            }
            myClass = myClass.getSuperclass();
        }
        for(Method method : myClass.getDeclaredMethods()) {
            myMap.put(method.getName(), new NonMemo((event1, args) -> method.invoke(event1, ((Collection)args).toArray())));
        }
        eventFunctionsMap.put(event.getClass(), myMap);
    }
    public static void checkAndRunAllFunctions(Event event) {
        Map<String, Memo> eventFunctions = getEventFunctions(event);
        if(!nightscriptEventsMap.containsKey(event.getClass().getSimpleName())) {
            //This happens if we actually don't have anything declared for the event
            return;
        }
        for(NightscriptEvent nightscriptEvent : nightscriptEventsMap.get(event.getClass().getSimpleName())) {
            for(NightscriptFunction function : nightscriptEvent.nightscriptFunctions) {
                System.out.println("running : " + function.functionIdentifier);
                function.getLazyFunction(eventFunctions).evaluate(event);
            }
        }
    }

}
