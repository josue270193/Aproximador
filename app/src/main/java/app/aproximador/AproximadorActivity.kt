package app.aproximador

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.emoji.bundled.BundledEmojiCompatConfig
import androidx.emoji.text.EmojiCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import app.aproximador.databinding.ActivityAproximadorBinding

class AproximadorActivity : AppCompatActivity() {

    private val CALCULO_TAG: String = "CALCULO"
    private val STAR_EMOJI = '\u2B50'

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityAproximadorBinding = DataBindingUtil.setContentView(this, R.layout.activity_aproximador)
        drawerLayout = binding.drawerLayout

        navController = Navigation.findNavController(this, R.id.aproximador_nav_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

        // CONFIG ACTIONBAR
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)

        // CONFIG NAVIGATION
        binding.navigationView.setupWithNavController(navController)

        // CONFIG EMOJI
        val config = BundledEmojiCompatConfig(this)
        EmojiCompat.init(config)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
