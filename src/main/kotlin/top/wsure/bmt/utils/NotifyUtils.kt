package top.wsure.bmt.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.utils.MiraiLogger
import kotlin.system.measureTimeMillis

class NotifyUtils {
    companion object{
        @ConsoleExperimentalApi
        private val logger: MiraiLogger = MiraiConsole.createLogger(this::class.java.name)

        @ConsoleExperimentalApi
        suspend fun notifyAllGroup(bot:Bot?, message:Message):Long{
            val useTime = measureTimeMillis {
                withContext(Dispatchers.Default) {
                    if (bot != null) {
                        notifyGroup(bot, message)
                    } else {
                        Bot.instances.forEach { notifyGroup(it, message) }
                    }
                }
            }
            logger.info("notifyAllGroup 用时:$useTime ms")
            return useTime
        }

        private suspend fun notifyGroup(bot: Bot, message:Message){
            bot.groups.forEach {
                kotlin.runCatching {
                    it.sendMessage(message)
                }
                delay(3000L+(0 .. 1000).random())
            }
        }
    }
}