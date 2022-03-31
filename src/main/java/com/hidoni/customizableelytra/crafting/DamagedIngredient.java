package com.hidoni.customizableelytra.crafting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;

import javax.annotation.Nullable;
import java.util.stream.Stream;

import net.minecraft.world.item.crafting.Ingredient.Value;

public class DamagedIngredient extends Ingredient {
    private final Value itemList;
    private final int damageValue;

    public DamagedIngredient(Value list, int damageValue) {
        super(Stream.of(list));
        this.itemList = list;
        this.damageValue = damageValue;
    }

    @Override
    public boolean test(@Nullable ItemStack testStack) {
        if (testStack == null) {
            return false;
        }
        return super.test(testStack) && testStack.getDamageValue() == this.damageValue;
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public IIngredientSerializer<? extends Ingredient> getSerializer() {
        return DamagedIngredient.Serializer.INSTANCE;
    }

    @Override
    public JsonElement toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", CraftingHelper.getID(DamagedIngredient.Serializer.INSTANCE).toString());
        json.add("itemlist", this.itemList.serialize());
        json.addProperty("damage", this.damageValue);
        return json;
    }

    public static class Serializer implements IIngredientSerializer<DamagedIngredient> {
        public static final DamagedIngredient.Serializer INSTANCE = new DamagedIngredient.Serializer();

        @Override
        public DamagedIngredient parse(FriendlyByteBuf buffer) {
            JsonParser parser = new JsonParser();
            JsonElement itemList = parser.parse(buffer.readUtf());
            int damageValue = buffer.readInt();
            return new DamagedIngredient(valueFromJson(itemList.getAsJsonObject()), damageValue);
        }

        @Override
        public DamagedIngredient parse(JsonObject json) {
            JsonElement itemList = json.get("itemlist");
            int damageValue = json.get("damage").getAsInt();
            return new DamagedIngredient(valueFromJson(itemList.getAsJsonObject()), damageValue);
        }

        @Override
        public void write(FriendlyByteBuf buffer, DamagedIngredient ingredient) {
            buffer.writeUtf(ingredient.itemList.serialize().toString());
            buffer.writeInt(ingredient.damageValue);
        }
    }
}
