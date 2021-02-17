import java.math.BigInteger

class ProjectivePoint {
    private var x: BigInteger
    private var y: BigInteger
    private var z: BigInteger

    constructor(x: BigInteger, y: BigInteger, z: BigInteger) {
        this.x = x
        this.y = y
        this.z = z
    }

    constructor(x: BigInteger, y: BigInteger) {
        this.x = x
        this.y = y
        this.z = BigInteger.valueOf(1)
    }

    fun add(other: ProjectivePoint, ec: EllipticCurve): ProjectivePoint {
        if (this == other) {
            return double(ec)
        }
        val A: BigInteger = montgomeryTimes(other.y, z, ec) - montgomeryTimes(other.z, y, ec)
        val B: BigInteger = montgomeryTimes(other.x, z, ec) - montgomeryTimes(other.z, x, ec)
        val B2: BigInteger = montgomeryTimes(B, B, ec)
        val C: BigInteger = montgomeryTimes(montgomeryTimes(montgomeryTimes(A, A, ec), z, ec), other.z, ec) - montgomeryTimes(B2, B, ec) - montgomeryTimes(montgomeryTimes(montgomeryTimes(B2, x, ec), other.x, ec), montgomeryTimes(
            BigInteger.TWO, ec.r, ec), ec)
        return ProjectivePoint(
            montgomeryTimes(B, C, ec),
            montgomeryTimes(A, (montgomeryTimes(montgomeryTimes(B2, x, ec), other.x, ec)) - C, ec) - montgomeryTimes(montgomeryTimes(montgomeryTimes(B2, B, ec), y, ec), other.z, ec),
            montgomeryTimes(montgomeryTimes(montgomeryTimes(B2, B, ec), z, ec), other.z, ec)
        )
    }

    fun double(ec: EllipticCurve): ProjectivePoint {
        val A = montgomeryTimes(montgomeryTimes(ec.a, z, ec), z, ec) + montgomeryTimes(montgomeryTimes(montgomeryTimes(BigInteger("3"), ec.r, ec), x, ec), x, ec)
        val B = montgomeryTimes(y, z, ec)
        val B2 = montgomeryTimes(B, B, ec)
        val C = montgomeryTimes(montgomeryTimes(x, y, ec), B, ec)
        val D = montgomeryTimes(A, A, ec) - montgomeryTimes(montgomeryTimes(BigInteger("8"), ec.r, ec), C, ec)
        return ProjectivePoint(
            montgomeryTimes(montgomeryTimes(montgomeryTimes(BigInteger("3"), ec.r, ec), B, ec), D, ec),
            montgomeryTimes(A, (montgomeryTimes(montgomeryTimes(BigInteger("4"), ec.r, ec), C, ec) - D), ec) - montgomeryTimes(montgomeryTimes(montgomeryTimes(montgomeryTimes(BigInteger("8"), ec.r, ec), y, ec), y, ec), B2, ec),
        montgomeryTimes(montgomeryTimes(montgomeryTimes(BigInteger("8"), ec.r, ec), B2, ec), B, ec)
        )
    }

    fun montgomeryLadder(k: BigInteger, ec: EllipticCurve): ProjectivePoint {
        var t1 = this.toMontgomeryDomain(ec)
        var t2 = this.toMontgomeryDomain(ec).double(ec)
        for(j in k.toString(2).removeRange(0,3)) {
            if (j == '0') {
                t2 = t2.add(t1, ec)
                t1 = t1.double(ec)
            } else {
                t1 = t1.add(t2, ec)
                t2 = t2.double(ec)
            }
        }
        return t1.toClassicDomain(ec)
    }

    fun belongsToCurve(ec: EllipticCurve): Boolean {
        return ((z * y) % ec.p * y) % ec.p == (((x * x) % ec.p * x) % ec.p + (((ec.a * x) % ec.p * z) % ec.p * z) % ec.p + ((ec.b * z) % ec.p * z) % ec.p * z) % ec.p
    }

    fun displayCoordinates(ec: EllipticCurve) {
        if (x > BigInteger("0")) {
            println(x)
        } else {
            println(x + ec.p)
        }
        if (y > BigInteger("0")) {
            println(y)
        } else {
            println(y + ec.p)
        }
        if (z > BigInteger("0")) {
            println(z)
        } else {
            println(z + ec.p)
        }
    }

    private fun toMontgomeryDomain(ec: EllipticCurve): ProjectivePoint {
        return ProjectivePoint(
            montgomeryTimes(this.x, ec.r, ec),
            montgomeryTimes(this.y, ec.r, ec),
            montgomeryTimes(this.y, ec.r, ec)
        )
    }

    private fun toClassicDomain(ec: EllipticCurve): ProjectivePoint {
        return ProjectivePoint(
            montgomeryTimes(this.x, ec.rInv, ec),
            montgomeryTimes(this.y, ec.rInv, ec),
            montgomeryTimes(this.y, ec.rInv, ec)
        )
    }
}

fun montgomeryTimes(val1: BigInteger, val2: BigInteger, ec: EllipticCurve): BigInteger {
    val s = val1 * val2
    val sv = (s * ec.v)
    val t = sv - sv shr (ec.r.intValueExact())
    val m = s + t * ec.p
    val u = m shr ec.r.intValueExact()
    return if (u >= ec.p) u - ec.p else u
}