package kravis

import org.apache.commons.math3.distribution.MultivariateNormalDistribution
import org.apache.commons.math3.linear.MatrixUtils

/**
 * @author Holger Brandl
 */
class MathUtil {
    companion object {
        fun multNormDist(
            means: Array<Double> = arrayOf(0.0, 0.0),
            p: Array<DoubleArray> = MatrixUtils.createRealIdentityMatrix(2).data
        ) =
            MultivariateNormalDistribution(means.toDoubleArray(), p)
    }
}