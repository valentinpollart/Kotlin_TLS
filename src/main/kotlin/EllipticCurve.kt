import java.math.BigInteger
@ExperimentalStdlibApi
class EllipticCurve(
    var p: LargeNumber,
    var n: LargeNumber,
    aTemp: LargeNumber,
    bTemp: LargeNumber,
    Gx: LargeNumber,
    Gy: LargeNumber
) {
    var G: ProjectivePoint = ProjectivePoint(Gx, Gy)
    var r: LargeNumber = LargeNumber((p.modK() + 1).toString())
    var rInv: LargeNumber = LargeNumber(BigInteger(r.toString()).modInverse(BigInteger(p.toString())).toString())
    var v: LargeNumber = (r * rInv - LargeNumber.ONE) * LargeNumber(BigInteger(p.toString()).modInverse(BigInteger(r.toString())).toString())
    var a: LargeNumber = montgomeryTimes(aTemp, r, this)
    var b: LargeNumber = montgomeryTimes(bTemp, r, this)
}
