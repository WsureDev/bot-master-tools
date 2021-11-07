package top.wsure.bmt.data

import kotlinx.serialization.Serializable
import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value
import net.mamoe.mirai.contact.Member
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

object MasterConfig: AutoSavePluginConfig("bot-master-tools") {
    var masters: MutableSet<Long> by value(mutableSetOf())
    var useCM: Boolean by value(false)
    var oneVsOneCache: MutableMap<String,OneVsOneDetail> by value(mutableMapOf())

}
@Serializable
data class OneVsOneDetail(
    var timeout:Long = LocalDateTime.now().plusMinutes(3).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
    var approved:Boolean = false,
    val group:Long,
    val sponsor:Long,
    val invitee:Long,
    var endTime:Long
)

fun cleanTimeoutCache(){
    val now = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    MasterConfig.oneVsOneCache
        .filter { it.value.timeout <= now }
        .onEach { MasterConfig.oneVsOneCache.remove(it.key) }
}