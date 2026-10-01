package ru.minefunland.echoshards

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.io.File
import java.util.UUID

class ProgressManager(private val plugin: EchoShardsPlugin) {

    private val file: File = File(plugin.dataFolder, "progress.yml")
    private val config: FileConfiguration = YamlConfiguration.loadConfiguration(file)

    // Проверить: сдал ли игрок этот осколок?
    fun hasShard(player: Player, shardId: String): Boolean {
        val uuid = player.uniqueId.toString()
        return config.getStringList("players.$uuid").contains(shardId)
    }

    // Записать: игрок сдал осколок
    fun addShard(player: Player, shardId: String) {
        val uuid = player.uniqueId.toString()
        val list = config.getStringList("players.$uuid").toMutableList()
        if (!list.contains(shardId)) {
            list.add(shardId)
            config.set("players.$uuid", list)
            save()
        }
    }

    fun resetProgress(player: Player) {
        val uuid = player.uniqueId.toString()
        config.set("players.$uuid", null) // Удаляем запись
        save()
        plugin.logger.info("Прогресс ${player.name} сброшен")
    }

    // Сохранить в файл
    private fun save() {
        try {
            config.save(file)
        } catch (e: Exception) {
            plugin.logger.warning("Не удалось сохранить прогресс: ${e.message}")
        }
    }
}