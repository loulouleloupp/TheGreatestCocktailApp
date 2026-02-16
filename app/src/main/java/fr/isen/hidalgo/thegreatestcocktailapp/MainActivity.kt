package fr.isen.hidalgo.thegreatestcocktailapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import fr.isen.hidalgo.thegreatestcocktailapp.ui.theme.TheGreatestCocktailAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TheGreatestCocktailAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                    Test()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d("tags", "onPause")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("tags", "onRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("tags", "onDestroy")
    }

    override fun onResume() {
        super.onResume()
        Log.d("tags", "onResume")
    }

    override fun onStop() {
        super.onStop()
        Log.d("tags", "onStop")
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TheGreatestCocktailAppTheme {
        Column(){
            Test()
        }

    }
}

@Composable
fun Test() {
    Column(
        modifier = Modifier
            .background(Color(0xFF6200EE))
    ) {
        Text(
            text = "Bienvenue !",
            color = Color.White,
        )
        Text(
            text = "Test",
            color = Color.White
        )
    }
}