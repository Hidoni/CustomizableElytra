package com.hidoni.customizableelytra.items;

import com.hidoni.customizableelytra.CustomizableElytra;
import com.hidoni.customizableelytra.config.Config;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class CustomizableElytraItem extends ElytraItem implements IDyeableArmorItem
{

    public final static String LEFT_WING_TRANSLATION_KEY = "item.customizable_elytra.left_wing";
    public final static String RIGHT_WING_TRANSLATION_KEY = "item.customizable_elytra.right_wing";

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
        CompoundNBT bannerTag = stack.getChildTag("BlockEntityTag");
        CompoundNBT wingTag = stack.getChildTag("WingInfo");
        return IDyeableArmorItem.super.hasColor(stack) || bannerTag != null || wingTag != null;
    }

    @Override
    public void removeColor(ItemStack stack)
    {
        IDyeableArmorItem.super.removeColor(stack);
        stack.removeChildTag("BlockEntityTag");
        stack.removeChildTag("WingInfo");
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
        CompoundNBT wingInfo = stack.getChildTag("WingInfo");
        if (wingInfo != null)
        {
            if (wingInfo.contains("left"))
            {
                tooltip.add(new TranslationTextComponent(LEFT_WING_TRANSLATION_KEY).mergeStyle(TextFormatting.GRAY));
                CompoundNBT leftWing = wingInfo.getCompound("left");
                applyWingTooltip(tooltip, flagIn, leftWing);
            }
            if (wingInfo.contains("right"))
            {
                tooltip.add(new TranslationTextComponent(RIGHT_WING_TRANSLATION_KEY).mergeStyle(TextFormatting.GRAY));
                CompoundNBT leftWing = wingInfo.getCompound("right");
                applyWingTooltip(tooltip, flagIn, leftWing);
            }
        }
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

    private void applyWingTooltip(List<ITextComponent> tooltip, ITooltipFlag flagIn, CompoundNBT wingIn)
    {
        if (wingIn.contains("color"))
        {
            if (flagIn.isAdvanced())
            {
                tooltip.add((new TranslationTextComponent("item.color", String.format("#%06X", wingIn.getInt("color")))).mergeStyle(TextFormatting.GRAY));
            }
            else
            {
                tooltip.add((new TranslationTextComponent("item.dyed")).mergeStyle(TextFormatting.GRAY, TextFormatting.ITALIC));
            }
        }
        else if (wingIn.contains("Patterns"))
        {
            ListNBT listnbt = wingIn.getList("Patterns", 10);

            for (int i = 0; i < listnbt.size() && i < 6; ++i)
            {
                CompoundNBT patternNBT = listnbt.getCompound(i);
                DyeColor dyecolor = DyeColor.byId(patternNBT.getInt("Color"));
                BannerPattern bannerpattern = BannerPattern.byHash(patternNBT.getString("Pattern"));
                if (bannerpattern != null)
                {
                    tooltip.add((new TranslationTextComponent("block.minecraft.banner." + bannerpattern.getFileName() + '.' + dyecolor.getTranslationKey())).mergeStyle(TextFormatting.GRAY));
                }
            }
        }
    }
}
