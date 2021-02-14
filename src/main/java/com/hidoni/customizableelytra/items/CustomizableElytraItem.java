package com.hidoni.customizableelytra.items;

import com.hidoni.customizableelytra.CustomizableElytra;
import com.hidoni.customizableelytra.config.Config;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class CustomizableElytraItem extends ElytraItem implements IDyeableArmorItem
{
    public CustomizableElytraItem(Properties builder)
    {
        super(builder);
    }

    @Override
    public int getColor(ItemStack stack)
    {
        CompoundNBT compoundnbt = stack.getChildTag("display");
        if (compoundnbt != null)
        {
            return compoundnbt.contains("color", 99) ? compoundnbt.getInt("color") : 16777215;
        }
        compoundnbt = stack.getChildTag("BlockEntityTag");
        if (compoundnbt != null)
        {
            return DyeColor.byId(compoundnbt.getInt("Base")).getColorValue();
        }
        return 16777215;
    }

    @Override
    public boolean hasColor(ItemStack stack)
    {
        CompoundNBT compoundnbt = stack.getChildTag("BlockEntityTag");
        return IDyeableArmorItem.super.hasColor(stack) || compoundnbt != null;
    }

    @Override
    public void removeColor(ItemStack stack)
    {
        IDyeableArmorItem.super.removeColor(stack);
        CompoundNBT compoundnbt = stack.getChildTag("BlockEntityTag");
        if (compoundnbt != null)
        {
            stack.removeChildTag("BlockEntityTag");
        }
    }

    @Nullable
    @Override
    public EquipmentSlotType getEquipmentSlot(ItemStack stack)
    {
        return EquipmentSlotType.CHEST;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        BannerItem.appendHoverTextFromTileEntityTag(stack, tooltip);
    }

    @OnlyIn(Dist.CLIENT)
    public static ResourceLocation getTextureLocation(BannerPattern bannerIn)
    {
        if (Config.useLowQualityElytraBanners.get())
        {
            return new ResourceLocation(CustomizableElytra.MOD_ID, "entity/elytra_banner_low/" + bannerIn.getFileName());
        }
        return new ResourceLocation(CustomizableElytra.MOD_ID, "entity/elytra_banner/" + bannerIn.getFileName());
    }

    @Override
    public String getTranslationKey()
    {
        return Items.ELYTRA.getTranslationKey();
    }
}
