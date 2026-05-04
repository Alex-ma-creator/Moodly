# Moodly 🌱
### Diario de bienestar emocional para jóvenes universitarios

**Equipo:** [Nombre del equipo]  
**Brief asignado:** Brief #4 — Moodly  
**Práctica:** Mini Hackathon Semanas 1-8

---

## 📱 Pantallas

| Home | Registro | Historial | Recursos |
|------|----------|-----------|----------|
| Resumen semanal con emojis por día | Selector de ánimo + etiquetas + nota | Lista de entradas previas | Links y frases de motivación |

*(Ver carpeta `/screenshots/` para capturas)*

---

## 🎬 Video de navegación

[👉 Ver video de demostración](./screenshots/demo.mp4)

---

## ⚙️ Decisiones técnicas

### 1. `enum class MoodLevel` para los 5 niveles de ánimo
Se eligió un `enum class` con propiedades (`emoji`, `label`, `score`) en lugar de un simple `Int` o `String` porque:
- **Type-safety**: imposible asignar valores inválidos en tiempo de compilación.
- **Self-documenting**: `MoodLevel.GREAT` es más legible que `5` o `"great"`.
- **ROOM serialización**: se guarda como `String` (nombre del enum) vía `@TypeConverter`, haciendo la BD inspeccionable con DB Browser.
- **UI iteration**: `MoodLevel.entries` permite renderizar todos los botones de selección con un `forEach`, sin arrays manuales.

**Respuesta a pregunta de sustento:** *"¿Cómo representaste los 5 niveles? ¿Por qué ese tipo de dato?"* → `enum class` porque combina type-safety, legibilidad y serialización limpia. Se almacena como `String` en SQLite via TypeConverter.

---

### 2. MVVM con `StateFlow` en lugar de `LiveData`
- **StateFlow** es 100% Kotlin/Coroutines, sin dependencia de `lifecycle-livedata`.
- `stateIn(WhileSubscribed(5000))`: cancela la colección cuando no hay observadores, pero mantiene el estado 5 segundos para sobrevivir rotaciones de pantalla sin re-consultar la BD.
- El ViewModel nunca conoce la UI; la UI solo observa estados inmutables (`StateFlow` expuesto como `val`, no `MutableStateFlow`).

---

### 3. Repository como Single Source of Truth
El `MoodViewModel` nunca accede al `MoodDao` directamente. El `MoodRepository` es el único punto de acceso a datos, lo que:
- Facilita testeo: se puede inyectar un `FakeRepository` en unit tests.
- Permite cambiar la fuente de datos (ej: agregar cache en memoria) sin modificar el ViewModel.

---

### 4. TypeConverters para tipos complejos en ROOM
- `LocalDate` → `String` ISO 8601 (`"2025-05-04"`): permite queries `BETWEEN` por fechas sin parseo.
- `Set<MoodTag>` → `String` CSV (`"STRESS,SLEEP"`): almacenado en una sola columna, suficiente para este MVP sin necesidad de tabla de relación.

---

## 🛠️ Tecnologías
- **Kotlin** + **Jetpack Compose**
- **ROOM** (entidad `MoodEntry`, DAO, Database, TypeConverters)
- **MVVM** + **StateFlow**
- **Navigation Compose** con `NavHost` y Bottom Navigation Bar
- **Material Design 3**

---

## 📁 Estructura del proyecto
```
app/src/main/java/com/moodly/app/
├── data/
│   ├── db/         # MoodDatabase, MoodDao, Converters
│   ├── model/      # MoodEntry, MoodLevel, MoodTag
│   └── repository/ # MoodRepository
├── ui/
│   ├── navigation/ # Screen, MoodlyNavGraph
│   ├── screens/    # Home, Register, History, Detail, Resources
│   └── theme/      # MoodlyTheme
├── viewmodel/      # MoodViewModel
└── MainActivity.kt
```
