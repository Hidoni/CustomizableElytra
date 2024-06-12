package com.hidoni.customizableelytra.data.client;

import com.hidoni.customizableelytra.Constants;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.ArrayList;
import java.util.List;
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
            ResourceLocation resourceLocation = mcLoc(path.getPath() + "_trim_" + trim.name()).withPrefix("trims/items/");
            this.existingFileHelper.trackGenerated(resourceLocation, PackType.CLIENT_RESOURCES, ".png", "textures");
            trimmedItemBuilder.texture("layer" + textures.length, resourceLocation);
        }
        return baseItemBuilder;
    }

    record OverrideData(Map<ResourceLocation, Float> predicates, ModelFile model) {
    }

    private Pair<ItemModelBuilder, List<OverrideData>> generateItemWithWingTrims(ResourceLocation path, ResourceLocation... textures) {
        List<OverrideData> overrideData = new ArrayList<>();
        ItemModelBuilder baseItemBuilder = generateBaseItemBuilder(path, textures);
        for (ItemModelGenerators.TrimModelData leftTrim :
                ItemModelGenerators.GENERATED_TRIM_MODELS) {
            ResourceLocation trimmedItemPath = getTrimmedItemPath(path, leftTrim).withSuffix("_no_trim");
            ModelFile.UncheckedModelFile model = new ModelFile.UncheckedModelFile(trimmedItemPath);
            baseItemBuilder.override().
                    predicate(Constants.ELYTRA_LEFT_WING_TRIM_TYPE_PREDICATE, leftTrim.itemModelIndex())
                    .model(model)
                    .end();
            overrideData.add(new OverrideData(Map.of(Constants.ELYTRA_LEFT_WING_TRIM_TYPE_PREDICATE, leftTrim.itemModelIndex()), model));
            ItemModelBuilder trimmedItemBuilder = generateBaseItemBuilder(trimmedItemPath, textures);
            ResourceLocation resourceLocation = mcLoc(path.getPath() + "_left_wing_trim_" + leftTrim.name()).withPrefix("trims/items/");
            this.existingFileHelper.trackGenerated(resourceLocation, PackType.CLIENT_RESOURCES, ".png", "textures");
            trimmedItemBuilder.texture("layer" + textures.length, resourceLocation);
        }
        for (ItemModelGenerators.TrimModelData rightTrim :
                ItemModelGenerators.GENERATED_TRIM_MODELS) {
            ResourceLocation trimmedItemPath = getTrimmedItemPath(path.withSuffix("_no_trim"), rightTrim);
            ModelFile.UncheckedModelFile model = new ModelFile.UncheckedModelFile(trimmedItemPath);
            baseItemBuilder.override().predicate(Constants.ELYTRA_RIGHT_WING_TRIM_TYPE_PREDICATE, rightTrim.itemModelIndex())
                    .model(model)
                    .end();
            overrideData.add(new OverrideData(Map.of(Constants.ELYTRA_RIGHT_WING_TRIM_TYPE_PREDICATE, rightTrim.itemModelIndex()), model));
            ItemModelBuilder trimmedItemBuilder = generateBaseItemBuilder(trimmedItemPath, textures);
            ResourceLocation resourceLocation = mcLoc(path.getPath() + "_right_wing_trim_" + rightTrim.name()).withPrefix("trims/items/");
            this.existingFileHelper.trackGenerated(resourceLocation, PackType.CLIENT_RESOURCES, ".png", "textures");
            trimmedItemBuilder.texture("layer" + textures.length, resourceLocation);
        }
        for (ItemModelGenerators.TrimModelData leftTrim :
                ItemModelGenerators.GENERATED_TRIM_MODELS) {
            for (ItemModelGenerators.TrimModelData rightTrim :
                    ItemModelGenerators.GENERATED_TRIM_MODELS) {
                ResourceLocation trimmedItemPath = getTrimmedItemPath(getTrimmedItemPath(path, leftTrim), rightTrim);
                ModelFile.UncheckedModelFile model = new ModelFile.UncheckedModelFile(trimmedItemPath);
                baseItemBuilder.override()
                        .predicate(Constants.ELYTRA_LEFT_WING_TRIM_TYPE_PREDICATE, leftTrim.itemModelIndex())
                        .predicate(Constants.ELYTRA_RIGHT_WING_TRIM_TYPE_PREDICATE, rightTrim.itemModelIndex())
                        .model(model)
                        .end();
                overrideData.add(new OverrideData(Map.of(Constants.ELYTRA_LEFT_WING_TRIM_TYPE_PREDICATE, leftTrim.itemModelIndex(), Constants.ELYTRA_RIGHT_WING_TRIM_TYPE_PREDICATE, rightTrim.itemModelIndex()), model));
                ItemModelBuilder trimmedItemBuilder = generateBaseItemBuilder(trimmedItemPath, textures);
                ResourceLocation leftWingResourceLocation = mcLoc(path.getPath() + "_left_wing_trim_" + leftTrim.name()).withPrefix("trims/items/");
                this.existingFileHelper.trackGenerated(leftWingResourceLocation, PackType.CLIENT_RESOURCES, ".png", "textures");
                trimmedItemBuilder.texture("layer" + textures.length, leftWingResourceLocation);
                ResourceLocation rightWingResourceLocation = mcLoc(path.getPath() + "_right_wing_trim_" + rightTrim.name()).withPrefix("trims/items/");
                this.existingFileHelper.trackGenerated(rightWingResourceLocation, PackType.CLIENT_RESOURCES, ".png", "textures");
                trimmedItemBuilder.texture("layer" + (textures.length + 1), rightWingResourceLocation);
            }
        }
        return new Pair<>(baseItemBuilder, overrideData);
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
        List<OverrideData> brokenElytraOverrides = generateItemWithWingTrims(mcLoc("broken_elytra"), modLoc("item/broken_elytra_left"), modLoc("item/broken_elytra_right")).getSecond();
        ItemModelBuilder elytra = generateItemWithWingTrims(mcLoc("elytra"), modLoc("item/elytra_left"), modLoc("item/elytra_right")).getFirst();
        elytra.override()
                .predicate(mcLoc("broken"), 1.0F)
                .model(new ModelFile.UncheckedModelFile(mcLoc("item/broken_elytra")))
                .end();
        for (OverrideData overrideData :
                brokenElytraOverrides) {
            ItemModelBuilder.OverrideBuilder override = elytra.override();
            for (Map.Entry<ResourceLocation, Float> predicate : overrideData.predicates().entrySet()) {
                override.predicate(predicate.getKey(), predicate.getValue());
            }
            override.predicate(mcLoc("broken"), 1.0F)
                    .model(overrideData.model())
                    .end();
        }
        generateItemWithTrims(modLoc("elytra_wing"), modLoc("item/elytra_wing"));
    }
}
