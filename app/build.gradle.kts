plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.example.ApiTVNoticias"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.ApiTVNoticias"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        vectorDrawables {
            useSupportLibrary = true
        }

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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.tv.foundation)
    implementation(libs.androidx.tv.material)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Soporte para Jetpack Compose en actividades
    implementation("androidx.activity:activity-compose:1.7.2")
    // Componentes básicos y Material Design para aplicaciones de TV
    implementation("androidx.tv:tv-foundation:1.0.0-alpha11")
    implementation("androidx.tv:tv-material:1.0.0-rc02")
    // Herramientas para previsualización y depuración en Jetpack Compose
    implementation("androidx.compose.ui:ui-tooling-preview:1.4.0")
    implementation("androidx.compose.ui:ui-tooling:1.4.0")
    // Biblioteca principal de Jetpack Compose
    implementation("androidx.compose.ui:ui:1.4.0")
    // Componentes de Material Design 3 para Compose
    implementation("androidx.compose.material3:material3:1.1.0")
    // Extensiones Kotlin para el ciclo de vida de Android
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    // Biblioteca para navegación entre pantallas en Compose
    implementation("androidx.navigation:navigation-compose:2.5.3")
    // Biblioteca para la carga y visualización de imágenes con Coil
    implementation("io.coil-kt:coil-compose:2.3.0")
    // Biblioteca para Retrofit y conversión de JSON
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // Soporte para corutinas en Android
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
}