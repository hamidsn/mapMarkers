package tiger.spike.view

import org.junit.Assert
import org.junit.Test
import tiger.spike.maps.BaseFragmentTest

class OptionsBottomSheetFragmentTest :
    BaseFragmentTest<OptionsBottomSheetFragment>(OptionsBottomSheetFragment::class.java) {

    override fun buildFragment(): OptionsBottomSheetFragment =
        OptionsBottomSheetFragment.newInstance()

    @Test
    fun testOnCreate() {
        Assert.assertNotNull(fragment)
    }

}