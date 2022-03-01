package rotn.nightscript.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import rotn.nightscript.events.AllEvents;
import rotn.nightscript.parser.NightscriptParser;

public class TestCommand extends CommandBase {

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException {

        //EchoCommandMod.logger.info("execute called");

        if (params != null) {
            String message = "Reloading Nightscript";
            TextComponentString text = new TextComponentString(message);
            text.getStyle().setColor(TextFormatting.DARK_BLUE);
            sender.sendMessage(text);
            AllEvents.nightscriptEventsMap.clear();
            String errorFile = NightscriptParser.doNightscriptParsingAndSetup();
            if(!errorFile.equals("none")) {
                text = new TextComponentString("failed reloading nightscript on file : " + errorFile);
                text.getStyle().setColor(TextFormatting.RED);
                sender.sendMessage(text);
            } else {
                text = new TextComponentString("succesfully hot reloaded nightscript");
                text.getStyle().setColor(TextFormatting.DARK_BLUE);
                sender.sendMessage(text);
            }
        }
    }

    @Override
    public String getName() {
        return "HotReload";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "command.nightscript.usage";
    }
}