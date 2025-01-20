package com.ragnardragus.yhnp.capability.attributes;

import com.ragnardragus.yhnp.Yhnp;
import com.ragnardragus.yhnp.network.PacketHandler;
import com.ragnardragus.yhnp.network.attributes.WarningMsg;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ModAttribute {

    int[] getAttributeLevels();
    void setAttributeLevels(int[] attributes);
    int getAttributeLevel(ModAttributes attributes);
    void setAttributeLevel(ModAttributes attributes, int level);
    void increaseAttributeLevel(ModAttributes attributes);
    void decreaseAttributeLevel(ModAttributes attributes);
    void showWarning(Player player, ResourceLocation resource);
    CompoundTag serializeNBT();
    void deserializeNBT(CompoundTag nbt);


    class Implementation implements ModAttribute, INBTSerializable<CompoundTag> {

        public int[] attributeLevels = new int[]{1, 1, 1, 1, 1, 1, 1, 1};

        @Override
        public int[] getAttributeLevels() {
            return attributeLevels;
        }

        @Override
        public void setAttributeLevels(int[] attributeLevels) {
            this.attributeLevels = attributeLevels;
        }

        @Override
        public int getAttributeLevel(ModAttributes attributes) {
            return attributeLevels[attributes.index];
        }

        @Override
        public void setAttributeLevel(ModAttributes attributes, int level) {
            attributeLevels[attributes.index] = level;
        }

        @Override
        public void increaseAttributeLevel(ModAttributes attributes) {
            attributeLevels[attributes.index]++;
        }

        @Override
        public void decreaseAttributeLevel(ModAttributes attributes) {
            attributeLevels[attributes.index]--;
        }

        @Override
        public void showWarning(Player player, ResourceLocation resource) {
            PacketHandler.sendToPlayer(new WarningMsg(resource), (ServerPlayer) player);
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag compound = new CompoundTag();

            for (int i = 0; i < ModAttributes.values().length; i++) {
                compound.putInt(ModAttributes.values()[i].displayName, attributeLevels[ModAttributes.values()[i].index]);
            }

            return compound;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            for (int i = 0; i < ModAttributes.values().length; i++) {
                attributeLevels[i] = nbt.getInt(ModAttributes.values()[i].displayName);
            }
        }

        public static ModAttribute get(Player player) {
            return player.getCapability(AttributeCapability.INSTANCE).orElseThrow(() ->
                    new IllegalArgumentException("Player " + player.getName().getVisualOrderText() + " does not have a SRAttributes Model!")
            );
        }

        public static ModAttribute get() {
            return Minecraft.getInstance().player.getCapability(AttributeCapability.INSTANCE).orElseThrow(() ->
                    new IllegalArgumentException("Player does not have a SRAttributes Model!")
            );
        }
    }

    class AttributeCapability {
        public static Capability<ModAttribute> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {});

        public AttributeCapability() {}

    }

    class Provider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {

        public static final ResourceLocation IDENTIFIER = new ResourceLocation(Yhnp.RESOURCE_PREFIX + "cap_skills");

        private final Implementation backend = new Implementation();
        private final LazyOptional<ModAttribute> optionalData = LazyOptional.of(() -> backend);

        public void invalidate()
        {
            optionalData.invalidate();
        }

        @NotNull
        @Override
        public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side) {
            return AttributeCapability.INSTANCE.orEmpty(capability, this.optionalData);
        }

        @Override
        public CompoundTag serializeNBT() {
            return backend.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            backend.deserializeNBT(nbt);
        }
    }
}
