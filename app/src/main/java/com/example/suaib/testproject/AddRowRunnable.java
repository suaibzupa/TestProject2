package com.example.suaib.testproject;

/**
 * Created by suaib on 5/22/2018.
 */

class AddRowRunnable implements Runnable {
    //Signal To add Row
    private static final int SIGN_ADD_ROW = 1;

    //ThreadPool singleton Object.
    private static ThreadsManager sPhotoManager;

    //Thread Work Add Row Threads
    @Override
    public void run() {

        sPhotoManager = ThreadsManager.getInstance();

        //send signal to Manager thread did work
        sPhotoManager.handleState(Thread.currentThread(),SIGN_ADD_ROW);

    }

}
