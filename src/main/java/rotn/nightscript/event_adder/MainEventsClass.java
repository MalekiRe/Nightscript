package rotn.nightscript.event_adder;

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
import rotn.nightscript.parser.Pair;

import java.util.*;

import static rotn.nightscript.event_adder.functionarg.ArgumentType.*;

public class MainEventsClass {
    static Set<ActionFunction> actionFunctionSet = new HashSet<>();
    static {
        actionFunctionSet.add(new ActionFunction("kill", MainEventsClass::killFunction, PlayerObject));
        actionFunctionSet.add(new ActionFunction("giveLiteral", MainEventsClass::giveLiteralFunc, PlayerObject, STRING));
        actionFunctionSet.add(new ActionFunction("giveLiteralInt", MainEventsClass::giveLiteralIntFunc, PlayerObject, STRING, INT));


        actionFunctionSet.add(new ActionFunction("repeat", MainEventsClass::repeatFunction, INT, FUNCTION));
        actionFunctionSet.add(new ActionFunction("print", MainEventsClass::printFunction, STRING));
    }
    public static Object printFunction(Object ...objects) {
        ArrayList<Object> arrayList = (ArrayList<Object>) Arrays.stream(objects).toArray()[0];
        System.out.println(arrayList.get(0));
        return null;
    }

    public static Object repeatFunction(Object ...objects) {
        Object[] myObjects = Arrays.stream(objects).toArray();
        ArrayList<Object> arrayList = (ArrayList<Object>) myObjects[0];
        Integer numberOfTimesToRepeat = (Integer) arrayList.get(0);
        Pair<NightscriptFunction, HashMap<String, Object>> functionHashMapPair = (Pair<NightscriptFunction, HashMap<String, Object>>) arrayList.get(1);
        NightscriptFunction nightscriptFunction = functionHashMapPair.first;
        HashMap<String, Object> eventFuncArgs = functionHashMapPair.second;
        for(int i = 0; i < numberOfTimesToRepeat; i++) {
            nightscriptFunction.runFunction(eventFuncArgs);
        }
        return null;
    }
    public static Object killFunction(Object ...objects) {
        Object[] myObjects = Arrays.stream(objects).toArray();
        System.out.println(myObjects[0]);

        EntityPlayer entityPlayerMP = (EntityPlayer) ((ArrayList)(myObjects[0])).get(0);
        entityPlayerMP.setHealth(0);
        return null;
    }
    public static Object giveLiteralFunc(Object ...objects) {
        Object[] myObjects = Arrays.stream(objects).toArray();
        System.out.println(myObjects[0]);
        if(((ArrayList)(myObjects[0])).get(0) instanceof EntityPlayer) {
            EntityPlayer entityPlayerMP = (EntityPlayer) ((ArrayList) (myObjects[0])).get(0);
            String itemName = (String) ((ArrayList) (myObjects[0])).get(1);
            itemName = itemName.replaceAll("\"", "");
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName));
            entityPlayerMP.addItemStackToInventory(new ItemStack(item));
            //entityPlayerMP.addItemStackToInventory(new ItemStack(Items.DIAMOND));
        }
        return null;
    }
    public static Object giveLiteralIntFunc(Object ...objects) {
        Object[] myObjects = Arrays.stream(objects).toArray();
        System.out.println(myObjects[0]);
        if(((ArrayList)(myObjects[0])).get(0) instanceof EntityPlayer) {
            System.out.println("works");
            EntityPlayer entityPlayerMP = (EntityPlayer) ((ArrayList) (myObjects[0])).get(0);
            String itemName = (String) ((ArrayList) (myObjects[0])).get(1);
            itemName = String.copyValueOf(itemName.toCharArray()).substring(1, itemName.length()-1);
            //itemName = "minecraft:iron_ingot";
            ResourceLocation resourceLocation = new ResourceLocation(itemName);
            Item item = ForgeRegistries.ITEMS.getValue(resourceLocation);
            System.out.println("resource location name is : " + resourceLocation);
            System.out.println("item name is : " + itemName);
            Integer amount = (Integer) ((ArrayList) (myObjects[0])).get(2);
            System.out.println("amount is : " + amount);
            System.out.println("item is : " + item);
            entityPlayerMP.addItemStackToInventory(new ItemStack(item, amount));
            //entityPlayerMP.addItemStackToInventory(new ItemStack(Items.DIAMOND));
        } else {
            System.out.println("doesn't work");
            System.out.println(((ArrayList)(myObjects[0])).get(0).getClass().getName());
        }
        return null;
    }
}
