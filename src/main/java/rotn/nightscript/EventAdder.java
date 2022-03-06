package rotn.nightscript;

import net.minecraft.block.Block;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import rotn.nightscript.event_adder.MainEventsClass;
import rotn.nightscript.event_adder.NightscriptEvent;
import rotn.nightscript.event_adder.NightscriptEventArgument;
import rotn.nightscript.event_adder.NightscriptFunction;
import rotn.nightscript.functionalstuff.Memo;
import rotn.nightscript.functionalstuff.NonMemo;
import rotn.nightscript.parser.NodeToken;
import rotn.nightscript.parser.Pair;
import rotn.nightscript.parser.Phrase;
import rotn.nightscript.parser.Triplet;

import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

import static rotn.nightscript.event_adder.MainEventsClass.*;
import static rotn.nightscript.events.AllEvents.*;

public class EventAdder {
    public static Set<NightscriptEvent> blockBreakEvents = new HashSet<>();
    public static Set<NightscriptEvent> playerInteractWithBlock = new HashSet<>();
    public static Set<NightscriptEvent> bonemealEvents = new HashSet<>();
    public static Set<NightscriptEvent> playerAttack = new HashSet<>();

    public static Map<Class<? extends Event>, Map<String, Memo>> eventFunctionsMap = new HashMap<>();
    public static Map<Class, Map<String, Memo>> allMethodReturnTypeFunctions = new HashMap<>();
    public static Set<Object> alreadyAddedMethods = new HashSet<>();
    public static void addNodeTokensToEvent(NodeToken token) {
        NodeToken.printTree(token, 0);
        if (token.phrase != Phrase.EVENT) {
            System.out.println("phrase : " + token.phrase + " isn't event getting children");
            for (NodeToken token1 : token.childTokens) {
                addNodeTokensToEvent(token1);
            }
            return;
        }
        NodeToken identifierToken = token.childTokens.get(0);
        for(int i = 0; i < token.childTokens.size(); i++) {
            if(identifierToken.phrase == Phrase.identifier) {
                break;
            }
            identifierToken = token.childTokens.get(i);
        }
        if(!nightscriptEventsMap.containsKey(identifierToken.relatedString)) {
            nightscriptEventsMap.put(identifierToken.relatedString, new HashSet<>());
        }
        nightscriptEventsMap.get(identifierToken.relatedString).add(new NightscriptEvent(token));
        System.out.println("identifier is : " + identifierToken.relatedString);
    }
    public static Map<String, Memo> getEventFunctions(Event event) {
        return getEventFunctions(event.getClass());
    }
    public static Map<String, Memo> getEventFunctions(Class< ? extends Event> event) {
        if(!eventFunctionsMap.containsKey(event) && event != null) {
            System.out.println("doesn't contain event : " + event);
            addEventFunctionsToMap(event);
        }
        return eventFunctionsMap.get(event);
    }
    public static void recursiveAdd(Method method) {
        if(!alreadyAddedMethods.contains(method)) {
            alreadyAddedMethods.add(method);
            if(!autoGenMethods.containsKey("@" + method.getReturnType().getSimpleName() + ".CREATE")) {
                autoGenConstructors.put("@" + method.getReturnType().getSimpleName() + ".CREATE", new HashSet<Pair<Constructor, Memo>>());
            }
            for(Field field : method.getReturnType().getFields()) {
                String addingString = "@"+method.getReturnType().getSimpleName() + ".SET_" + field.getName();
                String secondAddedString = "@"+method.getReturnType().getSimpleName() + ".GET_" + field.getName();
                System.out.println("added string 1 is : " + addingString);
                String testString = method.getReturnType().getSimpleName() + field.getName();
                System.out.println("test string : " + testString);
                if(!alreadyAddedMethods.contains(testString)) {
                    alreadyAddedMethods.add(testString);
                    System.out.println("are adding : " + testString);
                    try {
                        autoGenMethods.put(addingString, Pair.of(method, new HashSet<>()));
                        autoGenMethods.get(addingString).second.add(Pair.of(field.getClass().getDeclaredMethod("get", Object.class), new NonMemo((event, args) -> {
                            Object obj = field.get(evaluateOrGetThings(args, event).get(0));
                            System.out.println("Here look : " + obj);
                            return obj;
                        })));
                        autoGenMethods.put(secondAddedString, Pair.of(method, new HashSet<>()));
                        autoGenMethods.get(secondAddedString).second.add(Pair.of(field.getClass().getDeclaredMethod("set", Object.class, Object.class), new NonMemo((event, args) -> {
                            List<Object> myList = evaluateOrGetThings(args, event);
                            field.set(myList.get(0), myList.get(1));
                            System.out.println("not supposed to be here");
                            return null;
                        })));
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                    for(Method method1 : field.getType().getMethods()) {
                        recursiveAdd(method1);
                    }
                }
            }
            for(Constructor constructor : method.getReturnType().getConstructors()) {
                autoGenConstructors.get("@" + method.getReturnType().getSimpleName() + ".CREATE").add(Pair.of(constructor, new Memo((event, args) -> {
                    try {
//                        for(Class s : constructor.getParameterTypes()) {
//                            System.out.println("parameter types are : " +s);
//                        }
//                        for(Object o : (List)args) {
//                            System.out.println("objects are : " + o);
//                        }
                        return constructor.newInstance(((List<?>) args).toArray());
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    }
                    return null;//TODO add error here
                })));

            }
            for(Method method2 : method.getReturnType().getDeclaredMethods()) {
                String adder = "";
                String savedName = "@"+adder+method.getReturnType().getSimpleName() + "." + method2.getName();
                if(!autoGenMethods.containsKey(savedName)) {
                    autoGenMethods.put(savedName, new Pair<>(method, new HashSet<>()));
                }
                Set<Pair<Method, Memo>> mySet = autoGenMethods.get(savedName).second;
                boolean flag = false;
                for(Pair<Method, Memo> setPair : mySet) {
                    if(setPair.first.equals(method2)) {
                        flag = true;
                        break;
                    }
                }
                if(flag) continue;
                mySet.add(Pair.of(method2, new NonMemo((event2, args) -> {
                    List<Object> objects = MainEventsClass.evaluateOrGetThings(args, event2);
                    System.out.println(method2.toGenericString());
                    try {
                        System.out.println(objects);
                        System.out.println(objects.get(0));
                        System.out.println(objects.get(0).getClass());
                        System.out.println(objects.get(0).getClass().getMethod(method2.getName(), method2.getParameterTypes()).getName());
                        return objects.get(0).getClass().getMethod(method2.getName(), method2.getParameterTypes()).invoke(objects.get(0), objects.subList(1, objects.size()).toArray());
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                    return method2.invoke(objects.get(0), objects.subList(1, objects.size()).toArray());
                })));

                recursiveAdd(method2);
            }
        }
    }
    public static void addEventFunctionsToMap(Class event) {
        Map<String, Memo> myMap = new HashMap<>();
        Class myClass = event;
        while(myClass != Event.class && myClass != null) {
            autoGenMethods.put("@"+event.getSimpleName()+"STATIC_", Pair.of(event.getClass().))
            for(Method method : myClass.getDeclaredMethods()) {
//                if(method.getParameters().length == 0) {
//                    myMap.put(method.getName(), new NonMemo((event1, args) -> method.invoke(event1, ((Collection)args).toArray())));
//                } else {
                myMap.put(method.getName(), new NonMemo((event1, args) -> method.invoke(event1, ((Collection) args).toArray())));
                //Adding all data types to array
                for(Field field : event.getFields()) {
                    String addingString = "@"+event.getSimpleName() + ".SET_" + field.getName();
                    String secondAddedString = "@"+event.getSimpleName() + ".GET_" + field.getName();
                    System.out.println("added string 1 is : " + addingString);
                    String testString = event.getSimpleName() + field.getName();
                    System.out.println("test string : " + testString);
                    if(!alreadyAddedMethods.contains(testString)) {
                        alreadyAddedMethods.add(testString);
                        System.out.println("are adding : " + testString);
                        try {
                            autoGenMethods.put(addingString, Pair.of(method, new HashSet<>()));
                            autoGenMethods.get(addingString).second.add(Pair.of(field.getClass().getDeclaredMethod("get", Object.class), new NonMemo((event1, args) -> {
                                Object obj = field.get(evaluateOrGetThings(args, event1).get(0));
                                System.out.println("Here look : " + obj);
                                return obj;
                            })));
                            autoGenMethods.put(secondAddedString, Pair.of(method, new HashSet<>()));
                            autoGenMethods.get(secondAddedString).second.add(Pair.of(field.getClass().getDeclaredMethod("set", Object.class, Object.class), new NonMemo((event1, args) -> {
                                List<Object> myList = evaluateOrGetThings(args, event1);
                                field.set(myList.get(0), myList.get(1));
                                System.out.println("not supposed to be here");
                                return null;
                            })));
                            Memo memo1 = autoGenMethods.get(addingString).second.stream().findFirst().get().second;
                            Memo memo2 = autoGenMethods.get(secondAddedString).second.stream().findFirst().get().second;
                            myMap.put(addingString, memo1);
                            myMap.put(secondAddedString, memo2);
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        }
                    }
                    for(Method method1 : field.getType().getMethods()) {
                        recursiveAdd(method1);
                    }
                }
                recursiveAdd(method);

                //}
            }

            myClass = myClass.getSuperclass();
        }
        if(myClass != null) {
            for (Method method : myClass.getDeclaredMethods()) {
                myMap.put(method.getName(), new NonMemo((event1, args) -> method.invoke(event1, ((Collection) args).toArray())));
            }
        }
        eventFunctionsMap.put(event, myMap);
    }
    public static String generateClassString(Class class1) {
        String s = "";
        if(class1.isMemberClass()) {
            s += class1.getEnclosingClass().getSimpleName() + ".";
        }
        s += class1.getSimpleName();
        return s;
    }
    static Map<Class< ? extends Event>, String> cache = new HashMap<>();
    static boolean first = true;
    public static void checkAndRunAllFunctions(Event event) {
        if(first) {
            first = false;
            addEventFunctionsToMap(ForgeRegistries.class);
            addEventFunctionsToMap(Block.class);
            addEventFunctionsToMap(ForgeRegistries.BLOCKS.getClass());
            addEventFunctionsToMap(ForgeRegistries.ITEMS.getClass());
            addEventFunctionsToMap(ForgeRegistries.SOUND_EVENTS.getClass());
            addEventFunctionsToMap(ForgeRegistries.BIOMES.getClass());
            addEventFunctionsToMap(ForgeRegistries.ENCHANTMENTS.getClass());
            addEventFunctionsToMap(ForgeRegistries.POTION_TYPES.getClass());
            addEventFunctionsToMap(ForgeRegistries.POTIONS.getClass());
            addEventFunctionsToMap(ForgeRegistries.RECIPES.getClass());
            addEventFunctionsToMap(ForgeRegistries.VILLAGER_PROFESSIONS.getClass());
        }
        Map<String, Memo> eventFunctions = getEventFunctions(event);
        if(!cache.containsKey(event.getClass())) {
            cache.put(event.getClass(), generateClassString(event.getClass()));
        }
        if(!nightscriptEventsMap.containsKey(cache.get(event.getClass()))) {
            //This happens if we actually don't have anything declared for the event
            return;
        }
        for(NightscriptEvent nightscriptEvent : nightscriptEventsMap.get(cache.get(event.getClass()))) {
            for(NightscriptFunction function : nightscriptEvent.nightscriptFunctions) {
                //System.out.println("running : " + function.functionIdentifier);
                function.getLazyFunction(eventFunctions).evaluate(event);
            }
        }
    }

}
