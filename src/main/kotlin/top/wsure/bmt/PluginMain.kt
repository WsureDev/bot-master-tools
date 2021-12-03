package top.wsure.bmt

import kotlinx.coroutines.launch
import net.mamoe.mirai.console.command.CommandManager
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.command.CommandSender.Companion.toCommandSender
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.permission.AbstractPermitteeId
import net.mamoe.mirai.console.permission.PermissionService.Companion.permit
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.utils.info
import top.wsure.bmt.commands.*
import top.wsure.bmt.data.MasterConfig
import java.time.LocalDateTime
import java.time.ZoneId

object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        id = "top.wsure.bmt.PluginMain",
        name = "bot-master-tools",
        version = "0.0.1"
    ) {
        author("WsureDev")
        info(
            """
            这是一个为机器人管理者开发的简单插件, 
            目前仅打算支持很少的几个功能
        """.trimIndent()
        )
    }
) {
    @ExperimentalCommandDescriptors
    @ConsoleExperimentalApi
    override fun onEnable() {
        logger.info { "bot-master-tools is loading" }

        MasterConfig.reload()

        LotteryMuteCmd.register()

        NightNightMuteCmd.register()

        DailyMuteCmd.register()

        OneVsOne.register()

        OneVsOneApprove.register()

        OneVsOneReject.register()

        BeltAndRoadInitiative.register()

        AbstractPermitteeId.AnyContact.permit(this.parentPermission)

        globalEventChannel().subscribeAlways<GroupMessageEvent>{
            groupMessageEvent ->
            val key = "${groupMessageEvent.group.id}::${groupMessageEvent.sender.id}"
            val member = MasterConfig.oneVsOneCache[key]
            if(member != null && member.approved){
                groupMessageEvent.sender.mute(
                    (member.endTime - LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()
                        .toEpochMilli()).toInt() / 1000
                )
                groupMessageEvent.group.sendMessage("击剑也当逃兵是吧？抓回去")
            }
        }

        logger.info { "bot-master-tools is loaded" }
    }
}