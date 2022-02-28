package rotn.nightscript.events;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import rotn.nightscript.EventAdder;
import rotn.nightscript.event_adder.NightscriptEvent;
import rotn.nightscript.event_adder.NightscriptFunction;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static rotn.nightscript.EventAdder.blockBreakEvents;
import static rotn.nightscript.EventAdder.checkAndRunAllFunctions;

@Mod.EventBusSubscriber
public class TickEvents {
    public static Set<NightscriptEvent> serverTickEvents = new HashSet<>();
    public static Set<NightscriptEvent> clientTickEvents = new HashSet<>();
    public static Set<NightscriptEvent> worldTickEvents = new HashSet<>();
    public static Set<NightscriptEvent> playerTickEvents = new HashSet<>();
    public static Set<NightscriptEvent> renderTickEvents = new HashSet<>();


    @SubscribeEvent
    public static void onServerTickEvent(TickEvent.ServerTickEvent event) {
        checkAndRunAllFunctions(event, serverTickEvents);
    }
    @SubscribeEvent
    public static void onClientTickEvent(TickEvent.ClientTickEvent event) {
        checkAndRunAllFunctions(event, clientTickEvents);
    }
    @SubscribeEvent
    public static void onWorldTickEvent(TickEvent.WorldTickEvent event) {
        checkAndRunAllFunctions(event, worldTickEvents);
    }
    @SubscribeEvent
    public static void onPlayerTickEvent(TickEvent.PlayerTickEvent event) {
        checkAndRunAllFunctions(event, playerTickEvents);
    }
    @SubscribeEvent
    public static void onRenderTickEvent(TickEvent.RenderTickEvent event) {
        checkAndRunAllFunctions(event, renderTickEvents);
    }
}
