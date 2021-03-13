package top.wsure.bmt.commands

import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.RawCommand
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.utils.MiraiLogger
import top.wsure.bmt.PluginMain
import top.wsure.bmt.utils.MasterUtils.Companion.isMaster
import top.wsure.bmt.utils.NotifyUtils.Companion.notifyAllGroup

@ConsoleExperimentalApi
object SendToAllGroup : RawCommand(
    PluginMain,
    "发送全部群",
    "sendToAllGroup"
) {
    private val logger: MiraiLogger = MiraiConsole.createLogger(this.javaClass.canonicalName)

    @ExperimentalCommandDescriptors
    override val prefixOptional = true

    override suspend fun CommandSender.onCommand(args: MessageChain) {
        if (isMaster(this.user)) {
            val msg = args.joinToString(" ") { it.contentToString() }
            logger.info("发送全部群 :${msg}")
            sendMessage("发送全部群 完成，耗时:${notifyAllGroup(this.bot, args)}ms")
            logger.info("发送全部群 :完成")
        }
    }
}