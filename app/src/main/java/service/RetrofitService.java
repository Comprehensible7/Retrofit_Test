package service;


import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import vo.VisitListModel;
import vo.VisitVo;


public interface RetrofitService {

    /*
       Call<반환타입> method명(인자);
       인자작성시
       @Path(패스명) 변수명   <= URI상 패스  ex)   /visit/{idx}
       @Body(객체)   객체명   <= POST or PUT요청시 Body를 통해서 파라메터 전달시
       @Query(query명) 변수명 <=  v1/book.json?query=java
           ex) @GET("/v1/book.json")
               Call<BookListModel> getList(@Query("query") String query);

       @Headers() <= 요청헤더에 넣어서 넘기는 정보
                      X-Naver-Client-Id

    */

    //조회
    @GET("db2/visits")
    Call<VisitListModel> getList();

    //insert
    @POST("db2/visit")
    Call<JsonObject> insert(@Body VisitVo vo);

    //update
    @PUT("db2/visit")
    Call<JsonObject> update(@Body VisitVo vo);

    //delete
    @DELETE("db2/visit/{idx}")
    Call<JsonObject> delete(@Path("idx") int idx);

}
