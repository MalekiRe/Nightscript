package rotn.nightscript;

import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import rotn.nightscript.event_adder.NightscriptEvent;
import rotn.nightscript.event_adder.NightscriptEventArgument;
import rotn.nightscript.event_adder.NightscriptFunction;
import rotn.nightscript.events.LivingEvents;
import rotn.nightscript.parser.NodeToken;
import rotn.nightscript.parser.Phrase;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static rotn.nightscript.events.TickEvents.*;

public class EventAdder {
    public static Set<NightscriptEvent> blockBreakEvents = new HashSet<>();
    public static Set<NightscriptEvent> playerInteractWithBlock = new HashSet<>();
    public static Set<NightscriptEvent> bonemealEvents = new HashSet<>();
    public static Set<NightscriptEvent> playerAttack = new HashSet<>();

    public static Map<Class, Map<String, Object>> eventsToArgsMap = new HashMap<>();

    public static void addNodeTokensToEvent(NodeToken token) {
        if (token.phrase != Phrase.EVENT) {
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
        switch (identifierToken.relatedString) {
            case "BlockBreakEvent" : blockBreakEvents.add(new NightscriptEvent(token)); break;
            case "playerInteractWithBlock" : playerInteractWithBlock.add(new NightscriptEvent(token)); break;
            case "bonemeal" : bonemealEvents.add(new NightscriptEvent(token)); break;
            case "playerAttack" : playerAttack.add(new NightscriptEvent(token)); break;
            case "ServerTickEvent" : serverTickEvents.add(new NightscriptEvent(token)); break;
            case "ClientTickEvent" : clientTickEvents.add(new NightscriptEvent(token)); break;
            case "WorldTickEvent" : worldTickEvents.add(new NightscriptEvent(token)); break;
            case "PlayerTickEvent" : playerTickEvents.add(new NightscriptEvent(token)); break;
            case "RenderTickEvent" : renderTickEvents.add(new NightscriptEvent(token)); break;
        }
        System.out.println("identifier is : " + identifierToken.relatedString);
    }
    public static Map<String, Object> createEventArguments(Event event) {
        Map<String, Object> hashMap = new HashMap<>();
        if(event instanceof EntityEvent) {
            hashMap.put("Entity", ((EntityEvent)event).getEntity());
            if(event instanceof EntityJoinWorldEvent) {
                hashMap.put("World", ((EntityJoinWorldEvent)event).getWorld());
            }
            if(event instanceof LivingEvent) {
                if(event instanceof LivingAttackEvent) {

                }
            }
        }
        if(event instanceof BlockEvent) {
            hashMap.put("World", ((BlockEvent)event).getWorld());
            hashMap.put("State", ((BlockEvent)event).getState());
            hashMap.put("Position", ((BlockEvent)event).getPos());
            if(event instanceof BlockEvent.BreakEvent) {
                hashMap.put("Player", ((BlockEvent.BreakEvent)event).getPlayer());
                hashMap.put("ExpToDrop", ((BlockEvent.BreakEvent)event).getExpToDrop());
            }
        }
        return hashMap;
    }
    public static Map<String, Object> addVariableToEventFuncArguments(Map<String, Object> hashMap, Set<NightscriptEventArgument> arguments) {
        Map<String, Object> nightscriptArgumentMap = new HashMap<>();
        for(NightscriptEventArgument nightscriptEventArgument : arguments) {
            if(hashMap.containsKey(nightscriptEventArgument.classType)) {
                nightscriptArgumentMap.put(nightscriptEventArgument.variableIdentifier, hashMap.get(nightscriptEventArgument.classType));
            }
        }
        return nightscriptArgumentMap;
    }
    public static void checkAndRunAllFunctions(Event event, Set<NightscriptEvent> nightscriptEvents) {
        Map<String, Object> eventArguments = EventAdder.createEventArguments(event);
        for(NightscriptEvent nightscriptEvent : nightscriptEvents) {
            Map<String, Object> eventVariableArgs = EventAdder.addVariableToEventFuncArguments(eventArguments, nightscriptEvent.eventArguments);
            for(NightscriptFunction function : nightscriptEvent.nightscriptFunctions) {
                function.runFunction(eventVariableArgs);
            }
        }
    }

}
