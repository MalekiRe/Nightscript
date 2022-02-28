package rotn.nightscript;

import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import rotn.nightscript.event_adder.FunctionDo;
import rotn.nightscript.event_adder.NightscriptEvent;
import rotn.nightscript.event_adder.NightscriptEventArgument;
import rotn.nightscript.event_adder.NightscriptFunction;
import rotn.nightscript.events.LivingEvents;
import rotn.nightscript.functionalstuff.Memo;
import rotn.nightscript.parser.NodeToken;
import rotn.nightscript.parser.Pair;
import rotn.nightscript.parser.Phrase;
import scala.tools.nsc.Global;

import java.util.*;

import static rotn.nightscript.events.TickEvents.*;

public class EventAdder {
    public static Set<NightscriptEvent> blockBreakEvents = new HashSet<>();
    public static Set<NightscriptEvent> playerInteractWithBlock = new HashSet<>();
    public static Set<NightscriptEvent> bonemealEvents = new HashSet<>();
    public static Set<NightscriptEvent> playerAttack = new HashSet<>();

    public static Map<Class, Map<String, Object>> eventsToArgsMap = new HashMap<>();

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
    public static Pair<Map<String, Object>, Map<String, Memo>> createEventArguments(Event event) {
        Map<String, Object> hashMap = new HashMap<>();
        Map<String, Memo> functionDoMap = new HashMap<>();
        functionDoMap.put("setCanceled", new Memo((u) -> {
            List objects = (List) u;
            event.setCanceled((Boolean) objects.get(0));
            return null;
        }));
        if(event instanceof EntityEvent) {
            hashMap.put("Entity", ((EntityEvent)event).getEntity());
            if(event instanceof EntityJoinWorldEvent) {
                hashMap.put("World", ((EntityJoinWorldEvent)event).getWorld());
            }
            if(event instanceof LivingEvent) {
                if(event instanceof LivingAttackEvent) {
                    hashMap.put("DamageSource", ((LivingAttackEvent)event).getSource());
                    hashMap.put("Amount", ((LivingAttackEvent)event).getAmount());
                }
                if(event instanceof LivingDeathEvent) {
                    hashMap.put("Source", ((LivingDeathEvent)event).getSource());
                }
                if(event instanceof LivingFallEvent) {
                    hashMap.put("Distance", ((LivingFallEvent)event).getDistance());
                    hashMap.put("DamageMultiplier", ((LivingFallEvent)event).getDamageMultiplier());
                }
                if(event instanceof LivingDropsEvent) {
                    hashMap.put("DamageSource", ((LivingDropsEvent)event).getSource());
                    hashMap.put("Drops", ((LivingDropsEvent)event).getDrops());
                    hashMap.put("LootingLevel", ((LivingDropsEvent)event).getLootingLevel());
                    hashMap.put("IsRecentlyHit", ((LivingDropsEvent)event).isRecentlyHit());
                }
                if(event instanceof LivingHurtEvent) {
                    hashMap.put("DamageSource", ((LivingHurtEvent)event).getSource());
                    hashMap.put("Amount", ((LivingHurtEvent)event).getAmount());
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
        return Pair.of(hashMap, functionDoMap);
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
        Pair<Map<String, Object>, Map<String, Memo>> myPair = EventAdder.createEventArguments(event);
        Map<String, Object> eventArguments = myPair.first;
        Map<String, Memo> eventFunctions = myPair.second;
        for(NightscriptEvent nightscriptEvent : nightscriptEvents) {
            Map<String, Object> eventVariableArgs = EventAdder.addVariableToEventFuncArguments(eventArguments, nightscriptEvent.eventArguments);
            for(NightscriptFunction function : nightscriptEvent.nightscriptFunctions) {
                System.out.println("running : " + function.functionIdentifier);
                function.getLazyFunction(eventVariableArgs, eventFunctions).evaluate();
            }
        }
    }

}
