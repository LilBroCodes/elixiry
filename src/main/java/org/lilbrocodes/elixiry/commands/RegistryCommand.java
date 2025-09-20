package org.lilbrocodes.elixiry.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.lilbrocodes.elixiry.util.AbstractPseudoRegistry;

import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RegistryCommand extends CommandUtil {
    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(
                CommandManager.literal("registry").then(
                        CommandManager.literal("dump").then(
                                CommandManager.argument("registry", StringArgumentType.string()).suggests(this::registries).executes(ctx -> {
                                    String id = ctx.getArgument("registry", String.class);

                                    AbstractPseudoRegistry<?> registry = AbstractPseudoRegistry.registry(id);

                                    if (registry == null) {
                                        feedback(ctx, Text.literal("Registry not found: " + id));
                                        return 0;
                                    }

                                    Path dumpDir = ctx.getSource().getServer().getRunDirectory().toPath()
                                            .resolve("dumps");
                                    try {
                                        Files.createDirectories(dumpDir);
                                    } catch (Exception e) {
                                        feedback(ctx, Text.literal("Failed to create dumps directory: " + e.getMessage()));
                                        return 0;
                                    }

                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
                                    String timestamp = LocalDateTime.now().format(formatter);

                                    String fileName = "dump-" + id.replace("_", "-") + "-" + timestamp + ".txt";
                                    Path filePath = dumpDir.resolve(fileName);

                                    try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
                                        registry.getAll().entrySet().forEach(entry -> {
                                            try {
                                                writer.write(entry.toString());
                                                writer.newLine();
                                            } catch (Exception e) {
                                                feedback(ctx, Text.literal("Failed to write registry dump: " + e.getMessage()));
                                            }
                                        });
                                    } catch (Exception e) {
                                        feedback(ctx, Text.literal("Failed to write registry dump: " + e.getMessage()));
                                        return 0;
                                    }

                                    feedback(ctx, Text.literal("Registry dump written to " + filePath.getFileName()));
                                    return 1;
                                })

                        )
                )
        );
    }

    private CompletableFuture<Suggestions> registries(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
        List.of(
                "brewing_recipes"
        ).forEach(builder::suggest);
        return builder.buildFuture();
    }

}
