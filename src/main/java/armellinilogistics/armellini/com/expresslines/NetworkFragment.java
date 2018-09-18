/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package armellinilogistics.armellini.com.expresslines;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.JsonReader;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Implementation of headless Fragment that runs an AsyncTask to fetch data from the network.
 */
public class NetworkFragment extends Fragment {
    public static final String TAG = "NetworkFragment";
  //  static final String myUrlApi ="secure/restArmellini/service/rest.svc/authenticate/aeldwf/aeldwf";
    private static final String URL_KEY = "";

    private DownloadCallback mCallback;
    private DownloadTask mDownloadTask;
    private String mUrlString;
    private DownloadCallback mCallback2;
    private DownloadTask2 mDownloadTask2;
    private String mUrlString2;
    private DownloadCallback mCallback3;
    private DownloadTask3 mDownloadTask3;
    private String mUrlString3;

    private DownloadCallback mCallback4;
    private DownloadTask4 mDownloadTask4;
    private String mUrlString4;


    public boolean validation;


    public NetworkFragment() throws JSONException {
    }

    /**
     * Static initializer for NetworkFragment that sets the URL of the host it will be downloading
     * from.
     */
    public static NetworkFragment getInstance(FragmentManager fragmentManager, String url) throws JSONException {
        // Recover NetworkFragment in case we are re-creating the Activity due to a config change.
        // This is necessary because NetworkFragment might have a task that began running before
        // the config change and has not finished yet.
        // The NetworkFragment is recoverable via this method because it calls
        // setRetainInstance(true) upon creation.
        NetworkFragment networkFragment = (NetworkFragment) fragmentManager
                .findFragmentByTag(NetworkFragment.TAG);
        if (networkFragment == null) {
            networkFragment = new NetworkFragment();
            Bundle args = new Bundle();

             //Asignando el Url deseado. Con esto se invalida el Url Protocolo. .
            url=Config.myUrlApi;


           // MainActivity.prueba="esta es la url:"+url;

            args.putString(URL_KEY, url);


            networkFragment.setArguments(args);
            fragmentManager.beginTransaction().add(networkFragment, TAG).commit();
        }
        return networkFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retain this Fragment across configuration changes in the host Activity.
        setRetainInstance(true);


        mUrlString = getArguments().getString(URL_KEY);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Host Activity will handle callbacks from task.
        mCallback = (DownloadCallback)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Clear reference to host Activity.
        mCallback = null;
    }

    @Override
    public void onDestroy() {
        // Cancel task when Fragment is destroyed.
        cancelDownload();
        super.onDestroy();
    }

    /**
     * This switch is for the differents  DownloadTask.
     */
    public boolean startDownload(int t) {
        cancelDownload();

        boolean result = true;



        switch (t) {
            case 1:
                //Config.entrada = true;

                mDownloadTask = new DownloadTask();
                mDownloadTask.execute(mUrlString);
                break;
            case 2:
                mDownloadTask2 = new DownloadTask2();
                mDownloadTask2.execute(mUrlString2);
                break;
            case 3:
                mDownloadTask3 = new DownloadTask3();
                mDownloadTask3.execute(mUrlString3);
                break;
            case 4:
                mDownloadTask4 = new DownloadTask4();
                mDownloadTask4.execute(mUrlString4);
                break;
        }

        return result;
    }

    /**
     * Cancel (and interrupt if necessary) any ongoing DownloadTask execution.
     */
    public void cancelDownload() {
        if (mDownloadTask != null) {
            mDownloadTask.cancel(true);
            mDownloadTask = null;
        }
    }

    /**
     * Implementation of AsyncTask that runs a network operation on a background thread.
     */
    class DownloadTask extends AsyncTask<String, Integer, DownloadTask.Result> {
        /**
         * Wrapper class that serves as a union of a result value and an exception. When the
         * download task has completed, either the result value or exception can be a non-null
         * value. This allows you to pass exceptions to the UI thread that were thrown during
         * doInBackground().
         */


        class Result {



            public String mResultValue;
            public Exception mException;
            public Result(String resultValue) {
                mResultValue = resultValue;
            }
            public Result(Exception exception) {
                mException = exception;
            }
        }

        /**
         * Cancel background network operation if we do not have network connectivity.
         */
        @Override
        protected void onPreExecute() {
            if (mCallback != null) {
                NetworkInfo networkInfo = mCallback.getActiveNetworkInfo();
                if (networkInfo == null || !networkInfo.isConnected() ||
                        (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                                && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
                    // If no connectivity, cancel task and update Callback with null data.


                    mCallback.updateFromDownload(null);
                    cancel(true);
                }
            }
        }

        /**
         * Defines work to perform on the background thread.
         */
        @Override
        protected Result doInBackground(String... urls) {
            Result result = null;


          //  Config.prueba = Config.myUrlApi;


            if (!isCancelled() && urls != null && urls.length > 0) {
                String urlString = urls[0];



                try {
                   // URL url = new URL(urlString);
                //   MainActivity.prueba = "doInBackGround:"+MainActivity.myUrlApi;
                    URL url = new URL(Config.myUrlApi);



                    //



                    String resultString = downloadUrl(url);





                    if (resultString != null) {
                        result = new Result(resultString);
                    } else {
                        throw new IOException("No response received.");
                    }
                } catch(Exception e) {

                    Config.prueba="El url es:"+Config.myUrlApi;

                    //Config.entrada= true;

                    result = new Result(e);
                }
            }


            return result;
        }

        /**
         * Send DownloadCallback a progress update.
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (values.length >= 2) {
                mCallback.onProgressUpdate(values[0], values[1]);
            }
        }

        /**
         * Updates the DownloadCallback with the result.
         */
        @Override
        protected void onPostExecute(Result result) {
            if (result != null && mCallback != null) {
                if (result.mException != null) {
                    mCallback.updateFromDownload(result.mException.getMessage());
                } else if (result.mResultValue != null) {
                    mCallback.updateFromDownload(result.mResultValue);
                }
                mCallback.finishDownloading();
            }
        }

        /**
         * Override to add special behavior for cancelled AsyncTask.
         */
        @Override
        protected void onCancelled(Result result) {
        }

        /**
         * Given a URL, sets up a connection and gets the HTTP response body from the server.
         * If the network request is successful, it returns the response body in String form. Otherwise,
         * it will throw an IOException.
         */

        private String downloadUrl(URL url) throws IOException {
            InputStream stream = null;
            HttpsURLConnection connection = null;
            String result = null;

           // Config.prueba = "Esto dentro de downloadUrl:"+url;

          //  MainActivity.prueba = "El Url es"+url;
            try {
                connection = (HttpsURLConnection) url.openConnection();
                // Timeout for reading InputStream arbitrarily set to 3000ms.
                connection.setReadTimeout(3000);
                // Timeout for connection.connect() arbitrarily set to 3000ms.
                connection.setConnectTimeout(3000);
                // For this use case, set HTTP method to GET.
                connection.setRequestMethod("GET");
                // Already true by default but setting just in case; needs to be true since this request
                // is carrying an input (response) body.
                connection.setDoInput(true);
                // Open communications link (network traffic occurs here).

                connection.connect();
               // Config.prueba="Me connecte..";
                publishProgress(DownloadCallback.Progress.CONNECT_SUCCESS);
                int responseCode = connection.getResponseCode();

                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    throw new IOException("HTTP error code: " + responseCode);
                }
                // Retrieve the response body as an InputStream.
                stream = connection.getInputStream();

                publishProgress(DownloadCallback.Progress.GET_INPUT_STREAM_SUCCESS, 0);
                if (stream != null) {

                    // Converts Stream to String with max length of 500.
                  //  result = readStream(stream, 500);


               // Config.prueba = stream.toString();

                    result = readJsonStream(stream,500);
                  //   Config.prueba= result;
                    publishProgress(DownloadCallback.Progress.PROCESS_INPUT_STREAM_SUCCESS, 0);
                }
            } finally {
                // Close Stream and disconnect HTTPS connection.
                if (stream != null) {
                    stream.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return result;
        }

        /**
         * Converts the contents of an InputStream to a String.
         */
        private String readStream(InputStream stream, int maxLength) throws IOException {
            String result = null;
            // Read InputStream using the UTF-8 charset.
            InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
            // Create temporary buffer to hold Stream data with specified max length.


           // readJsonStream(stream);


            char[] buffer = new char[maxLength];
            // Populate temporary buffer with Stream data.
            int numChars = 0;
            int readSize = 0;
            while (numChars < maxLength && readSize != -1) {
                numChars += readSize;
                int pct = (100 * numChars) / maxLength;
                publishProgress(DownloadCallback.Progress.PROCESS_INPUT_STREAM_IN_PROGRESS, pct);
                readSize = reader.read(buffer, numChars, buffer.length - numChars);
            }
            if (numChars != -1) {
                // The stream was not empty.
                // Create String that is actual length of response body if actual length was less than
                // max length.
                numChars = Math.min(numChars, maxLength);
                result = new String(buffer, 0, numChars);
            }


            return result;
        }
    }


    // after this my codigo

    private String readJsonStream(InputStream in, int maxLength) throws IOException, RuntimeException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
      // boolean result = true;

        String result = null;

        try {

            result = readJsonUser(reader);

         //   result = readJsonStreanLoad(reader);


        } finally {
            reader.close();
        }

        return result;
    }

    private boolean loginSuccess(String result) {
        if (result.equals("Success")) //is a "Success"
        {
            Config.entrada = true;
            //validation = true;
            return true;
        } else //is a "Failure"
        {
            //validation = false;
            Config.entrada = false;
            return false;
        }
    }

    private String readJsonUser(JsonReader reader) throws IOException, RuntimeException {
        String result=null;

        reader.beginObject();
        while (reader.hasNext()) {
            String key = reader.nextName();
            if (key.equals("result")) {
                result = reader.nextString();

                 //Config.prueba = "El resultado es:"+result;
                validation = true;
                    /* Check for failed login */
                if (!loginSuccess(result)) {
                    validation = false;
                    return result;
                }
            } else if (key.equals("user")) {
                readUser(reader);
            } else if (key.equals("services")) {
                readServicesArray(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

            /* Assuming the result key exists in the
             * Json and has been read, at this point
              * it is safe to assume a successful
              * login */
        return result;
    }

    private void readServicesArray(JsonReader reader) throws IOException, RuntimeException {
        reader.beginArray();
        while (reader.hasNext()) {
            readServices(reader);
        }
        reader.endArray();
    }

    private void readServices(JsonReader reader) throws IOException, RuntimeException {
        String serviceIcon = "";
        int serviceId = 0;
        String serviceName = "";

        reader.beginObject();
        while (reader.hasNext()) {
            String key = reader.nextName();
            if (key.equals("ServiceIcon")) {
                serviceIcon = reader.nextString();
            } else if (key.equals("ServiceId")) {
                serviceId = reader.nextInt();
            } else if (key.equals("ServiceName")) {
                serviceName = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

            /* Add service to user object */
      //  Global.user.addService(serviceIcon, serviceId, serviceName);
    }

    private void readUser(JsonReader reader) throws IOException, RuntimeException { //TODO: Save the below data to user information object
        String email;
        String login;
        String name;
        String pword;
        int userID;
        String token;
        String account;

        reader.beginObject();
        while (reader.hasNext()) {
            String key = reader.nextName();
            if (key.equals("account")) {
                account = reader.nextString();
                Config.user.setAccount(account);

            } else if (key.equals("email")) {
                email = reader.nextString();
                Config.user.setUserEmail(email);
            } else if (key.equals("login")) {
                login = reader.nextString();
                Config.user.setLogin(login);
            } else if (key.equals("name")) {
                name = reader.nextString();
                Config.user.setName(name);
                Config.prueba ="Welcome "+ Config.user.getName();
            } else if (key.equals("password")) {
                pword = reader.nextString();
                Config.user.setPassword(pword);
            } else if (key.equals("userId")) {
                userID = reader.nextInt();
                Config.user.setUserID(userID);
            } else if (key.equals("token")) {
                token = reader.nextString();
                Config.user.setToken(token);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
    }



    // This class is for download the dEta

    /**
     * Implementation of AsyncTask that runs a network operation on a background thread.
     */
    class DownloadTask2 extends AsyncTask<String, Integer, DownloadTask2.Result> {

          /**
         * Wrapper class that serves as a union of a result value and an exception. When the
         * download task has completed, either the result value or exception can be a non-null
         * value. This allows you to pass exceptions to the UI thread that were thrown during
         * doInBackground().
         */
        class Result {



            public String mResultValue;
            public Exception mException;
            public Result(String resultValue) {
                mResultValue = resultValue;
            }
            public Result(Exception exception) {
                mException = exception;
            }
        }

        /**
         * Cancel background network operation if we do not have network connectivity.
         */
        @Override
        protected void onPreExecute() {
            if (mCallback != null) {
                NetworkInfo networkInfo = mCallback.getActiveNetworkInfo();
                if (networkInfo == null || !networkInfo.isConnected() ||
                        (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                                && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
                    // If no connectivity, cancel task and update Callback with null data.
                    mCallback.updateFromDownload(null);
                    cancel(true);
                }
            }
        }

        /**
         * Defines work to perform on the background thread.
         */
        @Override
        protected Result doInBackground(String... urls) {

            Result result = null;
            if (!isCancelled() && urls != null && urls.length > 0) {
                String urlString = urls[0];
                try {
                    // URL url = new URL(urlString);
                 //   MainActivity.prueba = "doInBackGround:"+MainActivity.myUrlApi;


                    //Here I force the Network to use my UrlApi

                    URL url = new URL(Config.myUrlApi);

                 //   MainActivity.prueba = "Voy a leer:"+url;

                    String resultString = downloadUrl(url);
                    if (resultString != null) {
                        result = new Result(resultString);
                    } else {
                        throw new IOException("No response received.");
                    }
                } catch(Exception e) {

                    // MainActivity.prueba = "No resulto nada:";
                    result = new Result(e);
                }
            }
            return result;
        }

        /**
         * Send DownloadCallback a progress update.
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (values.length >= 2) {
                mCallback.onProgressUpdate(values[0], values[1]);
            }
        }

        /**
         * Updates the DownloadCallback with the result.
         */
        @Override
        protected void onPostExecute(Result result) {
            if (result != null && mCallback != null) {
                if (result.mException != null) {
                    mCallback.updateFromDownload(result.mException.getMessage());
                } else if (result.mResultValue != null) {
                    mCallback.updateFromDownload(result.mResultValue);
                }
                mCallback.finishDownloading();
            }
        }

        /**
         * Override to add special behavior for cancelled AsyncTask.
         */
        @Override
        protected void onCancelled(Result result) {
        }

        /**
         * Given a URL, sets up a connection and gets the HTTP response body from the server.
         * If the network request is successful, it returns the response body in String form. Otherwise,
         * it will throw an IOException.
         */

        private String downloadUrl(URL url) throws IOException {
            InputStream stream = null;
            HttpsURLConnection connection = null;
            String result = null;

            //  MainActivity.prueba = "Esto dentro de downloadUrl:"+url;

            //  MainActivity.prueba = "El Url es"+url;
            try {
                connection = (HttpsURLConnection) url.openConnection();
                // Timeout for reading InputStream arbitrarily set to 3000ms.
                connection.setReadTimeout(3000);
                // Timeout for connection.connect() arbitrarily set to 3000ms.
                connection.setConnectTimeout(3000);
                // For this use case, set HTTP method to GET.
                connection.setRequestMethod("GET");
                // Already true by default but setting just in case; needs to be true since this request
                // is carrying an input (response) body.
                connection.setDoInput(true);
                // Open communications link (network traffic occurs here).
                connection.connect();
                publishProgress(DownloadCallback.Progress.CONNECT_SUCCESS);
                int responseCode = connection.getResponseCode();
                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    throw new IOException("HTTP error code: " + responseCode);
                }
                // Retrieve the response body as an InputStream.
                stream = connection.getInputStream();
                publishProgress(DownloadCallback.Progress.GET_INPUT_STREAM_SUCCESS, 0);
                if (stream != null) {

                    // Converts Stream to String with max length of 500.
                    //  result = readStream(stream, 500);


                    //   MainActivity.prueba = stream.toString();

                    result = readJsonStream2(stream,500);

                  //  MainActivity.prueba = "el resultado"+result;

                    //  MainActivity.prueba=result;

                    publishProgress(DownloadCallback.Progress.PROCESS_INPUT_STREAM_SUCCESS, 0);
                }
            } finally {
                // Close Stream and disconnect HTTPS connection.
                if (stream != null) {
                    stream.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return result;
        }

        /**
         * Converts the contents of an InputStream to a String.
         */
        private String readStream(InputStream stream, int maxLength) throws IOException {
            String result = null;
            // Read InputStream using the UTF-8 charset.
            InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
            // Create temporary buffer to hold Stream data with specified max length.

            char[] buffer = new char[maxLength];
            // Populate temporary buffer with Stream data.
            int numChars = 0;
            int readSize = 0;
            while (numChars < maxLength && readSize != -1) {
                numChars += readSize;
                int pct = (100 * numChars) / maxLength;
                publishProgress(DownloadCallback.Progress.PROCESS_INPUT_STREAM_IN_PROGRESS, pct);
                readSize = reader.read(buffer, numChars, buffer.length - numChars);
            }
            if (numChars != -1) {
                // The stream was not empty.
                // Create String that is actual length of response body if actual length was less than
                // max length.
                numChars = Math.min(numChars, maxLength);
                result = new String(buffer, 0, numChars);
            }
           return result;
        }
    }

    // after this my codigo

    private String readJsonStream2(InputStream in, int maxLength) throws IOException, RuntimeException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        String result = null;
        try {
             result = jsonParseETAs(reader);
            } finally {
            reader.close();
        }
        return result;
    }

    private String jsonParseETAs(JsonReader reader) throws IOException, RuntimeException {
        String result = null;

        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String key = reader.nextName();
                if (key.equals("result")) {
                    result = reader.nextString();
                    readETAsArray(reader);
                    /* Check for failure */
                    if (!result.equals("Success")) {

                    }
                } else if (key.equals("ETAs")) {
                    readETAsArray(reader);
                } else {
                    reader.skipValue();

                }
            }
        } finally {
            reader.close();

        }

        /* If made it down here, result from
         * JSON was successful */
        return  result;
    }

    /**
     * Function reads through the array of ETA objects
     * and adds them to this ETAFragment's instance of
     * the ArrayList loadedETAs
     **/
    public void readETAsArray(JsonReader reader) throws IOException, RuntimeException {
        ETA tempETA=null;

        reader.beginArray();
        while (reader.hasNext()) {
            tempETA = readETAs(reader);
            Config.user.addLoadObject(tempETA);
        }
        reader.endArray();
        Config.mLoad = tempETA.loadNo;
      //  Config.prueba = Config.mLoad;
    }

    /**
     * Function reads the ETA json object in the array and
     * returns an ETA() object
     **/
    public ETA readETAs(JsonReader reader) throws IOException, RuntimeException {
        ETA tempETA = new ETA();
        String delvDate = "",
                etaDate = "",
                loadNo = "",
                pickDate = "",
                origin = "";

        reader.beginObject();
        while (reader.hasNext()) {
            String key = reader.nextName();
            if (key.equals("delvDate")) {
                delvDate = reader.nextString();
            } else if (key.equals("etaDate")) {
                etaDate = reader.nextString();
            } else if (key.equals("loadNo")) {
                loadNo = reader.nextString();
            } else if (key.equals("pickDate")) {
                pickDate = reader.nextString();
            } else if (key.equals("origin")) {
                origin = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        tempETA.delvDate = delvDate;
        tempETA.etaDate = etaDate;
        tempETA.loadNo = loadNo;
        tempETA.pickDate = pickDate;
        tempETA.origin = origin;

       // MainActivity.prueba = tempETA.delvDate +"-"+tempETA.etaDate +"-"+tempETA.loadNo +"-"+tempETA.pickDate +"-"+tempETA.origin ;

        return tempETA;
    }

    public void showLoadDialog(final boolean show) {

    }


    protected void onPostExecute(final Boolean responseCheck) {
      // mEtaTask = null;
        showLoadDialog(false);



        if (!responseCheck) {
          // prueba = "error...";
        } else {

            /** Async task completed, now produce view with
             * custom list adapter based on the data
             * from the server **/

         //  Config.prueba="Todo salio bien"+Config.mLoad;

        }
    }





//Contrlling the shipper

    /**
     * return Configs.WEBSERVICE_GET_LOADSHIPPERS
     + mParam1 //loadNo selected
     + "/"
     + Configs.user.getAccount()
     + "/"
     + Configs.user.getToken();
     *
     *
     */


    // This class is for download the dEta

    /**
     * Implementation of AsyncTask that runs a network operation on a background thread.
     */
    class DownloadTask3 extends AsyncTask<String, Integer, DownloadTask3.Result> {

        /**
         * Wrapper class that serves as a union of a result value and an exception. When the
         * download task has completed, either the result value or exception can be a non-null
         * value. This allows you to pass exceptions to the UI thread that were thrown during
         * doInBackground().
         */
        class Result {


            public String mResultValue;
            public Exception mException;

            public Result(String resultValue) {
                mResultValue = resultValue;
            }

            public Result(Exception exception) {
                mException = exception;
            }
        }

        /**
         * Cancel background network operation if we do not have network connectivity.
         */
        @Override
        protected void onPreExecute() {
            if (mCallback != null) {
                NetworkInfo networkInfo = mCallback.getActiveNetworkInfo();
                if (networkInfo == null || !networkInfo.isConnected() ||
                        (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                                && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
                    // If no connectivity, cancel task and update Callback with null data.
                    mCallback.updateFromDownload(null);
                    cancel(true);
                }
            }
        }

        /**
         * Defines work to perform on the background thread.
         */
        @Override
        protected Result doInBackground(String... urls) {

            Result result = null;
            if (!isCancelled() && urls != null && urls.length > 0) {
                String urlString = urls[0];
                try {
                    // URL url = new URL(urlString);
                    //   MainActivity.prueba = "doInBackGround:"+MainActivity.myUrlApi;


                    //Here I force the Network to use my UrlApi

                    URL url = new URL(Config.myUrlApi);

                 // Main.prueba = "Estoy en el Background del 3 y esta es la Url: " + url;

                    String resultString = downloadUrl(url);
                    if (resultString != null) {
                        result = new Result(resultString);
                    } else {
                        throw new IOException("No response received.");
                    }
                } catch (Exception e) {

                     // Main.prueba = "No resulto nada:";
                    result = new Result(e);
                }
            }
            return result;
        }

        /**
         * Send DownloadCallback a progress update.
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (values.length >= 2) {
                mCallback.onProgressUpdate(values[0], values[1]);
            }
        }

        /**
         * Updates the DownloadCallback with the result.
         */
        @Override
        protected void onPostExecute(Result result) {
            if (result != null && mCallback != null) {
                if (result.mException != null) {
                    mCallback.updateFromDownload(result.mException.getMessage());
                } else if (result.mResultValue != null) {
                    mCallback.updateFromDownload(result.mResultValue);
                }
                mCallback.finishDownloading();
            }
        }

        /**
         * Override to add special behavior for cancelled AsyncTask.
         */
        @Override
        protected void onCancelled(Result result) {
        }

        /**
         * Given a URL, sets up a connection and gets the HTTP response body from the server.
         * If the network request is successful, it returns the response body in String form. Otherwise,
         * it will throw an IOException.
         */

        private String downloadUrl(URL url) throws IOException {
            InputStream stream = null;
            HttpsURLConnection connection = null;
            String result = null;

            //  Main.prueba = "Esto dentro de downloadUrl:"+url;

            //  Main.prueba = "El Url es"+url;
            try {
                connection = (HttpsURLConnection) url.openConnection();
                // Timeout for reading InputStream arbitrarily set to 3000ms.
                connection.setReadTimeout(3000);
                // Timeout for connection.connect() arbitrarily set to 3000ms.
                connection.setConnectTimeout(3000);
                // For this use case, set HTTP method to GET.
                connection.setRequestMethod("GET");
                // Already true by default but setting just in case; needs to be true since this request
                // is carrying an input (response) body.
                connection.setDoInput(true);
                // Open communications link (network traffic occurs here).
                connection.connect();
                publishProgress(DownloadCallback.Progress.CONNECT_SUCCESS);
                int responseCode = connection.getResponseCode();
                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    throw new IOException("HTTP error code: " + responseCode);
                }
                // Retrieve the response body as an InputStream.
                stream = connection.getInputStream();
                publishProgress(DownloadCallback.Progress.GET_INPUT_STREAM_SUCCESS, 0);
                if (stream != null) {

                    // Converts Stream to String with max length of 500.
                    //  result = readStream(stream, 500);


                   // Main.prueba = " Estoy leyendo el estreen " +stream.toString();

                    result = readJsonStream3(stream, 1500);

                  //  Main.prueba = "el resultado" + result;

                    //  Main.prueba=result;

                    publishProgress(DownloadCallback.Progress.PROCESS_INPUT_STREAM_SUCCESS, 0);
                }
            } finally {
                // Close Stream and disconnect HTTPS connection.
                if (stream != null) {
                    stream.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return result;
        }


        private String readJsonStream3(InputStream in, int maxLength) throws IOException, RuntimeException {
            JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
            String result = null;
            try {
                result = jsonParseShippers(reader);
            } finally {
                reader.close();
            }
            return result;
        }


        private String jsonParseShippers(JsonReader reader) throws IOException, RuntimeException {
            String result = null;

            try {
                reader.beginObject();
                while (reader.hasNext()) {
                    String key = reader.nextName();
                    if (key.equals("result")) {
                        result = reader.nextString();
                      //  Main.prueba="Resulto";
                        readShipperListArray(reader);
                    /* Check for failure */
                        if (!result.equals("Success")) {

                            result = "fail";
                           // Main.prueba=result+"Esta pinga";
                        }
                    } else if (key.equals("ShipperList")) {
                        readShipperListArray(reader);
                    } else {
                        reader.skipValue();
                    }
                }
            } finally {
                reader.close();
            }

        /* If made it down here, result from
         * JSON was successful */
            return result;
        }

        public void readShipperListArray(JsonReader reader) throws IOException, RuntimeException {
            Shipper tempShipper;

            reader.beginArray();
            while (reader.hasNext()) {
                tempShipper = readShipperList(reader);
                //loadedShippers.add(tempShipper);
                Config.user.addShipperObject(tempShipper);
            }
            reader.endArray();



        }

        public Shipper readShipperList(JsonReader reader) throws IOException, RuntimeException {
            Shipper tempShipper = new Shipper();
            String loadNo = "",
                    shipperAcct = "",
                    shipperName = "";
            int pieces = -1;
            double cubes = 0.0;

            reader.beginObject();
            while (reader.hasNext()) {
                String key = reader.nextName();
                if (key.equals("cubes")) {
                    cubes = reader.nextDouble();
                } else if (key.equals("loadNo")) {
                    loadNo = reader.nextString();
                } else if (key.equals("pieces")) {
                    pieces = reader.nextInt();
                } else if (key.equals("shipperAccount")) {
                    shipperAcct = reader.nextString();
                } else if (key.equals("shipperName")) {
                    shipperName = reader.nextString();
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();

            tempShipper.cubes = cubes;
            tempShipper.loadNo = loadNo;
            tempShipper.pieces = pieces;
            tempShipper.shipperAcct = shipperAcct;
            tempShipper.shipperName = shipperName;
            Config.mShipperAccount = tempShipper.shipperAcct;
        //   Config.prueba= tempShipper.shipperName;
            return tempShipper;
        }


        public void showLoadDialog(final boolean show) {

        }


        protected void onPostExecute(final Boolean responseCheck) {
            // mEtaTask = null;
            showLoadDialog(false);

            if (!responseCheck) {
                // prueba = "error...";
            } else {

                /** Async task completed, now produce view with
                 * custom list adapter based on the data
                 * from the server **/

            }
        }


    }



    //Contrlling the products

    /**
     * return Configs.WEBSERVICE_GET_LOADSHIPPERS
     + mParam1 //loadNo selected
     + "/"
     + Configs.user.getAccount()
     + "/"
     + Configs.user.getToken();
     *
     *
     */


    // This class is for download the dEta

    /**
     * Implementation of AsyncTask that runs a network operation on a background thread.
     */
    class DownloadTask4 extends AsyncTask<String, Integer, DownloadTask4.Result> {

        /**
         * Wrapper class that serves as a union of a result value and an exception. When the
         * download task has completed, either the result value or exception can be a non-null
         * value. This allows you to pass exceptions to the UI thread that were thrown during
         * doInBackground().
         */
        class Result {


            public String mResultValue;
            public Exception mException;

            public Result(String resultValue) {
                mResultValue = resultValue;
            }

            public Result(Exception exception) {
                mException = exception;
            }
        }

        /**
         * Cancel background network operation if we do not have network connectivity.
         */
        @Override
        protected void onPreExecute() {
            if (mCallback != null) {
                NetworkInfo networkInfo = mCallback.getActiveNetworkInfo();
                if (networkInfo == null || !networkInfo.isConnected() ||
                        (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                                && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
                    // If no connectivity, cancel task and update Callback with null data.
                    mCallback.updateFromDownload(null);
                    cancel(true);
                }
            }
        }

        /**
         * Defines work to perform on the background thread.
         */
        @Override
        protected Result doInBackground(String... urls) {

            Result result = null;
            if (!isCancelled() && urls != null && urls.length > 0) {
                String urlString = urls[0];
                try {
                    // URL url = new URL(urlString);
                    //   Main.prueba = "doInBackGround:"+Config.myUrlApi;


                    //Here I force the Network to use my UrlApi

                    URL url = new URL(Config.myUrlApi);

            //      Config.prueba = "Estoy en el Background del 3 y esta es la Url: " + url;

                    String resultString = downloadUrl(url);
                    if (resultString != null) {
                        result = new Result(resultString);
                    } else {
                        throw new IOException("No response received.");
                    }
                } catch (Exception e) {

                    // Main.prueba = "No resulto nada:";
                    result = new Result(e);
                }
            }
            return result;
        }

        /**
         * Send DownloadCallback a progress update.
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (values.length >= 2) {
                mCallback.onProgressUpdate(values[0], values[1]);
            }
        }

        /**
         * Updates the DownloadCallback with the result.
         */
        @Override
        protected void onPostExecute(Result result) {
            if (result != null && mCallback != null) {
                if (result.mException != null) {
                    mCallback.updateFromDownload(result.mException.getMessage());
                } else if (result.mResultValue != null) {
                    mCallback.updateFromDownload(result.mResultValue);
                }
                mCallback.finishDownloading();
            }
        }

        /**
         * Override to add special behavior for cancelled AsyncTask.
         */
        @Override
        protected void onCancelled(Result result) {
        }

        /**
         * Given a URL, sets up a connection and gets the HTTP response body from the server.
         * If the network request is successful, it returns the response body in String form. Otherwise,
         * it will throw an IOException.
         */

        private String downloadUrl(URL url) throws IOException {
            InputStream stream = null;
            HttpsURLConnection connection = null;
            String result = null;

            //  Main.prueba = "Esto dentro de downloadUrl:"+url;

            //  Main.prueba = "El Url es"+url;
            try {
                connection = (HttpsURLConnection) url.openConnection();
                // Timeout for reading InputStream arbitrarily set to 3000ms.
                connection.setReadTimeout(3000);
                // Timeout for connection.connect() arbitrarily set to 3000ms.
                connection.setConnectTimeout(3000);
                // For this use case, set HTTP method to GET.
                connection.setRequestMethod("GET");
                // Already true by default but setting just in case; needs to be true since this request
                // is carrying an input (response) body.
                connection.setDoInput(true);
                // Open communications link (network traffic occurs here).
                connection.connect();
                publishProgress(DownloadCallback.Progress.CONNECT_SUCCESS);
                int responseCode = connection.getResponseCode();
                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    throw new IOException("HTTP error code: " + responseCode);
                }
                // Retrieve the response body as an InputStream.
                stream = connection.getInputStream();
                publishProgress(DownloadCallback.Progress.GET_INPUT_STREAM_SUCCESS, 0);
                if (stream != null) {

                    // Converts Stream to String with max length of 500.
                    //  result = readStream(stream, 500);


                    //Main.prueba = " Estoy leyendo el estreen " +stream.toString();

                    result = readJsonStream4(stream, 1500);

                    //  Main.prueba = "el resultado" + result;

                    //  Main.prueba=result;

                    publishProgress(DownloadCallback.Progress.PROCESS_INPUT_STREAM_SUCCESS, 0);
                }
            } finally {
                // Close Stream and disconnect HTTPS connection.
                if (stream != null) {
                    stream.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return result;
        }


        private String readJsonStream4(InputStream in, int maxLength) throws IOException, RuntimeException {
            JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
            String result = null;
            try {
                result = jsonParseProducts(reader);
            } finally {
                reader.close();
            }
            return result;
        }


        /** Parses Shipper json **/
        private String jsonParseProducts(JsonReader reader) throws IOException, RuntimeException {
            String result = null;


            try {
                reader.beginObject();
                while (reader.hasNext())
                {
                    String key = reader.nextName();
                    if (key.equals("result"))
                    {
                        result = reader.nextString();

                    /* Check for failure */
                        if (!result.equals("Success"))
                        {
                           result ="fail";
                        }
                    }
                    else if (key.equals("ProductList"))
                    {
                        readProductListArray(reader);
                    }
                    else
                    {
                        reader.skipValue();
                    }
                }
            }
            finally {
                reader.close();
            }

        /* If made it down here, result from
         * JSON was successful */
          return result;
        }

        public void readProductListArray(JsonReader reader) throws IOException, RuntimeException
        {
            Product tempProduct;

            reader.beginArray();
            while (reader.hasNext())
            {
                tempProduct = readProductList(reader);
                //loadedProducts.add(tempProduct);
                Config.user.addProductObject(tempProduct);
            }
            reader.endArray();
        }


        public Product readProductList(JsonReader reader) throws IOException, RuntimeException
        {
            Product tempProduct = new Product();
            String boxSize = "",
                    loadNo = "",
                    shipperAcct = "",
                    product = "";
            int pPieces = -1;
            double pCubes = 0.0;

            reader.beginObject();
            while (reader.hasNext())
            {
                String key = reader.nextName();
                if (key.equals("boxSize"))
                {
                    boxSize = reader.nextString();
                }
                else if (key.equals("cubes"))
                {
                    pCubes = reader.nextDouble();
                }
                else if (key.equals("loadNo"))
                {
                    loadNo = reader.nextString();
                }
                else if (key.equals("pieces"))
                {
                    pPieces = reader.nextInt();
                }
                else if (key.equals("product"))
                {
                    product = reader.nextString();
                }
                else if (key.equals("shipperAccount"))
                {
                    shipperAcct = reader.nextString();
                }
                else
                {
                    reader.skipValue();
                }
            }
            reader.endObject();

            tempProduct.boxSize = boxSize;
            tempProduct.pCubes = pCubes;
            tempProduct.loadNo = loadNo;
            tempProduct.pPieces = pPieces;
            tempProduct.product = product;
            tempProduct.shipperAcct = shipperAcct;

          // Config.prueba = "valor de piezas"+tempProduct.pPieces;

            return tempProduct;
        }

    }

}
