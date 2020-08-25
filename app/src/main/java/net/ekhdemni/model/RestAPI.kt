package net.ekhdemni.model

import net.ekhdemni.model.configs.ConfigsResponse
import net.ekhdemni.model.models.*
import net.ekhdemni.model.models.requests.*
import net.ekhdemni.model.models.responses.*
import net.ekhdemni.model.models.user.User

import retrofit2.Call
import retrofit2.http.*
import tn.core.model.responses.PagingResponse
import tn.core.model.responses.BaseResponse

interface RestAPI {
    @POST("login")
    fun login(@Body body: LoginRequest): Call<BaseResponse<AuthResponse>>
    @POST("register")
    fun register(@Body body: RegisterRequest): Call<BaseResponse<AuthResponse>>
    @POST("recover")
    fun recover(@Query("email") page: String): Call<BaseResponse<Any>>



    @GET("{path}")
    fun <T> customation(@Path("path") path: String, @Query("page") page: Int): Call<BaseResponse<PagingResponse<T>>>
    @GET("{type}/custom/{path}")
    fun <T> customPagination(@Path("type") type: String, @Path("path") path: String, @Query("page") page: Int): Call<BaseResponse<PagingResponse<T>>>
    @GET("ideas/custom/{path}")
    fun customIdeas(@Path("path") path: String, @Query("page") page: Int): Call<BaseResponse<PagingResponse<Idea>>>
    @GET("works/custom/{path}")
    fun customWorks(@Path("path") path: String, @Query("page") page: Int): Call<BaseResponse<PagingResponse<Work>>>

    @Headers("Cacheable: 3601")
    @GET("forums/custom/{path}")
    fun customForums(@Path("path") path: String, @Query("page") page: Int): Call<BaseResponse<PagingResponse<Forum>>>
    @GET("posts/custom/{path}")
    fun customPosts(@Path("path") path: String, @Query("page") page: Int): Call<BaseResponse<PagingResponse<Post>>>
    @GET("jobs/custom/{path}")
    fun customJobs(@Path("path") path: String, @Query("page") page: Int): Call<BaseResponse<PagingResponse<Job>>>
    @GET("users/custom/{path}")
    fun customUsers(@Path("path") path: String, @Query("page") page: Int): Call<BaseResponse<PagingResponse<User>>>

    @POST("reports")
    fun report(@Body report:ReportRequest): Call<BaseResponse<Any>>

    @GET("liker/{id}/{type}")
    fun like(@Path("id") id: Int, @Path("type") type: Int): Call<BaseResponse<LikeResponse>>

    @GET("ideas")
    fun ideas(): Call<BaseResponse<PagingResponse<Idea>>>


    @GET("data")
    fun newData(): Call<BaseResponse<NewDataResponse>>
    @GET("configs")
    fun configs(@Query("last_config_time") time: Int): Call<BaseResponse<ConfigsResponse>>

    @GET("ideas")
    fun getIdeas(@Query("page") page: Int = 1): Call<BaseResponse<PagingResponse<Idea>>>
    @GET("users/{id}/ideas")
    fun getUserIdeas(@Path("id") id: Int, @Query("page") page: Int): Call<BaseResponse<PagingResponse<Idea>>>
    @GET("ideas/{id}")
    fun getIdea(@Path("id") id: Int): Call<BaseResponse<Idea>>

    @GET("users/{id}/experiences")
    fun experiences(@Path("id") id: Int): Call<BaseResponse<List<Experience>>>

    @GET("apps")
    fun apps(): Call<BaseResponse<List<App>>>
    @GET("notifications")
    fun notifications(): Call<BaseResponse<List<Notification>>>


    @GET("requests")
    fun requests(): Call<BaseResponse<List<Relation>>>
    @GET("users/{id}/friends")
    fun relationsFor(@Path("id") id: Int): Call<BaseResponse<List<Relation>>>
    //Ekhdemni.relations+"/"+operation+"/"+model.id
    @GET("relations/{operation}/{id}")
    fun relationsChange(@Path("operation") operation: Int, @Path("id") id: Int): Call<BaseResponse<Relation>>

    @GET("countries/{id}/services")
    fun services(@Path("id") id: Int): Call<BaseResponse<List<Service>>>
    @GET("resources")
    fun resources(): Call<BaseResponse<List<Resource>>>
    @GET("alerts")
    fun alerts(): Call<BaseResponse<List<Alert>>>
    @GET("countries")
    fun countries(): Call<BaseResponse<List<Country>>>

    


    @GET("languages")
    fun languages(): Call<List<Broadcast>>
    @GET("languagesLevels")
    fun languagesLevels(): Call<List<Commun>>
    @GET("degrees")
    fun degrees(): Call<List<Commun>>
    @GET("worktypes")
    fun workTypes(): Call<List<Commun>>


    @GET("broadcasts")
    fun broadcasts(): Call<BaseResponse<List<Broadcast>>>

    //action.enableCache = true;
    @GET("forums_types")
    fun forumsTypes(): Call<BaseResponse<PagingResponse<Forum>>>

    @GET("categories")
    fun categories(): Call<BaseResponse<List<Category>>>


    @Headers("Cacheable: 3601")
    @GET("homevi")
    fun paginateHome(): Call<BaseResponse<HomeResponse>>


    @POST("conversations")
    fun pushMessage(@Body body: MessageSetter): Call<BaseResponse<List<Message>>>
    @GET("conversations/{id}")
    fun getMessages(@Path("id") id: Int, @Query("page") page: Int): Call<BaseResponse<PagingResponse<Message>>>
    @GET("conversations/users/{id}")
    fun getMessagesWithUser(@Path("id") id: Int, @Query("page") page: Int): Call<BaseResponse<PagingResponse<Message>>>

    @GET("conversations")
    fun getConversations(@Query("page") page: Int): Call<BaseResponse<PagingResponse<Conversation>>>

    @GET("forums_types/{id}/forums")
    fun getForums(@Path("id") id: Int, @Query("page") page: Int): Call<BaseResponse<PagingResponse<Forum>>>

    @GET("forums/{id}/posts")
    fun getPosts(@Path("id") id: Int, @Query("page") page: Int=1): Call<BaseResponse<PagingResponse<Post>>>

    @GET("users/{id}/posts")
    fun getUserPosts(@Path("id") id: Int, @Query("page") page: Int=1): Call<BaseResponse<PagingResponse<Post>>>

    @GET("posts/{id}")
    fun getPost(@Path("id") id: Int): Call<BaseResponse<Post>>

    @POST("links/comments")
    fun getLinkComments(@Body comment: CommentGetter, @Query("page") page: Int=1): Call<BaseResponse<PagingResponse<Comment>>>

    @GET("comments/{type}/{id}")
    fun getComments(@Path("type") type: String, @Path("id") id: String, @Query("page") page: Int=1): Call<BaseResponse<PagingResponse<Comment>>>

    @POST("comments")
    fun commentsPush(@Body comment: CommentSetter): Call<BaseResponse<List<Comment>>>

    @GET("categories/{id}/children")
    fun getCategories(@Path("id") id: Int): Call<BaseResponse<List<Category>>>

    @GET("categories/{id}/jobs")
    fun getCategoryJobs(@Path("id") id: Int, @Query("page") page: Int=1): Call<BaseResponse<PagingResponse<Job>>>

    @GET("categories/{id}/works")
    fun getCategoryWorks(@Path("id") id: Int, @Query("page") page: Int=1): Call<BaseResponse<PagingResponse<Work>>>

    @GET("works")
    fun getWorks(@Query("page") page: Int=1): Call<BaseResponse<PagingResponse<Work>>>
    @GET("works/{id}")
    fun getWork(@Path("id") id: Int): Call<BaseResponse<Work>>


    @GET("jobs")
    fun getJobs(@Query("page") page: Int=1): Call<BaseResponse<PagingResponse<Job>>>
    @GET("jobs/{id}")
    fun getJob(@Path("id") id: Int): Call<BaseResponse<Job>>


    @POST("search/advanced")
    fun searchJobs(@Body searcher: Searcher, @Query("page") page: Int=1): Call<BaseResponse<PagingResponse<Job>>>
    @POST("search/users/advanced")
    fun searchUsers(@Body searcher: Searcher, @Query("page") page: Int=1): Call<BaseResponse<PagingResponse<User>>>



    @GET("users")
    fun getUsers(@Query("page") page: Int): Call<BaseResponse<PagingResponse<User>>>
    @GET("users/{id}")
    fun getUser(@Path("id") id: Int): Call<BaseResponse<User>>



    /*
    @HTTP(method = "DELETE", path = "cart", hasBody = true) //@DELETE("cart") Non-body HTTP method cannot contain @Body
    Call<BaseResponse<CartResponse>> cartDeleteItem(@Body ItemKey key);
*/
}
