package com.gurumi.kakaobankreport.data

import com.gurumi.kakaobankreport.BuildConfig
import com.gurumi.kakaobankreport.data.model.ImageRes
import com.gurumi.kakaobankreport.data.model.KAKAOBaseResponse
import com.gurumi.kakaobankreport.data.model.VideoRes
import com.gurumi.kakaobankreport.utils.DEFAULT_PAGE_SIZE
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 *
 * Create : Kwon IkYoung
 * Date : 2022/12/14
 */
interface KAKAOApis {
    @Headers("Authorization: KakaoAK ${BuildConfig.KAKAO_API_KEY}")
    @GET("/v2/search/image")
    suspend fun searchImages(
        @Query("query") query: String,
        @Query("sort") sort : String,
        @Query("page") page : Int,
        @Query("size") size : Int = DEFAULT_PAGE_SIZE,
    ) : Response<KAKAOBaseResponse<ImageRes>>

    @Headers("Authorization: KakaoAK ${BuildConfig.KAKAO_API_KEY}")
    @GET("/v2/search/vclip")
    suspend fun searchVideos(
        @Query("query") query: String,
        @Query("sort") sort : String,
        @Query("page") page : Int,
        @Query("size") size : Int = DEFAULT_PAGE_SIZE,
    ) : Response<KAKAOBaseResponse<VideoRes>>

}