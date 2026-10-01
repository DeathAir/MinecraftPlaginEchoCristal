package ru.minefunland.echoshards

import org.bukkit.plugin.java.JavaPlugin

class EchoShardsPlugin : JavaPlugin() {

    lateinit var progressManager: ProgressManager
        private set

    override fun onEnable() {
        logger.info("EchoShards enabled!")

        // Создаём менеджер прогресса
        progressManager = ProgressManager(this)

        // Регистрируем слушатель событий (алтарь)
        server.pluginManager.registerEvents(AltarListener(this), this)

        // Регистрируем команды
        getCommand("shard")?.setExecutor(ShardCommand(this))
    }

    override fun onDisable() {
        logger.info("EchoShards disabled!")
    }
}