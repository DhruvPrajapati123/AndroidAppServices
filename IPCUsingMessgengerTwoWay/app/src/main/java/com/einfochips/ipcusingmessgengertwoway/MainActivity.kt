package com.einfochips.ipcusingmessgengertwoway

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

/**
 * Example of binding and unbinding to the remote service.
 * This demonstrates the implementation of a service which the client will
 * bind to, interacting with it through an aidl interface.
 *
 * Note that this is implemented as an inner class only keep the sample
 * all together; typically this code would appear in some separate class.
 */
class MainActivity : AppCompatActivity() {

    /**
     * Some text view we are using to show state information.
     */
    private lateinit var mCallbackText: TextView

    /**
     * Flag indicating whether we have called bind on the service.
     */
    private var isBound = false;

    /**
     * Messenger for communicating with service.
     */
    private var mService: Messenger? = null

    /**
     * Handler of incoming messages from service.
     */
    inner class IncomingHandler : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                3 -> mCallbackText.text = "Received from service: " + msg.arg1
                else -> super.handleMessage(msg)
            }
        }
    }

    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    val mMessenger: Messenger = Messenger(IncomingHandler())

    /**
     * Class for interacting with the main interface of the service.
     */
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  We are communicating with our
            // service through an IDL interface, so get a client-side
            // representation of that from the raw service object.
            mService = Messenger(service)
            mCallbackText.text = getString(R.string.attached)

            // We want to monitor the service for as long as we are connected to it.
            var msg = Message.obtain(null, 1)
            msg.replyTo = mMessenger
            mService?.send(msg)

            // Give it some value as an example.
            msg = Message.obtain(null,
                3, Random.nextInt(100), 0);
            mService?.send(msg);

            // As part of the sample, tell the user what happened.
            Toast.makeText(applicationContext, R.string.remote_service_connected, Toast.LENGTH_SHORT).show();
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            mService = null
            mCallbackText.text = getString(R.string.disconnected)

            // As part of the sample, tell the user what happened.
            Toast.makeText(applicationContext, R.string.remote_service_disconnected, Toast.LENGTH_SHORT).show();
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var button = findViewById<Button>(R.id.bindButton)
        button.setOnClickListener(mBindListener)
        button = findViewById<Button>(R.id.unbindButton)
        button.setOnClickListener(mUnbindListener)

        mCallbackText = findViewById(R.id.mCallbackText)
        mCallbackText.text = getString(R.string.not_attached)
    }

    private val mBindListener = View.OnClickListener { doBindService() }

    private fun doBindService() {
        if (!isBound) {
            // Establish a connection with the service.  We use an explicit
            // class name because there is no reason to be able to let other
            // applications replace our component.
            val intent = Intent("MessengerService")
            intent.setPackage("com.einfochips.messageserviceremoteapp")
            bindService(intent, serviceConnection, BIND_AUTO_CREATE)
            isBound = true
            mCallbackText.text = getString(R.string.binding)
        }
    }

    private val mUnbindListener = View.OnClickListener { doUnbindService() }

    private fun doUnbindService() {
        if (isBound) {
            // If we have received the service, and hence registered with
            // it, then now is the time to unregister.
            if (mService != null) {
                try {
                    val msg = Message.obtain(null, 2)
                    msg.replyTo = mMessenger
                    mService?.send(msg)
                } catch (e: RemoteException) {
                    // There is nothing special we need to do if the service
                    // has crashed.
                }
            }

            // Detach our existing connection.
            unbindService(serviceConnection)
            isBound = false;
            mCallbackText.text = getString(R.string.unbinding)
        }
    }
}