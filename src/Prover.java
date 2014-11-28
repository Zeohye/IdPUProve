import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Nils Henning on 11/25/2014.
 */
public class Prover {
	private HashMap<UProveToken,BigInteger> tokens = new HashMap<UProveToken,BigInteger>();
	private ArrayList<Integer> currentEs;
	private BigInteger name, age, gender, email, city;
	private Issuer issuer;
    private HashMap<BigInteger,String> attributeMap = new HashMap<BigInteger, String>();
    private ArrayList<BigInteger> attributeList = new ArrayList<BigInteger>();
    private HashMap<PresentationProof,ArrayList<BigInteger>> secretCommitmentValues = new HashMap<PresentationProof, ArrayList<BigInteger>>();

	public Prover(String name, String age, String gender, String email, String city, Issuer issuer) {
		this.name = new BigInteger("0");
		this.age = new BigInteger("1");
		this.gender = new BigInteger("2");
		this.email = new BigInteger("3");
		this.city = new BigInteger("4");
        attributeList.add(this.name);
        attributeList.add(this.age);
        attributeList.add(this.gender);
        attributeList.add(this.email);
        attributeList.add(this.city);

        attributeMap.put(this.name,name);
        attributeMap.put(this.age,age);
        attributeMap.put(this.gender,gender);
        attributeMap.put(this.email,email);
        attributeMap.put(this.city,city);
        this.issuer = issuer;
	}

	public UProveToken GetToken(ArrayList<Integer> es, BigInteger TI, BigInteger PI) {
		currentEs = es;
		ArrayList<BigInteger> xis = new ArrayList<BigInteger>();
        for(int i=0; i<attributeList.size();i++)
            xis.add(Util.ComputeXi(Parameters.q,es.get(i),attributeList.get(i)));

        BigInteger xt = Util.ComputeXt(issuer.getUIDp(), issuer.g0, es, TI);
        BigInteger gamma = Util.computeGamma(xt, xis, issuer.g0);
        BigInteger a = Util.GetRandom(true);
        BigInteger b1 = Util.GetRandom();
        BigInteger b2 = Util.GetRandom();
        BigInteger h = gamma.modPow(a,Parameters.p);
        BigInteger t1 = issuer.g0.modPow(b1,Parameters.p).multiply(Parameters.g.modPow(b2,Parameters.p));
        BigInteger t2 = h.multiply(b2);

        BigInteger sk = computePrivateKey(a);

		Issuer.FirstMessage msg1 = issuer.getFirstMessage(attributeList,es,TI);

        BigInteger sigmazp = msg1.sigmaz.multiply(a);
        BigInteger sigmaap = t1.multiply(msg1.sigmaa);
        BigInteger sigmabp = sigmazp.modPow(b1,Parameters.p).multiply(t2).multiply(msg1.sigmab.modPow(a,Parameters.p));
        ArrayList<Object> hashList = new ArrayList<Object>();
        hashList.add(h);
        hashList.add(PI);
        hashList.add(sigmazp);
        hashList.add(sigmaap);
        hashList.add(sigmabp);
        BigInteger sigmacp = Util.HashToGroup(Util.Hash(hashList));
        BigInteger sigmac = sigmacp.add(b1).mod(Parameters.q);

        BigInteger sigmar = issuer.getThirdMessage(sigmac);
        BigInteger sigmarp = sigmar.add(b2).mod(Parameters.q);

		// Here we should delete stuff, but they are internal variables and will be deleted when we return

		UProveToken newToken = new UProveToken(issuer.getUIDp(), h, TI, PI, sigmazp, sigmacp, sigmarp);
		tokens.put(newToken,sk);
        return newToken;
	}

    public PresentationProof generatePresentaionProve(ArrayList<Integer> es,ArrayList<Integer> D, ArrayList<Integer> U, ArrayList<Integer>C,int p,String s,String m, UProveToken t){
        ArrayList<BigInteger> xis = new ArrayList<BigInteger>();
        for(int i=0; i<attributeList.size();i++)
            xis.add(Util.ComputeXi(Parameters.q,es.get(i),attributeList.get(i)));
        BigInteger w0 = Util.GetRandom();
        ArrayList<BigInteger> wi = new ArrayList<BigInteger>();
        BigInteger hw0 = t.h.modPow(w0,Parameters.p);
        for(int i=0; i< U.size();i++) {
            BigInteger r = Util.GetRandom();
            wi.add(r);
            hw0 = hw0.multiply(Parameters.gxList.get(U.get(i)).modPow(r,Parameters.p));
        }

        BigInteger a = Util.Hash(hw0);
        BigInteger ap;
        BigInteger Ps;
        if(p !=-1 && s != null){
            BigInteger gs = Util.generateScopeElement(Util.descGroup(), s);
            ap = Util.Hash(gs.modPow(wi.get(p),Parameters.p));
            Ps = gs.modPow(xis.get(p),Parameters.p);
        }else{
            ap = null;
            Ps = null;
        }

        ArrayList<BigInteger> cInC = new ArrayList<BigInteger>();
        ArrayList<BigInteger> aInC = new ArrayList<BigInteger>();
        ArrayList<BigInteger> oRan = new ArrayList<BigInteger>();
        ArrayList<BigInteger> wRan = new ArrayList<BigInteger>();

        for(int i=0; i<C.size();i++){
            BigInteger oRand = Util.GetRandom();
            BigInteger wRand = Util.GetRandom();
            oRan.add(oRand);
            wRan.add(wRand);
            cInC.add(Parameters.g.modPow(xis.get(C.get(i)), Parameters.p).multiply(Parameters.g1.modPow(oRand, Parameters.p)));
            aInC.add(Util.Hash(Parameters.g.modPow(wi.get(C.get(i)), Parameters.p.multiply(Parameters.g1.modPow(wRand, Parameters.p)))));
        }

        BigInteger UIDt = Util.ComputeIDToken(t);
        ArrayList<Object> hashList = new ArrayList<Object>();
        hashList.add(UIDt);
        hashList.add(a);
        hashList.add(D);
        ArrayList<Object> xesInD = new ArrayList<Object>();
        for(int i=0; i<D.size();i++)
            xesInD.add(xis.get(D.get(i)));
        hashList.add(xesInD);
        hashList.add(C);
        hashList.add(cInC);
        hashList.add(aInC);
        hashList.add(p);
        hashList.add(ap);
        hashList.add(Ps);
        hashList.add(m);
        BigInteger cp = Util.Hash(hashList);


        BigInteger c = Util.HashToGroup(Util.Hash(cp));
        BigInteger r0 = c.multiply(tokens.get(t)).add(w0).mod(Parameters.q);

        ArrayList<BigInteger> rInU = new ArrayList<BigInteger>();
        for(int i=0;i<U.size();i++)
            rInU.add(c.negate().multiply(xis.get(U.get(i))).add(wi.get(U.get(i))).mod(Parameters.q));

        ArrayList<BigInteger> rInC = new ArrayList<BigInteger>();
        for(int i=0;i<C.size();i++)
            rInC.add(c.negate().multiply(oRan.get(C.get(i))).add(wRan.get(C.get(i))).mod(Parameters.q));

        ArrayList<BigInteger> attributesInD = new ArrayList<BigInteger>();
        for(int i=0; i<D.size();i++)
            attributesInD.add(attributeList.get(D.get(i)));
        PresentationProof PP = new PresentationProof(attributesInD,a,ap,Ps,r0,rInU,rInC,cInC,aInC);
        secretCommitmentValues.put(PP,oRan);

        return PP;
    }

    private BigInteger computePrivateKey(BigInteger a) {
		return a.modInverse(Parameters.q);
	}

    public HashMap<BigInteger,String> getAttributeMap(){return attributeMap;}
}
