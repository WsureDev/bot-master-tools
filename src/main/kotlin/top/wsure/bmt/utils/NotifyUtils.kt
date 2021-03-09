package top.wsure.bmt.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import net.mamoe.mirai.Bot
import net.mamoe.mirai.message.data.Message
import kotlin.random.Random

class NotifyUtils {
    companion object{
        suspend fun notifyAllGroup(bot:Bot,message:Message){
            withContext(Dispatchers.Default){
                val random = Random(1000L)
                bot.groups.forEach {
                    it.sendMessage(message)
                    delay(3000+ random.nextLong())
                }
            }
        }
    }
}