import java.math.BigInteger

@ExperimentalStdlibApi
class ProjectivePoint {
    private var x: LargeNumber
    private var y: LargeNumber
    private var z: LargeNumber

    constructor(x: LargeNumber, y: LargeNumber, z: LargeNumber) {
        this.x = x
        this.y = y
        this.z = z
    }

    constructor(x: LargeNumber, y: LargeNumber) {
        this.x = x
        this.y = y
        this.z = LargeNumber.ONE
    }

    fun add(other: ProjectivePoint, ec: EllipticCurve): ProjectivePoint {
        if (this == other) {
            return double(ec)
        }
        val A: LargeNumber = montgomeryTimes(other.y, z, ec) - montgomeryTimes(other.z, y, ec)
        val B: LargeNumber = montgomeryTimes(other.x, z, ec) - montgomeryTimes(other.z, x, ec)
        val B2: LargeNumber = montgomeryTimes(B, B, ec)
        val C: LargeNumber = montgomeryTimes(montgomeryTimes(montgomeryTimes(A, A, ec), z, ec), other.z, ec) - montgomeryTimes(B2, B, ec) - montgomeryTimes(montgomeryTimes(montgomeryTimes(B2, x, ec), other.x, ec), montgomeryTimes(
            LargeNumber.TWO, ec.r, ec), ec)
        return ProjectivePoint(
            montgomeryTimes(B, C, ec),
            montgomeryTimes(A, (montgomeryTimes(montgomeryTimes(B2, x, ec), other.x, ec)) - C, ec) - montgomeryTimes(montgomeryTimes(montgomeryTimes(B2, B, ec), y, ec), other.z, ec),
            montgomeryTimes(montgomeryTimes(montgomeryTimes(B2, B, ec), z, ec), other.z, ec)
        )
    }

    fun double(ec: EllipticCurve): ProjectivePoint {
        val A = montgomeryTimes(montgomeryTimes(ec.a, z, ec), z, ec) + montgomeryTimes(montgomeryTimes(montgomeryTimes(LargeNumber("3"), ec.r, ec), x, ec), x, ec)
        val B = montgomeryTimes(y, z, ec)
        val B2 = montgomeryTimes(B, B, ec)
        val C = montgomeryTimes(montgomeryTimes(x, y, ec), B, ec)
        val D = montgomeryTimes(A, A, ec) - montgomeryTimes(montgomeryTimes(LargeNumber("8"), ec.r, ec), C, ec)
        return ProjectivePoint(
            montgomeryTimes(montgomeryTimes(montgomeryTimes(LargeNumber("3"), ec.r, ec), B, ec), D, ec),
            montgomeryTimes(A, (montgomeryTimes(montgomeryTimes(LargeNumber("4"), ec.r, ec), C, ec) - D), ec) - montgomeryTimes(montgomeryTimes(montgomeryTimes(montgomeryTimes(LargeNumber("8"), ec.r, ec), y, ec), y, ec), B2, ec),
        montgomeryTimes(montgomeryTimes(montgomeryTimes(LargeNumber("8"), ec.r, ec), B2, ec), B, ec)
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
        return montgomeryTimes(montgomeryTimes(z, y ,ec), y, ec) == montgomeryTimes(montgomeryTimes(x, x, ec), x, ec) + montgomeryTimes(montgomeryTimes(ec.a, x, ec), z, ec) + montgomeryTimes(montgomeryTimes(ec.b, z, ec), z, ec)
    }

    fun displayCoordinates(ec: EllipticCurve) {
        if (x > LargeNumber.ZERO) {
            println(x)
        } else {
            println(x + ec.p)
        }
        if (y > LargeNumber.ZERO) {
            println(y)
        } else {
            println(y + ec.p)
        }
        if (z > LargeNumber.ZERO) {
            println(z)
        } else {
            println(z + ec.p)
        }
    }

    private fun toMontgomeryDomain(ec: EllipticCurve): ProjectivePoint {
        return ProjectivePoint(
            this.x.changeDomain(ec.r, ec.p, ec.v),
            this.y.changeDomain(ec.r, ec.p, ec.v),
            this.z.changeDomain(ec.r, ec.p, ec.v)
        )
    }


    private fun toClassicDomain(ec: EllipticCurve): ProjectivePoint {
        return ProjectivePoint(
            this.x.changeDomain(ec.rInv, ec.p, ec.v),
            this.y.changeDomain(ec.rInv, ec.p, ec.v),
            this.z.changeDomain(ec.rInv, ec.p, ec.v)
        )
    }
}

@ExperimentalStdlibApi
fun montgomeryTimes(val1: LargeNumber, val2: LargeNumber, ec: EllipticCurve): LargeNumber {
    return val1.montgomeryTimes(val2, ec.p, ec.v)
}
