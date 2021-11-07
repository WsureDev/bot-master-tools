package top.wsure.bmt.data

import kotlinx.serialization.Serializable
import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value
import java.lang.reflect.Member
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
    var timeout:Long = LocalDateTime.now().plusMinutes(5).atOffset(ZoneOffset.UTC).toEpochSecond(),
    var approved:Boolean = false,
    val sponsor:Member,
    val invitee:Member,
    val endTime:Long
)

fun cleanTimeoutCache(){
    val now = LocalDateTime.now().atOffset(ZoneOffset.UTC).toEpochSecond()
    MasterConfig.oneVsOneCache
        .filter { it.value.timeout <= now }
        .onEach { MasterConfig.oneVsOneCache.remove(it.key) }
}