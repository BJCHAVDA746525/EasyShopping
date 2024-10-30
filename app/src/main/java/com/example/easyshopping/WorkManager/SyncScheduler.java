package com.example.easyshopping.WorkManager;

import androidx.constraintlayout.widget.ConstraintSet;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import java.util.concurrent.TimeUnit;

public class SyncScheduler {

    public void ScheduleFirebaseSync(){

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        WorkRequest SyncWorkRequest = new PeriodicWorkRequest.Builder(
                FirebaseSyncWorker.class,
                1, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build();




        WorkManager.getInstance().enqueue(SyncWorkRequest);

    }

    public void UserSync(){

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();


        WorkRequest SyncWorkRequest2 = new PeriodicWorkRequest.Builder(
                FireUserSync.class,
                1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .build();


        WorkManager.getInstance().enqueue(SyncWorkRequest2);

    }

}
