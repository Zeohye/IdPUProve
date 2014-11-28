import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Created by Nils Henning on 28-11-2014.
 */
public class PresentationProof {
    public final ArrayList<BigInteger> attributesInD;
    public final BigInteger a;
    public final BigInteger ap;
    public final BigInteger Ps;
    public final BigInteger r0;
    public final ArrayList<BigInteger> rInU;
    public final ArrayList<BigInteger> rInC;
    public final ArrayList<BigInteger> cInC;
    public final ArrayList<BigInteger> aInC;

    public PresentationProof(ArrayList<BigInteger> attributesInD,BigInteger a,BigInteger ap,BigInteger Ps,BigInteger r0,ArrayList<BigInteger> rInU,ArrayList<BigInteger> rInC,ArrayList<BigInteger>cInC,ArrayList<BigInteger> aInC){
        this.attributesInD = attributesInD;
        this.a = a;
        this.ap = ap;
        this.Ps = Ps;
        this.r0 = r0;
        this.rInU = rInU;
        this.rInC = rInC;
        this.cInC = cInC;
        this.aInC = aInC;

    }

}
