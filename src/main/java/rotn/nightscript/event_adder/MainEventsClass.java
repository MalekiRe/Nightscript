package rotn.nightscript.event_adder;


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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static rotn.nightscript.event_adder.functionarg.ArgumentType.*;

public class MainEventsClass {
    static Set<ActionFunction> actionFunctionSet = new HashSet<>();
    public static Map<String, Pair<Class[], Memo>> memoSet = new HashMap<>();
    public static Map<String, Pair<Method, HashSet<Pair<Method, Memo>>>> autoGenMethods = new HashMap<>();
    public static Map<String, HashSet<Pair<Constructor, Memo>>> autoGenConstructors = new HashMap<>();
    public static Set<Pair<Integer, AnonFunc>> functionStack = new ConcurrentSet<>();

    public static void makeNewMethod(String name, runThings func, Class... classes) {
        memoSet.put(name, Pair.of(classes, new NonMemo((event, args) -> {
            return func.accept(evaluateOrGetThings(args, event).toArray());
        })));
    }
    public static void makeNewMethod(String name, Memo nonMemo, Class... classes) {
        memoSet.put(name, Pair.of(classes, nonMemo));
    }
    @FunctionalInterface
    interface runThings {
        Object accept(Object ...objects);
    }
    public static int addInts(int i1, int i2) {
        return i1 + i2;
    }

    static {
        makeNewMethod("addInts", (i1) -> {return addInts((Integer) i1[0], (Integer)i1[1]);}, Integer.class, Integer.class);
        makeNewMethod("addDoubles", (i1) -> {return (Double)i1[0] + (Double)i1[1];}, Double.class, Double.class);
        makeNewMethod("addStrings", (i1) -> {return (String)i1[0] + (String)i1[1];}, String.class, String.class);
        makeNewMethod("addFloats", (i1) -> {return (Float)i1[0] + (Float)i1[1];}, Float.class, Float.class);
        makeNewMethod("print", (i1) -> {System.out.println("Printing : " + i1[0]); return null;}, Object.class);
        makeNewMethod("getBlockFromID", (i1) -> {return ForgeRegistries.BLOCKS.getValue((ResourceLocation) i1[0]).getDefaultState();}, ResourceLocation.class);
        makeNewMethod("getItemFromID", (i1) -> {return ForgeRegistries.ITEMS.getValue((ResourceLocation) i1[0]);}, ResourceLocation.class);
        memoSet.put("repeat", Pair.of(new Class[]{Integer.class, Object.class}, new NonMemo((event, args) -> {
            List objects = (List) args;
            for(int i = 0; i < (Integer) objects.get(0); i++) {
                ((FuncArgPair)objects.get(1)).evaluate(event);
            }
            return null;
        })));
        makeNewMethod("if",  new NonMemo((event, args) -> {
            List arguments = (List) args;
            if((boolean) evaluateOrGetThing(arguments.get(0), event)) {
                return evaluateOrGetThing(arguments.get(1), event);
            }
            return null;
        }), boolean.class, Object.class);
        makeNewMethod("equals",  new Memo((event, args) -> {
            List arguments = (List) args;
            Object object1 = evaluateOrGetThing(arguments.get(0), event);
            Object object2 = evaluateOrGetThing(arguments.get(1), event);
            return object1.equals(object2);
        }), boolean.class, boolean.class);
        makeNewMethod("do",  new NonMemo((event, args) -> {
            List<Object> myArgs = evaluateOrGetThings(args, event);
            myArgs.add(0);
            return null;
        }), List.class);
        makeNewMethod("delay",  new NonMemo((event, args) -> {
            functionStack.add(Pair.of((Integer) ((List) args).get(0), () -> {
                ((FuncArgPair) ((List) args).get(1)).evaluate(event);
            }));
            return null;
        }), List.class);
        memoSet.put("not", Pair.of(new Class[]{boolean.class}, new Memo((event, args) -> ! (boolean) evaluateOrGetThing(((List)args).get(0), event))));

//        memoSet.put("addInts", new Memo((event, u) -> {
//            List<Object> objects = (List<Object>) u;
//            int first = (int) evaluateOrGetThing(objects.get(0), event);
//            int second = (int) evaluateOrGetThing(objects.get(1), event);
//            return first+second;
//        })));
//        memoSet.put("addDoubles", new Memo((event, u) -> {
//            List<Object> objects = (List<Object>) u;
//            double first = (double) evaluateOrGetThing(objects.get(0), event);
//            double second = (double) evaluateOrGetThing(objects.get(1), event);
//            return first+second;
//        }));
//        memoSet.put("addFloats", new Memo((event, u) -> {
//            List<Object> objects = (List<Object>) u;
//            float first = (float) evaluateOrGetThing(objects.get(0), event);
//            float second = (float) evaluateOrGetThing(objects.get(1), event);
//            return first+second;
//        }));
//        memoSet.put("addStrings", new Memo((event, u) -> {
//            List<Object> objects = (List<Object>) u;
//            String first = (String) evaluateOrGetThing(objects.get(0), event);
//            String second = (String) evaluateOrGetThing(objects.get(1), event);
//            return first+second;
//        }));
//        memoSet.put("print", new NonMemo((event, u) -> {
//            List objects = (List) u;
//            for(Object object : objects) {
//                System.out.println("nightscript is printing : " + evaluateOrGetThing(object, event));
//            }
//            return null;
//        }));
//        memoSet.put("repeat", new NonMemo((event, u) -> {
//            List objects = (List) u;
//            for(int i = 0; i < (Integer) objects.get(0); i++) {
//                ((FuncArgPair)objects.get(1)).evaluate(event);
//            }
//            return null;
//        }));
//        memoSet.put("if", new Memo((event, args) -> {
//            List arguments = (List) args;
//            if((boolean) evaluateOrGetThing(arguments.get(0), event)) {
//                for(int i = 1; i < arguments.size(); i++) {
//                    ((FuncArgPair)arguments.get(i)).evaluate(event);
//                }
//            }
//            return null;
//        }));
//        memoSet.put("equals", new Memo((event, args) -> {
//            List arguments = (List) args;
//            Object object1 = evaluateOrGetThing(arguments.get(0), event);
//            Object object2 = evaluateOrGetThing(arguments.get(1), event);
//            return object1.equals(object2);
//        }));
//        memoSet.put("not", new Memo((event, args) -> ! (boolean) evaluateOrGetThing(((List)args).get(0), event)));
//        memoSet.put("delay", new NonMemo((event, args) -> {
//            functionStack.add(Pair.of((Integer) ((List) args).get(0), () -> {
//                ((FuncArgPair) ((List) args).get(1)).evaluate(event);
//            }));
//            return null;
//        }));
//        memoSet.put("do", new NonMemo((event, args)-> {
//            List<Object> myArgs = evaluateOrGetThings(args, event);
//            return null;
//        }));
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
