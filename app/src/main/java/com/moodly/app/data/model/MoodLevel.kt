package com.moodly.app.data.model

/**
 * Representa los 5 niveles de estado de ánimo.
 *
 * DECISIÓN TÉCNICA: Se usa un enum class en lugar de Int o String porque:
 * 1. Type-safety: imposible asignar un valor inválido (ej: nivel 6 o "malo").
 * 2. Legibilidad: MoodLevel.HAPPY es más claro que el entero 4.
 * 3. ROOM lo almacena como String (nombre del enum) via @TypeConverter,
 *    lo que hace la BD legible si se inspecciona con DB Browser.
 * 4. Facilita iterar todos los niveles en la UI con MoodLevel.entries.
 */
enum class MoodLevel(val emoji: String, val label: String, val score: Int) {
    TERRIBLE("😔", "Muy mal", 1),
    BAD("😟", "Mal", 2),
    OKAY("😐", "Regular", 3),
    GOOD("🙂", "Bien", 4),
    GREAT("😄", "Excelente", 5)
}
