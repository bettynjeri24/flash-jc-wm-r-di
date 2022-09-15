package com.ekenya.rnd.baseapp.ui.workmanager

/*
class SyncDataWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {
    private val bookService: BookService
    private val bookDao: BookDao
    fun doWork(): Result {
        val applicationContext: Context = ApplicationProvider.getApplicationContext()
        //simulate slow work
        // WorkerUtils.makeStatusNotification("Fetching Data", applicationContext);
        Log.i(TAG, "Fetching Data from Remote host")
        WorkerUtils.sleep()
        return try {
            //create a call to network
            val call: Call<List<Book>> = bookService.fetchBooks()
            val response: Response<List<Book>> = call.execute()
            if (response.isSuccessful() && response.body() != null && !response.body()
                    .isEmpty() && response.body().size() > 0
            ) {
                val data: String = WorkerUtils.toJson(response.body())
                Log.i(TAG, "Json String from network $data")

                //delete existing book data
                bookDao.deleteBooks()
                bookDao.saveBooks(response.body())
                WorkerUtils.makeStatusNotification(
                    applicationContext.getString(R.string.new_data_available),
                    applicationContext
                )
                Result.success()
            } else {
                Result.retry()
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            // Technically WorkManager will return Result.failure()
            // but it's best to be explicit about it.
            // Thus if there were errors, we're return FAILURE
            Log.e(TAG, "Error fetching data", e)
            Result.failure()
        }
    }

    fun onStopped() {
        super.onStopped()
        Log.i(TAG, "OnStopped called for this worker")
    }

    companion object {
        private val TAG = SyncDataWorker::class.java.simpleName
    }

    init {
        bookService = App.get().getBookService()
        bookDao = App.get().getBookDao()
    }
}*/
