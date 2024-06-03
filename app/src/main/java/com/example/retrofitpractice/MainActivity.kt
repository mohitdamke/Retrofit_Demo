package com.example.retrofitpractice

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight.Companion.W600
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.retrofitpractice.model.CatFacts
import com.example.retrofitpractice.ui.theme.RetrofitPracticeTheme
import com.example.retrofitpractice.util.RetrofitInstance
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class MainActivity : ComponentActivity() {
    private var fact = mutableStateOf(CatFacts())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RetrofitPracticeTheme {
                Surface(modifier = Modifier
                    .fillMaxSize()
                   , onClick = { onScreenClick() }) {
                    onScreenClick()

                    AppUI(fact = fact)
                }
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun onScreenClick() {
        GlobalScope.launch(Dispatchers.IO) {
            val response = try {
                RetrofitInstance.api.getRandomFact()
            } catch (e: HttpException) {
                Toast.makeText(applicationContext, "HTTP ERROR ${e.message}", Toast.LENGTH_SHORT)
                    .show()
                return@launch
            } catch (e: IOException) {
                Toast.makeText(applicationContext, "IO ERROR ${e.message}", Toast.LENGTH_SHORT)
                    .show()
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                withContext(Dispatchers.Main) {
                    fact.value = response.body()!!
                }
            }
        }
    }
}


@Composable
fun AppUI(fact: MutableState<CatFacts>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(22.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text(text = "Cat Facts : ", fontSize = 30.sp, fontWeight = W600)
        Spacer(modifier = Modifier.padding(top = 10.dp))
        Text(text = fact.value.fact, fontSize = 30.sp, fontWeight = W600, lineHeight = 40.sp)
    }
}
