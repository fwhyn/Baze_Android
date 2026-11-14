# AGENTS.md

## Project layout

- `:baze` — Android library of reusable base classes (`com.fwhyn.lib.baze`), published to Maven Central as
  `io.github.fwhyn.lib:baze`.
- `:deandro` — Compose demo app (`com.fwhyn.app.deandro`) that consumes `:baze`; features, DI, and network code live
  here.
- `build-logic/` — included Gradle build providing the `myapp.android.application` / `myapp.android.library` convention
  plugins. `AndroidConfig` reads SDK, version, and app-id values from `gradle/libs.versions.toml`; change them there,
  not in module scripts.
- Kotlin sources live under `src/main/java` (not `src/main/kotlin`). Package prefix is `com.fwhyn`; minSdk 26,
  compile/target 36, Java 17, Gradle 8.13.

## Commands

- On Windows use `.\gradlew.bat`; on POSIX use `./gradlew`.
- Build the demo app: `./gradlew :deandro:assembleDebug`; library: `./gradlew :baze:assembleDebug`.
- Run all library unit tests: `./gradlew :baze:testDebugUnitTest` (`:deandro` has no unit tests).
- Run one test: `./gradlew :baze:testDebugUnitTest --tests "com.fwhyn.lib.baze.codec.helper.CodecTest"`.
- Lint: `./gradlew :baze:lintDebug` or `./gradlew :deandro:lintDebug`.
- No CI workflows, formatter, or static-analysis gates are checked in; verify with Gradle and lint.

## Build variants

- There are no product flavors; debug and release are the only build types.
- `:baze` publishes the release variant only (`singleVariant("release")` and `components.release`).
- `BuildConfig.SERVER_URL` still differs by build type in `deandro`.

## `:deandro` conventions

- Feature code is split into `feature/presentation/<screen>/` (`*Screen`, `*ViewModel`, `*UiState`, `*UiData`,
  per-screen VM interface) and `feature/func/<capability>/{domain,data}` with use case, repository, and a per-feature
  `*Di` Hilt module.
- Use cases extend `BaseRunner<PARAM, RESULT>` from `:baze` and are invoked with `scope`, `onStart`, `onFetchParam`,
  `onOmitResult`, `onFinish` (see `HomeViewModel`), not as plain suspend functions. Failures are reported by throwing
  exceptions.
- Hilt is wired with kapt (`kotlin("kapt")`), not KSP. `MyApp` is `@HiltAndroidApp`; `MainActivity` hosts Compose
  Navigation starting at `LOGIN_ROUTE`.
- `BuildConfig.WEB_CLIENT_ID` is injected from the Gradle property `WEB_CLIENT_ID`. Put it in
  `~/.gradle/gradle.properties` (template: `gradle.properties-global-example`); a missing value still compiles but
  Google Sign-In fails at runtime.

## `:baze` notes

- Publishing uses `publish-package.gradle` to publish the release variant. Publishing tasks need `ossrhUsername`,
  `ossrhPassword`, and `signing.*` in `~/.gradle/gradle.properties` (same template). Release publish:
  `./gradlew :baze:publishReleasePublicationToMavenRepository`.
- Third-party dependencies (Gson, Retrofit, OkHttp, Compose) are `implementation`. If new public API exposes their
  types, consumers must declare them or the dependency must become `api`.
- Library unit tests are JUnit4 + `kotlinx-coroutines-test`; reuse `MainDispatcherRule` for `Dispatchers.Main`. They are
  plain JVM tests (no Robolectric), so avoid unmocked Android framework calls.
- Some legacy tests under `baze/src/test/java/others` have no package declaration; filter them by simple name (e.g.
  `--tests "*FizzBuzzTest"`), not by `others.array.*`.
- `baze/src/androidTest` requires a device or emulator.

## Versions and branches

- Bump `androidVersionCode` and `androidVersionName` in `gradle/libs.versions.toml` together; the file documents the
  code scheme (`AA.BB.CC.DD`). `androidAppId` is also the base for new-module namespaces.
- Git has long-lived `develop`/`main` branches plus `version/x.y.z` branches.
