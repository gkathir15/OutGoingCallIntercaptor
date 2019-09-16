package com.guru.myapplication

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Service
import android.app.role.RoleManager
import android.content.ComponentName
import android.content.Intent
import android.content.Intent.ACTION_NEW_OUTGOING_CALL
import android.content.IntentFilter
import android.content.ServiceConnection
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.telecom.CallRedirectionService
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),ServiceConnection {
    /**
     * Called when a connection to the Service has been lost.  This typically
     * happens when the process hosting the service has crashed or been killed.
     * This does *not* remove the ServiceConnection itself -- this
     * binding to the service will remain active, and you will receive a call
     * to [.onServiceConnected] when the Service is next running.
     *
     * @param name The concrete component name of the service whose
     * connection has been lost.
     */
    override fun onServiceDisconnected(name: ComponentName?) {

    }

    /**
     * Called when a connection to the Service has been established, with
     * the [android.os.IBinder] of the communication channel to the
     * Service.
     *
     *
     * **Note:** If the system has started to bind your
     * client app to a service, it's possible that your app will never receive
     * this callback. Your app won't receive a callback if there's an issue with
     * the service, such as the service crashing while being created.
     *
     * @param name The concrete component name of the service that has
     * been connected.
     *
     * @param service The IBinder of the Service's communication channel,
     * which you can now make calls on.
     */
    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {

    }

    lateinit var arrayList: ArrayList<String>
    lateinit var interceptor: Interceptor
    lateinit var serviceConnection: ServiceConnection
    val ROLE_REQUEST_CODE = 50
    @SuppressLint("InlinedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        arrayList = ArrayList()
        arrayList.add("9500181587")
        arrayList.add("10101010101")
        interceptor = Interceptor()

        list.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        list.adapter = MyAdapter(arrayList)

        val filter = IntentFilter(ACTION_NEW_OUTGOING_CALL)
        serviceConnection = this
       // registerReceiver(interceptor, filter)
        if(Build.VERSION.SDK_INT<=28)
        startService(Intent(this,CallInterceptorService::class.java))
        else {
            startService(Intent(this, OutCallRedirectionService::class.java))
            bindService(
                Intent(this, OutCallRedirectionService::class.java).setIdentifier(
                    CallRedirectionService.SERVICE_INTERFACE
                ), serviceConnection,
                Service.BIND_AUTO_CREATE
            )
        }

    }

    @SuppressLint("NewApi")
    override fun onResume() {
        super.onResume()
        if(Build.VERSION.SDK_INT<=28){
        val filter = IntentFilter(ACTION_NEW_OUTGOING_CALL)
        registerReceiver(interceptor, filter)}
        else{
        val roleManager = getSystemService(RoleManager::class.java)
        val isRoleAvailable = roleManager.isRoleAvailable(RoleManager.ROLE_CALL_REDIRECTION)
        status.text = ("Redirection Availability  Not Available")
        if(isRoleAvailable) {
            val isRoleHeld = roleManager.isRoleHeld(RoleManager.ROLE_CALL_REDIRECTION)
            status.text = ("Redirection Availability  $isRoleHeld")
            if(!isRoleHeld) {
                val roleRequestIntent = roleManager.createRequestRoleIntent(
                    RoleManager.ROLE_CALL_REDIRECTION
                )
                val ROLE_REQUEST_CODE = 50
                startActivityForResult(roleRequestIntent, ROLE_REQUEST_CODE)
            }
        }}
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(ROLE_REQUEST_CODE == requestCode)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                status.text = ("Redirection Availability  " +"true")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(Build.VERSION.SDK_INT<=28)
            unregisterReceiver(interceptor)
        else{
        stopService(Intent(this, OutCallRedirectionService::class.java))
        unbindService(serviceConnection)}
    }

    override fun onPause() {
        super.onPause()
       // unregisterReceiver(interceptor)
    }

    fun dialPhoneNumber(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    inner class MyAdapter(private val myDataset: ArrayList<String>) :
        RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder.
        // Each data item is just a string in this case that is shown in a TextView.
        inner class MyViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)


        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(parent: ViewGroup,
                                        viewType: Int): MyAdapter.MyViewHolder {
            // create a new view
            val textView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item, parent, false) as TextView
            // set the view's size, margins, paddings and layout parameters


            return MyViewHolder(textView)
        }

        // Replace the contents of a view (invoked by the layout manager)
        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.textView.text = myDataset[position].toString()
            holder.textView.setOnClickListener(View.OnClickListener {

                dialPhoneNumber(myDataset[position].toString())

            })
        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = myDataset.size
    }
}
