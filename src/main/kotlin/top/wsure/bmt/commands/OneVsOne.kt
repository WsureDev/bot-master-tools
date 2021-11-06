package top.wsure.bmt.commands

import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.RawCommand
import net.mamoe.mirai.message.data.MessageChain
import top.wsure.bmt.PluginMain

object OneVsOne: RawCommand(
    PluginMain,
    "oneVsOne",
    "1v1",""
){
    override suspend fun CommandSender.onCommand(args: MessageChain) {
        TODO("Not yet implemented")
    }
}