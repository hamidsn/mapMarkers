package tiger.spike.maps

import android.app.Activity
import android.content.Intent
import androidx.annotation.CallSuper
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config
import java.io.Serializable

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [26])
abstract class BaseActivityTest<T : Activity>(val activityClz: Class<T>) {

    private var activityController: ActivityController<T>? = null
    protected lateinit var activity: T

    @Before
    @CallSuper
    open fun setup() {
        activity = buildTestActivity()
    }

    protected open fun buildTestActivity(): T {
        activityController = Robolectric.buildActivity(activityClz).create().start()
        return activityController!!.get()
    }

    protected open fun activityWithArguments(vararg params: Serializable?): T {
        val intent = Intent()

        for ((i, param) in params.withIndex()) {
            intent.putExtra("", param)
        }
        activity = Robolectric.buildActivity(activityClz, intent).create().start().get()
        return activity
    }
}