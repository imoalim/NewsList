import org.jetbrains.kotlin.types.expressions.GenericArrayClassLiteralSupport.Enabled.isEnabled


plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.newslist"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.newslist"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    viewBinding {
        isEnabled
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // these 4 are for view models and livedata
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0") // enables LiveData
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0") // enables viewModelScope
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0") // enables ViewModel
    implementation("androidx.activity:activity-ktx:1.8.2") // enables (by) viewModels()

     
    implementation("com.github.bumptech.glide:glide:4.15.1")
    implementation("androidx.preference:preference-ktx:1.2.1")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}