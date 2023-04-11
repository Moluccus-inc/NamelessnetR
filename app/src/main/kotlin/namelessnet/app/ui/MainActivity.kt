package namelessnet.app.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import namelessnet.app.R
import android.net.VpnService
import namelessnet.app.databinding.ActivityMainBinding
import namelessnet.app.services.LeafVpnService

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val vpnPermissionRequestCode = 1

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
            val intent = VpnService.prepare(applicationContext)
            if (intent != null) {
                startActivityForResult(intent, 0)
            } else {
                startVpnService()
            }
        }
    }

    private fun startVpnService() {
        val intent = Intent(this, LeafVpnService::class.java)
        startService(intent)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == vpnPermissionRequestCode) {
            if (resultCode == RESULT_OK) {
                startService(Intent(this, LeafVpnService::class.java))
            } else {
                Toast.makeText(this, "VPN permission denied", Toast.LENGTH_SHORT).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
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
