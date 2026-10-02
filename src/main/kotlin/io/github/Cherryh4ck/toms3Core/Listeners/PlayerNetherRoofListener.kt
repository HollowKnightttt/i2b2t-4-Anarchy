package io.github.Cherryh4ck.toms3Core.Listeners

import io.github.Cherryh4ck.toms3Core.Toms3Core
import io.papermc.paper.command.brigadier.argument.ArgumentTypes.world
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.vehicle.VehicleMoveEvent

class PlayerNetherRoofListener(private val plugin: Toms3Core) : Listener {
    // Cheap patch for VClip travel exploit
    // also prevents players from being killed by the void damage thing on the nether roof

    private fun doChecks(from: Location, to: Location, player: Player, world: World) {
        if (to.y > 127 && plugin.block_nether_roof){
            val playerLocale = player.locale().toString()
            val loc = Location(world, to.x, 120.0, to.z)
            if (player.isInsideVehicle) {
                player.leaveVehicle()
            }
            player.teleport(loc)
            if (playerLocale.startsWith("es") && plugin.spanish_enabled){
                player.sendMessage(plugin.minimessage.deserialize("<gold>${plugin.prefix} El techo del Nether está deshabilitado."))
            }
            else{
                player.sendMessage(plugin.minimessage.deserialize("<gold>${plugin.prefix} Nether Roof is currently disabled."))
            }
            plugin.logToConsole("<yellow>${player.name} tried to access Nether Roof.")
        }
        else if (to.y < 0 && plugin.patch_vclip_exploit) {
            val playerLocale = player.locale().toString()
            val isElytraEnabled = player.inventory.chestplate?.type == Material.ELYTRA && plugin.patch_vclip_exploit_mode == "ELYTRA"
            val isStrictEnabled = plugin.patch_vclip_exploit_mode == "STRICT"

            if (isElytraEnabled || isStrictEnabled){
                val loc = Location(world, to.x, 7.0, to.z)
                if (player.isInsideVehicle) {
                    player.leaveVehicle()
                }
                player.teleport(loc)
                if (playerLocale.startsWith("es") && plugin.spanish_enabled){
                    player.sendMessage(plugin.minimessage.deserialize("<gold>${plugin.prefix} No se pueden usar las elytras debajo de la bedrock debido a un exploit."))
                }
                else{
                    player.sendMessage(plugin.minimessage.deserialize("<gold>${plugin.prefix} This travel exploit is currently disabled."))
                }
                plugin.logToConsole("<yellow>${player.name} tried to execute the VClip exploit.")
            }
        }
    }

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val from = event.from
        val to = event.to ?: return

        if (from.blockY == to.blockY) {
            return
        }

        val player = event.player
        val world = player.world

        if (world.environment == World.Environment.NETHER){
            doChecks(from, to, player, world)
        }
    }

    @EventHandler
    fun onVehicleMove(event: VehicleMoveEvent) {
        val vehicle = event.vehicle
        val from = event.from
        val to = event.to ?: return
        if (from.blockY == to.blockY) {
            return
        }

        val player = vehicle.passengers.filterIsInstance<Player>().firstOrNull()
        if (player != null) {
            val world = player.world
            if (world.environment == World.Environment.NETHER){
                doChecks(from, to, player, world)
            }
        }
    }
}