package tiger.spike.util

import android.graphics.Color
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Test

class LocationUtilTest {

    @Test
    fun testColor() {
        val userName = "test user"
        assertEquals("#232323", LocationUtil.getMarkerColor(userName))

    }

    @Test
    fun testRgb() {
        val color = Color.rgb(160, 160, 160)
        assertEquals(color,-6250336)
    }

}