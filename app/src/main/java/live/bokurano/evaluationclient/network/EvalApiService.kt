package live.bokurano.evaluationclient.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import live.bokurano.evaluationclient.database.Evaluation
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL = "http://10.0.2.2:8080/"
private const val PROD_URL = "https://ali.bokurano.live:8443/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(PROD_URL)
    .build()

interface EvalApiService {
    @POST("auth")
    fun userLoginAsync(@Body loginUser: LoginUser): Deferred<LoginResponse>

    @GET("api/test")
    fun connectionTestAsync(@Header("Authorization") token: String): Deferred<Map<String, String>>

    @GET("api/student/getUnevaluatedCourses")
    fun getUnevaluatedCoursesAsync(
        @Header("Authorization") token: String,
        @Query("studentId") studentId: String
    ): Deferred<TransferResult<List<Course>>>

    @POST("/api/student/postEvaluations")
    fun postEvaluationAsync(
        @Header("Authorization") token: String,
        @Body evaluations: List<Evaluation>
    ): Deferred<TransferResult<String>>

    @GET("/api/student/getEvaluationHistory")
    fun getHistoryAsync(
        @Header("Authorization") token: String,
        @Query("studentId") studentId: String
    ): Deferred<TransferResult<List<WebEvaluation>>>

    @GET("/api/teacher/getEvaluationOfMyCourse")
    fun getStatAsync(
        @Header("Authorization") token: String,
        @Query("teacherId") teacherId: String
    ): Deferred<TransferResult<List<WebStat>>>
}

object EvalApi {
    val retrofitService: EvalApiService by lazy {
        retrofit.create(EvalApiService::class.java)
    }
}