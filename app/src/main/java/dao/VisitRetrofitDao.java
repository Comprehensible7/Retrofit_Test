package dao;

import android.os.Handler;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.RetrofitHelper;
import service.RetrofitService;
import vo.VisitListModel;
import vo.VisitVo;

public class VisitRetrofitDao {

    RetrofitService networkService;
    List<VisitVo>  list;

    static  VisitRetrofitDao single=null;
    public static VisitRetrofitDao getInstance(){
       if(single==null){
           single = new VisitRetrofitDao();
       }
       return  single;
    }

    public VisitRetrofitDao() {
        networkService = RetrofitHelper.getRetrofit().create(RetrofitService .class);
    }

    public List<VisitVo> getList() {
        return list;
    }

    //목록조회
    public void selectList(final Handler handler){

        Call<VisitListModel> call = networkService.getList();

        call.enqueue(new Callback<VisitListModel>() {
            @Override
            public void onResponse(Call<VisitListModel> call, Response<VisitListModel> response) {

                if(response.isSuccessful()){
                    VisitListModel model = response.body();
                    list = model.list;
                    handler.sendEmptyMessage(1);
                    /*for(int i=0;i<model.list.size();i++){
                        VisitVo vo = model.list.get(i);
                        Log.d("MY","--" + vo.getName());
                    }*/
                }
            }

            @Override
            public void onFailure(Call<VisitListModel> call, Throwable t) {
                handler.sendEmptyMessage(0);
            }
        });
    }

    //추가하기
    public void insert(VisitVo vo, final Handler handler){

        Call<JsonObject> call =  networkService.insert(vo);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if(response.isSuccessful()){
                    // json = {"result" : 1 }
                    // json = {"result" : 0 }
                    JsonObject json = response.body();
                    int result = json.get("result").getAsInt();

                    handler.sendEmptyMessage(result);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    //삭제하기
    public void delete(int idx, final Handler handler){

        Call<JsonObject> call =  networkService.delete(idx);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if(response.isSuccessful()){
                    // json = {"result" : 1 }
                    // json = {"result" : 0 }
                    JsonObject json = response.body();
                    int result = json.get("result").getAsInt();

                    handler.sendEmptyMessage(result);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }


    //수정하기
    public void update(VisitVo vo, final Handler handler){

        Call<JsonObject> call =  networkService.update(vo);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if(response.isSuccessful()){
                    // json = {"result" : 1 }
                    // json = {"result" : 0 }
                    JsonObject json = response.body();
                    int result = json.get("result").getAsInt();

                    handler.sendEmptyMessage(result);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }


}
