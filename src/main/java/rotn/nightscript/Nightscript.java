package rotn.nightscript;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import rotn.nightscript.commands.TestCommand;
import rotn.nightscript.event_adder.MainEventsClass;
import rotn.nightscript.events.AllEvents;
import rotn.nightscript.functionalstuff.Memo;
import rotn.nightscript.parser.NightscriptParser;
import rotn.nightscript.parser.Pair;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

import static rotn.nightscript.EventAdder.*;
import static rotn.nightscript.event_adder.MainEventsClass.*;
import static rotn.nightscript.event_adder.MainEventsClass.memoSet;

@Mod(
        modid = Nightscript.MOD_ID,
        name = Nightscript.MOD_NAME,
        version = Nightscript.VERSION
)
public class Nightscript {

    public static final String MOD_ID = "nightscript";
    public static final String MOD_NAME = "Nightscript";
    public static final String VERSION = "1.0-SNAPSHOT";
    /**
     * This is the instance of your mod as created by Forge. It will never be null.
     */
    @Mod.Instance(MOD_ID)
    public static Nightscript INSTANCE;

    static void doSetup() {
        HashSet<String> mySet = new HashSet<>();
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("the-file-name.txt", "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        for(Method method : AllEvents.class.getMethods()) {
            for(Class parameterType : method.getParameterTypes()) {
                writer.println(EventAdder.generateClassString(parameterType));
//                if(!eventFunctionsMap.containsKey(Event.class)) {
//                    System.out.println("doesn't contain event : " + Event.class);
//                    addEventFunctionsToMap(Event.class);
//                }
                if(Event.class.isAssignableFrom(parameterType)) {
                    getEventFunctions((Class<? extends Event>) parameterType);
                    for(String s : eventFunctionsMap.get(parameterType).keySet()) {
                        if(!mySet.contains(s)) {
                            writer.println("#"+s);
                            mySet.add(s);
                        }
                    }
                }
            }
        }
        for(String s : autoGenMethods.keySet()) {
            writer.print(s.substring(1) + "(");
            PrintWriter finalWriter = writer;
            for (Iterator<Pair<Method, Memo>> iterator = autoGenMethods.get(s).second.iterator(); iterator.hasNext(); ) {
                Pair<Method, Memo> myPair = iterator.next();
                if (!Modifier.isStatic(myPair.first.getModifiers())) {
                    finalWriter.print("@"+s.substring(1, s.indexOf('.')));
                    if (myPair.first.getParameters().length != 0) {
                        finalWriter.print(", ");
                    }
                }
                Parameter[] parameters = myPair.first.getParameters();
                for (int i = 0; i < parameters.length; i++) {
                    Parameter p = parameters[i];
                    finalWriter.print("@"+p.getType().getSimpleName());
                    if (i + 1 < parameters.length) {
                        finalWriter.print(", ");
                    }
                }
                if (iterator.hasNext()) {
                    finalWriter.print(" | ");
                }
            }
            finalWriter.println(")");
        }
        for(String s : autoGenConstructors.keySet()) {
            writer.print(s.substring(1) + "(");
            PrintWriter finalWriter = writer;
            for (Iterator<Pair<Constructor, Memo>> iterator = autoGenConstructors.get(s).iterator(); iterator.hasNext(); ) {
                Pair<Constructor, Memo> myPair = iterator.next();
                Class[] parameters = myPair.first.getParameterTypes();
                for (int i = 0; i < parameters.length; i++) {
                    Class p = parameters[i];
                    finalWriter.print("@"+p.getSimpleName());
                    if (i + 1 < parameters.length) {
                        finalWriter.print(", ");
                    }
                }
                if (iterator.hasNext()) {
                    finalWriter.print(" | ");
                }
            }
            finalWriter.println(")");
        }
        for(String s : memoSet.keySet()) {
            writer.print(s);
        }
        for(String s : memoSet.keySet()) {
            writer.print(s + "(");
            for (Iterator<Class> iterator = Arrays.stream(memoSet.get(s).first).iterator(); iterator.hasNext(); ) {
                Class p = iterator.next();
                writer.print("@"+p.getSimpleName());
                if (iterator.hasNext()) {
                    writer.print(", ");
                }
            }
            writer.println(")");
        }
        writer.close();
    }
        /**
     * This is the first initialization event. Register tile entities here.
     * The registry events below will have fired prior to entry to this method.
     */
    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        String errorFile = NightscriptParser.doNightscriptParsingAndSetup();
        if(!errorFile.equals("none")) {
            System.err.println("error unable to load Nightscript file : " + errorFile);
        }
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new TestCommand());
    }

    /**
     * This is the second initialization event. Register custom recipes
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(AllEvents.class);
    }

    /**
     * This is the final initialization event. Register actions from other mods here
     */
    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event) {
        MainEventsClass mainEventsClass = new MainEventsClass();
        doSetup();
    }

    /**
     * Forge will automatically look up and bind blocks to the fields in this class
     * based on their registry name.
     */
    @GameRegistry.ObjectHolder(MOD_ID)
    public static class Blocks {
      /*
          public static final MySpecialBlock mySpecialBlock = null; // placeholder for special block below
      */
    }

    /**
     * Forge will automatically look up and bind items to the fields in this class
     * based on their registry name.
     */
    @GameRegistry.ObjectHolder(MOD_ID)
    public static class Items {
      /*
          public static final ItemBlock mySpecialBlock = null; // itemblock for the block above
          public static final MySpecialItem mySpecialItem = null; // placeholder for special item below
      */
    }

    /**
     * This is a special class that listens to registry events, to allow creation of mod blocks and items at the proper time.
     */
    @Mod.EventBusSubscriber
    public static class ObjectRegistryHandler {
        /**
         * Listen for the register event for creating custom items
         */
        @SubscribeEvent
        public static void addItems(RegistryEvent.Register<Item> event) {
           /*
             event.getRegistry().register(new ItemBlock(Blocks.myBlock).setRegistryName(MOD_ID, "myBlock"));
             event.getRegistry().register(new MySpecialItem().setRegistryName(MOD_ID, "mySpecialItem"));
            */
        }

        /**
         * Listen for the register event for creating custom blocks
         */
        @SubscribeEvent
        public static void addBlocks(RegistryEvent.Register<Block> event) {
           /*
             event.getRegistry().register(new MySpecialBlock().setRegistryName(MOD_ID, "mySpecialBlock"));
            */
        }
    }
    /* EXAMPLE ITEM AND BLOCK - you probably want these in separate files
    public static class MySpecialItem extends Item {

    }

    public static class MySpecialBlock extends Block {

    }
    */
}
