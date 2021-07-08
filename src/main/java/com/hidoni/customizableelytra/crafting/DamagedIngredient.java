package com.hidoni.customizableelytra.crafting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public class DamagedIngredient extends Ingredient
{
    private final IItemList itemList;
    private final int damageValue;

    public DamagedIngredient(IItemList list, int damageValue)
    {
        super(Stream.of(list));
        this.itemList = list;
        this.damageValue = damageValue;
    }

    @Override
    public boolean test(@Nullable ItemStack testStack)
    {
        if (testStack == null)
        {
            return false;
        }
        return super.test(testStack) && testStack.getDamage() == this.damageValue;
    }

    @Override
    public boolean isSimple()
    {
        return false;
    }

    @Override
    public IIngredientSerializer<? extends Ingredient> getSerializer()
    {
        return DamagedIngredient.Serializer.INSTANCE;
    }

    @Override
    public JsonElement serialize()
    {
        JsonObject json = new JsonObject();
        json.addProperty("type", CraftingHelper.getID(DamagedIngredient.Serializer.INSTANCE).toString());
        json.add("itemlist", this.itemList.serialize());
        json.addProperty("damage", this.damageValue);
        return json;
    }

    public static class Serializer implements IIngredientSerializer<DamagedIngredient>
    {
        public static final DamagedIngredient.Serializer INSTANCE = new DamagedIngredient.Serializer();

        @Override
        public DamagedIngredient parse(PacketBuffer buffer)
        {
            JsonParser parser = new JsonParser();
            JsonElement itemList = parser.parse(buffer.readString());
            int damageValue = buffer.readInt();
            return new DamagedIngredient(deserializeItemList(itemList.getAsJsonObject()), damageValue);
        }

        @Override
        public DamagedIngredient parse(JsonObject json)
        {
            JsonElement itemList = json.get("itemlist");
            int damageValue = json.get("damage").getAsInt();
            return new DamagedIngredient(deserializeItemList(itemList.getAsJsonObject()), damageValue);
        }

        @Override
        public void write(PacketBuffer buffer, DamagedIngredient ingredient)
        {
            buffer.writeString(ingredient.itemList.serialize().toString());
            buffer.writeInt(ingredient.damageValue);
        }
    }
}
