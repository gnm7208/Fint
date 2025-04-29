plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id ("com.google.gms.google-services") version "4.4.2"
    id ("kotlin-kapt")
    id ("com.google.dagger.hilt.android") version "2.56.1"
}

kapt {
    javacOptions {
        option("-Adagger.hilt.android.internal.disableAndroidSuperclassValidation=true")
    }
}


android {
    namespace = "com.example.fint"
    compileSdk = 35

    kapt {
        correctErrorTypes = true
        useBuildCache = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.6.7"
    }

    defaultConfig {
        applicationId = "com.example.fint"
        minSdk = 29
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    buildToolsVersion = "35.0.0"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation (libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation("androidx.navigation:navigation-compose:2.8.9")

    implementation ("androidx.hilt:hilt-navigation-compose:1.2.0")

    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")

    implementation ("com.google.firebase:firebase-auth-ktx:23.2.0")

    implementation ("androidx.navigation:navigation-fragment-ktx:2.8.9")

    implementation ("androidx.navigation:navigation-ui-ktx:2.8.9")

    implementation(libs.coil.compose)

    implementation("androidx.compose.material:material-icons-extended:1.7.8")

    implementation  (platform("com.google.firebase:firebase-bom:33.12.0"))

    implementation("com.google.firebase:firebase-analytics:22.4.0")

    implementation ("com.google.firebase:firebase-firestore-ktx:25.1.3")

    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.10.1")

    implementation ("com.google.firebase:firebase-database-ktx:21.0.0")

    implementation ("com.google.dagger:hilt-android:2.56.1")

    kapt ("com.google.dagger:hilt-android-compiler:2.56.1")

    implementation ("androidx.room:room-runtime:2.7.0")

    implementation ("androidx.room:room-ktx:2.7.0")

    kapt ("androidx.room:room-compiler:2.7.0")

    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")

    implementation ("androidx.compose.runtime:runtime-livedata:1.7.8")

    implementation ("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.7.0")

    implementation ("com.google.maps.android:maps-compose:4.3.2")

    implementation ("com.google.android.gms:play-services-maps:19.2.0")

    implementation ("com.google.firebase:firebase-storage-ktx:20.3.0")



}