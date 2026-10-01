package ru.minefunland.echoshards

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.persistence.PersistentDataType

class AltarListener(private val plugin: EchoShardsPlugin) : Listener {

    private val shardKey = NamespacedKey(plugin, "shard_id")

    @EventHandler
    fun onAltarClick(event: PlayerInteractEvent) {
        // Проверяем: ПКМ по блоку?
        if (event.action != Action.RIGHT_CLICK_BLOCK) return
        val block = event.clickedBlock ?: return

        // Проверяем: это алтарь? (Lodestone = магнетит)
        if (block.type != Material.LODESTONE) return

        // Проверяем: в руке осколок?
        val player = event.player
        val item = player.inventory.itemInMainHand
        val shardId = item.itemMeta?.persistentDataContainer
            ?.get(shardKey, PersistentDataType.STRING) ?: return

        // Проверяем: не сдавал ли уже?
        if (plugin.progressManager.hasShard(player, shardId)) {
            player.sendMessage("§cТы уже сдал этот осколок!")
            event.isCancelled = true
            return
        }

        // Удаляем осколок из руки
        player.inventory.setItemInMainHand(null)

        // Записываем прогресс
        plugin.progressManager.addShard(player, shardId)

        // Сообщение игроку
        player.sendMessage("§aТы сдал осколок: §e$shardId")

        // Эффекты
        block.world.spawnParticle(
            org.bukkit.Particle.END_ROD,
            block.location.add(0.5, 1.0, 0.5),
            50, 0.5, 0.5, 0.5, 0.1
        )

        event.isCancelled = true
    }
}