# AGENTS.md

## Repo layout

- `:baze` — Android library ("Baze") published to Maven. The real product. Depends on: Compose (BOM 2025.02.00),
  Retrofit/OkHttp/Gson, coroutines.
- `:deandro` — demo/training app that consumes `:baze`. Hilt (via `kapt`), Compose, Navigation, Google Sign-In + Drive.
  Uses `project(":baze")`.
- `build-logic/convention` — convention plugins `myapp.android.application` / `myapp.android.library` (aliases
  `libs.plugins.myapp.application` / `.myapp.library`), shared by both modules and the included build.
- Root project name is `BazeApp` (not "Baze_Android" / "Baze").

## Build prerequisites

- Gradle 8.13 wrapper, AGP 8.9.0, Kotlin 2.0.0, **JDK 17**, compileSdk 36, minSdk 26, targetSdk 36. All versions live
  only in `gradle/libs.versions.toml` — change them there, never hardcode in `build.gradle.kts`.
- JDK is enforced via `javaVersion`/`jvmTarget` in `build-logic/convention/AndroidConfig.kt` and `gradle.properties`
  `org.gradle.jvmargs`; build with JDK 17.
- `local.properties` (gitignored) holds `sdk.dir`.

## Commands (flavor gotcha)

Both modules define `real`/`fake` product flavors on `environment` dimension (see convention plugins). **You must prefix
tasks with a flavor** — `realDebug` is the default variant:

- Focused build: `./gradlew :baze:assembleRealDebug`, `:deandro:assembleRealDebug`
- Run unit tests: `./gradlew :baze:testRealDebugUnitTest`
- Single test: `./gradlew :baze:testRealDebugUnitTest --tests "com.fwhyn.lib.baze.common.helper.CodecTest"`
- Plain `assembleDebug`/`testDebugUnitTest` won't exist in Studio-less CLI usage; use flavor-prefixed tasks.

## Matching `:deandro` build config

- `deandro/build.gradle.kts` reads `project.properties["WEB_CLIENT_ID"]` (Google Sign-In) into BuildConfig, and emits
  `SERVER_URL` per build type (`https://dev.atm-sehat.com/` debug / `https://prod.atm-sehat.com/` release). Missing
  `WEB_CLIENT_ID` builds but produces the literal `"null"`. Configure it in `~/.gradle/gradle.properties` (see
  `gradle.properties-global-example`).
- `.gradle/`, `build/`, `release/`, `local.properties`, keystores are gitignored; never commit them.

## Publishing `:baze`

- `baze/build.gradle.kts` does `apply(from = "../publish-package.gradle")` (Groovy DSL): maven-publish + signing.
- Requires `ossrhUsername`, `ossrhPassword`, and signing keys in `~/.gradle/gradle.properties` for Sonatype;
  `./gradlew :baze:publishToMavenLocal` works without them.
- Version is `versionCode = AA.BB.CC.DD` (e.g. `1100000` = 1.10.0) from the version catalog (`androidVersionCode`/
  `androidVersionName`). Bump both together.

## Namespace quirk

Convention plugins auto-derive library namespace as `<androidAppId>.<modulePath>` (i.e. `com.fwhyn.app.deandro.baze`),
but `baze/build.gradle.kts` **overrides** it to `com.fwhyn.lib.baze`. Keep that override — it defines the published
package.

## Conventions / quirks in the code

- Per-feature MVVM layering in `:deandro` (`data/domain/presentation`) with `XxxDi.kt` Hilt modules, `Raw`/`Model`/
  `Param`/`Repo`/`RepositoryImpl(...Fake)` suffixes, and `XxxExt.kt` extension helper files. `:baze` mirrors this (
  helper/extension packages).
- `:baze` contains some intentional idiosyncratic names — `Exzeption.kt`, `Rezult.kt`, `Util.kt` — match them when
  extending; don't "fix" them.
- Some files under `baze/src/test/java/others/**`, `deandro/.../login/Plus.kt`, `more/CoroutineTest.kt` are training
  exercises, not library API.
- Unit tests: JUnit4 + `kotlinx-coroutines-test` + Turbine. Reusable rule `MainDispatcherRule.kt` lives in the **default
  package** (no `package` line) in both modules — copy-paste it when a new test needs Main-dispatcher control.
- `build-logic/convention/build.gradle.kts` enables `validatePlugins` with `failOnWarning = true`; new convention
  plugins must not emit plugin warnings.