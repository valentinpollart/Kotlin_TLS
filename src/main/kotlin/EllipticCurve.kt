import java.math.BigInteger

class EllipticCurve(
    var p: BigInteger,
    var n: BigInteger,
    aTemp: BigInteger,
    bTemp: BigInteger,
    Gx: BigInteger,
    Gy: BigInteger
) {
    var G: ProjectivePoint = ProjectivePoint(Gx, Gy)
    var r: BigInteger = BigInteger(p.bitLength().toString())
    var rInv: BigInteger = r.modInverse(p)
    var v: BigInteger = (r*rInv - BigInteger.ONE)*p.modInverse(r)
    var a: BigInteger = montgomeryTimes(aTemp, r, this)
    var b: BigInteger = montgomeryTimes(bTemp, r, this)
}