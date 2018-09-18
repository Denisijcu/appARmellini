package armellinilogistics.armellini.com.expresslines;

/**
 * Created by sand on 8/2/2017.
 */

public class Config  {

    protected static final String LOGIN_HTTPS_QUERY =
            "https://secure2.armellini.com/secure/restArmellini/service/rest.svc/authenticate/";

    protected static final String WEBSERVICE_ROOT =
            "https://secure2.armellini.com/secure/restArmellini/service/rest.svc/";

    protected static final String WEBSERVICE_GET_LOADETA =
            "https://secure2.armellini.com/secure/restArmellini/service/rest.svc/getLoadETA/";

    //https://secure2.armellini.com/secure/restArmellini/service/rest.svc/getLoadETA/F120165/e1a832db-c89a-47c5-943e-3e06339f2ea9

    protected static final String WEBSERVICE_GET_LOADSHIPPERS =
            "https://secure2.armellini.com/secure/restArmellini/service/rest.svc/getLoadShippers/";

    protected static final String WEBSERVICE_GET_LOAD_PRODUCTS =
            "https://secure2.armellini.com/secure/restArmellini/service/rest.svc/getLoadProducts/";

    protected static final String LOGIN_WEBSERVICE_RETRIEVAL_ROOT =
            "https://consolidated.armellini.com/user/send_pswd_retrieve.do";

    protected static final String LOGIN_WEBSERVICE_RETRIEVAL_EMAIL =
            "?email=";

    protected static final String LOGIN_WEBSERVICE_RETRIEVAL_LOGIN =
            "&login=";



    protected static int net_response = 5;

    protected static String url_test;

    protected static String ticket_addresses[] = {"ael-helpdesk@armellini.com"};
    protected static String ticket_addy = "ael-helpdesk@armellini.com";

    protected static boolean loadServiceActive;


    protected boolean getLoadServiceStatus()
    {
        return loadServiceActive;
    }
    protected void setLoadServiceStatus(boolean status)
    {
        loadServiceActive = status;
    }
   protected static userInformation user = userInformation.getUserProfile(); //singleton object for user



    private static int UserID;
    private static String Name;
    private static String Email;
    private static String Password;
    private static String Token;
    private static String Login;
    private static String Account;









    /***
     * Variables Publicas que toman los valores de los fragments objets
     * myUrlAp --- dirige los urls
     * mLoad --- para buscar los shippers
     * mShipperAccount -- para buscar los productos
     * t --- set up el switch de los Urls
     * prueba --- para controlar  el flujo logico del codigo/ su uso solo es para el debug
     * **/
    public static String  myUrlApi;
    public static String  mLoad =null;
    public static String  mShipperAccount = null;
    public static int  t;
    public static String prueba = "Prueba Inicial";
    public static boolean entrada = false;
  //  private boolean mResultado = false;
   // private NetworkFragment mNetworkFragment;



    /***
     * Metodo para validar el Usuario. Si es Customer de Armelline Entra a la App, de lo contrario es rechazado.
     *
     * ***/



















}


