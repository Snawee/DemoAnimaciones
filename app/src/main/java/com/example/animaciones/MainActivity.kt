package com.example.animaciones

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxWidth()) {
                    PantallaAnimaciones()
                }
            }
        }
    }
}

@Composable
fun PantallaAnimaciones() {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer24()
        Text("🎬 Galería de Animaciones", fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Text("Toca los elementos para verlos animarse")

        TextoQueSeMueve()
        TextoPulsante()
        BotonQueSeMueve()
        BotonRebote()
        IconoGiratorio()
        AparecerDesaparecer()
        TarjetaExpandible()
        BotonTemblor()
        Spacer24()
    }
}

@Composable
private fun Spacer24() = Box(Modifier.height(24.dp))

@Composable
private fun TarjetaDemo(titulo: String, contenido: @Composable () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(titulo, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
            contenido()
        }
    }
}

// 1. texto que se mueve de lado a lado
@Composable
fun TextoQueSeMueve() {
    val transicion = rememberInfiniteTransition(label = "texto")
    val x by transicion.animateFloat(
        initialValue = -120f,
        targetValue = 120f,
        animationSpec = infiniteRepeatable(tween(2000, easing = LinearEasing), RepeatMode.Reverse),
        label = "x"
    )
    TarjetaDemo("1. Texto que se mueve") {
        Box(Modifier.fillMaxWidth().height(40.dp), contentAlignment = Alignment.Center) {
            Text(
                "¡Hola pijes!",
                fontSize = 22.sp,
                modifier = Modifier.offset { IntOffset(x.roundToInt(), 0) }
            )
        }
    }
}

// 2. texto que pulsa y cambia de color
@Composable
fun TextoPulsante() {
    val transicion = rememberInfiniteTransition(label = "pulso")
    val escala by transicion.animateFloat(
        initialValue = 1f,
        targetValue = 1.4f,
        animationSpec = infiniteRepeatable(tween(800), RepeatMode.Reverse),
        label = "escala"
    )
    val color by transicion.animateColor(
        initialValue = Color(0xFFE91E63),
        targetValue = Color(0xFF3F51B5),
        animationSpec = infiniteRepeatable(tween(1600), RepeatMode.Reverse),
        label = "color"
    )
    TarjetaDemo("2. Texto pulsante con color") {
        Box(Modifier.fillMaxWidth().height(50.dp), contentAlignment = Alignment.Center) {
            Text(
                "Ando pulsante!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = color,
                modifier = Modifier.scale(escala)
            )
        }
    }
}

// 3. boton que se desplaza con efecto simulando un resorte
@Composable
fun BotonQueSeMueve() {
    var alFinal by remember { mutableStateOf(false) }
    val desplazamiento by animateDpAsState(
        targetValue = if (alFinal) 180.dp else 0.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "desplazamiento"
    )
    TarjetaDemo("3. Botón que se mueve") {
        Box(Modifier.fillMaxWidth()) {
            Button(
                onClick = { alFinal = !alFinal },
                modifier = Modifier.offset(x = desplazamiento)
            ) { Text("Click Me :)") }
        }
    }
}

// 4. boton que rebota
@Composable
fun BotonRebote() {
    var presionado by remember { mutableStateOf(false) }
    val escala by animateFloatAsState(
        targetValue = if (presionado) 1.5f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy, stiffness = Spring.StiffnessLow),
        label = "rebote"
    )
    val colorFondo by animateColorAsState(
        targetValue = if (presionado) Color(0xFF4CAF50) else Color(0xFF6200EE),
        label = "colorBoton"
    )
    TarjetaDemo("4. Botón con rebote y cambio de color") {
        Box(Modifier.fillMaxWidth().height(70.dp), contentAlignment = Alignment.Center) {
            Button(
                onClick = { presionado = !presionado },
                modifier = Modifier.scale(escala),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = colorFondo)
            ) { Text("Tocame :(") }
        }
    }
}

// 5. icono que gira wiiiiiiiiiiiiiiiiiii
@Composable
fun IconoGiratorio() {
    var giros by remember { mutableStateOf(0) }
    val angulo by animateFloatAsState(
        targetValue = giros * 360f,
        animationSpec = tween(durationMillis = 700),
        label = "giro"
    )
    TarjetaDemo("5. Icono giratorio") {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            IconButton(onClick = { giros++ }) {
                Icon(Icons.Default.Refresh, contentDescription = "Girar", modifier = Modifier.size(40.dp).rotate(angulo))
            }
            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFC107),
                modifier = Modifier.size(40.dp).rotate(-angulo))
            Text("Toca el icono de la izquierda")
        }
    }
}

// 6. aparecer y desaparecer texto con deslizamiento
@Composable
fun AparecerDesaparecer() {
    var visible by remember { mutableStateOf(true) }
    TarjetaDemo("6. Aparecer y desaparecer") {
        Button(onClick = { visible = !visible }) { Text(if (visible) "Ocultar" else "Mostrar") }
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(500)) + slideInVertically(tween(500)) { -it },
            exit = fadeOut(tween(500)) + slideOutVertically(tween(500)) { -it }
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFFF9800)),
                contentAlignment = Alignment.Center
            ) { Text("HOLAAAAAAA", color = Color.White, fontSize = 20.sp) }
        }
    }
}

// 7. tarjeta expandilbe
@Composable
fun TarjetaExpandible() {
    var expandida by remember { mutableStateOf(false) }
    TarjetaDemo("7. Tarjeta expandible") {
        Column(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFE3F2FD))
                .animateContentSize(spring(stiffness = Spring.StiffnessLow))
                .padding(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Favorite, contentDescription = null, tint = Color.Red)
                Text("  Toca para ver más", modifier = Modifier.weight(1f))
                Button(onClick = { expandida = !expandida }) { Text(if (expandida) "Menos" else "Más") }
            }
            if (expandida) {
                Text(
                    "Chicos estoy comiendo mortadela, Soy un texto que si le picas aparece hola " +
                            "btw esto no usa logica externa se hace automaticamente... 67676767",
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

// 8. Boton que tiembla, marcado como si fuese un error
@Composable
fun BotonTemblor() {
    val desplazamientoX = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    TarjetaDemo("8. Botón que tiembla") {
        Box(Modifier.fillMaxWidth().height(60.dp), contentAlignment = Alignment.Center) {
            Button(
                onClick = {
                    scope.launch {
                        repeat(4) {
                            desplazamientoX.animateTo(20f, tween(50))
                            desplazamientoX.animateTo(-20f, tween(50))
                        }
                        desplazamientoX.animateTo(0f, tween(50))
                    }
                },
                modifier = Modifier.offset { IntOffset(desplazamientoX.value.roundToInt(), 0) },
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
            ) { Text("ERROR!") }
        }
    }
}