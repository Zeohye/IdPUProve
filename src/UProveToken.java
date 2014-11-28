import java.math.BigInteger;

public class UProveToken {
	public final BigInteger UIDp;
	public final BigInteger h;
	public final BigInteger TI;
	public final BigInteger PI;
	public final BigInteger sigmaz;
	public final BigInteger sigmac;
	public final BigInteger sigmar;
	public final boolean d = false;

	public UProveToken(BigInteger u, BigInteger h, BigInteger t, BigInteger p, BigInteger sz, BigInteger sc, BigInteger sr) {
		UIDp = u;
		this.h = h;
		TI = t;
		PI = p;
		sigmaz = sz;
		sigmac = sc;
		sigmar = sr;
	}
}