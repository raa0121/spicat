package com.github.raa0121.spicat

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapedRecipe

class MyRecipe {
    fun NetherWartsBlock2NetherWarts() : Recipe {
        return ShapedRecipe(ItemStack(Material.NETHER_WART, 9)).shape(
                "___",
                "_#_",
                "___"
        ).setIngredient('#', Material.NETHER_WART_BLOCK).setIngredient('_', Material.AIR)
    }
}