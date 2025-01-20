package com.ragnardragus.yhnp.compat;

import com.ragnardragus.yhnp.Yhnp;
import com.ragnardragus.yhnp.capability.attributes.ModAttribute;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

public class GameStageCompat {
    public static boolean isLoaded() {
        return ModList.get().isLoaded("gamestages");
    }


    public static boolean canUseItem(Player player, ItemStack item) {
        ResourceLocation rlItem = ForgeRegistries.ITEMS.getKey(item.getItem());
        return canUse(player, rlItem);
    }

    public static boolean canUseBlock(Player player, Block block) {
        ResourceLocation rlBlock = ForgeRegistries.BLOCKS.getKey(block);
        return canUse(player, rlBlock);
    }

    public static boolean canUseEntity(Player player, Entity entity) {
        ResourceLocation rlEntity = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
        return canUse(player, rlEntity);
    }

    private static boolean canUse(Player player, ResourceLocation resource) {

        String stageName = Yhnp.requirementsJsonListener.getRequirementStageName(resource);

        if(stageName != null) {
            boolean hasStage = GameStageHelper.hasStage(player, stageName);

            if(!hasStage) {

                if (player instanceof ServerPlayer) {
                    ModAttribute.Implementation.get(player).showWarning(player, resource);
                }

                return false;
            }
        }

        return true;
    }
}
