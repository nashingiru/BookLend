# 📚 BookLend
— Sistema de Préstamo de Libros

Aplicación Android nativa desarrollada con **Kotlin + MVVM + LiveData + Coroutines**.

## 🚀 Características

| Feature | Implementación |
|---|---|
| Arquitectura | MVVM (ViewModel + LiveData) |
| Asincronía | Kotlin Coroutines (viewModelScope, lifecycleScope) |
| UI | Material Design 3 + RecyclerView + ListAdapter |
| Navegación | Intents explícitos + Bundles |
| Ciclo de vida | Callbacks documentados en todas las Activities |
| Íconos | Vector Drawables SVG (sin imágenes rasterizadas) |
| Validación | Formulario con validaciones del lado del cliente |

## 📱 Pantallas

1. **SplashActivity** — Bienvenida con animaciones y coroutine delay
2. **HomeActivity** — Lista de libros con búsqueda y filtros de género
3. **DetailActivity** — Detalle completo de libro con estado de disponibilidad
4. **FormActivity** — Formulario de solicitud de préstamo con DatePicker

## 🏗️ Estructura del Proyecto

```
app/src/main/java/com/armijo/bookLend/
├── model/
│   ├── Book.kt
│   └── LoanRequest.kt
├── repository/
│   └── BookRepository.kt
├── viewmodel/
│   ├── HomeViewModel.kt
│   ├── DetailViewModel.kt
│   └── FormViewModel.kt
├── ui/
│   ├── splash/SplashActivity.kt
│   ├── home/HomeActivity.kt
│   ├── detail/DetailActivity.kt
│   └── form/FormActivity.kt
└── adapter/
    └── BookAdapter.kt
```

## 🔧 Configuración

- **minSdk**: 26 
- **targetSdk**: 36

## 📦 Generar APK

```bash
./gradlew assembleDebug
# Output: app/build/outputs/apk/debug/app-debug.apk
```

## 🌿 Flujo Git

```bash
git init
git add .
git commit -m "Primera version funcionando"
git branch -M main
git remote add origin https://github.com/nashingiru/BookLend.git
git push -u origin main
```

## 📋 Rúbrica Cubierta

- ✅ Kotlin sintaxis: data class, sealed class, lambdas, scope functions, coroutines
- ✅ Ciclo de vida: onCreate/onStart/onResume/onPause/onStop/onDestroy en todas las Activities
- ✅ MVVM: ViewModel + LiveData + Repository correctamente desacoplado
- ✅ UI 4 pantallas: Splash, Home, Detail, Form con Material Design
- ✅ RecyclerView con ListAdapter y DiffUtil
- ✅ Programación asíncrona: viewModelScope.launch + suspend fun + delay()
- ✅ APK debug generado con ./gradlew assembleDebug
- ✅ Git con .gitignore