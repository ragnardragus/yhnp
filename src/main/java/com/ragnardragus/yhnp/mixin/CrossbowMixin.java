package com.ragnardragus.yhnp.mixin;

import com.ragnardragus.yhnp.requirement.RequirementHelper;
import com.ragnardragus.yhnp.compat.GameStageCompat;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CrossbowItem.class)
public class CrossbowMixin {

    @Inject(method = {"use", "m_41682_"}, at = {@At("HEAD")}, cancellable = true)
    public void onUseCrossbow(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        ItemStack itemstack = player.getItemInHand(hand);
        ItemStack arrow = player.getProjectile(itemstack);

        if(GameStageCompat.isLoaded()) {
            if (!player.isCreative() && (!GameStageCompat.canUseItem(player, arrow))) {
                cir.setReturnValue(InteractionResultHolder.fail(itemstack));
            }
        }

        if(!arrow.isEmpty()) {
            if (!player.isCreative() && !RequirementHelper.canUseItem(player, arrow)) {
                cir.setReturnValue(InteractionResultHolder.fail(itemstack));
            }
        }
    }
}
