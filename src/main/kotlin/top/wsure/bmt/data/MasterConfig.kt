package top.wsure.bmt.data

import kotlinx.serialization.Serializable
import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value
import net.mamoe.mirai.contact.Member
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object MasterConfig : AutoSavePluginConfig("bot-master-tools") {
    var masters: MutableSet<Long> by value(mutableSetOf())
    var useCM: Boolean by value(false)
    var oneVsOneCache: MutableMap<String, OneVsOneDetail> by value(mutableMapOf())
    val dailyMuteCache: MutableMap<String, MutableMap<String, Long>> by value(mutableMapOf())
}

@Serializable
data class OneVsOneDetail(
    var timeout: Long = LocalDateTime.now().plusMinutes(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
    var approved: Boolean = false,
    val group: Long,
    val sponsor: Long,
    val invitee: Long,
    var endTime: Long
)

fun cleanTimeoutCache() {
    val now = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    MasterConfig.oneVsOneCache
        .filter { it.value.timeout <= now }
        .onEach { MasterConfig.oneVsOneCache.remove(it.key) }
}

fun cleanDailyMuteCache() {
    MasterConfig.dailyMuteCache.filter { it.key != todayString() }.onEach { MasterConfig.dailyMuteCache.remove(it.key) }
}

fun Member.addDailyMute(value: Long): Boolean {
    val todayMap = MasterConfig.dailyMuteCache[todayString()] ?: mutableMapOf<String, Long>().apply {
        MasterConfig.dailyMuteCache[todayString()] = this
    }
    val saveKey = "${this.group.id}::${this.id}"
    return if (todayMap[saveKey] == null) {
        todayMap[saveKey] = value
        true
    } else {
        false
    }
}


val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

fun todayString(): String {
    return LocalDateTime.now().format(formatter)
}