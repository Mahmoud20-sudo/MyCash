plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs")
    id("dagger.hilt.android.plugin")
    id ("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.codeIn.myCash"
    compileSdk = 34
    compileOptions.incremental = false
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.codeIn.myCash"
        minSdk = 24
        //noinspection OldTargetApi
        targetSdk = 34
        versionCode = 7
        versionName = "1.5.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
            isMinifyEnabled = false
            isShrinkResources = false
        }
        getByName("release") {
            //signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.txt")
        }
    }

    flavorDimensions.add("version")
    productFlavors {
        create("staging") {
            dimension = "version"
            versionNameSuffix = ".stage"
            applicationIdSuffix = ".stage"
        }


        create("live") {
            dimension = "version"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}



dependencies {

    implementation(project(path = ":Common"))
    implementation(project(path = ":Common:nearpay"))
    implementation(project(path = ":Common:printer"))
    implementation(project(path = ":Features"))
    implementation(project(path = ":Features:help"))
    implementation(project(path = ":Features:user"))
    implementation(project(path = ":Features:stock"))
    implementation(project(path = ":Features:sales"))
    implementation(project(path = ":Features:reports"))
    implementation(project(path = ":Integration"))

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


    //Hilt
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-compiler:2.48")


    implementation(files("./libs/printersdkv1.jar"))
    implementation(files("./libs/tscsdk.jar"))

}

kapt {
    correctErrorTypes = true
}