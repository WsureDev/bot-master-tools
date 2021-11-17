package top.wsure.bmt.commands

import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.MessageChainBuilder
import top.wsure.bmt.PluginMain
import top.wsure.bmt.data.addDailyMute
import top.wsure.bmt.data.cleanDailyMuteCache
import top.wsure.bmt.utils.memberLevelBlockWithNotify

object LotteryMuteCmd : SimpleCommand(
    PluginMain,
    "Lottery",
    "抽奖",
    description = "1. 抽奖 = 自抽0-10分钟禁言"
) {

    @Handler
    suspend fun CommandSender.handle() = this.memberLevelBlockWithNotify { member ->
        val muteLength = (0..60 * 10).random()
        member.mute(muteLength)
        sendMessage(
            MessageChainBuilder()
                .append("恭喜")
                .append(At(member))
                .append("获得${muteLength}秒禁言")
                .build()
        )
    }
}

object NightNightMuteCmd : SimpleCommand(
    PluginMain,
    "NightNight",
    "晚安",
    description = "2. 晚安 = 自抽1-8小时精致睡眠"
) {

    @Handler
    suspend fun CommandSender.handle() = this.memberLevelBlockWithNotify { member ->
        val muteLength = (1..8).random()
        member.mute(muteLength * 60 * 60)
        sendMessage(
            MessageChainBuilder()
                .append("你已获得${muteLength}小时精致睡眠")
                .append(At(member))
                .append(",晚安NightNight")
                .build()
        )
    }
}


object DailyMuteCmd : SimpleCommand(
    PluginMain,
    "Daily",
    "jrrp", "今日人品", "人品", "ybb", "小火车",
    description = "5. 今日人品 抽取每日一次的随机时长禁言"
) {

    @Handler
    suspend fun CommandSender.handle() = this.memberLevelBlockWithNotify { member ->
        val muteLength = (1..1000).random()
        cleanDailyMuteCache()
        if (member.addDailyMute(muteLength.toLong())) {

            sendMessage(
                MessageChainBuilder()
                    .append(At(member))
                    .append(",你今天的小火车速度为${muteLength}km/h,看我把你绑在铁轨上")
                    .build()
            )
            member.mute(1000 - muteLength)
        } else {
            sendMessage(
                MessageChainBuilder()
                    .append(At(member))
                    .append(",你今天已经被创过了,明天再来吧")
                    .build()
            )
        }
    }
}

