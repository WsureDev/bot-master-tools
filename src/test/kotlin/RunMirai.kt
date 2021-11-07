package org.example.mirai.plugin

import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.enable
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.load
import net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import top.wsure.bmt.PluginMain
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

@ConsoleExperimentalApi
suspend fun main() {
    MiraiConsoleTerminalLoader.startAsDaemon()
    PluginMain.load()
    PluginMain.enable()
    MiraiConsole.job.join()
}