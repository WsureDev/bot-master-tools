package top.wsure.bmt.commands

import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.message.data.PlainText
import top.wsure.bmt.PluginMain
import top.wsure.bmt.data.MasterConfig

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

    @SubCommand("add","新增","+")
    suspend fun CommandSender.addTo(qq:Long ){
        val isSuccess = MasterConfig.masters.add(qq)
        sendMessage(PlainText("添加${qq}${if (isSuccess) "成功" else "失败"}"))
    }
}