package com.hidoni.customizableelytra.data.client;

import com.hidoni.customizableelytra.Constants;
import com.hidoni.customizableelytra.mixin.datagen.ItemModelBuilderAccessor;
import com.hidoni.customizableelytra.mixin.datagen.ModelBuilderAccessor;
import com.hidoni.customizableelytra.mixin.datagen.OverrideBuilderAccessor;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Map;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
        super(output, modid, existingFileHelper);
    }

    private static ResourceLocation getTrimmedItemPath(ResourceLocation path, ItemModelGenerators.TrimModelData trim) {
        if (!path.getPath().startsWith("item/")) {
            path = path.withPrefix("item/");
        }
        return path.withSuffix("_" + trim.name() + "_trim");
    }

    private ItemModelBuilder generateItemWithTrims(ResourceLocation path, ResourceLocation... textures) {
        ItemModelBuilder baseItemBuilder = generateBaseItemBuilder(path, textures);
        for (ItemModelGenerators.TrimModelData trim :
                ItemModelGenerators.GENERATED_TRIM_MODELS) {
            ResourceLocation trimmedItemPath = getTrimmedItemPath(path, trim);
            baseItemBuilder.override().predicate(ItemModelGenerators.TRIM_TYPE_PREDICATE_ID, trim.itemModelIndex())
                    .model(new ModelFile.UncheckedModelFile(trimmedItemPath))
                    .end();
            ItemModelBuilder trimmedItemBuilder = generateBaseItemBuilder(trimmedItemPath, textures);
            // Mixin is necessary, forge checks that texture exists, but the textures we're referencing here are generated by MC at runtime
            ((ModelBuilderAccessor) trimmedItemBuilder).getTextures().put("layer" + textures.length, mcLoc(path.getPath() + "_trim_" + trim.name()).withPrefix("trims/items/").toString());
        }
        return baseItemBuilder;
    }

    private ItemModelBuilder generateItemWithWingTrims(ResourceLocation path, ResourceLocation... textures) {
        ItemModelBuilder baseItemBuilder = generateBaseItemBuilder(path, textures);
        for (ItemModelGenerators.TrimModelData leftTrim :
                ItemModelGenerators.GENERATED_TRIM_MODELS) {
            ResourceLocation trimmedItemPath = getTrimmedItemPath(path, leftTrim).withSuffix("_no_trim");
            baseItemBuilder.override().predicate(Constants.ELYTRA_LEFT_WING_TRIM_TYPE_PREDICATE, leftTrim.itemModelIndex())
                    .model(new ModelFile.UncheckedModelFile(trimmedItemPath))
                    .end();
            ItemModelBuilder trimmedItemBuilder = generateBaseItemBuilder(trimmedItemPath, textures);
            ((ModelBuilderAccessor) trimmedItemBuilder).getTextures().put("layer" + textures.length, mcLoc(path.getPath() + "_left_wing_trim_" + leftTrim.name()).withPrefix("trims/items/").toString());
        }
        for (ItemModelGenerators.TrimModelData rightTrim :
                ItemModelGenerators.GENERATED_TRIM_MODELS) {
            ResourceLocation trimmedItemPath = getTrimmedItemPath(path.withSuffix("_no_trim"), rightTrim);
            baseItemBuilder.override().predicate(Constants.ELYTRA_RIGHT_WING_TRIM_TYPE_PREDICATE, rightTrim.itemModelIndex())
                    .model(new ModelFile.UncheckedModelFile(trimmedItemPath))
                    .end();
            ItemModelBuilder trimmedItemBuilder = generateBaseItemBuilder(trimmedItemPath, textures);
            ((ModelBuilderAccessor) trimmedItemBuilder).getTextures().put("layer" + textures.length, mcLoc(path.getPath() + "_right_wing_trim_" + rightTrim.name()).withPrefix("trims/items/").toString());
        }
        for (ItemModelGenerators.TrimModelData leftTrim :
                ItemModelGenerators.GENERATED_TRIM_MODELS) {
            for (ItemModelGenerators.TrimModelData rightTrim :
                    ItemModelGenerators.GENERATED_TRIM_MODELS) {
                ResourceLocation trimmedItemPath = getTrimmedItemPath(getTrimmedItemPath(path, leftTrim), rightTrim);
                baseItemBuilder.override()
                        .predicate(Constants.ELYTRA_LEFT_WING_TRIM_TYPE_PREDICATE, leftTrim.itemModelIndex())
                        .predicate(Constants.ELYTRA_RIGHT_WING_TRIM_TYPE_PREDICATE, rightTrim.itemModelIndex())
                        .model(new ModelFile.UncheckedModelFile(trimmedItemPath))
                        .end();
                ItemModelBuilder trimmedItemBuilder = generateBaseItemBuilder(trimmedItemPath, textures);
                ((ModelBuilderAccessor) trimmedItemBuilder).getTextures().put("layer" + textures.length, mcLoc(path.getPath() + "_left_wing_trim_" + leftTrim.name()).withPrefix("trims/items/").toString());
                ((ModelBuilderAccessor) trimmedItemBuilder).getTextures().put("layer" + (textures.length + 1), mcLoc(path.getPath() + "_right_wing_trim_" + rightTrim.name()).withPrefix("trims/items/").toString());
            }
        }
        return baseItemBuilder;
    }

    private ItemModelBuilder generateBaseItemBuilder(ResourceLocation path, ResourceLocation[] textures) {
        ModelFile itemGenerated = getExistingFile(mcLoc("item/generated"));
        ItemModelBuilder builder = getBuilder(path.toString()).parent(itemGenerated);
        for (int i = 0; i < textures.length; i++) {
            builder.texture("layer" + i, textures[i]);
        }
        return builder;
    }

    @Override
    protected void registerModels() {
        ItemModelBuilder brokenElytra = generateItemWithWingTrims(mcLoc("broken_elytra"), modLoc("item/broken_elytra_left"), modLoc("item/broken_elytra_right"));
        ItemModelBuilder elytra = generateItemWithWingTrims(mcLoc("elytra"), modLoc("item/elytra_left"), modLoc("item/elytra_right"));
        elytra.override()
                .predicate(mcLoc("broken"), 1.0F)
                .model(new ModelFile.UncheckedModelFile(mcLoc("item/broken_elytra")))
                .end();
        for (ItemModelBuilder.OverrideBuilder overrideBuilder :
                ((ItemModelBuilderAccessor) brokenElytra).getOverrides()) {
            ItemModelBuilder.OverrideBuilder override = elytra.override();
            for (Map.Entry<ResourceLocation, Float> predicate : ((OverrideBuilderAccessor) overrideBuilder).getPredicates().entrySet()) {
                override.predicate(predicate.getKey(), predicate.getValue());
            }
            override.predicate(mcLoc("broken"), 1.0F)
                    .model(((OverrideBuilderAccessor) overrideBuilder).getModel())
                    .end();
        }
        generateItemWithTrims(modLoc("elytra_wing"), modLoc("item/elytra_wing"));
    }
}
