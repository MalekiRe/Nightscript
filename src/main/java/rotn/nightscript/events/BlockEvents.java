package rotn.nightscript.events;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import rotn.nightscript.EventAdder;
import rotn.nightscript.event_adder.NightscriptEvent;
import rotn.nightscript.event_adder.NightscriptEventArgument;
import rotn.nightscript.event_adder.NightscriptFunction;

import java.util.HashMap;
import java.util.Map;

import static rotn.nightscript.EventAdder.*;

@Mod.EventBusSubscriber
public class BlockEvents {
    @SubscribeEvent
    public static void onBreakEvent(BlockEvent.BreakEvent event) {
        checkAndRunAllFunctions(event, blockBreakEvents);
    }
}
