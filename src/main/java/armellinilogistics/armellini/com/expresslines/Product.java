package armellinilogistics.armellini.com.expresslines;

/**
 * Product object containing state variables for
 * a user's products to be generated for the screen
 * by query, adaptation to a list view, and drawn
 * to the screen.
 */
public class Product
{
    String boxSize,
            loadNo,
            shipperAcct,
            product;
    int pPieces;
    double pCubes;

    Product()
    {
        boxSize = "";
        loadNo = "";
        shipperAcct = "";
        product = "";
        pPieces = -1;
        pCubes = 0.0;
    }
}
