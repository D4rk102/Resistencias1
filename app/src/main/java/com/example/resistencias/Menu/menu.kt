package com.example.resistencias.Menu

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp

val colores = listOf(
    Pair("Gris", Color.Gray),
    Pair("Rojo", Color(0xFFEC0F0F)),
    Pair("Amarillo", Color(0xFFF8D839)),
    Pair("Café", Color(0xFF612C08)),
    Pair("Verde", Color(0xFF6ECC1B)),
    Pair("Blanco", Color.White),
    Pair("Naranja", Color(0xFFE6520D)),
    Pair("Gris", Color.Gray),
    Pair("Negro", Color.Black),
    Pair("Azul", Color(0xFF1D55E7)),
    Pair("Morado", Color(0xFFA258E7)),
)

val tolerancias = listOf(
    Pair("Café", "+/- 1%"),
    Pair("Gris", "+/- 0.05%"),
    Pair("Sin tolerancia", "+/- 20%"),
    Pair("Rojo", "+/- 2%"),
    Pair("Dorado", "+/- 5%"),
    Pair("Azul", "+/- 0.25%"),
    Pair("Plata", "+/- 10%"),
    Pair("Morado", "+/- 0.1%"),
    Pair("Verde", "+/- 0.5%"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorMenu(
    label: String,
    bandaSeleccionada: String?,
    enviarSeleccion: (String) -> Unit,
    opciones: List<Pair<String, Color>>
) {
    var isExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = !isExpanded }
    ) {
        OutlinedTextField(
            value = bandaSeleccionada ?: "$label",
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(isExpanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                Color.White, Color.Black
            ),
            modifier = Modifier
                .menuAnchor()
                .padding(15.dp)
        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            opciones.forEach { (name, color) ->
                DropdownMenuItem(
                    text = {
                        Row(modifier = Modifier.padding(vertical = 20.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .background(color, shape = MaterialTheme.shapes.extraLarge)
                                    .padding(2.dp)
                            )
                            Text("  $name")

                        }
                    },
                    onClick = {
                        enviarSeleccion(name)
                        isExpanded = false
                        Toast.makeText(context, "Eligió $name", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}

@Composable
fun ColorBandMenuExample() {
    var banda1 by remember { mutableStateOf<String?>(null) }
    var banda2 by remember { mutableStateOf<String?>(null) }
    var banda3 by remember { mutableStateOf<String?>(null) }
    var banda4 by remember { mutableStateOf<String?>(null) }

    // Centramos todo el contenido
    Column(
        modifier = Modifier
            .fillMaxSize()  // Llenar toda la pantalla
            .background(Color(0x80F38321))  // Fondo azul
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,  // Centrar verticalmente
        horizontalAlignment = Alignment.CenterHorizontally  // Centrar horizontalmente
    ) {
        Text(
            text = "RESISTENCIA\n",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontFamily = FontFamily.Default // Fuente estándar para evitar errores
            ),
            modifier = Modifier.padding(bottom = 0.dp)
        )

        ColorMenu(
            label = "Banda 1",
            bandaSeleccionada = banda1,
            enviarSeleccion = { banda1 = it },
            opciones = colores
        )
        Spacer(modifier = Modifier.height(8.dp))
        ColorMenu(
            label = "Banda 2",
            bandaSeleccionada = banda2,
            enviarSeleccion = { banda2 = it },
            opciones = colores
        )
        Spacer(modifier = Modifier.height(8.dp))
        ColorMenu(
            label = "Banda 3",
            bandaSeleccionada = banda3,
            enviarSeleccion = { banda3 = it },
            opciones = colores
        )
        Spacer(modifier = Modifier.height(8.dp))
        ColorMenu(
            label = "Tolerancia",
            bandaSeleccionada = banda4,
            enviarSeleccion = { banda4 = it },
            opciones = tolerancias.map { it.first to Color.Gray } // Color gris para la tolerancia
        )

        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = "VALOR DE LA RESISTENCIA: ${calculateResistance(banda1, banda2, banda3, banda4)}",
            style = MaterialTheme.typography.titleLarge.copy(
                fontFamily = FontFamily.Default // Fuente estándar
            )
        )
    }
}

fun calculateResistance(banda1: String?, banda2: String?, banda3: String?, banda4: String?): String {
    val value1 = when (banda1) {
        "Negro" -> 0
        "Café" -> 1
        "Rojo" -> 2
        "Naranja" -> 3
        "Amarillo" -> 4
        "Verde" -> 5
        "Azul" -> 6
        "Morado" -> 7
        "Gris" -> 8
        "Blanco" -> 9
        else -> 0
    }
    val value2 = when (banda2) {
        "Naranja" -> 3
        "Gris" -> 8
        "Blanco" -> 9
        "Negro" -> 0
        "Azul" -> 6
        "Verde" -> 5
        "Amarillo" -> 4
        "Morado" -> 7
        "Café" -> 1
        "Rojo" -> 2
        else -> 0
    }
    val multiplier = when (banda3) {
        "Negro" -> 1
        "Café" -> 10
        "Rojo" -> 100
        "Naranja" -> 1000
        "Amarillo" -> 10000
        "Verde" -> 100000
        "Azul" -> 1000000
        "Morado" -> 10000000
        "Gris" -> 100000000
        "Blanco" -> 1000000000
        else -> 1
    }
    val resistance = (value1 * 10 + value2) * multiplier

    val tolerance = when (banda4) {
        "Sin tolerancia" -> "+/- 20%"
        "Plata" -> "+/- 10%"
        "Dorado" -> "+/- 5%"
        "Marrón" -> "+/- 1%"
        "Rojo" -> "+/- 2%"
        "Verde" -> "+/- 0.5%"
        "Azul" -> "+/- 0.25%"
        "Violeta" -> "+/- 0.1%"
        "Gris" -> "+/- 0.05%"
        else -> "+/- 20%"
    }

    return "$resistance Ohmios $tolerance"
}





