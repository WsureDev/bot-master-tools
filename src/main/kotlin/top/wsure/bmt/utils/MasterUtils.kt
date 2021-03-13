package top.wsure.bmt.utils

import net.mamoe.mirai.contact.User
import top.wsure.bmt.data.MasterConfig

/**
 * FileName: MasterUtils
 * Author:   wsure
 * Date:     2021/3/13
 * Description: 
 */
class MasterUtils {
    companion object{
        fun isMaster(user:User?):Boolean{
            return !(user!=null && !MasterConfig.masters.contains(user.id))
        }
    }
}