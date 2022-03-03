package rotn.nightscript.events;

import net.minecraftforge.client.event.*;
import net.minecraftforge.client.event.sound.PlaySoundSourceEvent;
import net.minecraftforge.client.event.sound.PlayStreamingSourceEvent;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.brewing.PotionBrewEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.minecart.MinecartCollisionEvent;
import net.minecraftforge.event.entity.minecart.MinecartInteractEvent;
import net.minecraftforge.event.entity.minecart.MinecartUpdateEvent;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.terraingen.*;
import net.minecraftforge.event.world.*;
import net.minecraftforge.fluids.FluidEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.oredict.OreDictionary;
import rotn.nightscript.EventAdder;
import rotn.nightscript.event_adder.NightscriptEvent;
import rotn.nightscript.functionalstuff.Memo;
import rotn.nightscript.parser.Pair;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.*;

import static rotn.nightscript.EventAdder.checkAndRunAllFunctions;
import static rotn.nightscript.event_adder.MainEventsClass.autoGenMethods;
import static rotn.nightscript.event_adder.MainEventsClass.memoSet;

@Mod.EventBusSubscriber
public class AllEvents {
    public static Map<String, Set<NightscriptEvent>> nightscriptEventsMap = new HashMap<>();
    @SubscribeEvent
    public static void onEvent(InputEvent.MouseInputEvent event) {checkAndRunAllFunctions(event);}

    @SubscribeEvent
    public static void onEvent(InputEvent.KeyInputEvent event) {checkAndRunAllFunctions(event);}

    @SubscribeEvent
    public static void onEvent(PlayerEvent.ItemPickupEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(PlayerEvent.ItemCraftedEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(PlayerEvent.ItemSmeltedEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(PlayerEvent.PlayerLoggedInEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(PlayerEvent.PlayerLoggedOutEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(PlayerEvent.PlayerRespawnEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(PlayerEvent.PlayerChangedDimensionEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(FMLNetworkEvent.ClientConnectedToServerEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(FMLNetworkEvent.ServerConnectionFromClientEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(FMLNetworkEvent.ServerDisconnectionFromClientEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(FMLNetworkEvent.CustomPacketRegistrationEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(FMLNetworkEvent.ClientCustomPacketEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(FMLNetworkEvent.ServerCustomPacketEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(FMLNetworkEvent.CustomNetworkEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(ClientChatReceivedEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(EntityViewRenderEvent.FogDensity event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(EntityViewRenderEvent.FogColors event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(FOVUpdateEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(GuiOpenEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(GuiScreenEvent.InitGuiEvent.Pre event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(GuiScreenEvent.DrawScreenEvent.Pre event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(GuiScreenEvent.DrawScreenEvent.Post event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(GuiScreenEvent.ActionPerformedEvent.Pre event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(GuiScreenEvent.ActionPerformedEvent.Post event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(MouseEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(RenderGameOverlayEvent.Pre event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(RenderGameOverlayEvent.Post event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(RenderGameOverlayEvent.Text event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(RenderGameOverlayEvent.Chat event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(RenderHandEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(RenderLivingEvent.Pre event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(RenderLivingEvent.Post event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(RenderLivingEvent.Specials.Pre event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(RenderLivingEvent.Specials.Post event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(RenderWorldLastEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(TextureStitchEvent.Pre event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(TextureStitchEvent.Post event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(PlaySoundSourceEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(PlayStreamingSourceEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(ForgeChunkManager.ForceChunkEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(ForgeChunkManager.UnforceChunkEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(TickEvent.ServerTickEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(TickEvent.ClientTickEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(TickEvent.WorldTickEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(TickEvent.PlayerTickEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(TickEvent.RenderTickEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(AnvilUpdateEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(ServerChatEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(PotionBrewEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(EntityEvent.EntityConstructing event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(EntityEvent.CanUpdate event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(EntityEvent.EnteringChunk event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(LivingEvent.LivingUpdateEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(LivingEvent.LivingJumpEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(EntityJoinWorldEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(EntityStruckByLightningEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(PlaySoundAtEntityEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(ItemExpireEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(ItemTossEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(EnderTeleportEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(LivingAttackEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(LivingDeathEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(LivingDropsEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(LivingFallEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(LivingHurtEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(LivingPackSizeEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(LivingSetAttackTargetEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(LivingSpawnEvent.CheckSpawn event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(LivingSpawnEvent.SpecialSpawn event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(LivingSpawnEvent.AllowDespawn event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(ZombieEvent.SummonAidEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(MinecartCollisionEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(MinecartInteractEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(MinecartUpdateEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(net.minecraftforge.event.entity.player.PlayerEvent.HarvestCheck event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(net.minecraftforge.event.entity.player.PlayerEvent.NameFormat event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(net.minecraftforge.event.entity.player.PlayerEvent.Clone event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(net.minecraftforge.event.entity.player.PlayerEvent.StartTracking event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(net.minecraftforge.event.entity.player.PlayerEvent.StopTracking event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(net.minecraftforge.event.entity.player.PlayerEvent.LoadFromFile event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(net.minecraftforge.event.entity.player.PlayerEvent.SaveToFile event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(ArrowLooseEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(ArrowNockEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(AttackEntityEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(BonemealEvent event) {
        checkAndRunAllFunctions(event);
        //TODO:: remove this
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("the-file-name.txt", "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        for(String s : autoGenMethods.keySet()) {
            writer.print(s + "(");
            PrintWriter finalWriter = writer;
            for (Iterator<Pair<Method, Memo>> iterator = autoGenMethods.get(s).second.iterator(); iterator.hasNext(); ) {
                Pair<Method, Memo> myPair = iterator.next();
                if (!Modifier.isStatic(myPair.first.getModifiers())) {
                    finalWriter.print(s.substring(1, s.indexOf('.')));
                    if (myPair.first.getParameters().length != 0) {
                        finalWriter.print(", ");
                    }
                }
                Parameter[] parameters = myPair.first.getParameters();
                for (int i = 0; i < parameters.length; i++) {
                    Parameter p = parameters[i];
                    finalWriter.print(p.getType().getSimpleName());
                    if (i + 1 < parameters.length) {
                        finalWriter.print(", ");
                    }
                }
                if (iterator.hasNext()) {
                    finalWriter.print(" | ");
                }
            }
            finalWriter.println(")");
            //            System.out.println("memoset string : " + s);
//            System.out.println("this is for : " + autoGenMethods.get(s).first);
//            System.out.println(" which has the methods : ");
//            for(Pair<Method, Memo> temp : autoGenMethods.get(s).second) {
//                System.out.println("   " + temp.first);
//            }
//            System.out.println("");
        }
        for(String s : memoSet.keySet()) {
            writer.print(s);
        }
        writer.close();
    }
    @SubscribeEvent
    public static void onEvent(PlayerInteractEvent.EntityInteract event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(EntityItemPickupEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(FillBucketEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(ItemTooltipEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(PlayerDestroyItemEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(PlayerDropsEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(PlayerFlyableFallEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(PlayerInteractEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(PlayerPickupXpEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(PlayerSleepInBedEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(UseHoeEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(BiomeEvent.CreateDecorator event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(BiomeEvent.BiomeColor event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(BiomeEvent.GetVillageBlockID event) {checkAndRunAllFunctions(event);}
//    @SubscribeEvent
//    public static void onEvent(BiomeEvent.GetVillageBlockMeta event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(BiomeEvent.GetGrassColor event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(BiomeEvent.GetFoliageColor event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(BiomeEvent.GetWaterColor event) {checkAndRunAllFunctions(event);}
//    @SubscribeEvent
//    public static void onEvent(ChunkProviderEvent.ReplaceBiomeBlocks event) {checkAndRunAllFunctions(event);}
//    @SubscribeEvent
//    public static void onEvent(ChunkProviderEvent.InitNoiseField event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(DecorateBiomeEvent.Pre event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(DecorateBiomeEvent.Post event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(DecorateBiomeEvent.Decorate event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(InitMapGenEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(InitNoiseGensEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(OreGenEvent.Pre event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(OreGenEvent.Post event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(OreGenEvent.GenerateMinable event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(PopulateChunkEvent.Pre event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(PopulateChunkEvent.Post event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(PopulateChunkEvent.Populate event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(SaplingGrowTreeEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(WorldTypeEvent.BiomeSize event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(WorldTypeEvent.InitBiomeGens event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(BlockEvent.HarvestDropsEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(BlockEvent.BreakEvent event) {
        checkAndRunAllFunctions(event);
    }
    @SubscribeEvent
    public static void onEvent(ChunkDataEvent.Load event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(ChunkDataEvent.Unload event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(ChunkEvent.Load event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(ChunkWatchEvent.Watch event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(ChunkWatchEvent.UnWatch event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(NoteBlockEvent.Play event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(NoteBlockEvent.Change event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(WorldEvent.Load event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(WorldEvent.Unload event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(WorldEvent.Save event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(WorldEvent.PotentialSpawns event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(FluidEvent.FluidMotionEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(FluidEvent.FluidFillingEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(FluidEvent.FluidDrainingEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(FluidEvent.FluidSpilledEvent event) {checkAndRunAllFunctions(event);}
//    @SubscribeEvent
//    public static void onEvent(FluidContainerRegisterEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(FluidRegistry.FluidRegisterEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(OreDictionary.OreRegisterEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(RenderItemInFrameEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(ConfigChangedEvent.OnConfigChangedEvent event) {checkAndRunAllFunctions(event);}
    @SubscribeEvent
    public static void onEvent(ConfigChangedEvent.PostConfigChangedEvent event) {checkAndRunAllFunctions(event);}




}
