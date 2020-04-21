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
private const val BASE_MOCK = "https://3e6afc28-f802-476e-82c4-7dacef64fea9.mock.pstmn.io/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
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

}

object EvalApi {
    val retrofitService: EvalApiService by lazy {
        retrofit.create(EvalApiService::class.java)
    }
}