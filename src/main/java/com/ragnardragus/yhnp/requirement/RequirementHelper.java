package com.ragnardragus.yhnp.requirement;

import com.ragnardragus.yhnp.Yhnp;
import com.ragnardragus.yhnp.capability.attributes.ModAttribute;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

public class RequirementHelper {

    public static boolean canUseItem(Player player, ItemStack item) {
        ResourceLocation rlItem = ForgeRegistries.ITEMS.getKey(item.getItem());
        return canUse(player, rlItem);
    }

    public static boolean canUseBlock(Player player, Block block) {
        ResourceLocation rsBlock = ForgeRegistries.BLOCKS.getKey(block);
        return canUse(player, rsBlock);
    }

    public static boolean canUseEntity(Player player, Entity entity) {
        ResourceLocation rlEntityType = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
        return canUse(player, rlEntityType);
    }

    public static boolean haveRequirements(Player player, Requirement[] requirements) {
        if (requirements != null) {
            for (Requirement requirement : requirements) {
                if (ModAttribute.Implementation.get(player).getAttributeLevel(requirement.modAttributes) < requirement.level) {
                    return false;
                }
            }
        }

        return true;
    }

    private static boolean canUse(Player player, ResourceLocation resource) {

        Requirement[] requirements = Yhnp.requirementsJsonListener.getRequirements(resource);

        if (requirements != null) {
            for (Requirement requirement : requirements) {
                if (ModAttribute.Implementation.get(player).getAttributeLevel(requirement.modAttributes) < requirement.level) {
                    if (player instanceof ServerPlayer) {
                        ModAttribute.Implementation.get(player).showWarning(player, resource);
                    }

                    return false;
                }
            }
        }

        return true;
    }
}
