package com.example.ApiTVNoticias

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import coil.compose.AsyncImage
import com.example.ApiTVNoticias.ui.theme.ApiTV
import com.example.ApiTVNoticias.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ApiTV {
                val navController = rememberNavController()
                val apiService = createNewsApiService()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = RectangleShape
                ) {
                    Scaffold(
                        topBar = {
                            TopNavigationBar(navController)
                        }
                    ) { paddingValues ->
                        AppNavigation(navController = navController, apiService = apiService, modifier = Modifier.padding(paddingValues))
                    }
                }
            }
        }
    }

    private fun createNewsApiService(): NewsApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(NewsApiService::class.java)
    }
}

@Composable
fun TopNavigationBar(navController: NavHostController) {
    var expanded by remember { mutableStateOf(false) }
    @OptIn(ExperimentalMaterial3Api::class)
    TopAppBar(
        title = { Text("Noticias", style = MaterialTheme.typography.titleLarge) },
        actions = {
            IconButton(onClick = { expanded = true }) {
                Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = Color.White)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Color.Blue)
            ) {
                DropdownMenuItem(
                    text = { Text("Lista", color = Color.White) },
                    onClick = {
                        expanded = false
                        navController.navigate("list_screen")
                    }
                )
                DropdownMenuItem(
                    text = { Text("Acerca de...", color = Color.White) },
                    onClick = {
                        expanded = false
                        navController.navigate("about_screen")
                    }
                )
                DropdownMenuItem(
                    text = { Text("Salir", color = Color.White) },
                    onClick = {
                        expanded = false
                        System.exit(0)
                    }
                )
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = Color.Blue,
            titleContentColor = Color.White
        )
    )
}

@Composable
fun AppNavigation(navController: NavHostController, apiService: NewsApiService, modifier: Modifier = Modifier) {
    NavHost(navController, startDestination = "main_menu", modifier = modifier) {
        composable("main_menu") {
            MainMenu(navController)
        }
        composable("list_screen") {
            ListScreen(navController, apiService)
        }
        composable("about_screen") {
            AboutScreen()
        }
        composable("details_screen/{title}") { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: ""
            DetailScreen(title, apiService)
        }
    }
}
@Composable
fun MainMenu(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Lista",
            modifier = Modifier
                .padding(16.dp)
                .clickable { navController.navigate("list_screen") },
            fontSize = 24.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Acerca de...",
            modifier = Modifier
                .padding(16.dp)
                .clickable { navController.navigate("about_screen") },
            fontSize = 24.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Salir",
            modifier = Modifier
                .padding(16.dp)
                .clickable { System.exit(0) },
            fontSize = 24.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ListScreen(navController: NavHostController, apiService: NewsApiService) {
    var articles by remember { mutableStateOf<List<Article>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val apiKey = "ea7a08c15861447298264c001076ffd3"
        val query = "technology"
        try {
            val response = apiService.searchNews(apiKey, query)
            articles = response.articles
        } catch (e: Exception) {
        } finally {
            isLoading = false
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item(span = { GridItemSpan(2) }) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "LISTA DE NOTICIAS",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .align(Alignment.Center)
                    )
                }
            }

            items(articles.size) { index ->
                val article = articles[index]
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate("details_screen/${article.title}") }
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = article.urlToImage,
                        contentDescription = "Imagen de ${article.title}",
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = article.title,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun DetailScreen(title: String, apiService: NewsApiService) {
    var article by remember { mutableStateOf<Article?>(null) }

    LaunchedEffect(Unit) {
        val apiKey = "ea7a08c15861447298264c001076ffd3"
        /*val apiKey = "1c6bf351d63b4ecb914adf6dc60bafee"*/
        val query = title
        try {
            val response = apiService.searchNews(apiKey, query)
            article = response.articles.firstOrNull { it.title == title }
        } catch (e: Exception) {
        }
    }

    article?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = it.urlToImage,
                contentDescription = "Imagen de ${it.title}",
                modifier = Modifier.size(200.dp)
            )
            Text(text = "Autor: ${it.author ?: "Desconocido"}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Título: ${it.title}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Descripción: ${it.description ?: "No disponible"}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Fuente: ${it.source.name}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Publicado en: ${it.publishedAt}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Contenido: ${it.content ?: "No disponible"}", style = MaterialTheme.typography.bodyLarge)
        }
    } ?: run {
        Text("Noticia no encontrada", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun AboutScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Integrantes del equipo", fontSize = 26.sp)

        Image(
            painter = painterResource(id = R.drawable.eduardo),
            contentDescription = "eduardo",
            modifier = Modifier
                .size(128.dp)
                .padding(bottom = 16.dp)
        )

        Text(text = "Eduardo Antonio Domínguez Santiago", fontSize = 20.sp)
        Text(text = "9°A", fontSize = 20.sp)
        Text(text = "Desarrollo para dispositivos inteligentes", fontSize = 20.sp)
        Text(text = "Dr. Armando Méndez Morales", fontSize =20.sp)

        Image(
            painter = painterResource(id = R.drawable.rodiber),
            contentDescription = "rodiber",
            modifier = Modifier
                .size(128.dp)
                .padding(bottom = 16.dp)
        )

        Text(text = "Rodiber Cruz Morales", fontSize = 20.sp)
        Text(text = "9°A", fontSize = 20.sp)
        Text(text = "Desarrollo para dispositivos inteligentes", fontSize = 20.sp)
        Text(text = "Dr. Armando Méndez Morales", fontSize =20.sp)
    }
}

interface NewsApiService {
    @GET("everything")
    suspend fun searchNews(
        @Query("apiKey") apiKey: String,
        @Query("q") query: String,
        @Query("pageSize") pageSize: Int = 8
    ): NewsResponse
}

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)

data class Article(
    val source: Source,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?
)

data class Source(
    val id: String?,
    val name: String
)