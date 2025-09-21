package org.lilbrocodes.elixiry.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public abstract class CommandUtil {
    public static void feedback(CommandContext<ServerCommandSource> src, Text message) {
        src.getSource().sendFeedback(() -> appendPrefix(message), false);
    }

    public static void feedback(PlayerEntity player, Text message) {
        player.sendMessage(appendPrefix(message), false);
    }

    public static Text appendPrefix(Text text) {
        MutableText prefix = Text.literal("")
                .append(Text.literal("[")
                        .setStyle(Style.EMPTY.withColor(0xAAAAAA)))
                .append(Text.literal("T").setStyle(Style.EMPTY.withColor(0x00a8a8)))
                .append(Text.literal("h").setStyle(Style.EMPTY.withColor(0x21bdbd)))
                .append(Text.literal("e").setStyle(Style.EMPTY.withColor(0x35d3d3)))
                .append(Text.literal("a").setStyle(Style.EMPTY.withColor(0x46e9e9)))
                .append(Text.literal("t").setStyle(Style.EMPTY.withColor(0x57ffff)))
                .append(Text.literal("r").setStyle(Style.EMPTY.withColor(0x57ffff)))
                .append(Text.literal("i").setStyle(Style.EMPTY.withColor(0x46e9e9)))
                .append(Text.literal("c").setStyle(Style.EMPTY.withColor(0x35d3d3)))
                .append(Text.literal("a").setStyle(Style.EMPTY.withColor(0x21bdbd)))
                .append(Text.literal("l").setStyle(Style.EMPTY.withColor(0x00a8a8)))
                .append(Text.literal("] ")
                        .setStyle(Style.EMPTY.withColor(0xAAAAAA)));

        return prefix.append(text);
    }

    public abstract void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment);
}
