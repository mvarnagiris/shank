package life.shank

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import life.shank.ParameterSingletonModule.fiveParam
import life.shank.ParameterSingletonModule.fourParam
import life.shank.ParameterSingletonModule.noParam
import life.shank.ParameterSingletonModule.oneParam
import life.shank.ParameterSingletonModule.threeParam
import life.shank.ParameterSingletonModule.twoParam
import life.shank.SingletonTests.ConcurrentSingleton.nanotime0
import life.shank.SingletonTests.ConcurrentSingleton.nanotime1
import life.shank.SingletonTests.ConcurrentSingleton.nanotime2
import life.shank.SingletonTests.ConcurrentSingleton.nanotime3
import life.shank.SingletonTests.ConcurrentSingleton.nanotime4
import life.shank.SingletonTests.ConcurrentSingleton.nanotime5
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import shouldBeEqualTo
import shouldBeSameReference

private object ParameterSingletonModule : ShankModule {
    val noParam = singleton { -> ParamData() }
    val oneParam = singleton { a: Int -> ParamData(a) }
    val twoParam = singleton { a: Int, b: Int -> ParamData(a, b) }
    val threeParam = singleton { a: Int, b: Int, c: Int -> ParamData(a, b, c) }
    val fourParam = singleton { a: Int, b: Int, c: Int, d: Int -> ParamData(a, b, c, d) }
    val fiveParam = singleton { a: Int, b: Int, c: Int, d: Int, e: Int -> ParamData(a, b, c, d, e) }
}

class SingletonTests {

    @Before
    fun setUp() {
        resetShank()
    }

    @Test
    fun `supports parameters`() {
        oneParam(1) shouldBeEqualTo ParamData(1)
        oneParam(1) shouldBeSameReference oneParam(1)

        twoParam(1, 2) shouldBeEqualTo ParamData(1, 2)
        twoParam(1, 2) shouldBeSameReference twoParam(1, 2)

        threeParam(1, 2, 3) shouldBeEqualTo ParamData(1, 2, 3)
        threeParam(1, 2, 3) shouldBeSameReference threeParam(1, 2, 3)

        fourParam(1, 2, 3, 4) shouldBeEqualTo ParamData(1, 2, 3, 4)
        fourParam(1, 2, 3, 4) shouldBeSameReference fourParam(1, 2, 3, 4)

        fiveParam(1, 2, 3, 4, 5) shouldBeEqualTo ParamData(1, 2, 3, 4, 5)
        fiveParam(1, 2, 3, 4, 5) shouldBeSameReference fiveParam(1, 2, 3, 4, 5)
    }

    @Test
    fun `provides different values per params`() {
        oneParam(1) shouldBeSameReference oneParam(1)
        oneParam(2).a shouldBeEqualTo 2

        twoParam(1, 2) shouldBeEqualTo ParamData(1, 2)
        twoParam(2, 3) shouldBeEqualTo ParamData(2, 3)

        threeParam(1, 2, 3) shouldBeEqualTo ParamData(1, 2, 3)
        threeParam(2, 3, 4) shouldBeEqualTo ParamData(2, 3, 4)

        fourParam(1, 2, 3, 4) shouldBeEqualTo ParamData(1, 2, 3, 4)
        fourParam(2, 3, 4, 5) shouldBeEqualTo ParamData(2, 3, 4, 5)

        fiveParam(1, 2, 3, 4, 5) shouldBeEqualTo ParamData(1, 2, 3, 4, 5)
        fiveParam(2, 3, 4, 5, 6) shouldBeEqualTo ParamData(2, 3, 4, 5, 6)
    }


    @Test
    fun `allows override`() {
        noParam() shouldBeEqualTo ParamData()
        noParam.overrideFactory { -> ParamData(2) }
        noParam() shouldBeEqualTo ParamData(2)
        noParam.restore()

        oneParam(1) shouldBeEqualTo ParamData(1)
        oneParam.overrideFactory { a: Int -> ParamData(a * 2) }
        oneParam(1) shouldBeEqualTo ParamData(2)
        oneParam.restore()

        twoParam(1, 2) shouldBeEqualTo ParamData(1, 2)
        twoParam.overrideFactory { a: Int, b: Int -> ParamData(a * 2, b * 2) }
        twoParam(1, 2) shouldBeEqualTo ParamData(2, 4)
        twoParam.restore()

        threeParam(1, 2, 3) shouldBeEqualTo ParamData(1, 2, 3)
        threeParam.overrideFactory { a: Int, b: Int, c: Int -> ParamData(a * 2, b * 2, c * 2) }
        threeParam(1, 2, 3) shouldBeEqualTo ParamData(2, 4, 6)
        threeParam.restore()

        fourParam(1, 2, 3, 4) shouldBeEqualTo ParamData(1, 2, 3, 4)
        fourParam.overrideFactory { a: Int, b: Int, c: Int, d: Int ->
            ParamData(
                a * 2,
                b * 2,
                c * 2,
                d * 2
                     )
        }
        fourParam(1, 2, 3, 4) shouldBeEqualTo ParamData(2, 4, 6, 8)
        fourParam.restore()

        fiveParam(1, 2, 3, 4, 5) shouldBeEqualTo ParamData(1, 2, 3, 4, 5)
        fiveParam.overrideFactory { a: Int, b: Int, c: Int, d: Int, e: Int ->
            ParamData(
                a * 2,
                b * 2,
                c * 2,
                d * 2,
                e * 2
                     )
        }
        fiveParam(1, 2, 3, 4, 5) shouldBeEqualTo ParamData(2, 4, 6, 8, 10)
        fiveParam.restore()
    }


    @Test @Ignore
    fun `supports concurrent requests`() {
        runBlocking(Dispatchers.Default) {
            testConcurrentAccess { nanotime0() }
            testConcurrentAccess { nanotime1(0) }
            testConcurrentAccess { nanotime2(0, 0) }
            testConcurrentAccess { nanotime3(0, 0, 0) }
            testConcurrentAccess { nanotime4(0, 0, 0, 0) }
            testConcurrentAccess { nanotime5(0, 0, 0, 0, 0) }
        }

    }

    private object ConcurrentSingleton : ShankModule {
        val nanotime0 = singleton { -> getTimeSlow() }
        val nanotime1 = singleton { _: Any -> getTimeSlow() }
        val nanotime2 = singleton { _: Any, _: Any -> getTimeSlow() }
        val nanotime3 = singleton { _: Any, _: Any, _: Any -> getTimeSlow() }
        val nanotime4 = singleton { _: Any, _: Any, _: Any, _: Any -> getTimeSlow() }
        val nanotime5 = singleton { _: Any, _: Any, _: Any, _: Any, _: Any -> getTimeSlow() }

        private inline fun getTimeSlow(): Long {
            Thread.sleep(1)
            return System.nanoTime()
        }
    }
}

