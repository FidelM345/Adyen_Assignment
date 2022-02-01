package com.example.androidplaylist.utils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.outsideintddexample.acceptancetests.MainCoroutineScopeRule
import org.junit.Rule

//open keyword makes it possible for us to inherit the attributes of the class
open class BaseunitTest {
    //comes from MainCoroutineScopeRule in utils folder class allows testing of coroutines
    @get:Rule
    var coroutineTestRule=MainCoroutineScopeRule()

    //coming from the LiveDataTestExtensions.kt in utils folder. Allows the live data to execute instantly so we can use the values in our test
    @get:Rule
    var instantTaskExecutorRule=InstantTaskExecutorRule()
}