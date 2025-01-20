package com.ragnardragus.yhnp.capability.level;

import com.ragnardragus.yhnp.Yhnp;
import com.ragnardragus.yhnp.network.PacketHandler;
import com.ragnardragus.yhnp.network.levels.McLevelsNeedMsg;
import com.ragnardragus.yhnp.network.levels.ModLevelsMsg;
import com.ragnardragus.yhnp.network.levels.SkillPointMsg;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ModLevel {


    int getSkillPoints();
    void setSkillPoints(int skillPoint);

    void addSkillPoints();

    void subtractSkillPoints();

    int getMcLevelsNeed();
    void setMcLevelsNeed(int level);

    int getPlayerModLevel();
    void setPlayerModLevel(int level);
    void addPlayerModLevel();

    void sync(ServerPlayer player);

    class Implementation implements ModLevel {

        private int skillPoints = 0;
        private int mcLevelsNeed = 1;
        private int playerModLevel = 0;

        @Override
        public int getSkillPoints() {
            return this.skillPoints;
        }

        @Override
        public void setSkillPoints(int skillPoint) {
            if(skillPoint < 0) {
                this.skillPoints = 0;
            } else {
                this.skillPoints = skillPoint;
            }
        }

        @Override
        public void addSkillPoints() {
            setSkillPoints(this.skillPoints + 1);
        }

        @Override
        public void subtractSkillPoints() {
            setSkillPoints(this.skillPoints - 1);
        }

        @Override
        public int getMcLevelsNeed() {
            return this.mcLevelsNeed;
        }

        @Override
        public void setMcLevelsNeed(int level) {
            if(level < 0) {
                this.mcLevelsNeed = 0;
            } else {
                this.mcLevelsNeed = level;
            }
        }

        @Override
        public int getPlayerModLevel() {
            return this.playerModLevel;
        }

        @Override
        public void setPlayerModLevel(int level) {
            if(level < 0) {
                this.playerModLevel = 0;
            } else if(level > 240) {
                this.playerModLevel = 240;
            } else {
                this.playerModLevel = level;
            }
        }

        @Override
        public void addPlayerModLevel() {
            setPlayerModLevel(this.playerModLevel + 1);
        }

        @Override
        public void sync(ServerPlayer player) {
            PacketHandler.sendToPlayer(new SkillPointMsg(this.getSkillPoints()), player);
            PacketHandler.sendToPlayer(new McLevelsNeedMsg(this.getMcLevelsNeed()), player);
            PacketHandler.sendToPlayer(new ModLevelsMsg(this.getPlayerModLevel()), player);
        }
    }

    class LevelCapability {

        public static Capability<ModLevel> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {});

        public LevelCapability() {}
    }

    class Provider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {

        public static final ResourceLocation IDENTIFIER = new ResourceLocation(Yhnp.RESOURCE_PREFIX + "cap_levels");

        private final ModLevel backend = new Implementation();
        private final LazyOptional<ModLevel> optionalData = LazyOptional.of(() -> backend);

        public void invalidate() {
            this.optionalData.invalidate();
        }

        @NotNull
        @Override
        public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return LevelCapability.INSTANCE.orEmpty(cap, this.optionalData);
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag compound = new CompoundTag();
            compound.putInt("skill_points", backend.getSkillPoints());
            compound.putInt("mc_levels_need", backend.getMcLevelsNeed());
            compound.putInt("player_mod_level", backend.getPlayerModLevel());
            return compound;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            backend.setSkillPoints(nbt.getInt("skill_points"));
            backend.setMcLevelsNeed(nbt.getInt("mc_levels_need"));
            backend.setPlayerModLevel(nbt.getInt("player_mod_level"));
        }
    }
}
