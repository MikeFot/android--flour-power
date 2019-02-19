package com.michaelfotiadis.flourpower.net.error

import okhttp3.Protocol
import okhttp3.Request
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
class RetrofitExceptionTest {

    @Mock
    private lateinit var responseBody: ResponseBody
    @Mock
    private lateinit var ioException: IOException
    @Mock
    private lateinit var genericException: Throwable

    @Test
    fun httpError() {

        val errorMessage = "Not found"
        val code = 404
        val url = "https://www.thecakeisalie.com"

        val okHttpResponse: okhttp3.Response = okhttp3.Response.Builder()
            .request(Request.Builder().url(url).build())
            .code(code)
            .message(errorMessage)
            .protocol(Protocol.HTTP_2)
            .build()
        val response: Response<Any> = Response.error(responseBody, okHttpResponse)

        val retrofitException = RetrofitException.httpError(url, response, null)

        assertNotNull("Null exception generated", retrofitException)
        assertEquals(code, retrofitException.code)
        assertEquals("$code $errorMessage", retrofitException.message)
        assertEquals(url, retrofitException.url)
        assertEquals(RetrofitException.Kind.HTTP, retrofitException.kind)
    }

    @Test
    fun networkError() {
        val errorMessage = "Server Issue"
        Mockito.`when`(ioException.message).thenReturn(errorMessage)

        val retrofitException = RetrofitException.networkError(ioException)

        assertNotNull(retrofitException)
        assertNull(retrofitException.code)
        assertEquals(errorMessage, retrofitException.message)
        assertNull(retrofitException.url)
        assertNull(retrofitException.retrofit)
        assertEquals(RetrofitException.Kind.NETWORK, retrofitException.kind)
    }

    @Test
    fun unexpectedError() {
        val errorMessage = "Unexpected"
        Mockito.`when`(genericException.message).thenReturn(errorMessage)

        val retrofitException = RetrofitException.unexpectedError(genericException)

        assertNotNull(retrofitException)
        assertNull(retrofitException.code)
        assertEquals(errorMessage, retrofitException.message)
        assertNull(retrofitException.url)
        assertNull(retrofitException.retrofit)
        assertEquals(RetrofitException.Kind.UNEXPECTED, retrofitException.kind)
    }
}