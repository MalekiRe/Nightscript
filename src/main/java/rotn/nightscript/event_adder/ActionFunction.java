package rotn.nightscript.event_adder;

import net.minecraft.entity.player.EntityPlayer;
import rotn.nightscript.event_adder.functionarg.ArgumentType;

import java.util.ArrayList;
import java.util.Arrays;

public class ActionFunction {
    String actionFunctionName;
    ArrayList<ArgumentType> argumentList = new ArrayList<ArgumentType>();
    public FunctionDo functionDo;
    public ActionFunction(String name, FunctionDo functionDo, ArgumentType... argumentTypes) {
        this.actionFunctionName = name;
        this.argumentList.addAll(Arrays.asList(argumentTypes));
        this.functionDo = functionDo;
    }
    public ActionFunction(String name, FunctionDo functionDo, ArgumentType argumentType) {
        this.actionFunctionName = name;
        this.argumentList.add(argumentType);
        this.functionDo = functionDo;
    }

}
