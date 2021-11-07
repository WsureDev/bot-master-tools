package top.wsure.bmt.utils

import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.MemberPermission
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.MessageChainBuilder

fun Member.isMemberLevel():Boolean{
    return this.permission.level == MemberPermission.MEMBER.level
}

fun Group.amITheManager():Boolean{
    return this.botPermission.level > MemberPermission.MEMBER.level
}

fun MessageChain.getAtMembers(senderMember:Member):List<Member>{
    val persons = this.mapNotNull { it as At }.map { it.target }
    return senderMember.group.members.filter { persons.contains(it.id)  }
}

suspend fun CommandSender.memberLevelBlockWithNotify(block:suspend CommandSender.(Member) -> Unit){
    when (this.subject) {
        is Group,
        is Member -> {
            val member = this.user as Member
            if(!member.group.amITheManager()){
                sendMessage(
                    MessageChainBuilder()
                    .append("机器人无禁言权限，无法使用此功能")
                    .append(At(member))
                    .build()
                )
                return
            }
            if(member.isMemberLevel()){
                // do something
                block(member)

            } else {
                sendMessage(
                    MessageChainBuilder()
                    .append("狗管理爬，自己玩去")
                    .append(At(member))
                    .build()
                )
            }
        }
        else -> {
            sendMessage("不在q群里，无法使用")
        }
    }
}
suspend fun CommandSender.memberLevelBlock(block:suspend CommandSender.(Member) -> Unit){
    when (this.subject) {
        is Group,
        is Member -> {
            val member = this.user as Member
            if(member.isMemberLevel()){
                // do something
                block(member)

            }
        }
    }
}