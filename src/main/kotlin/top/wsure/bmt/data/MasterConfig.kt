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
    val damageDescriptionConfig: List<DamageRangeDescription> by value(mutableListOf(
        DamageRangeDescription(-1,0, mutableListOf(
            "{o}踩到了观众丢出的香蕉皮滑倒了",
            "{o}的攻势被车万玩家{d}完全闪避开了",
            "气喘嘘嘘的{o}只来得及喘了口气",
            "{o}一剑从{d}的门牙中缝划下一片菜叶",
            "{o}掏出一张不够色的图",
            "{o}一剑剐掉了{d}的一片汗毛",
            "{o}一剑挑断{d}的裤腰带",
        )),
        DamageRangeDescription(0,10, mutableListOf(
            "{o}剑尖划破了{d}的皮肤",
            "{o}还没动手,{d}自己崴了脚",
            "{o}吐出牙签,扎到了{d}脚背上",
            "{o}发动火球术却降下冰雹",
            "{o}一剑磕掉了{d}的门牙",
            "{o}掏出一张足够色的色图",
            "{d}向{o}弹出鼻屎,却被{o}一剑拍了回来",
        )),
        DamageRangeDescription(10,30, mutableListOf(
            "{o}在{d}面前咬碎打火机",
            "{o}对{d}使出女子防身术",
            "{o}对{d}施展军体拳",
            "{o}对{d}弹出鼻屎",
            "{o}对{d}丢出xz同款唇膏",
        )),
        DamageRangeDescription(30,60, mutableListOf(
            "{o}施展闪电五连鞭",
            "{o}施展混元形意太极",
            "{o}使用接化发",
            "{o}使用马家刀法之'就这么一刀'",
            "{o}使出左正蹬",
            "{o}使用右边腿",
            "{o}施展左刺拳",
            "{o}使出点到为止的传统功夫",
            "{o}使用不讲武德的散打",
        )),
        DamageRangeDescription(60,90, mutableListOf(
            "{o}召唤小火车创向{d}",
            "{o}召唤许昊龙施展炫步",
            "{o}扔出人机分离10米的8848",
            "{o}召唤一眼顶真",
            "{o}召唤嘉然小姐的狗",
            "{o}对{d}讲述s6第一个王者的故事",
        )),
        DamageRangeDescription(90,100, mutableListOf(
            "{o}一剑挑破{d}的大动脉",
            "{o}施展滑铲从{d}身下开膛",
            "{o}嘲笑{d}打篮球像cxk",
            "{o}把{d}的信息发布在同性交友平台上",
            "{o}云了{d}最爱的游戏,并且发表云言论",
        ))
    )
    )
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
@Serializable
data class DamageRangeDescription(
    val min:Int = 0,
    val max:Int = 100,
    val descriptions:List<String> = emptyList()
)

