package com.michaelfotiadis.flourpower.ui.main.mapper

import com.michaelfotiadis.flourpower.ui.main.model.UiCakeItem
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import org.junit.Assert.assertEquals
import org.junit.Test

class UiCakeSorterTest {

    @Test
    fun sort() {

        val cakeOne = mock<UiCakeItem> { on { title } doReturn "Bob" }
        val cakeTwo = mock<UiCakeItem> { on { title } doReturn "Eugene" }
        val cakeThree = mock<UiCakeItem> { on { title } doReturn "Gabriel" }
        val cakeFour = mock<UiCakeItem> { on { title } doReturn "Fiona" }
        val cakeFive = mock<UiCakeItem> { on { title } doReturn "Dave" }
        val cakeSix = mock<UiCakeItem> { on { title } doReturn "Alex" }
        val cakeSeven = mock<UiCakeItem> { on { title } doReturn "Cynthia" }

        val mixed = listOf(cakeOne, cakeTwo, cakeThree, cakeFour, cakeFive, cakeSix, cakeSeven)

        val cakeSorter = UiCakeSorter()

        val sorted = cakeSorter.sort(mixed)

        assertEquals(mixed.size, sorted.size)
        assertEquals(cakeSix.title, sorted[0].title)
        assertEquals(cakeOne.title, sorted[1].title)
        assertEquals(cakeSeven.title, sorted[2].title)
        assertEquals(cakeFive.title, sorted[3].title)
        assertEquals(cakeTwo.title, sorted[4].title)
        assertEquals(cakeFour.title, sorted[5].title)
        assertEquals(cakeThree.title, sorted[6].title)
    }
}