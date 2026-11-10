package com.tecsup.books.ui.lista

import com.tecsup.books.domain.model.Libro

data class CategoriaInfo(
    val id: String,
    val etiqueta: String,
    val conteo: Int = 0
)

val CATEGORIAS_DEFINICION = listOf(
    CategoriaInfo("ciencia_ficcion", "Ciencia ficción"),
    CategoriaInfo("fantasia", "Fantasía"),
    CategoriaInfo("misterio", "Misterio"),
    CategoriaInfo("robots", "Robots"),
    CategoriaInfo("terror", "Terror"),
    CategoriaInfo("otros", "Otros")
)

val REGEX_CATEGORIAS = mapOf(
    "ciencia_ficcion" to listOf(
        Regex("""\bscience fiction\b"""),
        Regex("""\bsci\-fi\b"""),
        Regex("""\bdystopian\b"""),
        Regex("""\bspace flight\b"""),
        Regex("""\bspace opera\b"""),
        Regex("""\bspace exploration\b"""),
        Regex("""\bcyberpunk\b"""),
        Regex("""\btime travel\b"""),
        Regex("""\bextraterrestrial\b""")
    ),
    "fantasia" to listOf(
        Regex("""\bfantasy\b"""),
        Regex("""\bmagic\b"""),
        Regex("""\bwizard\b"""),
        Regex("""\bdragons?\b"""),
        Regex("""\bmythology\b"""),
        Regex("""\bfairy\b"""),
        Regex("""\bwitch\b"""),
        Regex("""\bsupernatural\b""")
    ),
    "misterio" to listOf(
        Regex("""\bmystery\b"""),
        Regex("""\bdetective\b"""),
        Regex("""\bcrime fiction\b"""),
        Regex("""\bmurder mystery\b"""),
        Regex("""\bthriller\b"""),
        Regex("""\bsuspense\b"""),
        Regex("""\binvestigation\b""")
    ),
    "robots" to listOf(
        Regex("""^\brobots?\b$"""),
        Regex("""\brobots in fiction\b"""),
        Regex("""\brobot fiction\b""")
    ),
    "terror" to listOf(
        Regex("""\bhorror\b"""),
        Regex("""\bghosts?\b"""),
        Regex("""\bvampires?\b"""),
        Regex("""\bzombies?\b"""),
        Regex("""\bmonsters?\b"""),
        Regex("""\bscary\b"""),
        Regex("""\bhaunted\b""")
    )
)

fun normalizarTema(tema: String): String {
    return tema.trim().trimEnd('.', ',', ';', ':', '!', '?').lowercase()
}

fun clasificarLibro(libro: Libro): Set<String> {
    if (libro.temas.isEmpty()) {
        return setOf("otros")
    }

    val resultado = mutableSetOf<String>()

    for (tema in libro.temas) {
        val claveNormalizada = normalizarTema(tema)
        if (claveNormalizada.isBlank()) continue

        for ((catId, regexList) in REGEX_CATEGORIAS) {
            if (regexList.any { regex -> regex.containsMatchIn(claveNormalizada) }) {
                resultado.add(catId)
            }
        }
    }

    return if (resultado.isEmpty()) setOf("otros") else resultado
}