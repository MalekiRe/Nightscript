package rotn.nightscript.event_adder;

import com.sun.corba.se.impl.orbutil.graph.GraphImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistrySimple;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import rotn.nightscript.event_adder.functionarg.ArgumentType;

import rotn.nightscript.event_adder.functionarg.ArgumentType.*;
import rotn.nightscript.functionalstuff.FuncArgPair;
import rotn.nightscript.functionalstuff.Memo;
import rotn.nightscript.functionalstuff.NonMemo;
import rotn.nightscript.parser.Pair;
import scala.Int;

import java.util.*;

import static rotn.nightscript.event_adder.functionarg.ArgumentType.*;

public class MainEventsClass {
    static Set<ActionFunction> actionFunctionSet = new HashSet<>();
    public static Map<String, Memo> memoSet = new HashMap<>();
    static {
//        actionFunctionSet.add(new ActionFunction("kill", MainEventsClass::killFunction, PlayerObject));
//        actionFunctionSet.add(new ActionFunction("giveLiteral", MainEventsClass::giveLiteralFunc, PlayerObject, STRING));
//        actionFunctionSet.add(new ActionFunction("giveLiteralInt", MainEventsClass::giveLiteralIntFunc, PlayerObject, STRING, INT));
//
//
//        actionFunctionSet.add(new ActionFunction("repeat", MainEventsClass::repeatFunction, INT, FUNCTION));
//        actionFunctionSet.add(new ActionFunction("print", MainEventsClass::printFunction, STRING));

        memoSet.put("1", new Memo((event, u) -> {
            return 1;
        }));
        memoSet.put("add", new Memo((event, u) -> {
            List<Object> objects = (List<Object>) u;
            double first = 0;
            double second = 0;
            if (objects.get(0) instanceof FuncArgPair) {
                first = (double) ((FuncArgPair) objects.get(0)).evaluate(event);
            } else {
                first = (double) objects.get(0);
            }
            if (objects.get(1) instanceof FuncArgPair) {
                second = (double) ((FuncArgPair) objects.get(1)).evaluate(event);
            } else {
                second = (double) objects.get(1);
            }
            return first+second;
        }));
        memoSet.put("print", new NonMemo((event, u) -> {
            List objects = (List) u;
            for(Object object : objects) {
                if (object instanceof FuncArgPair) {
                    System.out.println("instance of FuncArgPair");
                    System.out.println("I AM PRINTING : " + ((FuncArgPair) object).evaluate(event));
                } else {
                    System.out.println("I AM PRINTING : " + object);
                }
            }
            return null;
        }));
        memoSet.put("repeat", new NonMemo((event, u) -> {
            System.out.println(u);
            List objects = (List) u;
            System.out.println("repeat args is : ");
            System.out.println(objects);
            for(int i = 0; i < (Integer) objects.get(0); i++) {
                ((FuncArgPair)objects.get(1)).evaluate(event);
            }
            return null;
        }));

    }

}
