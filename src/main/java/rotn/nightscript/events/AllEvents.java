package rotn.nightscript.events;

import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import rotn.nightscript.event_adder.NightscriptEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static rotn.nightscript.EventAdder.checkAndRunAllFunctions;

@Mod.EventBusSubscriber
public class AllEvents {
    public static Map<String, Set<NightscriptEvent>> nightscriptEventsMap = new HashMap<>();

    @SubscribeEvent
    public static void onEvent(PlayerEvent.ItemPickupEvent event) {checkAndRunAllFunctions(event);}

}
