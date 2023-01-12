package com.gurumi.kakaobankreport.data.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

/**
 *
 * Create : Kwon IkYoung
 * Date : 2022/12/14
 */
sealed class APIResult<out T> {
    data class Success<out T>(val data: T): APIResult<T>()
    data class Failure(val code: Int, val message: String, val raw: String) : APIResult<Nothing>()
    data class NetworkError(val error: Throwable): APIResult<Nothing>()
}

fun <T> safeApiFlow(apiBlock: suspend () -> Response<KAKAOBaseResponse<T>>): Flow<APIResult<KAKAOBaseResponse<T>>> = flow {
    try {
        val response = apiBlock.invoke()
        if (response.isSuccessful) {
            response.body()?.let {
                emit(APIResult.Success(it))
            } ?: emit(APIResult.Failure(response.code(),"Body is Empty", response.raw().toString()))
        } else {
            emit(
                APIResult.Failure(
                    response.code(),
                    response.body()?.message ?: "",
                    response.message()
                )
            )
        }
    } catch (e: Exception) {
        emit(APIResult.NetworkError(e))
    }
}
