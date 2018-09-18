package armellinilogistics.armellini.com.expresslines;

/**
 * Shipper object containing state variables for
 * a user's shippers to be generated for the screen
 * by query, adaptation to a list view, and drawn
 * to the screen.
 */
public class Shipper
{
    String loadNo,
            shipperAcct,
            shipperName;
    int pieces;
    double cubes;

    Shipper()
    {
        loadNo = shipperAcct = shipperName = "";
        pieces = -1;
        cubes = 0.0;
    }
}
