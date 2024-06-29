package com.example.vashopify.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.vashopify.auth.AuthApi
import com.example.vashopify.auth.AuthRepository
import com.example.vashopify.auth.AuthRepositoryImpl
import com.example.vashopify.auth.UserApi
import com.example.vashopify.fragments.categories.api.CategoryApi
import com.example.vashopify.fragments.categories.repository.CatRepo
import com.example.vashopify.fragments.categories.repository.ImplCatRepo
import com.example.vashopify.fragments.settings.api.OrderApi
import com.example.vashopify.fragments.shopping.api.AddressApi
import com.example.vashopify.fragments.shopping.api.CartApi
import com.example.vashopify.fragments.shopping.repository.CartRepository
import com.example.vashopify.fragments.shopping.repository.ImplCartRepository
import com.example.vashopify.shop.AddressRepository
import com.example.vashopify.shop.ImplAddressRepository
import com.example.vashopify.shop.ImplProductRepository
import com.example.vashopify.shop.ProductApi
import com.example.vashopify.shop.ProductRepository
import com.example.vashopify.util.AuthInterceptor
import com.example.vashopify.util.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
//        var moshi: Moshi = Moshi.Builder().build()
    @Provides
    @Singleton
    fun provideRetrofitBuilder(): Retrofit.Builder{
        return Retrofit.Builder()
            .baseUrl("http://192.168.1.91:3001")
            .addConverterFactory(MoshiConverterFactory.create())

    }
    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    }
    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor,loggingInterceptor: HttpLoggingInterceptor):OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor).build()
    }




//    @Provides
//    @Singleton
//    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
//        return OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
//    }

//    @Provides
//    @Singleton
//    fun provideUploadApiService(retrofit: Retrofit): UploadApi {
//        return retrofit.create(UploadApi::class.java)
//    }
    @Provides
    @Singleton
    fun provideProductApiService(retrofitBuilder: Retrofit.Builder): ProductApi {
        return retrofitBuilder.build().create(ProductApi::class.java)
    }
    @Provides
    @Singleton
    fun provideCategoryApiService(retrofitBuilder: Retrofit.Builder): CategoryApi {
        return retrofitBuilder.build().create(CategoryApi::class.java)
    }
    @Provides
    @Singleton
    fun provideCartApiService(retrofitBuilder: Retrofit.Builder,okHttpClient: OkHttpClient): CartApi {
        return retrofitBuilder
            .client(okHttpClient)
            .build().create(CartApi::class.java)
    }
    @Provides
    @Singleton
    fun provideAddressApiService(retrofitBuilder: Retrofit.Builder,okHttpClient: OkHttpClient): AddressApi {
        return retrofitBuilder
            .client(okHttpClient)
            .build().create(AddressApi::class.java)
    }
    @Provides
    @Singleton
    fun provideOrderApiService(retrofitBuilder: Retrofit.Builder,okHttpClient: OkHttpClient): OrderApi {
        return retrofitBuilder
            .client(okHttpClient)
            .build().create(OrderApi::class.java)
    }
    @Provides
    @Singleton
    fun provideAuthApiService(retrofitBuilder: Retrofit.Builder): AuthApi {
        return retrofitBuilder.build().create(AuthApi::class.java)
    }
    @Provides
    @Singleton
    fun provideUserApiService(retrofitBuilder: Retrofit.Builder,okHttpClient: OkHttpClient): UserApi {
        return retrofitBuilder
            .client(okHttpClient)
            .build().create(UserApi::class.java)

    }
    @Provides
    @Singleton
    fun provideSharedPref(app: Application): SharedPreferences {
        return app.getSharedPreferences("prefs", MODE_PRIVATE)
    }
    @Singleton
    @Provides
    fun provideTokenManager(prefs: SharedPreferences):TokenManager {
        return TokenManager(prefs)
    }
    @Provides
    @Singleton
    fun provideAuthRepository(api: AuthApi,userApi: UserApi ,tokenManager: TokenManager ): AuthRepository {
        return AuthRepositoryImpl(api,userApi, tokenManager )
    }
    @Provides
    @Singleton
    fun provideProductRepository(api: ProductApi): ProductRepository {
        return ImplProductRepository(api)
    }
    @Provides
    @Singleton
    fun provideCatRepo(api: CategoryApi): CatRepo {
        return ImplCatRepo(api)
    }
    @Provides
    @Singleton
    fun provideCartRepository(api: CartApi, orderApi: OrderApi): CartRepository {
        return ImplCartRepository(api,orderApi)
    }
    @Provides
    @Singleton
    fun provideAddressRepository(api: AddressApi): AddressRepository {
        return ImplAddressRepository(api)
    }


}