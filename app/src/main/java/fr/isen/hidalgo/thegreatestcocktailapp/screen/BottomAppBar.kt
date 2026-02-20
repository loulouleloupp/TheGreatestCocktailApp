package fr.isen.hidalgo.thegreatestcocktailapp.screen

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import fr.isen.hidalgo.thegreatestcocktailapp.R
import fr.isen.hidalgo.thegreatestcocktailapp.TabBarItem

@Composable
fun BottomAppBar(items: List<TabBarItem>, navController: NavController) {
 var selectedTabIndex by rememberSaveable { mutableStateOf(0) }

 NavigationBar(
  containerColor = Color.White.copy(alpha = 0.92f),
  contentColor = colorResource(id = R.color.orange_700)
 ) {
  items.forEachIndexed { index, item ->
   val isSelected = selectedTabIndex == index

   NavigationBarItem(
    selected = isSelected,
    onClick = {
     selectedTabIndex = index
     navController.navigate(item.title)
    },
    icon = {
     TabBarIcon(
      isSelected = isSelected,
      selectedIcon = item.selectedIcon,
      unselectedIcon = item.unselectedIcon,
      title = item.title
     )
    },
    label = {
     Text(
      text = item.title,
      fontSize = 12.sp,
      fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
      color = if (isSelected) colorResource(id = R.color.orange_700) else Color.Gray
     )
    },

    colors = NavigationBarItemDefaults.colors(

     selectedIconColor = colorResource(id = R.color.orange_700),

     indicatorColor = colorResource(id = R.color.orange_500).copy(alpha = 0.2f),

     unselectedIconColor = Color.Gray,
     unselectedTextColor = Color.Gray
    )
   )
  }
 }
}

@Composable
fun TabBarIcon(isSelected: Boolean, selectedIcon: ImageVector, unselectedIcon: ImageVector, title: String) {
 Icon(
  imageVector = if (isSelected) selectedIcon else unselectedIcon,
  contentDescription = title
 )
}