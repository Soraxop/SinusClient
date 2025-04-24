package sinus.hack.features.cmd.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import org.jetbrains.annotations.NotNull;
import sinus.hack.features.cmd.Command;
import sinus.hack.core.manager.client.ModuleManager;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class TrackerCommand extends Command {
    public TrackerCommand() {
        super("tracker");
    }

    @Override
    public void executeBuild(@NotNull LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            if (ModuleManager.tracker.isEnabled()) {
                ModuleManager.tracker.sendTrack();
            }

            return SINGLE_SUCCESS;
        });
    }
}
