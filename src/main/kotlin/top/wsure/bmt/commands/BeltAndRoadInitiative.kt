package top.wsure.bmt.commands

import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.RawCommand
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.MessageChainBuilder
import top.wsure.bmt.PluginMain
import top.wsure.bmt.utils.getAtMembers
import top.wsure.bmt.utils.isMemberLevel
import top.wsure.bmt.utils.memberLevelBlockWithNotify

object BeltAndRoadInitiative : RawCommand(
    PluginMain,
    "BeltAndRoadInitiative",
    "一带一路", "带路",
    description = "4. 一带一路 邀请最多5位群友与你与你一起禁言"
) {
    override suspend fun CommandSender.onCommand(args: MessageChain) {
        this.memberLevelBlockWithNotify { sponsor ->
            //get at person
            var persons = args.getAtMembers(sponsor)
            if (persons.isEmpty()) {
                sendMessage("不带别人玩是吧？")
                sponsor.mute((0..10 * 60).random())
                return@memberLevelBlockWithNotify
            }
            persons = persons.filter { it.isMemberLevel() }
            if (persons.isEmpty()) {
                sendMessage("at的全是狗管理,你是不是没事找事")
                sponsor.mute((0..10 * 60).random())
                return@memberLevelBlockWithNotify
            } else {
                val sponsorTime = (0..10 * 60).random()
                sponsor.mute(sponsorTime)
                val chain = MessageChainBuilder()
                    .append("恭喜你成功带动了 ")

                persons.take(5).forEach {
                    chain.append(At(it))
                        .append(" ")
                    it.mute(sponsorTime / 2)
                }
                chain.append("的经济发展,你做的好啊")

                sendMessage(chain.build())

                return@memberLevelBlockWithNotify
            }
        }
    }
}