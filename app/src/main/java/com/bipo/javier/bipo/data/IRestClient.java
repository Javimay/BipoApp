package com.bipo.javier.bipo.data;

import com.bipo.javier.bipo.account.models.BikesResponse;
import com.bipo.javier.bipo.home.models.GetBikesResponse;
import com.bipo.javier.bipo.home.models.GetReportResponse;
import com.bipo.javier.bipo.report.models.BikeBrandsResponse;
import com.bipo.javier.bipo.report.models.BikeColorsResponse;
import com.bipo.javier.bipo.report.models.ReportResponse;
import com.bipo.javier.bipo.report.models.ReportTypesResponse;
import com.bipo.javier.bipo.report.models.BikeTypesResponse;
import com.bipo.javier.bipo.login.models.EmailResponse;
import com.bipo.javier.bipo.login.models.LoginResponse;
import com.bipo.javier.bipo.login.models.UserResponse;
//import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.RequestBody;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Javier on 08/04/2017.
 */

public interface IRestClient {

    //LOGIN
    @GET("login")
    Call<LoginResponse> login(@Query("email") String email, @Query("password") String password,
                              @Query("loggedWeb") int loggedWeb,
                              @Query("loggedMobile") int loggedMobile);

    //Logout
    @FormUrlEncoded
    @POST("logout")
    Call<BikesResponse> logout(@Field("token")String token, @Field("loggedWeb")boolean loggedWeb,
                               @Field("loggedMobile")boolean loggedMobile);


    //Valida que el correo exista en el servidor
    @GET("user/{email}")
    Call<EmailResponse> emailVerify(@Path("email") String email);

    @FormUrlEncoded
    @POST("recoverPass")
    Call<BikesResponse> recoveryPass(@Field("email") String email);

    @GET("brands")
    Call<BikeBrandsResponse> getBikeBrands();

    @GET("bikeColors")
    Call<BikeColorsResponse> getBikeColors();

    @GET("bikeTypes")
    Call<BikeTypesResponse> getBikeTypes();

    @GET("reportType")
    Call<ReportTypesResponse> getReportTypes();

    @GET("reports/{reportType}")
    Call<GetReportResponse> getReports(@Path("reportType") int reportType,
                                       @Query("fhInicio") String fcInicio,
                                       @Query("fhFin") String fhFin);

    @GET("lastReports")
    Call<GetReportResponse> getLastReports();

    @FormUrlEncoded
    @POST("register")
    Call<UserResponse> registerUser(@Field("name") String name, @Field("lastName") String lastName,
                                    @Field("email") String email, @Field("birthdate") String birthdate,
                                    @Field("cellphone") String cellphone, @Field("document") String document,
                                    @Field("password") String password);

    @GET("bikes/{userName}")
    Call<GetBikesResponse> getAccountBikes(@Path("userName") String token);

    @GET("bike/{userName}/{bikeName}")
    Call<GetBikesResponse> getBikeByName(@Path("userName") String token, @Path("bikeName") String bikeName);

    @FormUrlEncoded
    @POST("bike")
    Call<BikesResponse> registerBike(@Field("bikeName") String bikeName, @Field("idBrand") int idBrand,
                                     @Field("idColor") int idcolor, @Field("idFrame") String idFrame,
                                     @Field("idType") int idType, @Field("bikeFeatures") String bikeFeatures,
                                     @Field("idBikeState") int idBikeState, @Field("token") String token);

    @Multipart
    @POST("bikePhoto")
    Call<BikesResponse> registerBikePhoto(@Part("bikeName") String bikeName,
                                          @Part("file; filename=image.png") RequestBody image,
                                          //@Part("file\"; filename=\"image.png") RequestBody image,
                                          @Part("token") String token);

    @FormUrlEncoded
    @POST("report")
    Call<ReportResponse> registerReport(@Field("token") String token, @Field("reportName") String reportName,
                                        @Field("reportType") int reportType, @Field("coordinates") String coordinates,
                                        @Field("idBike") int idBike, @Field("reportDetails") String reportDetails);

    @FormUrlEncoded
    @POST("updatePassword")
    Call<BikesResponse> changePass(@Field("email") String email, @Field("password") String password,
                                    @Field("newPassword") String newPassword);

    @Multipart
    @POST("reportPhoto")
    Call<BikesResponse> registerReportPhoto(@Part("reportName") String reportName,
                                            @Part("token") String token,
                                          @Part("image\"; fileName=\"view.jpg\"") RequestBody image);

    @FormUrlEncoded
    @POST("deleteBike")
    Call<BikesResponse> deleteBike(@Field("bikeId") int bikeId, @Field("token") String token);


    @FormUrlEncoded
    @POST("defaultBike")
    Call<BikesResponse> defaultBike(@Field("bikeId") int bikeId, @Field("token") String token);

    @FormUrlEncoded
    @POST("updateBike")
    Call<BikesResponse> updateBike(@Field("bikeId") int bikeId, @Field("token") String token,
                                   @Field("idColor") int idColor, @Field("bikeFeatures") String bikeFeatures);

    @FormUrlEncoded
    @POST("setPreferences")
    Call<UserResponse> savePreferences(@Field("token") String token, @Field("emailReceiver") int emailRecv,
                                    @Field("photoPublication") int photoPublic,
                                    @Field("enableReportUbication") int reportUbication,
                                    @Field("enableLocationUbication") int location);
}