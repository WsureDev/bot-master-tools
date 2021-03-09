package top.wsure.bmt

import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.utils.info
import top.wsure.bmt.commands.EditMaster
import top.wsure.bmt.commands.SendToAllGroup
import top.wsure.bmt.data.MasterConfig

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
    @ConsoleExperimentalApi
    override fun onEnable() {
        logger.info { "bot-master-tools is loading" }

        MasterConfig.reload()

        SendToAllGroup.register()

        EditMaster.register()

        logger.info { "bot-master-tools is loaded" }
    }
}