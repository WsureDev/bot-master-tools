package top.wsure.bmt.data

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value

object MasterConfig: AutoSavePluginConfig("bot-master-tools") {
    var masters: HashSet<Long> by value(HashSet())
}