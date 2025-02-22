package com.hidoni.customizableelytra.data.client;

import com.hidoni.customizableelytra.Constants;
import com.hidoni.customizableelytra.client.CustomizableElytraDyeItemTintSource;
import com.hidoni.customizableelytra.client.CustomizableElytraTrimMaterialProperty;
import com.hidoni.customizableelytra.registry.ModItems;
import net.minecraft.client.color.item.Dye;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.SelectItemModel;
import net.minecraft.client.renderer.item.properties.conditional.Broken;
import net.minecraft.client.renderer.item.properties.select.TrimMaterialProperty;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.equipment.trim.TrimMaterial;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class ModItemModelGenerators extends ItemModelGenerators  {
    public ModItemModelGenerators(ItemModelOutput itemModelOutput, BiConsumer<ResourceLocation, ModelInstance> modelOutput) {
        super(itemModelOutput, modelOutput);
    }

    private void generateNonArmorTrimmableItem(Item item) {
        ResourceLocation modelLocation = ModelLocationUtils.getModelLocation(item);
        ResourceLocation itemTexture = TextureMapping.getItemTexture(item);
        String itemPath = BuiltInRegistries.ITEM.getKey(item).getPath();
        List<SelectItemModel.SwitchCase<ResourceKey<TrimMaterial>>> trims = new ArrayList<>(TRIM_MATERIAL_MODELS.size());

        for(TrimMaterialData trim : TRIM_MATERIAL_MODELS) {
            ResourceLocation trimModelLocation = modelLocation.withSuffix("_" + trim.name() + "_trim");
            ResourceLocation trimItemTexture = ResourceLocation.withDefaultNamespace("trims/items/" + itemPath + "_trim_" + trim.name());
            this.generateLayeredItem(trimModelLocation, itemTexture, trimItemTexture);
            ItemModel.Unbaked trimModel = ItemModelUtils.tintedModel(trimModelLocation, new Dye(0xFFFFFFFF));
            trims.add(ItemModelUtils.when(trim.materialKey(), trimModel));
        }

        ModelTemplates.FLAT_ITEM.create(modelLocation, TextureMapping.layer0(itemTexture), this.modelOutput);
        ItemModel.Unbaked defaultModel = ItemModelUtils.tintedModel(modelLocation, new Dye(0xFFFFFFFF));
        this.itemModelOutput.accept(item, ItemModelUtils.select(new TrimMaterialProperty(), defaultModel, trims));
    }

    private static ResourceLocation getModelLocationWithPrefix(Item elytraItem, String prefix) {
        ResourceLocation key = BuiltInRegistries.ITEM.getKey(elytraItem);
        return key.withPrefix("item/" + prefix);
    }

    private static ResourceLocation getModelLocationWithPrefixAndSuffix(Item elytraItem, String prefix, String suffix) {
        return getModelLocationWithPrefix(elytraItem, prefix).withSuffix(suffix);
    }

    private static ResourceLocation getItemTextureWithPrefix(Item elytraItem, String prefix) {
        ResourceLocation key = BuiltInRegistries.ITEM.getKey(elytraItem);
        return key.withPrefix("item/" + prefix);
    }

    private static ResourceLocation getItemTextureWithPrefixAndSuffix(Item elytraItem, String prefix, String suffix) {
        return getItemTextureWithPrefix(elytraItem, prefix).withSuffix(suffix);
    }

    private ItemModel.Unbaked generateElytraWingModel(Item elytraItem, boolean broken, boolean rightWing) {
        String prefix = broken ? "broken_" : "";
        String suffix = rightWing ? "_right" : "_left";
        ResourceLocation modelLocation = getModelLocationWithPrefixAndSuffix(elytraItem, prefix, suffix);
        ResourceLocation itemTexture = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, getItemTextureWithPrefixAndSuffix(elytraItem, prefix, suffix).getPath());
        String itemPath = BuiltInRegistries.ITEM.getKey(elytraItem).getPath();
        List<SelectItemModel.SwitchCase<ResourceKey<TrimMaterial>>> trims = new ArrayList<>(TRIM_MATERIAL_MODELS.size());
        for(TrimMaterialData trim : TRIM_MATERIAL_MODELS) {
            ResourceLocation trimModelLocation = modelLocation.withSuffix("_" + trim.name() + "_trim");
            ResourceLocation trimTextureLocation = ResourceLocation.withDefaultNamespace("trims/items/" + itemPath + suffix + "_wing_trim_" + trim.name());
            this.generateLayeredItem(trimModelLocation, itemTexture, trimTextureLocation);
            ItemModel.Unbaked trimModel = ItemModelUtils.tintedModel(trimModelLocation, new CustomizableElytraDyeItemTintSource(rightWing));
            trims.add(ItemModelUtils.when(trim.materialKey(), trimModel));
        }
        ModelTemplates.FLAT_ITEM.create(modelLocation, TextureMapping.layer0(itemTexture), this.modelOutput);
        ItemModel.Unbaked defaultModel = ItemModelUtils.tintedModel(modelLocation, new CustomizableElytraDyeItemTintSource(rightWing));
        return ItemModelUtils.select(new CustomizableElytraTrimMaterialProperty(rightWing), defaultModel, trims);
    }

    @SuppressWarnings("SameParameterValue")
    private void generateElytraModel(Item elytraItem) {
        ItemModel.Unbaked unbroken = ItemModelUtils.composite(generateElytraWingModel(elytraItem, false, false), generateElytraWingModel(elytraItem, false, true));
        ItemModel.Unbaked broken = ItemModelUtils.composite(generateElytraWingModel(elytraItem, true, false), generateElytraWingModel(elytraItem, true, true));
        this.generateBooleanDispatch(elytraItem, new Broken(), broken, unbroken);
    }

    @Override
    public void run() {
        generateElytraModel(Items.ELYTRA);
        this.generateNonArmorTrimmableItem(ModItems.ELYTRA_WING.get().asItem());
    }
}
