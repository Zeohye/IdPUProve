import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Nils Henning on 11/25/2014.
 */
public class Prover {
	private HashMap<String,UProveToken> tokens = new HashMap<String,UProveToken>();
	private ArrayList<Integer> currentEs;
	private String currentUIDp;
	private String name, age, gender, email, city;
	private Issuer issuer;

	public Prover(String name, String age, String gender, String email, String city, Issuer issuer) {
		this.name = name;
		this.age = age;
		this.gender = gender;
		this.email = email;
		this.city = city;
		this.issuer = issuer;
	}

	public void GetToken(String UIDp, ArrayList<Integer> es, String TI, String PI) {
		currentEs = es;
		currentUIDp = ""; // TODO: compute
		ArrayList<String> xis = new ArrayList<String>();
		String xt = Util.ComputeXt(UIDp, issuer.g0, es, TI);
		String gamma = computeGamma(xt, xis);
		String a = Util.GetRandom(true);
		String b1 = Util.GetRandom();
		String b2 = Util.GetRandom();
		String h = Util.ModPow(gamma, a);
		String t1 = Util.Mult(Util.ModPow(issuer.g0, b1), Util.ModPow(Parameters.g, b2));
		String t2 = Util.ModPow(h, b2);

		String sk = computePrivateKey(a);

		Issuer.FirstMessage msg1 = issuer.getFirstMessage(xt, xis, gamma);

		String sigmazp = Util.ModPow(msg1.sigmaz, a);
		String sigmaap = Util.Mult(t1,msg1.sigmaa);
		String sigmabp = Util.Mult(Util.Mult(Util.ModPow(sigmazp, b1), t2), Util.ModPow(msg1.sigmab, a));
		String sigmacp = ""; // Hashing!
		String sigmac = Util.Addq(sigmacp, b1);

		String sigmar = issuer.getThirdMessage(sigmac);
		String sigmarp = Util.Addq(sigmar, b2);

		// Here we should delete stuff, but they are internal variables and will be deleted when we return

		UProveToken newToken = new UProveToken(UIDp, h, TI, PI, sigmazp, sigmacp, sigmarp);
		tokens.put(sk, newToken);
	}

	private String computePrivateKey(String a) {
		return "43"; // TODO: fix
	}

	private String computeGamma(String xt, ArrayList<String> xis) {
		return "41"; // TODO: fix
	}
}
