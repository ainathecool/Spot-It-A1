package com.aleenafatimakhalid.k201688;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class MediaProjectionService extends Service {
    private MediaProjectionManager mediaProjectionManager;
    private MediaProjection mediaProjection;

    private int screenCaptureWidth;
    private int screenCaptureHeight;
    private int screenDensity;

    private static final String CHANNEL_ID = "ForegroundServiceChannel";
    private static final int NOTIFICATION_ID = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);

        Log.d("firebase", "im in media service");
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        screenCaptureWidth = metrics.widthPixels;  // Width of the screen
        screenCaptureHeight = metrics.heightPixels;
        screenDensity = metrics.densityDpi;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("firebase", "im in start command service");

        if (intent == null) {
            Log.e("firebase", "Intent is null");
            // Handle this case appropriately
            return START_NOT_STICKY;
        }

        int resultCode = intent.getIntExtra("resultCode", -1);
        Intent data = intent.getParcelableExtra("data");

        if (mediaProjection == null) {
            // Initialize mediaProjection
            mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data);
            if (mediaProjection == null) {
                Log.e("firebase", "Failed to obtain media projection");
                // Handle this case appropriately
                return START_NOT_STICKY;
            }

            Log.d("firebase", "Media projection obtained successfully");
            captureScreen();
        } else {
            Log.d("firebase", "Media projection already initialized");
            captureScreen();
        }

        return START_NOT_STICKY;
    }

    private void captureScreen() {
        Log.d("firebase", "im in start capture screen");
        if (mediaProjection != null) {
            final ImageReader imageReader = ImageReader.newInstance(
                    screenCaptureWidth, screenCaptureHeight, ImageFormat.RGB_565, 2
            );

            VirtualDisplay virtualDisplay = mediaProjection.createVirtualDisplay(
                    "ScreenCapture", screenCaptureWidth, screenCaptureHeight, screenDensity,
                    DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, imageReader.getSurface(), null, null
            );

            imageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    Image image = null;
                    try {
                        image = reader.acquireLatestImage();
                        if (image != null) {
                            Bitmap screenshot = imageToBitmap(image);
                            saveScreenshotToFirebaseStorage(screenshot);
                            notifyScreenshotTaken();
                        }
                    } finally {
                        if (image != null) {
                            image.close();
                        }
                        if (virtualDisplay != null) {
                            virtualDisplay.release();
                        }
                    }
                }
            }, null);

            // Wait for a moment before stopping the projection and capturing the screen
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mediaProjection.stop();
                    stopForeground(true);
                    stopSelf();
                }
            }, 1000);  // Adjust the delay as needed
        } else {
            // MediaProjection is not initialized
        }
    }

    private Bitmap imageToBitmap(Image image) {
        Log.d("firebase", "im in bitmap");
        if (image == null) return null;

        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;

        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }

    private void saveScreenshotToFirebaseStorage(Bitmap screenshot) {
        Log.d("firebase", "im in firebase");

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference screenshotRef = storageRef.child("screenshots/screenshot.png");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        screenshot.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = screenshotRef.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Handle the successful upload
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle the failure
            }
        });
    }

    private void notifyScreenshotTaken() {
        Log.d("firebase", "im in notify");
        // Notify the sender and recipient that a screenshot was taken
        // You can use Toast or create a notification
        Toast.makeText(this, "Screenshot taken", Toast.LENGTH_SHORT).show();

        // Optionally, you can create a notification to provide a more visible alert
        createNotification();
    }

    private void createNotification() {
        Log.d("firebase", "im in create noti");
        CharSequence channelName = "Screenshot Notifications";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
        channel.setDescription("Notification for Screenshot Taken");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_screenshot_24)
                .setContentTitle("Screenshot Taken")
                .setContentText("A screenshot of this chat has been taken.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent notificationIntent = new Intent(this, chatmessage10.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        Notification notification = builder.build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(NOTIFICATION_ID, notification);
        } else {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.createNotificationChannel(channel);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            notificationManager.notify(NOTIFICATION_ID, notification);
        }
    }

    @Override
    public void onDestroy() {
        if (mediaProjection != null) {
            mediaProjection.stop();
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
