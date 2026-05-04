package com.moodly.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Resource(val emoji: String, val title: String, val description: String, val link: String = "")
data class Quote(val text: String, val author: String)

val supportResources = listOf(
    Resource("🧠", "IESM - Instituto Especializado de Salud Mental", "Consultas psicológicas gratuitas para estudiantes universitarios en Lima.", "www.insm.gob.pe"),
    Resource("📞", "Línea 113 - MINSA", "Línea de salud mental disponible 24/7. Llama o escribe al 113.", "www.minsa.gob.pe"),
    Resource("🌐", "Headspace", "App de meditación y mindfulness con ejercicios para el estrés académico.", "www.headspace.com"),
    Resource("💬", "Crisis Text Line", "Escribe tu situación y recibe apoyo inmediato de un especialista.", "www.crisistextline.org"),
    Resource("📚", "UNI - Bienestar Estudiantil", "Servicio de psicología para estudiantes universitarios de forma gratuita.", "bienestar.uni.edu.pe"),
)

val motivationalQuotes = listOf(
    Quote("No tienes que ser perfecto para ser valioso.", "Desconocido"),
    Quote("Cuidar tu salud mental es un acto de valentía, no de debilidad.", ""),
    Quote("Un día a la vez. Eso es todo lo que necesitas.", ""),
    Quote("Pedir ayuda no es rendirse, es seguir adelante de forma inteligente.", ""),
    Quote("Tus emociones son válidas. Todas ellas.", ""),
)

@Composable
fun ResourcesScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Recursos de apoyo", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text("Siempre hay alguien que puede ayudarte 💙", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(supportResources) { res ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row {
                        Text(text = res.emoji, fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = res.title, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = res.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    if (res.link.isNotBlank()) {
                        Text(text = "🔗 ${res.link}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text("Frases de motivación 🌟", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(motivationalQuotes) { quote ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "\"${quote.text}\"", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                    if (quote.author.isNotBlank()) {
                        Text(text = "— ${quote.author}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}
