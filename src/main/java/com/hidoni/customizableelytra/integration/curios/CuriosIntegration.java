package com.hidoni.customizableelytra.integration.curios;

import com.hidoni.customizableelytra.items.CustomizableElytraItem;
import com.hidoni.customizableelytra.util.ElytraInventoryUtil;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.theillusivec4.caelus.api.CaelusApi;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;
import top.theillusivec4.curios.api.type.capability.ICurio;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

// This is HEAVILY based on Curious Elytra code from https://github.com/TheIllusiveC4/CuriousElytra
public class CuriosIntegration {
    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().register(new ModEventBusHandler());
        MinecraftForge.EVENT_BUS.register(new ForgeEventBusHandler());
    }

    public static class ModEventBusHandler {
        @SubscribeEvent
        public void imcEnqueueHandler(final InterModEnqueueEvent event) {
            InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE,
                    () -> SlotTypePreset.BACK.getMessageBuilder().build());
        }
    }

    public static class ForgeEventBusHandler {
        @SubscribeEvent
        public void playerTickHandler(final TickEvent.PlayerTickEvent event) {
            Player player = event.player;
            AttributeInstance attributeInstance =
                    player.getAttribute(CaelusApi.getInstance().getFlightAttribute());

            if (attributeInstance != null) {
                attributeInstance.removeModifier(CustomizableElytraCurio.ELYTRA_FLIGHT_MODIFIER);

                ItemStack curioElytra = ElytraInventoryUtil.getCurioElytra(player);
                if (!attributeInstance.hasModifier(CustomizableElytraCurio.ELYTRA_FLIGHT_MODIFIER) &&
                        curioElytra != ItemStack.EMPTY && CustomizableElytraItem.isFlyEnabled(curioElytra)) {
                    attributeInstance.addTransientModifier(CustomizableElytraCurio.ELYTRA_FLIGHT_MODIFIER);
                }
            }
        }

        @SubscribeEvent
        public void attachCapabilitiesHandler(final AttachCapabilitiesEvent<ItemStack> event) {
            ItemStack stack = event.getObject();
            if (stack.getItem() instanceof CustomizableElytraItem) {
                final LazyOptional<ICurio> curio = LazyOptional.of(() -> new CustomizableElytraCurio(stack));
                event.addCapability(CuriosCapability.ID_ITEM, new ICapabilityProvider() {
                    @Nonnull
                    @Override
                    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap,
                                                             @Nullable Direction side) {
                        return CuriosCapability.ITEM.orEmpty(cap, curio);
                    }
                });
                event.addListener(curio::invalidate);
            }
        }
    }
}
