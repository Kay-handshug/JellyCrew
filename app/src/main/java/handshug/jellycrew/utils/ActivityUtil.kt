package handshug.jellycrew.utils

import android.app.Activity

object ActivityUtil {

    val activityList = ArrayList<Activity>()

    fun addActivity(activity: Activity) {
        activityList.add(activity)
    }

    fun removeActivity(activity: Activity) {
        activityList.remove(activity)
    }

    fun getTopActivity(): Activity {
        return activityList.last()
    }

    fun behindAllRemove() {
        while (activityList.count() > 1) {
            activityList[0].finish()
        }
    }

    fun shutdown() {
        while (activityList.count() > 0) {
            val activity = activityList[0]
            activity.finish()
        }
    }

    fun removeAll() {
        while (activityList.count() > 0) {
            activityList[0].finish()
        }
    }

    fun getList(): ArrayList<Activity> {
        return activityList
    }

    inline fun <reified T : Activity> behindRemoveToType() {
        if (activityList.lastIndex - 1 < 0) {
            return
        }

        while (activityList[activityList.lastIndex - 1] !is T) {
            if (activityList.lastIndex - 1 < 0) {
                return
            }
            activityList[activityList.lastIndex - 1].finish()
        }
    }
}
