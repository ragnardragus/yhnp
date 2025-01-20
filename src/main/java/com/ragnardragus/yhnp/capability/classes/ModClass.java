package com.ragnardragus.yhnp.capability.classes;

import com.ragnardragus.yhnp.Yhnp;
import com.ragnardragus.yhnp.network.PacketHandler;
import com.ragnardragus.yhnp.network.classes.ModClassMsg;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ModClass {

    String getModClass();

    void setModClass(String ModClass);

    boolean hasPicked();

    boolean isClassScreenOpened();

    void setClassScreenOpened(boolean classScreenOpened);

    void sync(ServerPlayer player);

    class Implementation implements ModClass {

        private String modClass = "";
        private boolean classScreenOpened = false;

        @Override
        public String getModClass() {
            return modClass;
        }

        @Override
        public void setModClass(String modClass) {
            this.modClass = modClass;
        }

        @Override
        public boolean hasPicked() {
            return modClass != null && !modClass.equals("");
        }

        @Override
        public boolean isClassScreenOpened() {
            return classScreenOpened;
        }

        @Override
        public void setClassScreenOpened(boolean classScreenOpened) {
            this.classScreenOpened = classScreenOpened;
        }

        @Override
        public void sync(ServerPlayer player) {
            PacketHandler.sendToPlayer(new ModClassMsg(this.getModClass()), player);
        }
    }

    class ModClassCapability {
        public static Capability<ModClass> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {});

        public ModClassCapability() {}
    }

    class Provider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {

        public static final ResourceLocation IDENTIFIER = new ResourceLocation(Yhnp.RESOURCE_PREFIX + "cap_classes");

        private final ModClass backend = new Implementation();
        private final LazyOptional<ModClass> optionalData = LazyOptional.of(() -> backend);

        public void invalidate() {
            this.optionalData.invalidate();
        }

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull net.minecraftforge.common.capabilities.Capability<T> cap, @Nullable Direction side) {
            return ModClassCapability.INSTANCE.orEmpty(cap, this.optionalData);
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag compound = new CompoundTag();
            compound.putString("class_id", backend.getModClass());
            return compound;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            backend.setModClass(nbt.getString("class_id"));
        }
    }
}
