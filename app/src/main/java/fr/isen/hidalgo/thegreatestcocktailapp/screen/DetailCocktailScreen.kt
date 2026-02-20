package fr.isen.hidalgo.thegreatestcocktailapp.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.hidalgo.thegreatestcocktailapp.R
import fr.isen.hidalgo.thegreatestcocktailapp.dataClasses.Drink
import fr.isen.hidalgo.thegreatestcocktailapp.ui.theme.TheGreatestCocktailAppTheme
import coil.compose.AsyncImage

//enum class CocktailCategory(val label: String, val icon: String, val color: Color) {
//    SOUR("Acide", "🍋", Color(0xFFFFEB3B)),
//    ALCOHOLIC("Avec Alcool", "🥃", Color(0xFFFFCCBC)),
//    NON_ALCOHOLIC("Sans Alcool", "🍵", Color(0xFFC8E6C9)),
//    SWEET("Sucré", "🍯", Color(0xFFF8BBD0))
//}
//
//enum class GlassType(val label: String, val icon: String) {
//    OLD_FASHIONED("Verre Old Fashioned", "🥃"),
//    MARTINI("Verre à Martini", "🍸"),
//    HIGHBALL("Verre Highball", "🍹"),
//    COUPE("Coupe", "🥂")
//}


@Composable
fun DetailCocktailScreen(modifier: Modifier = Modifier, drink: Drink?) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Pour l'image du cocktail
        AsyncImage(
            model = drink?.thumbUrl,
            contentDescription = "Photo du cocktail",
            modifier = Modifier
                .padding(top = 20.dp, bottom = 20.dp)
                .height(300.dp)
                .width(250.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
        )

        Text(
            text = drink?.name ?: "Chargement...",
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = Color.White,
            letterSpacing = 2.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        // les autres infos du cocktail
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            BadgeSimple(label = drink?.category ?: "Cocktail", icon = "🏷️")
            Spacer(Modifier.width(10.dp))
            BadgeSimple(label = drink?.alcoholic ?: "Standard", icon = "🍸")
        }

        Spacer(modifier = Modifier.height(12.dp))

        // pour le verre
        Surface(
            color = Color.White.copy(alpha = 0.3f),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.padding(horizontal = 20.dp).width(250.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "🍹", fontSize = 20.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = drink?.glass ?: "Verre standard",
                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                )
            }
        }

        Spacer(modifier = Modifier.height(15.dp))

        // ingredient dynamique
        CocktailSection(
            title = stringResource(id = R.string.Ingredients),
            content = formatIngredients(drink)
        )

        // recette dynamique
        CocktailSection(
            title = stringResource(id = R.string.Recette),
            content = drink?.instructions ?: "Aucune instruction disponible."
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

//
fun formatIngredients(drink: Drink?): String {
    if (drink == null) return ""
    val list = mutableListOf<String>()

    val ingredients = listOf(
        drink.ingredient1 to drink.measure1,
        drink.ingredient2 to drink.measure2,
        drink.ingredient3 to drink.measure3,
        drink.ingredient4 to drink.measure4,
        drink.ingredient5 to drink.measure5
    )

    ingredients.forEach { (ing, meas) ->
        if (!ing.isNullOrBlank()) {
            val measureStr = if (!meas.isNullOrBlank()) "$meas " else ""
            list.add("• $measureStr$ing")
        }
    }

    return list.joinToString("\n")
}

@Composable
fun BadgeSimple(label: String, icon: String) {
    Surface(
        color = Color(0xFFF5F5F5),
        shape = RoundedCornerShape(50.dp),
        border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = icon, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
            )
        }
    }
}

@Composable
fun CocktailSection(title: String, content: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.background(Color(0xFFF5F5F5))) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFEEEEEE))
                    .padding(16.dp)
            ) {
                Text(
                    text = title,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Serif,
                        color = Color.Black
                    )
                )
            }
            Text(
                text = content,
                modifier = Modifier.padding(16.dp),
                style = TextStyle(fontSize = 15.sp, lineHeight = 22.sp, color = Color.Black)
            )
        }
    }
}