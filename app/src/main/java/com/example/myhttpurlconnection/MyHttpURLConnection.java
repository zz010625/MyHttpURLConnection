package com.example.myhttpurlconnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class MyHttpURLConnection {
    private String mUrl;
    private int mConnectTimeout;
    private int mReadTimeout;
    private String mRequestMethod;//默认为GET
    private String mResult;//请求返回的结果
    private String mPostData;
    private MyHttpURLConnection.CallBack mCallBack;

    public MyHttpURLConnection(Builder builder) {
        this.mUrl = builder.mUrl;
        this.mConnectTimeout = builder.connectTimeout;
        this.mReadTimeout = builder.readTimeout;
        this.mRequestMethod = builder.requestMethod;
        this.mPostData=builder.postData;
    }

    /**
     * 通过HttpURLConnection实现GET请求
     */
    private void GETNetRequest() {
        //使用线程池中的CachedThreadPool
        ThreadPoolManager.INSTANCE.setMode(ThreadPoolManager.ThreadPoolMode.CACHEDTHREADPOOL).setRunnable(new Runnable() {
            @Override
            public void run() {
                URL url;
                try {
                    url = new URL(mUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(mConnectTimeout);
                    connection.setReadTimeout(mReadTimeout);
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = connection.getInputStream();
                        mResult = streamToJSON(inputStream);//调用 streamToJSON 获得请求结果
                        mCallBack.onResponse(mResult);//回调成功接口方法
                    } else {
                        mCallBack.onFailure();//回调失败接口方法
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    /**
     * 通过HttpURLConnection实现POST请求
     *
     */
    private void POSTNetRequest(){
        //使用线程池中的CachedThreadPool
        ThreadPoolManager.INSTANCE.setMode(ThreadPoolManager.ThreadPoolMode.CACHEDTHREADPOOL).setRunnable(new Runnable() {
            @Override
            public void run() {
                URL url;
                try {
                    url = new URL(mUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(mConnectTimeout);
                    connection.setReadTimeout(mReadTimeout);
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setUseCaches(false);
                    connection.connect();
                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(mPostData.getBytes());
                    outputStream.flush();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = connection.getInputStream();
                        mResult = streamToJSON(inputStream);//调用 streamToJSON 获得请求结果
                        mCallBack.onResponse(mResult);//回调成功接口方法
                    } else {
                        mCallBack.onFailure();//回调失败接口方法
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
    public void start() {
        switch (mRequestMethod) {
            case "GET":
                GETNetRequest();
                break;
            case "POST":
                POSTNetRequest();
                break;
        }
    }

    //将stream转为JSON字符串
    private String streamToJSON(InputStream inputStream) {
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                //关闭inputStream
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * Builder类
     */
    public static class Builder {
        private String mUrl;
        private int connectTimeout = 5000;
        private int readTimeout = 5000;
        private String requestMethod = "GET";//默认为GET
        private String postData;
        public Builder url(String mUrl) {
            this.mUrl = mUrl;
            return this;
        }

        public Builder connectTimeout(int connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public Builder readTimeout(int readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }

        public Builder requestMethod(String requestMethod) {
            this.requestMethod = requestMethod;
            return this;
        }

        public Builder setPostData(String postData) {
            this.postData = postData;
            return this;
        }

        public MyHttpURLConnection build() {
            return new MyHttpURLConnection(this);
        }
    }

    /**
     * 请求处理后回调的接口
     * 接口中方法均在子线程运行
     * 若需更新UI 则应在传入的接口方法中将相关操作运行在主线程
     */
    public interface CallBack {
        void onResponse(String result);

        void onFailure();
    }

    public MyHttpURLConnection setCallBack(CallBack mCallBack) {
        this.mCallBack = mCallBack;
        return this;
    }

    public String getUrl() {
        return mUrl;
    }

    public MyHttpURLConnection setUrl(String url) {
        this.mUrl = url;
        return this;
    }

    public int getConnectTimeout() {
        return mConnectTimeout;
    }

    public MyHttpURLConnection setConnectTimeout(int mConnectTimeout) {
        this.mConnectTimeout = mConnectTimeout;
        return this;
    }

    public int getReadTimeout() {
        return mReadTimeout;
    }

    public MyHttpURLConnection setReadTimeout(int mReadTimeout) {
        this.mReadTimeout = mReadTimeout;
        return this;

    }

    public String getRequestMethod() {
        return mRequestMethod;
    }

    public MyHttpURLConnection setRequestMethod(String mRequestMethod) {
        this.mRequestMethod = mRequestMethod;
        return this;
    }

    public String getPostData() {
        return mPostData;
    }

    public MyHttpURLConnection setPostData(String postData) {
        this.mPostData = postData;
        return this;
    }


}
