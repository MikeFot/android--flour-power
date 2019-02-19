package com.michaelfotiadis.flourpower.domain.cake.mapper

import com.michaelfotiadis.flourpower.net.model.CakeItem
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import junit.framework.TestCase.assertEquals
import org.junit.Test

class CakeDuplicateRemoverTest {

    @Test
    fun cleanup() {

        val titleOne = "TitleOne"
        val titleTwo = "TitleTwo"
        val titleThree = "TitleThree"

        val cakeOne = mock<CakeItem> { on { title } doReturn titleOne }
        val cakeTwo = mock<CakeItem> { on { title } doReturn titleTwo }
        val cakeThree = mock<CakeItem> { on { title } doReturn titleOne }
        val cakeFour = mock<CakeItem> { on { title } doReturn titleTwo }
        val cakeFive = mock<CakeItem> { on { title } doReturn titleOne }
        val cakeSix = mock<CakeItem> { on { title } doReturn titleOne }
        val cakeSeven = mock<CakeItem> { on { title } doReturn titleThree }

        val duplicates = listOf(cakeOne, cakeTwo, cakeThree, cakeFour, cakeFive, cakeSix, cakeSeven)

        val cakeDuplicateRemover = CakeDuplicateRemover()

        val uniques = cakeDuplicateRemover.cleanup(duplicates)

        assertEquals(3, uniques.size)
    }
}