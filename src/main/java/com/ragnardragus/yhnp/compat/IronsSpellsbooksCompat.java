package com.ragnardragus.yhnp.compat;

import com.ragnardragus.yhnp.Yhnp;
import com.ragnardragus.yhnp.capability.attributes.ModAttribute;
import com.ragnardragus.yhnp.requirement.Requirement;
import io.redspace.ironsspellbooks.api.events.SpellPreCastEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class IronsSpellsbooksCompat {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onCastSpell(SpellPreCastEvent event) {
        Player player = event.getEntity();
        int spellLevel = event.getSpellLevel();

        if(!player.level().isClientSide) {
            String spellId = event.getSpellId();
            if(!canCastSpell(player, spellId, spellLevel)) {
                event.setCanceled(true);
            }
        }
    }

    public static boolean canCastSpell(Player player, String spellId, int spellLevel) {

        ResourceLocation resource = new ResourceLocation(spellId);
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
