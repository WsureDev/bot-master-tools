package top.wsure.bmt.commands

import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.RawCommand
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.MessageChainBuilder
import top.wsure.bmt.PluginMain
import top.wsure.bmt.data.MasterConfig
import top.wsure.bmt.utils.getAtMembers
import top.wsure.bmt.utils.isMemberLevel
import top.wsure.bmt.utils.memberLevelBlock

object OneVsOne: RawCommand(
    PluginMain,
    "oneVsOne",
    "1v1","单挑",
    description = "3. 1v1 邀请一位群友与你1v1决斗，两人先被禁言10分钟，先被解禁的为获胜者"
){
    override suspend fun CommandSender.onCommand(args: MessageChain){

        this.memberLevelBlock{ member ->
            //get at person
            var persons = args.getAtMembers(member)
            if(persons.isEmpty()){
                sendMessage("1v1需要at你的对手")
                return@memberLevelBlock
            }
            persons = persons.filter { it.isMemberLevel() }
            if(persons.isEmpty()) {
                sendMessage("at的全是狗管理，你是不是没事找事")
                return@memberLevelBlock
            } else if(persons.size > 1) {
                sendMessage("1v1只能邀请一位对手")
                return@memberLevelBlock
            } else {
                val invitee = persons.first()
                //todo write cache
                val detail = MasterConfig.oneVsOneCache["${member.group.id}::${invitee}"]
                //todo clean cache Trigger
                if(detail == null){
                    sendMessage(MessageChainBuilder()
                        .append(At(invitee))
                        .append("阁下, ")
                        .append(At(member))
                        .append("向你发起1v1决斗，是否接受？")
                        .build())
                } else {
                    sendMessage(MessageChainBuilder()
                        .append(At(invitee))
                        .append("阁下, ")
                        .append(At(member))
                        .append("向你发起1v1决斗，是否接受？")
                        .build())
                }

            }

        }
    }


}