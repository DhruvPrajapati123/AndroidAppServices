package com.einfochips.messageserviceremoteapp

import android.app.*
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.NotificationCompat

/**
 * This is an example of implementing an application service that uses the
 * {@link Messenger} class for communicating with clients.  This allows for
 * remote interaction with a service, without needing to define an AIDL
 * interface.
 *
 * <p>Notice the use of the {@link NotificationManager} when interesting things
 * happen in the service.  This is generally how background services should
 * interact with the user, rather than doing something more disruptive such as
 * calling startActivity().
 */
class MessengerService: Service() {

    /**
     * For showing and hiding our notification.
     */
    private lateinit var notificationManager: NotificationManager

    /**
     * Keeps track of all current registered clients.
     */
    private val mClients = ArrayList<Messenger>()

    /**
     * Holds last value set by a client.
     */
    private var mValue = 0

    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    private val messenger = Messenger(IncomingHandler())


    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel("service", "notification", NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)
        // Display a notification about us starting.
        showNotification()
    }

    private fun showNotification() {
        // The PendingIntent to launch our activity if the user selects this notification
        val contentIntent = PendingIntent.getActivity(
            this, 0,
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS), 0
        )
        val notification: Notification = NotificationCompat.Builder(this, "service")
            .setSmallIcon(android.R.drawable.sym_def_app_icon) // the status icon
            .setTicker(getText(R.string.remote_service_started)) // the status text
            .setWhen(System.currentTimeMillis()) // the time stamp
            .setContentTitle(getText(R.string.local_service_label)) // the label of the entry
            .setContentText("Remote service started") // the contents of the entry
            .setContentIntent(contentIntent) // The intent to send when the entry is clicked
            .build()

        // Send the notification.
        // We use a string id because it is a unique number.  We use it later to cancel.
        notificationManager.notify(R.string.remote_service_started, notification)
    }

    override fun onDestroy() {
        // Cancel the persistent notification.
        notificationManager.cancelAll()
        // Tell the user we stopped.
        Toast.makeText(this, R.string.remote_service_stopped, Toast.LENGTH_SHORT).show();
        super.onDestroy()
    }

    /**
     * When binding to the service, we return an interface to our messenger
     * for sending messages to the service.
     */
    override fun onBind(intent: Intent?): IBinder? = messenger.binder

    companion object {

        /**
         * Command to the service to register a client, receiving callbacks
         * from the service.  The Message's replyTo field must be a Messenger of
         * the client where callbacks should be sent.
         */
        const val MSG_REGISTER_CLIENT = 1

        /**
         * Command to the service to unregister a client, ot stop receiving callbacks
         * from the service.  The Message's replyTo field must be a Messenger of
         * the client as previously given with MSG_REGISTER_CLIENT.
         */
        const val MSG_UNREGISTER_CLIENT = 2

        /**
         * Command to service to set a new value.  This can be sent to the
         * service to supply a new value, and will be sent by the service to
         * any registered clients with the new value.
         */
        const val MSG_SET_VALUE = 3
    }

    /**
     * Handler of incoming messages from clients.
     */
    inner class IncomingHandler: Handler() {
        override fun handleMessage(msg: Message) {
            when(msg.what) {
                MSG_REGISTER_CLIENT -> mClients.add(msg.replyTo)
                MSG_UNREGISTER_CLIENT -> mClients.remove(msg.replyTo)
                MSG_SET_VALUE -> {
                    mValue = msg.arg1
                    for (i in mClients.size - 1 downTo 0) {
                        mClients[i].send(Message.obtain(null, MSG_SET_VALUE, mValue, 0))
                    }
                }
                else -> super.handleMessage(msg)
            }
        }
    }
}