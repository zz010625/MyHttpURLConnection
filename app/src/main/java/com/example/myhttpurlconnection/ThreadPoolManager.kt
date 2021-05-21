package com.example.myhttpurlconnection

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit


object ThreadPoolManager {
    private var threadPoolMode: ThreadPoolMode? = null
    private var corePoolSize: Int = 1
    private var timeUnit: TimeUnit = TimeUnit.SECONDS
    private var delayTime: Long = 1
    private var taskNumber: Int = 1
    private var runnable: Runnable? = null
    private var fixedThreadPool: ExecutorService? = null
    private var cachedThreadPool: ExecutorService? = null
    private var singleThreadPool: ExecutorService? = null
    private var scheduledThreadPool: ScheduledExecutorService? = null

    enum class ThreadPoolMode {
        FIXEDTHREADPOOL, CACHEDTHREADPOOL, SINGLETHREADPOOL, SCHEDULEDTHREADPOOL
    }

    fun start() {
        when (threadPoolMode) {
            ThreadPoolMode.FIXEDTHREADPOOL -> setFixedThreadPool()
            ThreadPoolMode.CACHEDTHREADPOOL -> setCachedThreadPool()
            ThreadPoolMode.SINGLETHREADPOOL -> setSingleThreadPool()
            ThreadPoolMode.SCHEDULEDTHREADPOOL -> setScheduledThreadPool()
        }
    }

    /**
     * 创建线程池并提交任务
     */
    fun setFixedThreadPool() {
        fixedThreadPool = Executors.newFixedThreadPool(corePoolSize)
        for (i in 0 until taskNumber) {
            fixedThreadPool?.execute(runnable)
        }
    }

    fun setCachedThreadPool() {
        cachedThreadPool = Executors.newCachedThreadPool()
        for (i in 0 until taskNumber) {
            cachedThreadPool?.execute(runnable)
        }
    }

    fun setSingleThreadPool() {
        singleThreadPool = Executors.newSingleThreadExecutor()
        for (i in 0 until taskNumber) {
            singleThreadPool?.execute(runnable)
        }
    }

    fun setScheduledThreadPool() {
        scheduledThreadPool = Executors.newScheduledThreadPool(corePoolSize)
        for (i in 0 until taskNumber) {
            scheduledThreadPool?.schedule(runnable, delayTime, timeUnit)
        }
    }

    /**
     * 设置线程池相关属性
     */
    fun setMode(threadPoolMode: ThreadPoolMode): ThreadPoolManager {
        this.threadPoolMode = threadPoolMode
        return this
    }

    fun setCorePoolSize(corePoolSize: Int): ThreadPoolManager {
        this.corePoolSize = corePoolSize
        return this
    }

    fun setTaskNumber(taskNumber: Int): ThreadPoolManager {
        this.taskNumber = taskNumber
        return this
    }

    fun setTimeUnit(unit: TimeUnit): ThreadPoolManager {
        this.timeUnit = unit
        return this
    }

    fun setDelayTime(delayTime: Long): ThreadPoolManager {
        this.delayTime = delayTime
        return this
    }

    fun setRunnable(runnable: Runnable): ThreadPoolManager {
        this.runnable = runnable
        return this
    }

    /**
     * 关闭线程池
     */
    fun shutdownFixedThreadPool() {
        fixedThreadPool?.shutdown()
    }

    fun shutdownCachedThreadPool() {
        cachedThreadPool?.shutdown()
    }

    fun shutdownSingleThreadPool() {
        singleThreadPool?.shutdown()
    }

    fun shutdownScheduledThreadPool() {
        scheduledThreadPool?.shutdown()
    }
}