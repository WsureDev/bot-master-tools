package top.wsure.bmt.data

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value

object MasterConfig: AutoSavePluginConfig("bot-master-tools") {
    var masters: MutableSet<Long> by value(mutableSetOf())
    var useCM: Boolean by value(false)
}