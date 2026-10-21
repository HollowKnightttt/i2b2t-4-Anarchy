package io.github.Cherryh4ck.toms3Core.Commands

import io.github.Cherryh4ck.toms3Core.Toms3Core
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

class Suicide : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(Component.text("Only players can execute this command!", NamedTextColor.RED))
            return true
        }

        val player: Player = sender

        
        val currentHealth: Double = player.health
        
        
        player.health = 0.0

        
        player.sendMessage(Component.text("You have taken your own life.", NamedTextColor.GRAY))
        
        return true
    }
}
