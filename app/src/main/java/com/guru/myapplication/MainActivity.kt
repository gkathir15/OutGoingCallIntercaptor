package com.guru.myapplication

import android.Manifest.permission.PROCESS_OUTGOING_CALLS
import android.content.Intent
import android.content.Intent.ACTION_NEW_OUTGOING_CALL
import android.content.IntentFilter
import android.graphics.drawable.GradientDrawable
import android.icu.lang.UCharacter
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var arrayList: ArrayList<String>
    lateinit var interceptor: Interceptor
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
       // registerReceiver(interceptor, filter)
        startService(Intent(this,CallInterceptorService::class.java))

    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter(ACTION_NEW_OUTGOING_CALL)
        //registerReceiver(interceptor, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
            //unregisterReceiver(interceptor)
        stopService(Intent(this,CallInterceptorService::class.java))
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
