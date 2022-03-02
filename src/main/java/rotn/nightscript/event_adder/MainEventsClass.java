package rotn.nightscript.event_adder;

import com.sun.corba.se.impl.orbutil.graph.GraphImpl;
import io.netty.util.internal.ConcurrentSet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistrySimple;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import rotn.nightscript.event_adder.functionarg.AnonFunc;
import rotn.nightscript.event_adder.functionarg.ArgumentType;

import rotn.nightscript.event_adder.functionarg.ArgumentType.*;
import rotn.nightscript.functionalstuff.FuncArgPair;
import rotn.nightscript.functionalstuff.Memo;
import rotn.nightscript.functionalstuff.NonMemo;
import rotn.nightscript.parser.Pair;
import rotn.nightscript.parser.Triplet;
import scala.Int;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static rotn.nightscript.event_adder.functionarg.ArgumentType.*;

public class MainEventsClass {
    static Set<ActionFunction> actionFunctionSet = new HashSet<>();
    public static Map<String, Memo> memoSet = new HashMap<>();
    public static Map<String, Pair<Method, HashSet<Pair<Method, Memo>>>> autoGenMethods = new HashMap<>();
    public static Set<Pair<Integer, AnonFunc>> functionStack = new ConcurrentSet<>();
    static {
        memoSet.put("addInt", new Memo((event, u) -> {
            List<Object> objects = (List<Object>) u;
            int first = (int) evaluateOrGetThing(objects.get(0), event);
            int second = (int) evaluateOrGetThing(objects.get(1), event);
            return first+second;
        }));
        memoSet.put("addDouble", new Memo((event, u) -> {
            List<Object> objects = (List<Object>) u;
            double first = (double) evaluateOrGetThing(objects.get(0), event);
            double second = (double) evaluateOrGetThing(objects.get(1), event);
            return first+second;
        }));
        memoSet.put("addFloat", new Memo((event, u) -> {
            List<Object> objects = (List<Object>) u;
            float first = (float) evaluateOrGetThing(objects.get(0), event);
            float second = (float) evaluateOrGetThing(objects.get(1), event);
            return first+second;
        }));
        memoSet.put("addStrings", new Memo((event, u) -> {
            List<Object> objects = (List<Object>) u;
            String first = (String) evaluateOrGetThing(objects.get(0), event);
            String second = (String) evaluateOrGetThing(objects.get(1), event);
            return first+second;
        }));
        memoSet.put("print", new NonMemo((event, u) -> {
            List objects = (List) u;
            for(Object object : objects) {
                System.out.println("nightscript is printing : " + evaluateOrGetThing(object, event));
            }
            return null;
        }));
        memoSet.put("repeat", new NonMemo((event, u) -> {
            List objects = (List) u;
            for(int i = 0; i < (Integer) objects.get(0); i++) {
                ((FuncArgPair)objects.get(1)).evaluate(event);
            }
            return null;
        }));
        memoSet.put("if", new Memo((event, args) -> {
            List arguments = (List) args;
            if((boolean) evaluateOrGetThing(arguments.get(0), event)) {
                for(int i = 1; i < arguments.size(); i++) {
                    ((FuncArgPair)arguments.get(i)).evaluate(event);
                }
            }
            return null;
        }));
        memoSet.put("equals", new Memo((event, args) -> {
            List arguments = (List) args;
            Object object1 = evaluateOrGetThing(arguments.get(0), event);
            Object object2 = evaluateOrGetThing(arguments.get(1), event);
            return object1.equals(object2);
        }));
        memoSet.put("not", new Memo((event, args) -> ! (boolean) evaluateOrGetThing(((List)args).get(0), event)));
        memoSet.put("delay", new NonMemo((event, args) -> {

            functionStack.add(Pair.of((Integer) ((List) args).get(0), () -> {
                ((FuncArgPair) ((List) args).get(1)).evaluate(event);
            }));

            return null;
        }));
        memoSet.put("createBlockStatewithName", new NonMemo((event, args) -> {
            List arguments = (List) args;
            String name = (String) evaluateOrGetThing(arguments.get(0), event);
            return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(name)).getDefaultState();
        }));
        memoSet.put("setBlockStatewithBlockPos", new NonMemo((event, args) -> {
            List arguments = (List) args;
            World world = (World) evaluateOrGetThing(arguments.get(0), event);
            BlockPos blockPos = (BlockPos) evaluateOrGetThing(arguments.get(1), event);
            IBlockState blockState = (IBlockState) evaluateOrGetThing(arguments.get(2), event);
            world.setBlockState(blockPos, blockState);
            return null;
        }));
        memoSet.put("do", new NonMemo((event, args)-> {
            List<Object> myArgs = evaluateOrGetThings(args, event);
            return null;
        }));
    }
    public static List<Object> evaluateOrGetThings(Object object, Event event) {
        List<Object> list = ((List<Object>) object);
        List<Object> returnList = new ArrayList<>();
        for(Object object1 : list) {
            returnList.add(evaluateOrGetThing(object1, event));
        }
        return returnList;
    }
    public static Object evaluateOrGetThing(Object object, Event event) {
        if(object instanceof FuncArgPair) {
            return ((FuncArgPair)object).evaluate(event);
        }
        return object;
    }

}
