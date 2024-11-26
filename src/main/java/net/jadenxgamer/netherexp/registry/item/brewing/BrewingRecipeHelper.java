package net.jadenxgamer.netherexp.registry.item.brewing;

import net.jadenxgamer.netherexp.registry.item.JNEItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

public class BrewingRecipeHelper {
    public static boolean isIngredient(ItemStack stack) {
        return JNEPotionRecipe.getRecipes().stream().anyMatch(recipe -> recipe.getMiddle().matches(stack));
    }

    public static boolean hasMix(ItemStack input, ItemStack ingredient) {
        if (input.is(JNEItems.ANTIDOTE.get()) && ingredient.is(Items.GUNPOWDER)) {
            return true;
        }
        return JNEPotionRecipe.getRecipes().stream().anyMatch(recipe -> recipe.getLeft().getLeft().matches(input) && recipe.getMiddle().matches(ingredient));
    }

    public static ItemStack mix(ItemStack input, ItemStack ingredient) {
        if (input.is(JNEItems.ANTIDOTE.get()) && ingredient.is(Items.GUNPOWDER)) {
            return createConversionResult(input, JNEItems.GRENADE_ANTIDOTE.get());
        }

        for (Triple<Pair<BrewingIngredient, CompoundTag>, BrewingIngredient, Pair<ItemStack, CompoundTag>> recipe : JNEPotionRecipe.getRecipes()) {
            Pair<BrewingIngredient, CompoundTag> recipeInput = recipe.getLeft();
            if (recipeInput.getLeft().matches(input) && compareNbt(input.getOrCreateTag(), recipeInput.getRight()) && recipe.getMiddle().matches(ingredient)) {

                return createResult(recipe.getRight());
            }
        }
        return ItemStack.EMPTY;
    }

    private static boolean compareNbt(CompoundTag inputNbt, CompoundTag recipeNbt) {
        return recipeNbt == null || inputNbt.equals(recipeNbt);
    }

    // Converts one type of item to another retaining its previous NBT
    private static ItemStack createConversionResult(ItemStack input, Item item) {
        ItemStack result = item.getDefaultInstance();
        result.setTag(input.getOrCreateTag());
        return result;
    }

    // Creates a new resulting item with NBT provided in the JNERecipes class
    private static ItemStack createResult(Pair<ItemStack, CompoundTag> recipeOutput) {
        ItemStack result = recipeOutput.getLeft().copy();
        CompoundTag nbt = recipeOutput.getRight();
        if (nbt != null) {
            result.setTag(nbt);
        }
        return result;
    }
}
