package rotn.nightscript.events;

import net.minecraftforge.event.entity.EntityJoinWorldEvent;
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

public class MiscEvents {
    static Set<NightscriptEvent> entityJoinWorldEvents = new HashSet<>();
    @SubscribeEvent
    public static void onEntityJoinWorldEvent(EntityJoinWorldEvent event) {
        checkAndRunAllFunctions(event, entityJoinWorldEvents);
    }
}
