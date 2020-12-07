package tiger.spike.maps

import androidx.fragment.app.Fragment
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [26])
abstract class BaseFragmentTest<T : Fragment>(val fragmentClz: Class<T>) {

    protected var fragment: T = this.buildFragment()

    protected open fun buildFragment(): T {
        return fragmentClz.newInstance()
    }

}