package top.wsure.bmt.commands

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.RawCommand
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.NormalMember
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.MessageChainBuilder
import top.wsure.bmt.PluginMain
import top.wsure.bmt.commands.OneVsOneReject.handle
import top.wsure.bmt.data.MasterConfig
import top.wsure.bmt.data.OneVsOneDetail
import top.wsure.bmt.data.cleanTimeoutCache
import top.wsure.bmt.utils.getAtMembers
import top.wsure.bmt.utils.isMemberLevel
import top.wsure.bmt.utils.memberLevelBlock
import top.wsure.bmt.utils.memberLevelBlockWithNotify
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

object OneVsOne : RawCommand(
    PluginMain,
    "oneVsOne",
    "1v1", "单挑",
    description = "3. 1v1 邀请一位群友与你1v1决斗，两人先被禁言10分钟，先被解禁的为获胜者"
) {
    override suspend fun CommandSender.onCommand(args: MessageChain) {

        this.memberLevelBlockWithNotify { sponsor ->
            cleanTimeoutCache()
            val sponsorDetail = MasterConfig.oneVsOneCache["${sponsor.group.id}::${sponsor.id}"]
            if (sponsorDetail != null) {
                sendMessage("你已经在1v1当中了")
                return@memberLevelBlockWithNotify
            }
            //get at person
            var persons = args.getAtMembers(sponsor)
            if (persons.isEmpty()) {
                sendMessage("1v1需要at你的对手")
                return@memberLevelBlockWithNotify
            }
            persons = persons.filter { it.isMemberLevel() }
            if (persons.isEmpty()) {
                sendMessage("at的全是狗管理，你是不是没事找事")
                return@memberLevelBlockWithNotify
            } else if (persons.size > 1) {
                sendMessage("1v1只能邀请一位对手")
                return@memberLevelBlockWithNotify
            } else {
                val invitee = persons.first()
                //todo write cache
                cleanTimeoutCache()
                val detail = MasterConfig.oneVsOneCache["${sponsor.group.id}::${invitee.id}"]
                //todo clean cache Trigger
                if (detail != null) {
                    sendMessage("对方已经在1v1当中了")
                } else {
                    MasterConfig.oneVsOneCache["${sponsor.group.id}::${invitee.id}"] =
                        OneVsOneDetail(
                            group = sponsor.group.id,
                            sponsor = sponsor.id,
                            invitee = invitee.id,
                            endTime = -1L
                        )
                    MasterConfig.oneVsOneCache["${sponsor.group.id}::${sponsor.id}"] =
                        OneVsOneDetail(
                            group = sponsor.group.id,
                            sponsor = sponsor.id,
                            invitee = invitee.id,
                            endTime = -1L
                        )
                    sendMessage(
                        MessageChainBuilder()
                            .append(At(invitee))
                            .append("阁下, ")
                            .append(At(sponsor))
                            .append("向你发起1v1决斗，是否接受？")
                            .append("(发送 接受 或 拒绝 ,有效期3分钟)")
                            .build()
                    )
                }
            }

        }
    }
}

object OneVsOneApprove : SimpleCommand(
    PluginMain,
    "Approve",
    "接受",
    description = "3. 接受决斗"
) {
    @ExperimentalCommandDescriptors
    @ConsoleExperimentalApi
    override val prefixOptional = true

    @Handler
    suspend fun CommandSender.handle() = this.getOneVsOneSponsor { invitee, inviteeDetail, sponsor ->
        val sponsorDetail =
            MasterConfig.oneVsOneCache["${sponsor.group.id}::${sponsor.id}"] ?: inviteeDetail.copy().apply {
                MasterConfig.oneVsOneCache["${sponsor.group.id}::${sponsor.id}"] = this
            }
        inviteeDetail.approved = true
        sponsorDetail.approved = true
        invitee.mute(10 * 60)
        sponsor.mute(10 * 60)
        val now = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        inviteeDetail.endTime =
            LocalDateTime.now().plusSeconds((2 * 60L..8 * 60L).random()).atZone(ZoneId.systemDefault()).toInstant()
                .toEpochMilli()
        sponsorDetail.endTime =
            LocalDateTime.now().plusSeconds((2 * 60L..8 * 60L).random()).atZone(ZoneId.systemDefault()).toInstant()
                .toEpochMilli()
        val isSponsorWin = sponsorDetail.endTime < inviteeDetail.endTime
        launch {
            delay(inviteeDetail.endTime - now)
            (invitee as NormalMember).unmute()
            if (!isSponsorWin) {
                sendMessage(
                    MessageChainBuilder()
                        .append("恭喜")
                        .append(At(invitee))
                        .append("在与")
                        .append(At(sponsor))
                        .append("的决斗中获胜，趁ta还没解禁，赶紧说点垃圾话吧")
                        .build()
                )
            }
            MasterConfig.oneVsOneCache.remove("${invitee.group.id}::${invitee.id}")
        }
        launch {
            delay(sponsorDetail.endTime - now)
            (sponsor as NormalMember).unmute()
            if (isSponsorWin) {
                sendMessage(
                    MessageChainBuilder()
                        .append("恭喜")
                        .append(At(sponsor))
                        .append("在与")
                        .append(At(invitee))
                        .append("的决斗中获胜，趁ta还没解禁，赶紧说点垃圾话吧")
                        .build()
                )
            }
            MasterConfig.oneVsOneCache.remove("${sponsor.group.id}::${sponsor.id}")
        }
    }
}

object OneVsOneReject : SimpleCommand(
    PluginMain,
    "Reject",
    "拒绝",
    description = "3. 拒绝决斗"
) {
    @ExperimentalCommandDescriptors
    @ConsoleExperimentalApi
    override val prefixOptional = true

    @Handler
    suspend fun CommandSender.handle() = this.getOneVsOneSponsor { invitee, _, sponsor ->
        MasterConfig.oneVsOneCache.remove("${sponsor.group.id}::${sponsor.id}")
        MasterConfig.oneVsOneCache.remove("${invitee.group.id}::${invitee.id}")
        sendMessage(
            MessageChainBuilder()
                .append(At(sponsor))
                .append("你的邀请被")
                .append(At(invitee))
                .append("拒绝了，建议说点垃圾话嘲讽下ta吧")
                .build()
        )
    }
}

suspend fun CommandSender.getOneVsOneSponsor(block: suspend CommandSender.(Member, OneVsOneDetail, Member) -> Unit) =
    this.memberLevelBlock { invitee ->
        cleanTimeoutCache()
        val inviteeDetail = MasterConfig.oneVsOneCache["${invitee.group.id}::${invitee.id}"]
        if (inviteeDetail == null) {
            return@memberLevelBlock
        } else {
            if (inviteeDetail.approved) {
                invitee.mute(
                    (inviteeDetail.endTime - LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()
                        .toEpochMilli()).toInt() / 1000
                )
                sendMessage("提前越狱还起哄是吧？")
            } else {
                val sponsor = invitee.group.members[inviteeDetail.sponsor]
                if (sponsor == null) {
                    sendMessage("发起人已经退群,GG")
                    MasterConfig.oneVsOneCache.remove("${invitee.group.id}::${inviteeDetail.sponsor}")
                    MasterConfig.oneVsOneCache.remove("${invitee.group.id}::${invitee.id}")
                    return@memberLevelBlock
                }
                block(invitee, inviteeDetail, sponsor)
            }
        }
    }