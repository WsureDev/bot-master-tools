package top.wsure.bmt.commands

import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.PlainText
import top.wsure.bmt.PluginMain
import top.wsure.bmt.data.MasterConfig
import top.wsure.bmt.utils.MasterUtils.Companion.isMaster

object EditMaster:CompositeCommand(
    PluginMain,
    "bmt",
    "master","骂死他",
    description = "bot-master-tools插件设置"
) {
    @ExperimentalCommandDescriptors
    @ConsoleExperimentalApi
    override val prefixOptional = true

    @SubCommand("list","列表")
    @Description("master列表")
    suspend fun CommandSender.list(){
        sendMessage(PlainText(MasterConfig.masters.joinToString("\n")))
    }

    @SubCommand("add","新增","加")
    @Description("新增master")
    suspend fun CommandSender.addTo(user:User ){
        if (isMaster(this.user)) {
            val isSuccess = MasterConfig.masters.add(user.id)
            sendMessage(PlainText("添加${user.id}${if (isSuccess) "成功" else "失败"}"))
        }
    }

    @SubCommand("del","remove","删除","删")
    @Description("删除master")
    suspend fun CommandSender.delTo(user:User ){
        if (isMaster(this.user)) {
            val isSuccess = MasterConfig.masters.remove(user.id)
            sendMessage(PlainText("删除${user.id}${if (isSuccess) "成功" else "失败"}"))
        }
    }

    @SubCommand("聊天命令","cm","chatCommand")
    @Description("设置使用聊天命令true 或 false，开启后无需安装chat-command插件")
    suspend fun CommandSender.useCM(useCM:Boolean){
        if (isMaster(this.user)) {
            MasterConfig.useCM = useCM
            sendMessage(PlainText("设置使用聊天命令${if (MasterConfig.useCM) "开启,可以在聊天中使用命令" else "关闭,再次开启需要在控制台执行"}"))
        }
    }
}