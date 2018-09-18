package armellinilogistics.armellini.com.expresslines;

/**
 * Created by SAND on 8/7/2017.
 */
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

/**
 * Notes on userInformation (singleton/static) class:
 * - class is final, preventing extensions of the class
 * - constructor is private, preventing instantiation by client code, so just the one object exists
 * - all members and functions of the class are static, since only one of these objects must exist in memory
 * - thus, we have a static class
 **/
public final class userInformation
{
    private static userInformation userProfile = null;//new userInformation();
    //user info vars here, information to be saved in cache, probably
    //Note to self: A static variable is one that belongs to the class
    //itself, rather than an object or instance of that class. As such
    //it is universal among all instances of those objects.

    /*State*/
    //variables static because there need be only one userInformation class/object
    private static int jUserID;
    private static String jName;
    private static String jEmail;
    private static String jPassword;
    private static String jToken;
    private static String jLogin;
    private static String jAccount;

    String mDeviceModel,
            mDeviceManufacturer,
            mDeviceVersionCodename,
            mDeviceVersionRelease;
    int mDeviceVersionSDK;

    private static int servicesAvailableSize;

    //    private static ArrayList<Services> servicesAvail = new ArrayList<>();
    private static ArrayList<ETA> loadsAvail = new ArrayList<>();
    private static ArrayList<Shipper> shippersAvail = new ArrayList<>();
    private static ArrayList<Product> productsAvail = new ArrayList<>();
    private static ArrayList<String> savedCredentials = new ArrayList<>(2);

    private static int loadsAvailSize;

    private userInformation()
    {

    }

    /* ---- ---- Methods ---- ---- */

    protected static userInformation getUserProfile() //getInstance
    //TODO: May need to write function to check if a user profile exists in cache, and pull the object from cache to retrieve it
    {
        if (userProfile == null)
        {
            userProfile = new userInformation();
        }
        return userProfile;
    }

    // // (getters and setters) // //

    //product object
    protected ArrayList<Product> getProductsArray()
    {
        return productsAvail;
    }
    protected Product getProductObject(int loc)
    {
        return productsAvail.get(loc);
    }
    protected void addProductObject(Product loadIn)
    {
        productsAvail.add(loadIn);
    }
    protected void clearProductObject()
    {
        productsAvail.clear();
    }
    protected int getProductObjectSize()
    {
        return productsAvail.size();
    }

    //load object
    protected ArrayList<ETA> getLoadsArray()
    {
        return loadsAvail;
    }

    protected ETA getLoadObject(int loc)
    {
        return loadsAvail.get(loc);
    }
    protected void addLoadObject(ETA loadIn)
    {
        loadsAvail.add(loadIn);
    }
    protected void clearLoadObject()
    {
        loadsAvail.clear();
    }
    protected int getLoadObjectSize()
    {
        return loadsAvail.size();
    }

    //shipper object
    protected ArrayList<Shipper> getShipperArray()
    {
        return shippersAvail;
    }
    protected Shipper getShipperObject(int loc)
    {
        return shippersAvail.get(loc);
    }
    protected void addShipperObject(Shipper shipperIn)
    {
        shippersAvail.add(shipperIn);
    }
    protected void clearShipperObject()
    {
        shippersAvail.clear();
    }
    protected int getShipperObjectSize()
    {
        return shippersAvail.size();
    }

    //jEmail
    protected String getUserEmail()
    {
        return jEmail;
    }
    protected void setUserEmail(String emailIn)
    {
        jEmail = emailIn;
    }

    //jUserID
    protected int getUserID()
    {
        return jUserID;
    }
    protected void setUserID(int inUserID)
    {
        jUserID = inUserID;
    }

    //jName
    protected String getName()
    {
        return jName;
    }
    protected void setName(String inName)
    {
        jName = inName;
    }

    //jPassword
    protected String getPassword()
    {
        return jPassword;
    }
    protected void setPassword(String inPass)
    {
        jPassword = inPass; //when user changes pword, this should also be called after the change in the DB
    }

    protected String getToken()
    {
        return jToken;
    }
    protected void setToken(String tokenIn)
    {
        jToken = tokenIn;
    }

    protected String getLogin()
    {
        return jLogin;
    }
    protected void setLogin(String loginIn)
    {
        jLogin = loginIn;
    }

    protected String getAccount()
    {
        return jAccount;
    }
    protected void setAccount(String accountIn)
    {
        jAccount = accountIn;
    }


    // // (other members) // //

    //TODO: Create function for creating a new user (jCreateUser)

    //TODO: Log the creation of the user with jCreateDtTm, the aforementioned function probably calls this

    //TODO: Create function for changing user name (jChangeUser)

    //TODO: Log the change of the user name with jChangeDtTm

    /** Updates the userInfo object locally
     * and in the apps private storage **/

    protected void saveCredentials(Context appContext, String us, String pw)
    {
        /* Create SharedPreference object and initialize with appropriate file */
        SharedPreferences userCredentials = appContext.getSharedPreferences(
                "credData", Context.MODE_PRIVATE );

        /* Create SharedPreference.Editor object and attach the SharedReference object to it */
        SharedPreferences.Editor editor = userCredentials.edit();

        /* Push credentials to internal file */
        editor.putBoolean("SavedStatus", true);
        editor.putString("username", us);
        editor.putString("password", pw);

        editor.apply();
    }

    protected ArrayList<String> readCredentials(Context appContext)
    {
        /* Create SharedPreference object and initialize with appropriate file */
        SharedPreferences userCredentials = appContext.getSharedPreferences(
                "credData", Context.MODE_PRIVATE );

        savedCredentials.clear();

        String usIn = userCredentials.getString("username", "");
        String pwIn = userCredentials.getString("password", "");


        savedCredentials.add(0, usIn);
        savedCredentials.add(1, pwIn);

        return savedCredentials;
    }


}
