package com.example.suaib.testproject;

/**
 * Created by suaib on 5/22/2018.
 */

class RemoveRowRunnable implements Runnable {

    //Signal To Remove Row
    private static final int SIGN_REMOVE_ROW = 2;

    //ThreadPool singleton Object.
    private static ThreadsManager sPhotoManager;

    //Thread Work Add Row Threads
    @Override
    public void run() {

        sPhotoManager = ThreadsManager.getInstance();

        //send signal to Manager thread did work
        sPhotoManager.handleState(Thread.currentThread(),SIGN_REMOVE_ROW);

    }
}
