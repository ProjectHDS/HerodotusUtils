package projecthds.herodotusutils.event;

import WayofTime.bloodmagic.core.data.SoulTicket;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import baubles.api.BaubleType;
import baubles.api.cap.BaublesCapabilities;
import baubles.api.cap.IBaublesItemHandler;
import com.google.common.collect.Lists;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.data.DataInt;
import crafttweaker.api.data.IData;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.item.IngredientAnyAdvanced;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.recipes.IRecipeFunction;
import crafttweaker.mc1120.events.ActionApplyEvent;
import crafttweaker.mc1120.item.MCItemStack;
import crafttweaker.util.ArrayUtil;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.oredict.OreDictionary;
import projecthds.herodotusutils.block.BlockCreatureDataAnalyzer;
import projecthds.herodotusutils.block.BlockCreatureDataReEncodeInterface;
import projecthds.herodotusutils.block.BlockMercury;
import projecthds.herodotusutils.computing.event.ComputingUnitChangeEvent;
import projecthds.herodotusutils.item.ItemPenumbraRing;
import projecthds.herodotusutils.item.ItemRiftSword;
import projecthds.herodotusutils.item.RefinedBottle;
import projecthds.herodotusutils.modsupport.i18nupdatemod.I18nDetectedScreen;
import projecthds.herodotusutils.potion.LithiumAmalgamInfected;
import projecthds.herodotusutils.potion.Starvation;
import projecthds.herodotusutils.proxy.CommonProxy;
import projecthds.herodotusutils.util.Capabilities;
import projecthds.herodotusutils.util.interfaces.ITaint;
import projecthds.herodotusutils.util.SharedRiftAction;
import projecthds.herodotusutils.util.Util;
import projecthds.herodotusutils.world.PlainTeleporter;
import youyihj.zenutils.api.world.ZenUtilsWorld;
import youyihj.zenutils.impl.capability.ZenWorldCapabilityHandler;

/**
 * @author youyihj
 */
@Mod.EventBusSubscriber
public class EventHandler {

    public static final List<Item> RAW_MEAT_LIST = Lists.newArrayList(
            Items.BEEF,
            Items.CHICKEN,
            Items.MUTTON,
            Items.RABBIT,
            Items.PORKCHOP
    );

    @SubscribeEvent
    public static void onEntityLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        World world = entity.world;
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            IBaublesItemHandler baubles = player.getCapability(BaublesCapabilities.CAPABILITY_BAUBLES, null);
            for (int validSlot : BaubleType.RING.getValidSlots()) {
                if (baubles.getStackInSlot(validSlot).getItem() == ItemPenumbraRing.INSTANCE) {
                    ItemPenumbraRing.INSTANCE.handlePenumbraTick(player, world.isRemote);
                    break;
                }
            }
            player.getCapability(Capabilities.TAINT_CAPABILITY, null).syncToClientWhenNeeded();
        }
        if (!world.isRemote) {
            IItemHandler itemHandler = entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            if (world.getTotalWorldTime() % 40 == 0) {
                IData data = ZenUtilsWorld.getCustomChunkData(CraftTweakerMC.getIWorld(world), CraftTweakerMC.getIBlockPos(entity.getPosition()));
                Optional.ofNullable(data.memberGet(BlockMercury.TAG_POLLUTION))
                        .filter(IData::asBool)
                        .ifPresent(bool -> {
                            entity.addPotionEffect(new PotionEffect(MobEffects.POISON, 200, 1));
                        });

                for (int i = 0; i < itemHandler.getSlots(); i++) {
                    ItemStack itemStack = itemHandler.getStackInSlot(i);
                    if (itemStack.getItem() == RefinedBottle.INSTANCE)
                        continue;
                    FluidStack fluidStack = FluidUtil.getFluidContained(itemStack);
                    if (fluidStack != null) {
                        String name = fluidStack.getFluid().getName();
                        if (name.equals("mercury") || name.endsWith("mercury_suspension")) {
                            entity.addPotionEffect(new PotionEffect(MobEffects.POISON, 100, 3));
                        }
                    }
                }
            }
            if (entity instanceof EntityPlayerMP) {
                EntityPlayerMP player = (EntityPlayerMP) entity;
                ITaint taint = Objects.requireNonNull(player.getCapability(Capabilities.TAINT_CAPABILITY, null));
                taint.syncToClientWhenNeeded();
                int time = player.getStatFile().readStat(StatList.PLAY_ONE_MINUTE);
                if (time != 0 && time % 24000 == 0) {
                    taint.addInfectedTaint(-taint.getInfectedTaint() / 2);
                }
            }
        }
        if (world.provider.getDimension() == CommonProxy.ANCIENT_VOID_DIMENSION_ID) {
            if (entity.posY < -24.0f) {
                entity.changeDimension(0, new PlainTeleporter(entity.posX, 324.0, entity.posY));
                entity.getEntityData().setBoolean("DisableFallingDamage", true);
            }
            if (world.getTotalWorldTime() % 40 == 5) {
                SharedRiftAction.attackEntity(entity, 2.0f);
            }
        }
    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        World world = event.world;
        if (event.phase == TickEvent.Phase.END && world instanceof WorldServer) {
            for (Chunk chunk : ((WorldServer) world).getChunkProvider().getLoadedChunks()) {
                if (world.rand.nextInt(5000) == 0) {
                    chunk.getCapability(ZenWorldCapabilityHandler.ZEN_WORLD_CAPABILITY, null).updateData(Util.createDataMap(BlockMercury.TAG_POLLUTION, new DataInt(0)));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        if (event.getSource() == LithiumAmalgamInfected.DAMAGE_SOURCE) {
            ItemStack stack = OreDictionary.getOres("crystalLithium").get(0).copy();
            stack.setCount(2);
            event.getDrops().add(new EntityItem(entity.world, entity.posX, entity.posY + 0.5d, entity.posZ, stack));
        }
    }

    @SubscribeEvent
    public static void onComputingUnitChange(ComputingUnitChangeEvent event) {
        event.getComputingUnit().removeInvalidEntry(event.getWorld());
    }

    @SubscribeEvent
    public static void onItemUseStart(LivingEntityUseItemEvent.Start event) {
        if (event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            ItemStack item = event.getItem();
            if ((item.getItem() instanceof ItemFood || item.getItemUseAction() == EnumAction.DRINK || item.getItemUseAction() == EnumAction.EAT)) {
                if (!player.world.isRemote && player.isPotionActive(Starvation.INSTANCE)) {
                    event.setDuration(event.getDuration() / 2);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onItemUseFinish(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            ItemStack item = event.getItem();
            if (player.isPotionActive(Starvation.INSTANCE)) {
                if (RAW_MEAT_LIST.stream().anyMatch(item.getItem()::equals)) {
                    NetworkHelper.getSoulNetwork(player).add(new SoulTicket(100), 1000);
                    player.sendStatusMessage(new TextComponentTranslation("hdsutils.add_lp_while_eating_raw_meat"), true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        ITaint taint = event.getEntityLiving().getCapability(Capabilities.TAINT_CAPABILITY, null);
        taint.copyFrom(event.getOriginal().getCapability(Capabilities.TAINT_CAPABILITY, null));
        if (event.isWasDeath()) {
            taint.addInfectedTaint(-Math.round(taint.getInfectedTaint() * 0.05f));
        }
        taint.markDirty();
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerLoggedInEvent event) {
        event.player.getCapability(Capabilities.TAINT_CAPABILITY, null).addInfectedTaint(0); // to trigger sync message
    }

    @SubscribeEvent
    public static void onEntityFall(LivingFallEvent event) {
        if (event.getEntityLiving().world.isRemote) return;
        if (event.getEntityLiving().getEntityData().getBoolean("DisableFallingDamage")) {
            event.setCanceled(true);
            event.getEntityLiving().getEntityData().setBoolean("DisableFallingDamage", false);
        }
    }

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(Starvation.INSTANCE);
        event.getRegistry().register(LithiumAmalgamInfected.INSTANCE);
    }

    @SubscribeEvent
    public static void redirectToRiftDamage(LivingAttackEvent event) {
        Entity immediateSource = event.getSource().getImmediateSource();
        if (immediateSource instanceof EntityLivingBase && !event.getEntityLiving().world.isRemote) {
            ItemStack stack = ((EntityLivingBase) immediateSource).getHeldItemMainhand();
            if (stack.getItem() == ItemRiftSword.INSTANCE) {
                event.setCanceled(true);
                SharedRiftAction.attackEntity(event.getEntityLiving(), event.getAmount());
            }
        }
    }

    @SubscribeEvent
    public static void registerCreatureDataChannelRecipes(ActionApplyEvent.Pre event) {
        IRecipeFunction recipeFunction = ((output, inputs, craftingInfo) -> {
            ItemStack item = CraftTweakerMC.getItemStack(inputs.get("item"));
            return output.withTag(Util.createDataMap("channel", new DataInt(Objects.hash(item.getItem().getRegistryName(), item.getMetadata()))), true);
        });
        IItemStack analyzer = new MCItemStack(new ItemStack(BlockCreatureDataAnalyzer.ITEM_BLOCK));
        IItemStack encodeInterface = new MCItemStack(new ItemStack(BlockCreatureDataReEncodeInterface.ITEM_BLOCK));
        class IngredientAnyExcept extends IngredientAnyAdvanced {
            final IIngredient except;

            public IngredientAnyExcept(IIngredient except) {
                super("item", ArrayUtil.EMPTY_CONDITIONS, ArrayUtil.EMPTY_TRANSFORMERS, ArrayUtil.EMPTY_TRANSFORMERS_NEW);
                this.except = except;
            }

            @Override
            public boolean matches(IItemStack item) {
                return !except.matches(item);
            }
        }
        CraftTweakerAPI.recipes.addHiddenShapeless("creature_data_analyzer_channel", analyzer, new IIngredient[]{analyzer, new IngredientAnyExcept(analyzer)}, recipeFunction, null);
        CraftTweakerAPI.recipes.addHiddenShapeless("creature_data_encode_interface_channel", encodeInterface, new IIngredient[]{encodeInterface, new IngredientAnyExcept(encodeInterface)}, recipeFunction, null);
    }

    public static boolean shouldDisplay = true;

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void mainMenuOpen(GuiOpenEvent event) {
        if (shouldDisplay && event.getGui() instanceof GuiMainMenu && Loader.isModLoaded("i18nmod")) {
            event.setGui(new I18nDetectedScreen());
            shouldDisplay = false;
        }
    }
}
