package tiger.spike.maps

import org.junit.Assert.assertNotNull
import org.junit.Test
import tiger.spike.model.MarkerResponse

class MarkerDetailActivityTest :
    BaseActivityTest<MarkerDetailActivity>(MarkerDetailActivity::class.java) {

    @Test
    fun onCreate() {
        assertNotNull(activity)
    }

    override fun buildTestActivity(): MarkerDetailActivity = activityWithArguments(
        MarkerResponse(
            "note sample",
            "use",
            "33 building G",
            0.0,
            0.0
        )
    )


}