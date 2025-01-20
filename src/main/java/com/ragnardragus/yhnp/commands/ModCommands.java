package com.ragnardragus.yhnp.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.ragnardragus.yhnp.capability.attributes.ModAttribute;
import com.ragnardragus.yhnp.capability.attributes.ModAttributes;
import com.ragnardragus.yhnp.capability.level.ModLevel;
import com.ragnardragus.yhnp.network.PacketHandler;
import com.ragnardragus.yhnp.network.attributes.ModAttributeMsg;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.command.EnumArgument;

public class ModCommands {

    public ModCommands() {}

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("yhnp").requires(source -> source.hasPermission(2))
            .then(Commands.literal("add")

                .then(Commands.literal("skill_point")
                    .then(Commands.argument("targetPlayer", EntityArgument.player())
                            .executes(context -> addSkillPoint(context.getSource(), EntityArgument.getPlayer(context, "targetPlayer"), false))
                    )
                    .then(Commands.argument("targetPlayer", EntityArgument.player())
                        .then(Commands.literal("silent")
                            .executes(context -> addSkillPoint(context.getSource(), EntityArgument.getPlayer(context, "targetPlayer"), true))
                        )
                    )
                )

                .then(Commands.literal("attribute")
                    .then(Commands.argument("stats_name", EnumArgument.enumArgument(ModAttributes.class))
                        .then(Commands.argument("targetPlayer", EntityArgument.player())
                            .executes(context -> addStats(context.getSource(), EntityArgument.getPlayer(context, "targetPlayer"), context.getArgument("stats_name", ModAttributes.class)))
                        )
                    )
                )

                .then(Commands.literal("level")
                    .then(Commands.argument("targetPlayer", EntityArgument.player())
                            .executes(context -> addLevel(context.getSource(), EntityArgument.getPlayer(context, "targetPlayer")))
                    )
                )
            )
            .then(Commands.literal("set")

                .then(Commands.literal("skill_point")
                    .then(Commands.argument("value", IntegerArgumentType.integer(0, 256))
                        .then(Commands.argument("targetPlayer", EntityArgument.player())
                            .executes(context -> setSkillPoint(context.getSource(), EntityArgument.getPlayer(context, "targetPlayer"), IntegerArgumentType.getInteger(context, "value")))
                        )
                    )
                )

                .then(Commands.literal("attribute")
                    .then(Commands.argument("stats_name", EnumArgument.enumArgument(ModAttributes.class))
                        .then(Commands.argument("value", IntegerArgumentType.integer(1, 16))
                            .then(Commands.argument("targetPlayer", EntityArgument.player())
                                .executes(context -> setStats(context.getSource(), EntityArgument.getPlayer(context, "targetPlayer"), context.getArgument("stats_name", ModAttributes.class), IntegerArgumentType.getInteger(context, "value")))
                            )
                        )
                    )
                )
            )
            .then(Commands.literal("remove")

                .then(Commands.literal("skill_point")
                    .then(Commands.argument("targetPlayer", EntityArgument.player())
                        .executes(context -> removeSkillPoint(context.getSource(), EntityArgument.getPlayer(context, "targetPlayer")))
                    )
                )

                .then(Commands.literal("attribute")
                    .then(Commands.argument("stats_name", EnumArgument.enumArgument(ModAttributes.class))
                        .then(Commands.argument("targetPlayer", EntityArgument.player())
                            .executes(context -> removeStats(context.getSource(), EntityArgument.getPlayer(context, "targetPlayer"), context.getArgument("stats_name", ModAttributes.class)))
                        )
                    )
                )
            )
            .then(Commands.literal("clear_level")
                .then(Commands.argument("targetPlayer", EntityArgument.player())
                    .executes(context -> clearLevel(context.getSource(), EntityArgument.getPlayer(context, "targetPlayer")))
                )
            )
            .then(Commands.literal("reset")
                .then(Commands.argument("targetPlayer", EntityArgument.player())
                    .executes(context -> reset(context.getSource(), EntityArgument.getPlayer(context, "targetPlayer")))
                )
            )
        );
    }

    private static int addSkillPoint(CommandSourceStack source, ServerPlayer player, boolean silent) {
        player.getCapability(ModLevel.LevelCapability.INSTANCE).ifPresent(skillPoint -> {
            skillPoint.addSkillPoints();
            skillPoint.sync(player);

            if(!silent) {
                source.sendSuccess(() -> Component.translatable("commands.add_skill.success", player.getName()), true);
            }
        });
        return 1;
    }

    private static int setSkillPoint(CommandSourceStack source, ServerPlayer player, int amount) {
        player.getCapability(ModLevel.LevelCapability.INSTANCE).ifPresent(skillPoint -> {
            skillPoint.setSkillPoints(amount);
            skillPoint.sync(player);
            source.sendSuccess(() -> Component.translatable("commands.set_skill.success", amount, player.getName()), true);
        });

        return 1;
    }

    private static int removeSkillPoint(CommandSourceStack source, ServerPlayer player) {
        player.getCapability(ModLevel.LevelCapability.INSTANCE).ifPresent(skillPoint -> {
            skillPoint.subtractSkillPoints();
            skillPoint.sync(player);
            source.sendSuccess(() -> Component.translatable("commands.rem_skill.success", player.getName()), true);
        });

        return 1;
    }

    private static int addStats(CommandSourceStack source, ServerPlayer player, ModAttributes srAttributes) {

        player.getCapability(ModAttribute.AttributeCapability.INSTANCE).ifPresent(i -> {
            if(ModAttribute.Implementation.get(player).getAttributeLevel(srAttributes) < 16) {
                ModAttribute attribute = ModAttribute.Implementation.get(player);
                attribute.increaseAttributeLevel(srAttributes);

                PacketHandler.sendToPlayer(new ModAttributeMsg(attribute.serializeNBT()), player);
                source.sendSuccess(() -> Component.translatable("commands.add_stats.success", Component.translatable(srAttributes.displayName), player.getName()), true);
            }
        });

        return 1;
    }

    private static int setStats(CommandSourceStack source, ServerPlayer player, ModAttributes srAttributes, int amount) {

        player.getCapability(ModAttribute.AttributeCapability.INSTANCE).ifPresent( i -> {
            if(ModAttribute.Implementation.get(player).getAttributeLevel(srAttributes) >= 0 && ModAttribute.Implementation.get(player).getAttributeLevel(srAttributes) <= 16) {
                ModAttribute attribute = ModAttribute.Implementation.get(player);
                attribute.setAttributeLevel(srAttributes, amount);

                PacketHandler.sendToPlayer(new ModAttributeMsg(attribute.serializeNBT()), player);
                source.sendSuccess(() -> Component.translatable("commands.set_stats.success", amount, Component.translatable(srAttributes.displayName), player.getName()), true);
            }
        });

        return 1;
    }

    private static int removeStats(CommandSourceStack source, ServerPlayer player, ModAttributes srAttributes) {

        player.getCapability(ModAttribute.AttributeCapability.INSTANCE).ifPresent( i -> {
            if(ModAttribute.Implementation.get(player).getAttributeLevel(srAttributes) > 1) {
                ModAttribute attribute = ModAttribute.Implementation.get(player);
                attribute.decreaseAttributeLevel(srAttributes);

                PacketHandler.sendToPlayer(new ModAttributeMsg(attribute.serializeNBT()), player);
                source.sendSuccess(() -> Component.translatable("commands.rem_stats.success", Component.translatable(srAttributes.displayName), player.getName()), true);
            }
        });

        return 1;
    }

    private static int addLevel(CommandSourceStack source, ServerPlayer player) {

        player.getCapability(ModLevel.LevelCapability.INSTANCE).ifPresent(skillPoint -> {

            int oldLevelsNeed = skillPoint.getMcLevelsNeed();
            skillPoint.setMcLevelsNeed(oldLevelsNeed + 1);

            skillPoint.addPlayerModLevel();
            skillPoint.addSkillPoints();

            skillPoint.sync(player);

            source.sendSuccess(() -> Component.translatable("commands.add_level.success", player.getName()), true);
        });

        return 1;
    }

    private static int clearLevel(CommandSourceStack source, ServerPlayer player) {
        player.getCapability(ModLevel.LevelCapability.INSTANCE).ifPresent(skillPoint -> {

            skillPoint.setMcLevelsNeed(1);
            skillPoint.setPlayerModLevel(0);

            skillPoint.sync(player);

            source.sendSuccess(() -> Component.translatable("commands.clear_level.success", player.getName()), true);
        });

        return 1;
    }

    private static int reset(CommandSourceStack source, ServerPlayer player) {
        player.getCapability(ModLevel.LevelCapability.INSTANCE).ifPresent(skillPoint -> {
            player.getCapability(ModAttribute.AttributeCapability.INSTANCE).ifPresent( attr -> {

                skillPoint.setMcLevelsNeed(1);
                skillPoint.setPlayerModLevel(0);
                skillPoint.setSkillPoints(0);

                for (ModAttributes stat : ModAttributes.values()) {
                    ModAttribute attribute = ModAttribute.Implementation.get(player);
                    attribute.setAttributeLevel(stat, 1);

                    PacketHandler.sendToPlayer(new ModAttributeMsg(attribute.serializeNBT()), player);
                }

                skillPoint.sync(player);

                source.sendSuccess(() -> Component.translatable("commands.reset.success", player.getName()), true);
            });
        });

        return 1;
    }
}
