package top.wsure.bmt.commands

import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.contact.Member
import top.wsure.bmt.PluginMain

object BaseMuteCmd : SimpleCommand(
    PluginMain,
    "Lottery",
    "抽奖",
    description = "1. 抽奖 = 自抽0-10分钟禁用"
) {

    @Handler
    suspend fun CommandSender.handle() {
        when (this.subject) {
            is Member -> {
                val member = this.user as Member
                val group = member.group
            }
            else -> {
                sendMessage("不在q群里，无法游玩")
            }
        }
    }
}

