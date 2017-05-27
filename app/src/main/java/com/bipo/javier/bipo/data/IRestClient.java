package com.bipo.javier.bipo.data;

import com.bipo.javier.bipo.home.models.GetReportResponse;
import com.bipo.javier.bipo.login.models.BikeBrandsResponse;
import com.bipo.javier.bipo.login.models.BikeColorsResponse;
import com.bipo.javier.bipo.login.models.BikeStatesResponse;
import com.bipo.javier.bipo.login.models.BikeTypesResponse;
import com.bipo.javier.bipo.login.models.EmailResponse;
import com.bipo.javier.bipo.login.models.LoginResponse;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Javier on 08/04/2017.
 */

public interface IRestClient {


    //LOGIN
    //@FormUrlEncoded
    @GET("login")
    Call<LoginResponse> login(@Query("email") String email, @Query("password") String password);

    //Valida que el correo exista en el servidor
    @GET("user/{email}")
    Call<EmailResponse> emailVerify(@Path("email") String email);

    @GET("brands")
    Call<BikeBrandsResponse> getBikeBrands();

    @GET("bikeColors")
    Call<BikeColorsResponse> getBikeColors();

    @GET("bikeTypes")
    Call<BikeTypesResponse> getBikeTypes();

    @GET("bikeStates")
    Call<BikeStatesResponse> getBikeStates();

    @GET("reports/{reportType}")
    Call<GetReportResponse> getReports(@Path("reportType") int reportType,
                                       @Query("fhInicio") String fcInicio,
                                       @Query("fhFin") String fhFin);
/*
    @FormUrlEncoded
    @POST("/api/Account/Register")
    Call<RegisterResponse> registerUser(@Field("Email") String email,@Field("Name") String name,
                                        @Field("LastName") String lastName,@Field("Password") String password,
                                        @Field("ConfirmPassword") String confirmPass,@Field("Provider") String provider,
                                        @Field("Identifier") String identifier,
                                        @Field("DocumentTypeId")String docType,@Field("Document")String Document);
    @GET("/api/Profile/GetStatus")
    Call<GetStatusResponse> getStatus();

    @GET("/api/Profile/GetImages")
    Call<GetPhotosResponse> getPhotos();

    @Multipart
    @POST("/api/Profile/PostImage")
    Call<PostPhotoResponse> postPhotos(@Query("photo") String photo, @Part("file\"; filename=\"image.png") RequestBody file);

    @Multipart
    @POST("api/profile/PostDocImage")
    Call<PostPhotoResponse> postDocPhotos(@Query("photo") String photo, @Part("file\"; filename=\"image.png") RequestBody file);

    @GET("api/profile/getDocImages")
    Call<GetPhotosResponse> getDocPhotos();

    @POST("/api/Account/ConfirmEmail")
    Call<DefaultResponse> PostEmailVerification();

    @FormUrlEncoded
    @POST("api/Account/ReceiveCode")
    Call<DefaultResponse> PostCellphoneVerification(@Field("Code")String Code,@Field("Phone")String Phone );

    @FormUrlEncoded
    @POST("api/Account/ConfirmPhone")
    Call<DefaultResponse>PostCellphoneSMS (@Field("Phone")String Phone );

    @FormUrlEncoded
    @POST("api/Account/VerifyDocument")
    Call<DocumentResponse>PostDocumentVerification (@Field("Document")String Document, @Field("ExpDate")String ExpDate,
                                                    @Field("DocType") Integer DocType);

    @GET("api/Template/GetTemplates?published=")
    Call<GetTemplatesResponse> GetTemplates();

    @FormUrlEncoded
    @POST("api/Account/ChangePassword")
    Call<DefaultResponse>ChangePassword (@Field("Email")String Email);

    @GET("api/Account/GetByDocument")
    Call<DocumentResponse>GetByDocument (@Query("Document")String Document, @Query("ExpDate") String ExpDate,
                                         @Query("DocType") Integer DocType);

    @GET("/api/Template/GetFolders")
    Call<FolderResponse>GetFolders(@Query("folderparentID")String folderparentid);

    @GET("/api/Template/GetDocuments")
    Call<FolderResponse>GetFiles(@Query("folderparentID")String folderparentid);

    @Multipart
    @POST("api/profile/PostFingerPhoto")
    Call<PostPhotoResponse> postFingerPhoto(@Query("type") String type, @Part("file\"; filename=\"image.png") RequestBody file);

    @GET("api/profile/GetFingerPhotos")
    Call<GetPhotosResponse> getFingerPhotos();

    @GET("api/Template/GetContracts")
    Call<GetContractsResponse> getContracts();

    @GET("api/Template/GetDocument/")
    Call<FileResponse> getDocument(@Query("idDocument")String id);

    @FormUrlEncoded
    @POST("api/Template/CreateContract")
    Call<GetContractsResponse> createContract(@Field("UserName")String UserName,@Field("TemplateId")String TemplateId);

    @FormUrlEncoded
    @POST("api/Template/SaveContract")
    Call<GetContractsResponse> saveContract(@Field("documentId")String documentId,@Field("TemplateId")String TemplateId,
                                            @Field("UserName")String UserName,@Field("documentName")String DocumentName);
*/


}