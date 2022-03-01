package rotn.nightscript.events;

import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import rotn.nightscript.event_adder.NightscriptEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static rotn.nightscript.EventAdder.checkAndRunAllFunctions;

@Mod.EventBusSubscriber
public class AllEvents {
    public static Map<String, Set<NightscriptEvent>> nightscriptEventsMap = new HashMap<>();


    @SubscribeEvent
    public static void onLivingAttackEvent(LivingAttackEvent event) {
        checkAndRunAllFunctions(event);
    }
    @SubscribeEvent
    public static void onLivingDeathEvent(LivingDeathEvent event) {
        checkAndRunAllFunctions(event);
    }
    @SubscribeEvent
    public static void onLivingFallEvent(LivingFallEvent event) {
        checkAndRunAllFunctions(event);
    }
    @SubscribeEvent
    public static void onLivingHurtEvent(LivingHurtEvent event) {
        checkAndRunAllFunctions(event);
    }
    @SubscribeEvent
    public static void onLivingDropEvent(LivingDropsEvent event) {
        checkAndRunAllFunctions(event);
    }
    @SubscribeEvent
    public static void onBreakEvent(BlockEvent.BreakEvent event) {
        System.out.println("block break event simple name is : " + event.getClass().getSimpleName());
        checkAndRunAllFunctions(event);
    }
    @SubscribeEvent
    public static void onEntityJoinWorldEvent(EntityJoinWorldEvent event) {
        checkAndRunAllFunctions(event);
    }
    @SubscribeEvent
    public static void onServerTickEvent(TickEvent.ServerTickEvent event) {
        checkAndRunAllFunctions(event);
    }
    @SubscribeEvent
    public static void onClientTickEvent(TickEvent.ClientTickEvent event) {
        checkAndRunAllFunctions(event);
    }
    @SubscribeEvent
    public static void onWorldTickEvent(TickEvent.WorldTickEvent event) {
        checkAndRunAllFunctions(event);
    }
    @SubscribeEvent
    public static void onPlayerTickEvent(TickEvent.PlayerTickEvent event) {
        checkAndRunAllFunctions(event);
    }
    @SubscribeEvent
    public static void onRenderTickEvent(TickEvent.RenderTickEvent event) {
        checkAndRunAllFunctions(event);
    }
}
