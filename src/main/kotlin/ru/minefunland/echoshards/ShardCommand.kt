package ru.minefunland.echoshards

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

class ShardCommand(private val plugin: EchoShardsPlugin) : CommandExecutor {

    private val shardKey = NamespacedKey(plugin, "shard_id")

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        // Только для игроков
        if (sender !is Player) {
            sender.sendMessage("Только для игроков!")
            return true
        }

        // Если нет аргументов — показать использование
        if (args.isEmpty()) {
            sender.sendMessage("§cИспользование: /shard give <id> | /shard reset <игрок>")
            return true
        }

        // /shard give <id>
        if (args[0].equals("give", ignoreCase = true)) {
            if (args.size < 2) {
                sender.sendMessage("§cИспользование: /shard give <id>")
                return true
            }

            val shardId = args[1]

            val shard = ItemStack(Material.ECHO_SHARD)
            val meta = shard.itemMeta

            // Название
            meta.displayName(Component.text("Осколок: $shardId", NamedTextColor.LIGHT_PURPLE))

            // Записываем ID в PDC
            meta.persistentDataContainer.set(shardKey, PersistentDataType.STRING, shardId)

            shard.itemMeta = meta
            sender.inventory.addItem(shard)

            sender.sendMessage("§aВыдан осколок: §e$shardId")
            return true
        }

        // /shard reset <игрок>
        if (args[0].equals("reset", ignoreCase = true)) {
            if (!sender.hasPermission("echoshards.admin")) {
                sender.sendMessage("§cУ тебя нет прав!")
                return true
            }

            if (args.size < 2) {
                sender.sendMessage("§cИспользование: /shard reset <игрок>")
                return true
            }

            val target = Bukkit.getPlayer(args[1])
            if (target != null) {
                plugin.progressManager.resetProgress(target)
                sender.sendMessage("§aПрогресс ${target.name} сброшен!")
            } else {
                sender.sendMessage("§cИгрок не найден.")
            }
            return true
        }

        sender.sendMessage("§cНеизвестная команда. Использование: /shard give <id> | /shard reset <игрок>")
        return true
    }
}