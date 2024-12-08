plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
}

android {
    namespace = "com.reza.mbahlaptop"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.reza.mbahlaptop"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "BASE_URL_NEWS", "\"https://newsapi.org/v2/\"")
        buildConfigField("String", "API_KEY", "\"93b21820d88640e3bea33203ff62dd7d\"")
        buildConfigField(
            "String",
            "BASE_URL_MODEL",
            "\"https://laptop-price-predictor-api-642507985346.asia-southeast2.run.app/\""
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    //Local Persistence
    implementation(libs.datastore.preferences)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    //Retrofit and APIs
    implementation(libs.retrofit)
    implementation(libs.retrofit2.converter.gson)
    implementation(libs.glide)
    implementation(libs.logging.interceptor)

    //AndroidX and UIs
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.viewpager2)

    //Lifecycle
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.firebase.firestore)

    //App Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Firebase
    implementation(libs.firebase.auth)
    implementation(libs.play.services.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)

    //Utilities
    implementation(libs.threetenabp)
    implementation(libs.shimmer)
    implementation(libs.androidx.swiperefreshlayout)


    //Buttom Navigation
    implementation("com.github.Kwasow:BottomNavigationCircles-Android:1.2")
}