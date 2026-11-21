import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        outputModuleName.set("composeApp")
        browser {
            commonWebpackConfig {
                outputFileName = "composeApp.js"
            }
        }
        binaries.executable()
    }

    /**
     * The iPhone app: a static framework that `iosApp/` links, as in Touch VN and RestoApp touch. Only
     * the klibs build without Xcode (`./gradlew :composeApp:compileKotlinIosArm64`); linking takes a Mac.
     */
    listOf(iosArm64(), iosSimulatorArm64()).forEach { target ->
        target.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.ui)
            implementation(libs.compose.resources)
            implementation(libs.rikkaui.foundation)
            implementation(libs.rikkaui.components)
        }
    }
}

compose.resources {
    packageOfResClass = "seanata.res"
    publicResClass = false
}
