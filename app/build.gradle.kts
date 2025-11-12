plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.musicPlayer"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.musicPlayer"
        minSdk = 23
        targetSdk = 34
        versionCode = 11
        versionName = "2.0.1"

        buildConfigField("String", "VERSION_NAME", "\"$versionName\"")
        // ensure vector drawables inflate properly on all APIs
        vectorDrawables.useSupportLibrary = true
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

    buildFeatures{
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Pull to Refresh
    implementation(libs.legacy.support)

    // Glide for image loading
    implementation(libs.glide)

    // For storing objects in shared preferences
    implementation(libs.gson)

    // Notification
    implementation(libs.androidx.media)

    // Vertical Seekbar
    implementation(libs.verticalseekbar)

    // Firebase
    implementation(libs.firebase.firestore)
    implementation(libs.material)

    // ActionBarDrawerToggle
    implementation(libs.androidx.appcompat.v161)
    // Auth
    implementation(libs.google.firebase.auth.ktx)
    // Cac phien ban khac cua Firebase
    implementation(platform(libs.firebase.bom))





}