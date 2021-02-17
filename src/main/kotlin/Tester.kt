import java.math.BigInteger
import java.util.*
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

object Tester {
    @ExperimentalTime
    @JvmStatic
    fun main(args: Array<String>) {
        val secp256k1 = EllipticCurve(
            BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFC2F", 16),
            BigInteger("79BE667EF9DCBBAC55A06295CE870B07029BFCDB2DCE28D959F2815B16F81798", 16),
            BigInteger("0"),
            BigInteger("7"),
            BigInteger("6b17d1f2e12c4247f8bce6e563a440f277037d812deb33a0f4a13945d898c296", 16),
            BigInteger("4fe342e2fe1a7f9b8ee7eb4a7c0f9e162bce33576b315ececbb6406837bf51f5", 16)
        )

        val light_secp256k1 = EllipticCurve(
            BigInteger("46260047"),
            BigInteger("0"),
            BigInteger("0"),
            BigInteger("7"),
            BigInteger("25716408"),
            BigInteger("5891503")
        )

        val secp256r1 = EllipticCurve(
            BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"),
            BigInteger("115792089210356248762697446949407573529996955224135760342422259061068512044369"),
            BigInteger("-3"),
            BigInteger("5ac635d8aa3a93e7b3ebbd55769886bc651d06b0cc53b0f63bce3c3e27d2604b", 16),
            BigInteger("6b17d1f2e12c4247f8bce6e563a440f277037d812deb33a0f4a13945d898c296", 16),
            BigInteger("4fe342e2fe1a7f9b8ee7eb4a7c0f9e162bce33576b315ececbb6406837bf51f5", 16)
        )

        var meantime: Double = 0.0
        for (i in 0..99) {
            measureTime {
                var privateKey = BigInteger(256, Random())
                while (privateKey.signum() == 0) {
                    privateKey = BigInteger(256, Random())
                }
                var publicKey = secp256r1.G.montgomeryLadder(privateKey, secp256r1)
            }.also {
                meantime += it.inMilliseconds
            }
        }
        meantime /= 100
        println("Meantime for secp256r1 : $meantime ms")

        var privateKey = BigInteger(256, Random())
        while (privateKey.signum() == 0) {
            privateKey = BigInteger(256, Random())
        }

        val publicKey = secp256r1.G.montgomeryLadder(privateKey, secp256r1)
        println(publicKey.belongsToCurve(secp256r1))

        val testPoint = light_secp256k1.G.double(light_secp256k1).double(light_secp256k1).double(light_secp256k1)
        println(testPoint.belongsToCurve(light_secp256k1))
        testPoint.displayCoordinates(light_secp256k1)


        println(BigInteger("242412840434209077088177644443740781584562707253564639088917482008283351035359266884721096510743006211635153787537092321973356800538002417265394803072808116658337578562022193338400042224046359942203482092207289318204082073422444531184741794947959330211797184006006854262077554012664531297") * BigInteger("354453463037198170058027226121403090929528313663591923964726444357045370014836957453259151455511839082496932152883406851909247261013690101009391348914892997661895893452535087183538493064839959704245075168030970999027400487701186718005891896348992643496921365736323558610255487323473146835"))
    }
}