package net.team.helldivers.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class StopUseLodestoneCommand {
    public StopUseLodestoneCommand (CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("shouldUseLodestone")
                .then(Commands.literal("false").executes(this::execute)));
    }

    private int execute(CommandContext<CommandSourceStack> context) {
        Player player = Minecraft.getInstance().player;
        player.getPersistentData().putBoolean("helldivers.useLodestone", false);

        context.getSource().sendSuccess(() -> Component.literal("Stopped Using Lodestone Particles!"), true);
        return 1;
    }
}
