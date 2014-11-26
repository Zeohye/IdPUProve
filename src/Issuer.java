import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Created by Nils Henning on 11/25/2014.
 */
public class Issuer {
	private String y0;
	public final String g0;
	private String currentW;

	public Issuer(String y0, String g0) {
		// TODO: generation af g0
		this.y0 = y0;
		this.g0 = g0;
	}

	public class FirstMessage {
		public final String sigmaz, sigmaa, sigmab;
		public FirstMessage(String sz, String sa, String sb) {
			sigmaz = sz; sigmaa = sa; sigmab = sb;
		}
	}

	public FirstMessage getFirstMessage(String xt, ArrayList<String> xis, String gamma) {
		String sigmaz = Util.ModPow(gamma, y0);
		String w = currentW = Util.GetRandom();
		String sigmaa = Util.ModPow(Parameters.g, w);
		String sigmab = Util.ModPow(gamma, w);
		return new FirstMessage(sigmaz, sigmaa, sigmab);
	}

	public String getThirdMessage(String sigmac) {
		String sigmar = Util.Addq(Util.Mult(sigmac, y0), currentW); // TODO: this might go wrong mod P
		currentW = "";
		return sigmar;
	}
}
