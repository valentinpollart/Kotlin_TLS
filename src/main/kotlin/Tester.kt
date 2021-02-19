import java.math.BigInteger
import java.util.*
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

object Tester {
    @ExperimentalStdlibApi
    @ExperimentalTime
    @JvmStatic
    fun main(args: Array<String>) {
        val secp256k1 = EllipticCurve(
            LargeNumber(BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFC2F", 16).toString()),
            LargeNumber(BigInteger("79BE667EF9DCBBAC55A06295CE870B07029BFCDB2DCE28D959F2815B16F81798", 16).toString()),
            LargeNumber.ZERO,
            LargeNumber("7"),
            LargeNumber(BigInteger("6b17d1f2e12c4247f8bce6e563a440f277037d812deb33a0f4a13945d898c296", 16).toString()),
            LargeNumber(BigInteger("4fe342e2fe1a7f9b8ee7eb4a7c0f9e162bce33576b315ececbb6406837bf51f5", 16).toString())
        )

        val light_secp256k1 = EllipticCurve(
            LargeNumber("46260047"),
            LargeNumber.ZERO,
            LargeNumber.ZERO,
            LargeNumber("7"),
            LargeNumber("25716408"),
            LargeNumber("5891503")
        )

        val secp256r1 = EllipticCurve(
            LargeNumber("115792089210356248762697446949407573530086143415290314195533631308867097853951"),
            LargeNumber("115792089210356248762697446949407573529996955224135760342422259061068512044369"),
            LargeNumber("-3"),
            LargeNumber(BigInteger("5ac635d8aa3a93e7b3ebbd55769886bc651d06b0cc53b0f63bce3c3e27d2604b", 16).toString()),
            LargeNumber(BigInteger("6b17d1f2e12c4247f8bce6e563a440f277037d812deb33a0f4a13945d898c296", 16).toString()),
            LargeNumber(BigInteger("4fe342e2fe1a7f9b8ee7eb4a7c0f9e162bce33576b315ececbb6406837bf51f5", 16).toString())
        )

        println(montgomeryTimes(LargeNumber("11").changeDomain(secp256k1.r, secp256k1.p, secp256k1.v), LargeNumber("18").changeDomain(secp256k1.r, secp256k1.p, secp256k1.v), secp256k1).changeDomain(secp256k1.rInv, secp256k1.p, secp256k1.v).toString())

//        var meantime: Double = 0.0
//        for (i in 0..99) {
//            measureTime {
//                var privateKey = BigInteger(256, Random())
//                while (privateKey.signum() == 0) {
//                    privateKey = BigInteger(256, Random())
//                }
//                var publicKey = secp256k1.G.montgomeryLadder(privateKey, secp256k1)
//            }.also {
//                meantime += it.inMilliseconds
//            }
//        }
//        meantime /= 100
//        println("Meantime for secp256k1 : $meantime ms")

        var privateKey = BigInteger(256, Random())
        while (privateKey.signum() == 0) {
            privateKey = BigInteger(256, Random())
        }

        val publicKey = secp256r1.G.montgomeryLadder(privateKey, secp256k1)
        println(publicKey.belongsToCurve(secp256k1))
       }
}
