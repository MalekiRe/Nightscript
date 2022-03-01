package rotn.nightscript.functionalstuff;

import net.minecraftforge.fml.common.eventhandler.Event;

import java.lang.reflect.InvocationTargetException;

public interface Lazy<T, U> {
    T eval(Event event, U u) throws InvocationTargetException, IllegalAccessException;
}
