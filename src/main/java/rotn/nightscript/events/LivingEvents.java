package rotn.nightscript.events;

import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import rotn.nightscript.event_adder.NightscriptEvent;

import java.util.HashSet;
import java.util.Set;

import static rotn.nightscript.EventAdder.blockBreakEvents;
import static rotn.nightscript.EventAdder.checkAndRunAllFunctions;

public class LivingEvents {
    public static Set<NightscriptEvent> livingAttackEvents = new HashSet<>();
    public static Set<NightscriptEvent> livingDeathEvents = new HashSet<>();
    public static Set<NightscriptEvent> livingFallEvents = new HashSet<>();
    public static Set<NightscriptEvent> livingHurtEvents = new HashSet<>();
    public static Set<NightscriptEvent> livingDropEvents = new HashSet<>();

    @SubscribeEvent
    public static void onLivingAttackEvent(LivingAttackEvent event) {
        checkAndRunAllFunctions(event, livingAttackEvents);
    }
    @SubscribeEvent
    public static void onLivingDeathEvent(LivingDeathEvent event) {
        checkAndRunAllFunctions(event, livingDeathEvents);
    }
    @SubscribeEvent
    public static void onLivingFallEvent(LivingFallEvent event) {
        checkAndRunAllFunctions(event, livingFallEvents);
    }
    @SubscribeEvent
    public static void onLivingHurtEvent(LivingHurtEvent event) {
        checkAndRunAllFunctions(event, livingHurtEvents);
    }
    @SubscribeEvent
    public static void onLivingDropEvent(LivingDropsEvent event) {
        checkAndRunAllFunctions(event, livingDropEvents);
    }
}
