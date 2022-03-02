package rotn.nightscript.events;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import rotn.nightscript.event_adder.MainEventsClass;
import rotn.nightscript.event_adder.functionarg.AnonFunc;
import rotn.nightscript.parser.Pair;
import sun.awt.Mutex;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import static rotn.nightscript.EventAdder.checkAndRunAllFunctions;

@Mod.EventBusSubscriber
public class CustomEvents {
    @SubscribeEvent
    public static void onEvent(TickEvent.ServerTickEvent event) {

        Set<Pair<Integer, AnonFunc>> removeFuncs = new HashSet<>();
        for (Pair<Integer, AnonFunc> funcPair : MainEventsClass.functionStack) {
            if (funcPair.first == 0) {
                funcPair.second.run();
                removeFuncs.add(funcPair);
            } else {
                funcPair.first--;
            }
        }
        for (Pair<Integer, AnonFunc> funcPair : removeFuncs) {
            MainEventsClass.functionStack.remove(funcPair);
        }


    }
}
