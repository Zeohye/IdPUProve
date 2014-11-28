import sun.security.util.BigInt;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

/**
 * Created by Nils Henning on 11/25/2014.
 */
public class Issuer {
	private BigInteger y0;
	public final BigInteger g0;
	private BigInteger currentW;
    private final BigInteger UIDp;

	public Issuer(BigInteger y0,BigInteger UIDp) {
		this.y0 = y0;
		this.g0 = Parameters.g.modPow(y0,Parameters.p);
        this.UIDp = UIDp;
	}

	public class FirstMessage {
		public final BigInteger sigmaz, sigmaa, sigmab;
		public FirstMessage(BigInteger sz, BigInteger sa, BigInteger sb) {
			sigmaz = sz; sigmaa = sa; sigmab = sb;
		}
	}

	public FirstMessage getFirstMessage(ArrayList<BigInteger> attributeValues, ArrayList<Integer> es,BigInteger TI) {
        BigInteger xt = Util.ComputeXt(UIDp, g0, es, TI);
        ArrayList<BigInteger> xis = new ArrayList<BigInteger>();
        for(int i=0; i<attributeValues.size();i++)
            xis.add(Util.ComputeXi(Parameters.q,es.get(i),attributeValues.get(i)));
        BigInteger gamma = Util.computeGamma(xt, xis,g0);

        BigInteger sigmaz = gamma.modPow(y0,Parameters.p);
		BigInteger w = currentW = Util.GetRandom();
        BigInteger sigmaa = Parameters.g.modPow(w,Parameters.p);
        BigInteger sigmab = gamma.modPow(w,Parameters.p);

		return new FirstMessage(sigmaz, sigmaa, sigmab);
	}

	public BigInteger getThirdMessage(BigInteger sigmac) {
        BigInteger sigma = sigmac.multiply(y0).add(currentW).mod(Parameters.q);;
        currentW = null;
		return sigma;
	}
    public BigInteger getUIDp(){return UIDp;}
}
