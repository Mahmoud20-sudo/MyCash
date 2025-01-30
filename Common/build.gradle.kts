plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
}

android {
    namespace = "com.codeIn.common"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }


    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
        }
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.txt")
        }
    }

    flavorDimensions.add("version")
    productFlavors {
        create("staging") {
            dimension = "version"
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

    externalNativeBuild {
        cmake {
            path = file("src/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }
    ndkVersion = "26.1.10909125"
}

dependencies {

    api(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.google.dagger:hilt-android:2.48")


    api("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    api("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    api("androidx.fragment:fragment-ktx:1.6.2")


    api(platform("com.google.firebase:firebase-bom:33.0.0"))
    api("com.google.firebase:firebase-analytics")
    api("com.google.firebase:firebase-crashlytics")

    //splashscreen
    api("androidx.core:core-splashscreen:1.0.1")

    //Navigation
    api("androidx.navigation:navigation-fragment-ktx:2.7.6")
    api("androidx.navigation:navigation-ui-ktx:2.7.6")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.48")


    api("androidx.security:security-crypto:1.1.0-alpha06")
    api("com.akexorcist:localization:1.2.10")

    //gson
    api("com.google.code.gson:gson:2.10.1")


    //Retrofit
    api("com.squareup.retrofit2:retrofit:2.9.0")
    api("com.squareup.retrofit2:converter-gson:2.9.0")
    api("com.squareup.retrofit2:converter-moshi:2.9.0")
    api("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.10")


    // zxing
    api("com.journeyapps:zxing-android-embedded:4.3.0")
    api("com.google.zxing:core:3.5.1")

    //glide
    api("com.github.bumptech.glide:glide:4.14.2")

    //picasso
    api("com.squareup.picasso:picasso:2.71828")

    //escpos-coffee
    api("com.github.anastaciocintra:escpos-coffee:2.0.2")

    //Paging
    api("androidx.paging:paging-runtime-ktx:3.2.1")

    //otp view
    api("com.github.aabhasr1:OtpView:v1.1.2-ktx")
    api("com.alimuzaffar.lib:pinentryedittext:2.0.6")

    api("androidx.multidex:multidex:2.0.1")
    api("androidx.work:work-runtime-ktx:2.9.0")

    //Nearpay
    api("io.nearpay:nearpay-sdk:2.1.88")
    api ("com.github.anastaciocintra:escpos-coffee:2.0.2")
    api("com.github.skydoves:balloon:1.2.9")


    // sdp && ssp of font
    val scalableUnit = "1.1.0"
    api("com.intuit.sdp:sdp-android:$scalableUnit")
    api("com.intuit.ssp:ssp-android:$scalableUnit")

    //lottie
    api("com.airbnb.android:lottie:3.4.0")
    

    //pluto
    debugApi("com.plutolib:pluto:2.0.6")
    releaseApi("com.plutolib:pluto-no-op:2.0.6")
    debugApi("com.plutolib.plugins:network:2.0.6")
    releaseApi("com.plutolib.plugins:network-no-op:2.0.6")


    //Android Remote Debugger
    debugApi("com.github.zerobranch.android-remote-debugger:debugger:1.1.2")
    releaseApi("com.github.zerobranch.android-remote-debugger:noop:1.1.0")

    //SwipeRefreshLayout
    api("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    // timber
    api("com.jakewharton.timber:timber:5.0.1")
}

kapt {
    correctErrorTypes = true
}