package namelessnet.app.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import namelessnet.app.R
import namelessnet.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navView: BottomNavigationView = binding.bottomNavigation

        navController = findNavController(R.id.nav_host_fragment_container)
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.HomeFragment, R.id.StatsFragment, R.id.LogsFragment, R.id.MoreFragment)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        initLayout()
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.HomeFragment) {
                binding.Fab.show()
            } else {
                binding.Fab.hide()
            }
        }
    }

    private fun initLayout() {
        binding.Fab.setOnClickListener {

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // show menu only in HomeFragment
        when (navController.currentDestination?.id) {
            R.id.HomeFragment -> {
                menuInflater.inflate(R.menu.menu_main, menu)
                return true
            }
            R.id.StatsFragment -> {
                return false
            }
            R.id.LogsFragment -> {
                return false
            }
            R.id.MoreFragment -> {
                return false
            }
            else -> {
                return false
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            // R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
