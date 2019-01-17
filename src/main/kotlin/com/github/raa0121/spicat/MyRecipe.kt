package com.github.raa0121.spicat

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapedRecipe

class MyRecipe {
    fun QuartzBlock2Quartz() : Recipe {
        return ShapedRecipe(ItemStack(Material.QUARTZ, 4)).shape(
                "___",
                "_#_",
                "___"
        ).setIngredient('#', Material.QUARTZ_BLOCK).setIngredient('_', Material.AIR)
    }
}