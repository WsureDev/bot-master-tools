package top.wsure.bmt.commands

import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.message.data.PlainText
import top.wsure.bmt.PluginMain
import top.wsure.bmt.data.MasterConfig
import top.wsure.bmt.utils.MasterUtils.Companion.isMaster

object EditMaster:CompositeCommand(
    PluginMain,
    "bmt",
    "master","骂死他"
) {
    @ExperimentalCommandDescriptors
    @ConsoleExperimentalApi
    override val prefixOptional = true

    @SubCommand("list","列表")
    suspend fun CommandSender.list(){
        sendMessage(PlainText(MasterConfig.masters.joinToString("\n")))
    }

    @SubCommand("add","新增","加")
    suspend fun CommandSender.addTo(user:Member ){
        if (isMaster(this.user)) {
            val isSuccess = MasterConfig.masters.add(user.id)
            sendMessage(PlainText("添加${user.id}${if (isSuccess) "成功" else "失败"}"))
        }
    }

    @SubCommand("del","remove","删除","删")
    suspend fun CommandSender.delTo(user:Member ){
        if (isMaster(this.user)) {
            val isSuccess = MasterConfig.masters.remove(user.id)
            sendMessage(PlainText("删除${user.id}${if (isSuccess) "成功" else "失败"}"))
        }
    }
}